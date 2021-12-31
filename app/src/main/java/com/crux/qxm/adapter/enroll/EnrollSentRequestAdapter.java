package com.crux.qxm.adapter.enroll;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.crux.qxm.R;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.enroll.sentEnrollRequest.SentEnrollRequestData;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_QUESTION_OVERVIEW;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_PUBLIC;

public class EnrollSentRequestAdapter extends RecyclerView.Adapter<EnrollSentRequestAdapter.ViewHolder> {

    private static final String TAG = "EnrollParticipatedAdapt";

    private Context context;
    private List<SentEnrollRequestData> items;
    private FragmentActivity fragmentActivity;
    private Picasso picasso;
    private QxmApiService apiService;
    private QxmToken token;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    public EnrollSentRequestAdapter(Context context, List<SentEnrollRequestData> items, FragmentActivity fragmentActivity, Picasso picasso, QxmApiService apiService, QxmToken token) {
        this.context = context;
        this.items = items;
        this.fragmentActivity = fragmentActivity;
        this.picasso = picasso;
        this.apiService = apiService;
        this.token = token;
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(fragmentActivity);
    }


    @NonNull
    @Override
    public EnrollSentRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View enrollParticipatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.enroll_sent_request_feed_single_item, parent, false);

        return new EnrollSentRequestAdapter.ViewHolder(enrollParticipatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull EnrollSentRequestAdapter.ViewHolder holder, int position) {
        SentEnrollRequestData sentEnrollRequestData = items.get(position);
        Log.d(TAG, "onBindViewHolder: Normal Feed: " + sentEnrollRequestData.toString());

        if (sentEnrollRequestData.getEnrolledQxm().getQxmSettings().getQuestionPrivacy().equals(QXM_PRIVACY_PUBLIC)) {
            holder.tvFeedPrivacy.setText(context.getResources().getString(R.string.public_qxm));
            holder.ivFeedPrivacy.setImageDrawable(context.getResources()
                    .getDrawable(R.drawable.ic_earth));

        } else {
            holder.tvFeedPrivacy.setText(context.getResources().getString(R.string.private_qxm));
            holder.ivFeedPrivacy.setImageDrawable(context.getResources()
                    .getDrawable(R.drawable.ic_lock));

        }


        if (sentEnrollRequestData.getEnrolledQxm().getQxmCreator().getImage() != null)
            picasso.load(sentEnrollRequestData.getEnrolledQxm().getQxmCreator().getImage())
                    .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                    .into(holder.ivUserImage);
        holder.ivUserImage.setImageDrawable(context.getResources().getDrawable(
                R.drawable.ic_user_default
        ));

        // show contest text view if contest mode is on
        if (sentEnrollRequestData.getEnrolledQxm().getQxmSettings().isContestModeEnabled())
            holder.CVContest.setVisibility(View.VISIBLE);
        else holder.CVContest.setVisibility(View.GONE);

        //show thumbnail image view if feed contains thumbnail
        if (sentEnrollRequestData.getEnrolledQxm().getQuestionSet().getQuestionSetThumbnail() != null) {

            holder.ivFeedThumbnail.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(sentEnrollRequestData.getEnrolledQxm().getQuestionSet().getQuestionSetThumbnail()))
                picasso.load(sentEnrollRequestData.getEnrolledQxm().getQuestionSet().getQuestionSetThumbnail()).into(holder.ivFeedThumbnail);
        } else {
            holder.ivFeedThumbnail.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(sentEnrollRequestData.getEnrolledQxm().getQxmCreator().getFullName())
                && !TextUtils.isEmpty(sentEnrollRequestData.getEnrolledQxm().getQuestionSet().getQuestionSetName())
                && !TextUtils.isEmpty(sentEnrollRequestData.getEnrolledQxm().getQxmSettings().getQuestionPrivacy())) {
            String mainText = String.format("You sent a enroll request to <b>%s</b> to participate his %s qxm <b>%s</b>",
                    sentEnrollRequestData.getEnrolledQxm().getQxmCreator().getFullName(),
                    sentEnrollRequestData.getEnrolledQxm().getQxmSettings().getQuestionPrivacy(),
                    sentEnrollRequestData.getEnrolledQxm().getQuestionSet().getQuestionSetName());
            holder.tvMainText.setText(Html.fromHtml(mainText));
        }


        // show feed item time ago
        long feedCreationTime = Long.parseLong(sentEnrollRequestData.getCreatedAt());
        String feedPostTimeAgo = TimeAgo.using(feedCreationTime);
        Log.d(TAG, "onBindViewHolder: creationTime: " + feedPostTimeAgo);
        holder.tvRequestSentAt.setText(feedPostTimeAgo);


        // region feed main content onclick

        holder.llSingleFeedItem.setOnClickListener(v -> {

        });


        //region goto user profile

        holder.ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(
                sentEnrollRequestData.getEnrolledQxm().getQxmCreator().getId(),sentEnrollRequestData.getEnrolledQxm().getQxmCreator().getFullName())
        );

        //endregion

        holder.ivFeedThumbnail.setOnClickListener(v -> {

            Bundle args = new Bundle();
            String qxmId = sentEnrollRequestData.getEnrolledQxm().getId();
            Log.d(TAG, "QxmId: " + qxmId);
            args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, qxmId);

            qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);
        });

        // endregion

        //region cancel request
        holder.tvCancelRequest.setOnClickListener(v -> {

            Log.d(TAG, "Requested Qxm: " + sentEnrollRequestData);
            int pos = holder.getAdapterPosition();
            Log.d(TAG, "Requested Qxm Position: " + String.valueOf(pos));
            cancelMySentEnrollRequest(sentEnrollRequestData, holder.getAdapterPosition());

        });
        //endregion

        // region feed menu item onclick

        holder.ivFeedOptions.setOnClickListener(v -> {

            //Toast.makeText(context, "Clicked Normal feed Threed dot", Toast.LENGTH_SHORT).show();

            PopupMenu popup = new PopupMenu(fragmentActivity, v, Gravity.END);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_feed, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {


                switch (item.getItemId()) {


                    case R.id.action_share_to_group:

//                                AppCompatDialog dialog;
//                                AlertDialog.Builder builder = new AlertDialog.Builder(fragmentActivity);
//                                LayoutInflater groupInflater = LayoutInflater.from(fragmentActivity);
//                                View showGroupListView = groupInflater.inflate(R.layout.feed_menu_my_group_list_for_share,null);
//                                builder.setView(showGroupListView);
//                                dialog = builder.create();
//                                dialog.show();
//
//                                SharedPreferences userIdGet = fragmentActivity.getSharedPreferences("key",0);
//                                String userId = userIdGet.getString("userId","No msg saved");
//
//                                JsonArrayRequest groupListRequest = new JsonArrayRequest(Request.Method.GET, Config.groupUrl + userId + "/getGroups", null, new Response.Listener<JSONArray>() {
//                                    @Override
//                                    public void onResponse(JSONArray response) {
//
//                                        Log.d("GroupListSuccess",response.toString());
//                                        Gson groupNameJson = new GsonBuilder().create();
//                                        ArrayList<GroupBasic> groupBasicArrayList = groupNameJson.fromJson(response.toString(),new TypeToken<ArrayList<GroupBasic>>(){}.getType());
//                                        Log.d("GroupBasicArrayList",groupBasicArrayList.toString());
//                                        RecyclerView groupListRV = showGroupListView.findViewById(R.id.groupListRV);
//                                        String qxmId = myQxmFeedData.qxmId;
//                                        ShareToGroupAdapter shareToGroupAdapter = new ShareToGroupAdapter(showGroupListView.getContext(),groupBasicArrayList,userId,qxmId,dialog);
//                                        groupListRV.setLayoutManager(new LinearLayoutManager(showGroupListView.getContext()));
//                                        groupListRV.setAdapter(shareToGroupAdapter);
//
//                                    }
//                                }, new Response.ErrorListener() {
//                                    @Override
//                                    public void onErrorResponse(VolleyError error) {
//
//                                        Log.d("GroupListError",error.toString());
//                                    }
//                                });
//
//                                AppController.getInstance().addToRequestQueue(groupListRequest);
//                                Toast.makeText(context, "share to group", Toast.LENGTH_SHORT).show();
//                                break;
//
//                            case R.id.action_share_link:
//                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//                                sharingIntent.setType("text/plain");
//                                String qxmLink = myQxmFeedData.getQxmLink();
//                                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Share this qxm link with others");
//                                sharingIntent.putExtra(Intent.EXTRA_TEXT, qxmLink);
//                                fragmentActivity.startActivity(Intent.createChooser(sharingIntent, "Share via"));
//                                break;
//
//
//                            case R.id.action_add_to_list:
//                                android.support.v7.app.AlertDialog.Builder listBuilder = new android.support.v7.app.AlertDialog.Builder(context);
//                                LayoutInflater inflater12 = LayoutInflater.from(context);
//                                View createListVIew = inflater12.inflate(R.layout.list_popup_from_menu,null);
//                                listBuilder.setView(createListVIew);
//                                dialog = listBuilder.create();
//                                dialog.show();
//                                createListPojo = new CreateListPojo();
//                                RecyclerView listRV = createListVIew.findViewById(R.id.listRV);
//                                SharedPreferences userIdGett =context.getSharedPreferences("key",0);
//                                String userIdd = userIdGett.getString("userId","No msg saved");
//
//
//                                JsonArrayRequest listInMenuRequest = new JsonArrayRequest(Request.Method.GET, Config.url + userIdd + "/newList",
//                                        null, response -> {
//                                    Log.d("ListResponse", response.toString());
//                                    Gson listGson = new GsonBuilder().create();
//                                    createListPojoList = listGson.fromJson(response.toString()
//                                            ,new TypeToken<ArrayList<CreateListPojo>>(){}.getType());
//                                    Log.d("CreateListPojo",createListPojo.toString());
//                                    listRV.setLayoutManager(new LinearLayoutManager(context));
//                                    ShowListInMenu listAdapter = new ShowListInMenu(fragmentActivity,context,createListPojoList);
//                                    listRV.setAdapter(listAdapter);
//                                }, error -> Log.d("ListVolleyError",error.toString()));
//                                AppController.getInstance().addToRequestQueue(listInMenuRequest);
//                                break;
//
//                            case R.id.action_save_as:
//
//                                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
//                                break;
                }


//                if (item.getItemId() == R.id.action_add_to_list) {
//
//
//                }

                return false;
            });
            popup.show();

        });

        // endregion
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    // region EnrollParticipatedFeedViewHolder

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayoutCompat llSingleFeedItem;
        CardView CVContest;
        AppCompatImageView ivFeedPrivacy;
        AppCompatTextView tvFeedPrivacy;
        AppCompatImageView ivUserImage;
        AppCompatImageView ivFeedOptions;
        AppCompatTextView tvMainText;
        AppCompatImageView ivFeedThumbnail;
        AppCompatTextView tvEnrollStatus;
        AppCompatTextView tvCancelRequest;
        AppCompatTextView tvRequestSentAt;


        ViewHolder(View itemView) {
            super(itemView);

            llSingleFeedItem = itemView.findViewById(R.id.llSingleFeedItem);
            CVContest = itemView.findViewById(R.id.CVContest);
            ivFeedPrivacy = itemView.findViewById(R.id.ivFeedPrivacy);
            tvFeedPrivacy = itemView.findViewById(R.id.tvFeedPrivacy);
            ivUserImage = itemView.findViewById(R.id.ivUserImage);
            ivFeedOptions = itemView.findViewById(R.id.ivFeedOptions);
            tvMainText = itemView.findViewById(R.id.tvMainText);
            ivFeedThumbnail = itemView.findViewById(R.id.ivFeedThumbnail);
            tvEnrollStatus = itemView.findViewById(R.id.tvEnrollStatus);
            tvCancelRequest = itemView.findViewById(R.id.tvCancelRequest);
            tvRequestSentAt = itemView.findViewById(R.id.tvRequestSentAt);
        }
    }

    // endregion

    //region Clear
    // Clean all elements of the recycler
    public void clear() {

        items.clear();
        notifyDataSetChanged();

    }
    //endregion


    //region cancel my sent enroll request network call

    private void cancelMySentEnrollRequest(SentEnrollRequestData sentEnrollRequestData, int pos) {

        QxmProgressDialog dialog = new QxmProgressDialog(context);
        dialog.showProgressDialog("Cancelling Request", String.format("%s is being canceled. Please wait... ", sentEnrollRequestData.getEnrolledQxm().getQuestionSet().getQuestionSetName()), false);

        Call<QxmApiResponse> rejectReceivedEnrollResponse = apiService.cancelMySentEnrollRequest(token.getToken(), sentEnrollRequestData.getId(), sentEnrollRequestData.getEnrolledQxm().getId());
        rejectReceivedEnrollResponse.enqueue(new Callback<QxmApiResponse>() {

            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {

                dialog.closeProgressDialog();
                Log.d(TAG, "StatusCode: " + response.code());
                Log.d(TAG, "Message: " + response.body());
                if (response.code() == 201) {

                    if (response.body() != null) {

                        switch (response.body().getMessage()) {
                            case "Enroll Canceled Successfully":

                                items.remove(sentEnrollRequestData);
                                notifyItemRemoved(pos);
                                Toast.makeText(context, "Enroll is Canceled Successfully", Toast.LENGTH_SHORT).show();

                                break;
                            default:

                                Toast.makeText(context, "Something went wrong, try again later", Toast.LENGTH_SHORT).show();
                                break;
                        }

                    } else {

                        Toast.makeText(context, "Response body is null !", Toast.LENGTH_SHORT).show();
                    }

                }else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                }  else {

                    Log.d(TAG, "onResponse: Status code is:  " + response.code());
                    Toast.makeText(context, "Something went wrong,try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {

                dialog.closeProgressDialog();
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));

            }
        });

    }

    //endregion
}
