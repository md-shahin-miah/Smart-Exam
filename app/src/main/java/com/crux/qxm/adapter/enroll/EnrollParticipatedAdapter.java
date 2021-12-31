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
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.enroll.youParticipated.youParticipatedNew.YouParticipatedData;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.QxmStringIntegerChecker;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import static com.crux.qxm.utils.StaticValues.FEED_DATA_PASS_TO_QUESTION_OVERVIEW;

public class EnrollParticipatedAdapter extends RecyclerView.Adapter<EnrollParticipatedAdapter.ViewHolder> {

    private static final String TAG = "EnrollParticipatedAdapt";
    private final int NORMAL_FEED = 1;
    private final int NORMAL_FEED_WITH_LONG_NAME = 2;

    private Context context;
    private List<YouParticipatedData> items;
    private Picasso picasso;
    private FragmentActivity fragmentActivity;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;

    public EnrollParticipatedAdapter(Context context, List<YouParticipatedData> items, FragmentActivity fragmentActivity, Picasso picasso) {
        this.context = context;
        this.items = items;
        this.fragmentActivity = fragmentActivity;
        this.picasso = picasso;
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(fragmentActivity);

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == NORMAL_FEED) {
            View enrollParticipatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.enroll_participated_feed_single_item, parent, false);
            return new ViewHolder(enrollParticipatedView);
        } else if(viewType == NORMAL_FEED_WITH_LONG_NAME){
            View enrollParticipatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.enroll_participated_feed_single_item_for_long_name, parent, false);
            return new ViewHolder(enrollParticipatedView);
        }else {
            View enrollParticipatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.enroll_participated_feed_single_mcq_item, parent, false);
            return new ViewHolder(enrollParticipatedView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        YouParticipatedData youParticipatedData = items.get(position);

        Log.d(TAG, "onBindViewHolder: Normal Feed: " + youParticipatedData.toString());

        if (youParticipatedData.getParticipatedQxm().getQxmCreatorId() != null) {

            // region YouParticipated in QXM
             /*show contest text view if contest mode is on*/
            if (youParticipatedData.getParticipatedQxm().getQxmSettings().isContestModeEnabled()){

                holder.CVContest.setVisibility(View.VISIBLE);

                if(QxmStringIntegerChecker.isLongInt(youParticipatedData.getParticipatedQxm().getQxmSettings().getCorrectAnswerVisibilityDate())){

                    long correctAnswerVisibleTime = Long.parseLong(youParticipatedData.getParticipatedQxm().getQxmSettings().getCorrectAnswerVisibilityDate());

                    long currentTime = Calendar.getInstance().getTimeInMillis();

                    if(currentTime > correctAnswerVisibleTime){
                        // Show see result button
                        holder.tvResultStatus.setText(context.getString(R.string.see_result));
                        holder.tvResultStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));

                        holder.tvResultStatus.setOnClickListener(v -> qxmFragmentTransactionHelper
                                .loadFullResultFragment(youParticipatedData.getParticipatedQxm().getId()));
                    }else{
                        holder.tvResultStatus.setText(context.getString(R.string.result_pending));
                    }
                }
            }
            else{
                holder.CVContest.setVisibility(View.GONE);

                // Show see result button
                String resultPublished = "See Result";
                holder.tvResultStatus.setText(resultPublished);
                holder.tvResultStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));

                holder.tvResultStatus.setOnClickListener(v -> qxmFragmentTransactionHelper
                        .loadFullResultFragment(youParticipatedData.getParticipatedQxm().getId()));

            }

            // show contest text view if contest mode is on
            if (youParticipatedData.getParticipatedQxm().getQxmSettings().isContestModeEnabled())
                holder.CVContest.setVisibility(View.VISIBLE);
            else
                holder.CVContest.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(youParticipatedData.getParticipatedQxm().getQxmCreatorId().getModifiedProfileImage()))
                picasso.load(youParticipatedData.getParticipatedQxm().getQxmCreatorId().getModifiedProfileImage())
                        .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                        .into(holder.ivUserImage);
            else
                holder.ivUserImage.setImageDrawable(context.getResources().getDrawable(
                        R.drawable.ic_user_default
                ));

            holder.tvFeedCreator.setText(youParticipatedData.getParticipatedQxm().getQxmCreatorId().getFullName());

            holder.ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(
                    youParticipatedData.getParticipatedQxm().getQxmCreatorId().getId(),
                    youParticipatedData.getParticipatedQxm().getQxmCreatorId().getFullName()
            ));

            holder.tvFeedName.setOnClickListener(v -> loadQuestionOverviewFragment(youParticipatedData.getParticipatedQxm().getId()));

            holder.tvFeedDescription.setOnClickListener(v -> loadQuestionOverviewFragment(youParticipatedData.getParticipatedQxm().getId()));

            holder.llSingleFeedItem.setOnClickListener(v -> loadQuestionOverviewFragment(youParticipatedData.getParticipatedQxm().getId()));

            // endregion

        } else {

            // region YouParticipated in Single MCQ

            // show contest text view if contest mode is on
            if(youParticipatedData.getParticipatedQxm().getSingleMCQSettings() != null){

                if (youParticipatedData.getParticipatedQxm().getSingleMCQSettings().isContestModeEnabled()){

                    holder.CVContest.setVisibility(View.VISIBLE);

                    if(QxmStringIntegerChecker.isLongInt(youParticipatedData.getParticipatedQxm().getSingleMCQSettings().getCorrectAnswerVisibilityDate())){

                        long correctAnswerVisibleTime = Long.parseLong(youParticipatedData.getParticipatedQxm().getSingleMCQSettings().getCorrectAnswerVisibilityDate());

                        long currentTime = Calendar.getInstance().getTimeInMillis();

                        if(currentTime > correctAnswerVisibleTime){
                            // Show see result button
                            holder.tvResultStatus.setText(context.getString(R.string.see_result));
                            holder.tvResultStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));

                            holder.tvResultStatus.setOnClickListener(v -> qxmFragmentTransactionHelper
                                    .loadFullResultSingleMCQFragment(youParticipatedData.getParticipatedQxm().getId()));
                        }else{
                            holder.tvResultStatus.setText(context.getString(R.string.result_pending));
                        }
                    }else {
                        // Show see result button
                        holder.tvResultStatus.setText(context.getString(R.string.see_result));
                        holder.tvResultStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));

                        holder.tvResultStatus.setOnClickListener(v -> qxmFragmentTransactionHelper
                                .loadFullResultSingleMCQFragment(youParticipatedData.getParticipatedQxm().getId()));
                    }

                }
                else{
                    holder.CVContest.setVisibility(View.GONE);

                    // Show see result button
                    String resultPublished = "See Result";
                    holder.tvResultStatus.setText(resultPublished);
                    holder.tvResultStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));

                    holder.tvResultStatus.setOnClickListener(v -> qxmFragmentTransactionHelper
                            .loadFullResultSingleMCQFragment(youParticipatedData.getParticipatedQxm().getId()));

                }
            }else{
                // Show see result button
                holder.tvResultStatus.setText(context.getString(R.string.see_result));
                holder.tvResultStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));

                holder.tvResultStatus.setOnClickListener(v -> qxmFragmentTransactionHelper
                        .loadFullResultSingleMCQFragment(youParticipatedData.getParticipatedQxm().getId()));
            }


            holder.tvFeedName.setText(youParticipatedData.getParticipatedQxm().getSingleMCQ().getQuestionTitle());

            if (!TextUtils.isEmpty(youParticipatedData.getParticipatedQxm().getCreator().getModifiedProfileImage()))
                picasso.load(youParticipatedData.getParticipatedQxm().getCreator().getModifiedProfileImage())
                        .error(context.getResources().getDrawable(R.drawable.ic_user_default))
                        .into(holder.ivUserImage);
            else
                holder.ivUserImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_user_default));

            holder.tvFeedCreator.setText(youParticipatedData.getParticipatedQxm().getCreator().getFullName());

            holder.ivUserImage.setOnClickListener(v -> qxmFragmentTransactionHelper.loadUserProfileFragment(
                    youParticipatedData.getParticipatedQxm().getCreator().getId(),
                    youParticipatedData.getParticipatedQxm().getCreator().getFullName()
            ));

            //endregion
        }

        if (youParticipatedData.getParticipatedQxm().getQuestionSet() != null)
            holder.tvFeedName.setText(youParticipatedData.getParticipatedQxm().getQuestionSet().getQuestionSetName());

        holder.tvParticipantsQuantity.setText(String.format("%s %s", youParticipatedData.getParticipatedQxm().getParticipationNum(), context.getResources().getString(R.string.participants)));
        if (youParticipatedData.getParticipatedQxm().getParticipationNum() == 0) {
            holder.tvParticipantsQuantity.setTextColor(context.getResources().getColor(R.color.icon_color_gray));
        }

        // show feed item time ago
        long feedCreationTime = Long.parseLong(youParticipatedData.getParticipatedQxm().getCreatedAt());
        String feedPostTimeAgo = TimeAgo.using(feedCreationTime);
        Log.d(TAG, "onBindViewHolder: creationTime: " + feedPostTimeAgo);
        holder.tvFeedCreationTime.setText(feedPostTimeAgo);


        //holder.tvFeedDescription.setText(youParticipatedData.getQxmSetDescription());


        // region SetParticipateTime

        if (!TextUtils.isEmpty(youParticipatedData.getParticipatedAt())) {
            long participationTime = Long.parseLong(youParticipatedData.getParticipatedAt());
            String participationTimeAgo = TimeAgo.using(participationTime);
            holder.tvParticipationTime.setText(String.format("Participated %s", participationTimeAgo));
        } else {
            Log.d(TAG, "participation time is null");
            holder.tvParticipationTime.setVisibility(View.GONE);
        }

        // endregion


        // region feed main content onclick


        // endregion

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

    private void loadQuestionOverviewFragment(String qxmId) {
        Log.d("QxmID", qxmId);
        Bundle args = new Bundle();
        args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, qxmId);

        qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + items.size());
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (items.get(position).getParticipatedQxm().getQxmCreatorId() != null) {

            if (items.get(position).getParticipatedQxm().getQxmCreatorId().getFullName().length() < 22)
                return NORMAL_FEED;
            else return NORMAL_FEED_WITH_LONG_NAME;
        } else {
            /*if (items.get(position).getParticipatedQxm().getCreator().getFullName().length() < 22)
                return NORMAL_FEED;
            else return NORMAL_FEED_WITH_LONG_NAME;*/
            int SINGLE_MCQ_FEED = 3;
            return SINGLE_MCQ_FEED;
        }
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
        FrameLayout flFeedThumbnailContainer;
        CardView CVContest;
        AppCompatImageView ivUserImage;
        AppCompatImageView ivFeedOptions;
        AppCompatTextView tvFeedName;
        AppCompatTextView tvFeedCreator;
        AppCompatTextView tvFeedCreationTime;
        AppCompatTextView tvFeedDescription;
        AppCompatTextView tvParticipantsQuantity;
        AppCompatTextView tvResultStatus;
        AppCompatTextView tvParticipationTime;


        ViewHolder(View itemView) {
            super(itemView);

            llSingleFeedItem = itemView.findViewById(R.id.llSingleFeedItem);
            flFeedThumbnailContainer = itemView.findViewById(R.id.FLFeedThumbnailContainer);
            CVContest = itemView.findViewById(R.id.CVContest);
            ivUserImage = itemView.findViewById(R.id.ivUserImage);
            ivFeedOptions = itemView.findViewById(R.id.ivFeedOptions);
            tvFeedName = itemView.findViewById(R.id.tvFeedName);
            tvFeedCreator = itemView.findViewById(R.id.tvFeedCreator);
            tvFeedCreationTime = itemView.findViewById(R.id.tvFeedCreationTime);
            tvFeedDescription = itemView.findViewById(R.id.tvFeedDescription);
            tvParticipantsQuantity = itemView.findViewById(R.id.tvParticipantsQuantity);
            tvResultStatus = itemView.findViewById(R.id.tvResultStatus);
            tvParticipationTime = itemView.findViewById(R.id.tvParticipationTime);

            itemView.setOnClickListener(v -> {
                YouParticipatedData youParticipatedData = items.get(getAdapterPosition());
                if (youParticipatedData.getParticipatedQxm().getQxmCreatorId() != null) {
                    Bundle args = new Bundle();
                    args.putString(FEED_DATA_PASS_TO_QUESTION_OVERVIEW, items.get(getAdapterPosition()).getParticipatedQxm().getId());
                    qxmFragmentTransactionHelper.loadQuestionOverviewFragment(args);
                } else {

                    Log.d(TAG, "ViewHolder: single mcq data");

                    if (youParticipatedData.getParticipatedQxm().getSingleMCQSettings().isContestModeEnabled()){
                        if(QxmStringIntegerChecker.isLongInt(youParticipatedData.getParticipatedQxm().getSingleMCQSettings().getCorrectAnswerVisibilityDate())){

                            long correctAnswerVisibleTime = Long.parseLong(youParticipatedData.getParticipatedQxm().getSingleMCQSettings().getCorrectAnswerVisibilityDate());

                            long currentTime = Calendar.getInstance().getTimeInMillis();

                            if(currentTime > correctAnswerVisibleTime){
                                qxmFragmentTransactionHelper
                                        .loadFullResultSingleMCQFragment(youParticipatedData.getParticipatedQxm().getId());
                            }else {
                                Toast.makeText(context, "Result Pending", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            qxmFragmentTransactionHelper
                                    .loadFullResultSingleMCQFragment(youParticipatedData.getParticipatedQxm().getId());
                        }

                    }else{
                        qxmFragmentTransactionHelper
                                .loadFullResultSingleMCQFragment(youParticipatedData.getParticipatedQxm().getId());
                    }
                }
            });


        }
    }
    //endregion


}
