package com.crux.qxm.adapter.enroll;

import android.content.Context;
import android.os.Bundle;
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
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.enroll.youEnrolled.MyEnrolledData;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.TimeFormatString;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_QUESTION_OVERVIEW;
import static com.crux.qxm.utils.StaticValues.QXM_START_INSTANTLY;
import static com.crux.qxm.utils.StaticValues.QXM_START_MANUAL;

public class EnrollYouEnrolledAdapter extends RecyclerView.Adapter<EnrollYouEnrolledAdapter.ViewHolder> {

    private static final String TAG = "EnrollYouEnrolledAdapte";
    private final int NORMAL_FEED = 1;
    private Context context;
    private List<MyEnrolledData> items;
    private FragmentActivity fragmentActivity;
    private Picasso picasso;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private QxmApiService apiService;
    private QxmToken token;

    public EnrollYouEnrolledAdapter(Context context, List<MyEnrolledData> items, FragmentActivity fragmentActivity, Picasso picasso, QxmApiService apiService, QxmToken token) {
        this.context = context;
        this.items = items;
        this.fragmentActivity = fragmentActivity;
        this.picasso = picasso;
        this.token = token;
        this.apiService = apiService;
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(fragmentActivity);
    }


    @NonNull
    @Override
    public EnrollYouEnrolledAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == NORMAL_FEED){
            View enrollParticipatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.enroll_you_enrolled_feed_single_item, parent, false);
            return new EnrollYouEnrolledAdapter.ViewHolder(enrollParticipatedView);
        }else{
            View enrollParticipatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.enroll_you_enrolled_feed_single_item_for_long_name, parent, false);
            return new EnrollYouEnrolledAdapter.ViewHolder(enrollParticipatedView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull EnrollYouEnrolledAdapter.ViewHolder holder, int position) {
        MyEnrolledData myEnrolledData = items.get(position);

        Log.d(TAG, "onBindViewHolder: Normal Feed: " + myEnrolledData.toString());


        if (myEnrolledData.getEnrolledQxm().getQxmCreator() != null)
            if (myEnrolledData.getEnrolledQxm().getQxmCreator().getImage() != null) {
                picasso.load(myEnrolledData.getEnrolledQxm().getQxmCreator().getImage())
                        .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                        .into(holder.ivUserImage);
            } else {
                holder.ivUserImage.setImageDrawable(context.getResources().getDrawable(
                        R.drawable.ic_user_default
                ));
            }


        // show contest text view if contest mode is on
        if (myEnrolledData.getEnrolledQxm().getQxmSettings().isContestModeEnabled())
            holder.CVContest.setVisibility(View.VISIBLE);
        else holder.CVContest.setVisibility(View.GONE);

        holder.tvFeedName.setText(myEnrolledData.getEnrolledQxm().getQuestionSet().getQuestionSetName());
        if (myEnrolledData.getEnrolledQxm().getQxmCreator() != null)
            holder.tvFeedCreator.setText(myEnrolledData.getEnrolledQxm().getQxmCreator().getFullName());

        // show feed item time ago
        if (!TextUtils.isEmpty(myEnrolledData.getCreatedAt())) {

            long feedCreationTime = Long.parseLong(myEnrolledData.getCreatedAt());
            String feedPostTimeAgo = TimeAgo.using(feedCreationTime);
            Log.d(TAG, "onBindViewHolder: creationTime: " + feedPostTimeAgo);
            holder.tvFeedCreationTime.setText(feedPostTimeAgo);
        } else {
            Log.d(TAG, "onBindViewHolder: creationTime: null");
        }


        holder.tvFeedDescription.setText(myEnrolledData.getEnrolledQxm().getQuestionSet().getQuestionSetDescription());


        // show exam start time and date and remaining time to start
        String examStartTime = myEnrolledData.getEnrolledQxm().getQxmSettings().getQxmStartScheduleDate();

        if (examStartTime.equals(QXM_START_INSTANTLY)) {

            holder.tvExamStartTime.setText(context.getString(R.string.qxm_start_instantly_message));
            holder.tvTimeRemainingToStartExam.setVisibility(View.GONE);

        } else if (examStartTime.equals(QXM_START_MANUAL)) {
            holder.tvExamStartTime.setText(context.getString(R.string.qxm_start_manual_message));
            holder.tvTimeRemainingToStartExam.setVisibility(View.GONE);
        } else {
            String regex = "[0-9]+";
            if (examStartTime.matches(regex)) {
                long examStartTimeInLong = Long.parseLong(examStartTime);
                String examStartTimeString = "Starts at " + TimeFormatString.getTimeAndDate(examStartTimeInLong);
                holder.tvExamStartTime.setText(examStartTimeString);
            } else {
                holder.tvExamStartTime.setText(context.getString(R.string.it_is_not_defined_by_qxm_creator));
            }

        }


        // region feed main content onclick

        holder.llSingleFeedItem.setOnClickListener(v -> {

        });

        holder.tvFeedName.setOnClickListener(v -> {


            Bundle args = new Bundle();
            String qxmId = myEnrolledData.getEnrolledQxm().getId();
            Log.d(TAG, "onBindViewHolder: QxmId: " + qxmId);
            args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, qxmId);

            qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);

        });

        holder.ivUserImage.setOnClickListener(v ->
                qxmFragmentTransactionHelper.loadUserProfileFragment(
                        myEnrolledData.getEnrolledQxm().getQxmCreator().getId(), myEnrolledData.getEnrolledQxm().getQxmCreator().getFullName()
                ));

        // endregin

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

    @Override
    public int getItemViewType(int position) {

        int NORMAL_FEED_WITH_LONG_NAME = 2;

        if (items.get(position).getEnrolledQxm().getQxmCreator().getFullName().length() < 22) return NORMAL_FEED;
        else return NORMAL_FEED_WITH_LONG_NAME;

    }


    // region EnrollParticipatedFeedViewHolder

    //region Clear
    // Clean all elements of the recycler
    public void clear() {

        items.clear();
        notifyDataSetChanged();

    }

    // endregion


    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayoutCompat llSingleFeedItem;
        CardView CVContest;
        AppCompatImageView ivUserImage;
        AppCompatImageView ivFeedOptions;
        AppCompatTextView tvFeedName;
        AppCompatTextView tvFeedCreator;
        AppCompatTextView tvFeedCreationTime;
        AppCompatTextView tvFeedDescription;
        AppCompatTextView tvExamStartTime;
        AppCompatTextView tvTimeRemainingToStartExam;


        ViewHolder(View itemView) {
            super(itemView);

            llSingleFeedItem = itemView.findViewById(R.id.llSingleFeedItem);
            CVContest = itemView.findViewById(R.id.CVContest);
            ivUserImage = itemView.findViewById(R.id.ivUserImage);
            ivFeedOptions = itemView.findViewById(R.id.ivFeedOptions);
            tvFeedName = itemView.findViewById(R.id.tvFeedName);
            tvFeedCreator = itemView.findViewById(R.id.tvFeedCreator);
            tvFeedCreationTime = itemView.findViewById(R.id.tvFeedCreationTime);
            tvFeedDescription = itemView.findViewById(R.id.tvFeedDescription);
            tvExamStartTime = itemView.findViewById(R.id.tvExamStartTime);
            tvTimeRemainingToStartExam = itemView.findViewById(R.id.tvTimeRemainingToStartExam);
        }
    }
    //endregion
}
