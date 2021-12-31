package com.crux.qxm.adapter.enroll;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.enroll.peopleEnrolled.PeopleEnrolledData;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_MY_QXM_DETAILS;
import static com.crux.qxm.utils.StaticValues.QXM_PRIVACY_PUBLIC;

public class EnrollPeopleEnrolledAdapter extends RecyclerView.Adapter<EnrollPeopleEnrolledAdapter.ViewHolder> {

    // region properties
    private static final String TAG = "EnrollParticipatedAdapt";

    private Context context;
    private List<PeopleEnrolledData> items;
    private FragmentActivity fragmentActivity;
    private Picasso picasso;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    // endregion

    // region Constructor-EnrollPeopleEnrolledAdapter

    public EnrollPeopleEnrolledAdapter(Context context, List<PeopleEnrolledData> items, FragmentActivity fragmentActivity, Picasso picasso) {
        this.context = context;
        this.items = items;
        this.fragmentActivity = fragmentActivity;
        this.picasso = picasso;
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(fragmentActivity);
    }

    // endregion

    // region Override-onCreateViewHolder
    @NonNull
    @Override
    public EnrollPeopleEnrolledAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View enrollParticipatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.enroll_people_enrolled_feed_single_item, parent, false);

        return new EnrollPeopleEnrolledAdapter.ViewHolder(enrollParticipatedView);
    }

    // endregion

    // region Override-onBindViewHolder

    @Override
    public void onBindViewHolder(@NonNull EnrollPeopleEnrolledAdapter.ViewHolder holder, int position) {
        PeopleEnrolledData peopleEnrolledData = items.get(position);

        Log.d(TAG, "onBindViewHolder: Normal Feed: " + peopleEnrolledData.toString());

        // region load user profile image
        if (!TextUtils.isEmpty(peopleEnrolledData.getEnrolledUser().getModifiedProfileImage()))
            picasso.load(peopleEnrolledData.getEnrolledUser().getModifiedProfileImage())
                    .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                    .into(holder.ivUserImage);
        else
            holder.ivUserImage.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.ic_user_default
            ));

        // endregion

        // region SetUIForFeedPrivacy
        if (!TextUtils.isEmpty(peopleEnrolledData.getEnrolledQxm().getQxmSettings().getQuestionPrivacy())) {
            if (peopleEnrolledData.getEnrolledQxm().getQxmSettings().getQuestionPrivacy().equals(QXM_PRIVACY_PUBLIC)) {
                holder.tvFeedPrivacy.setText(context.getResources().getString(R.string.public_qxm));
                holder.ivFeedPrivacy.setImageDrawable(
                        context.getResources().getDrawable(R.drawable.ic_earth)
                );
            } else {
                holder.tvFeedPrivacy.setText(context.getResources().getString(R.string.private_qxm));
                holder.ivFeedPrivacy.setImageDrawable(
                        context.getResources().getDrawable(R.drawable.ic_lock)
                );
            }
        }
        // endregion

        // region SetMainText

        String mainText = String.format("<b>%s</b> has enrolled in your %s qxm <b>%s</b>.", peopleEnrolledData.getEnrolledUser().getFullName(),
                peopleEnrolledData.getEnrolledQxm().getQxmSettings().getQuestionPrivacy(), peopleEnrolledData.getEnrolledQxm().getQuestionSet().getQuestionSetName());

        holder.tvMainText.setText(Html.fromHtml(mainText));
        // endregion

        // region show contest text view if contest mode is on
        if (peopleEnrolledData.getEnrolledQxm().getQxmSettings().isContestModeEnabled())
            holder.CVContest.setVisibility(View.VISIBLE);
        else holder.CVContest.setVisibility(View.GONE);

        // endregion

        // region show thumbnail image view if feed contains thumbnail
        if (!TextUtils.isEmpty(peopleEnrolledData.getEnrolledQxm().getQuestionSet().getQuestionSetThumbnail())) {
            holder.ivFeedThumbnail.setVisibility(View.VISIBLE);
            picasso.load(peopleEnrolledData.getEnrolledQxm().getQuestionSet().getQuestionSetThumbnail()).into(holder.ivFeedThumbnail);
        } else {
            holder.ivFeedThumbnail.setVisibility(View.GONE);
        }

        // endregion

        // region Show Enrolled Time Ago

        //TODO:: get EnrolledAt from backend
        long enrolledAt = Long.parseLong(peopleEnrolledData.getCreatedAt());
        String feedPostTimeAgo = TimeAgo.using(enrolledAt);
        Log.d(TAG, "onBindViewHolder: creationTime: " + feedPostTimeAgo);
        holder.tvEnrolledAt.setText(feedPostTimeAgo);

        // endregion

        // region FeedThumbnail-OnClick

        holder.ivFeedThumbnail.setOnClickListener(v -> {
            // Todo:: goto MyQxmDetailsFragment
            Bundle args = new Bundle();
            args.putParcelable(FEED_DATA_PASS_TO_MY_QXM_DETAILS, peopleEnrolledData);

           qxmFragmentTransactionHelper.loadMyQxmDetailsFragment(args);

           // loadMyQxmDetailsFragment(args);

        });

        holder.ivFeedPrivacy.setOnClickListener(v->{

            Log.d(TAG, "EnrolledId: "+peopleEnrolledData.getId());
        });

        // endregion

        //region goto user profile

        holder.ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(
                peopleEnrolledData.getEnrolledQxm().getQxmCreator().getId(),peopleEnrolledData.getEnrolledUser().getFullName())
        );

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

    // endregion

    // region getItemCount
    @Override
    public int getItemCount() {
        return items.size();
    }
    // endregion

    // region FollowingViewHolder-EnrollParticipatedFeedAdapter

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView CVContest;
        AppCompatImageView ivFeedPrivacy;
        AppCompatTextView tvFeedPrivacy;
        AppCompatImageView ivUserImage;
        AppCompatImageView ivFeedOptions;
        AppCompatImageView ivFeedThumbnail;
        AppCompatTextView tvMainText;
        AppCompatTextView tvEnrolledAt;


        ViewHolder(View itemView) {
            super(itemView);

            CVContest = itemView.findViewById(R.id.CVContest);
            ivFeedPrivacy = itemView.findViewById(R.id.ivFeedPrivacy);
            tvFeedPrivacy = itemView.findViewById(R.id.tvFeedPrivacy);
            ivUserImage = itemView.findViewById(R.id.ivUserImage);
            ivFeedOptions = itemView.findViewById(R.id.ivFeedOptions);
            ivFeedThumbnail = itemView.findViewById(R.id.ivFeedThumbnail);
            tvMainText = itemView.findViewById(R.id.tvMainText);
            tvEnrolledAt = itemView.findViewById(R.id.tvEnrolledAt);

        }
    }

    // endregion

    //region ClearAdapterListItems
    // Clean all elements of the recycler
    public void clear() {

        items.clear();
        notifyDataSetChanged();

    }
    //endregion

}
