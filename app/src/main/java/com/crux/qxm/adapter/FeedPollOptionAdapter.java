package com.crux.qxm.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.QxmApiResponse;
import com.crux.qxm.db.models.feed.FeedData;
import com.crux.qxm.db.models.poll.PollOption;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.utils.QxmStringIntegerChecker;
import com.crux.qxm.utils.qxmDialogs.QxmProgressDialog;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedPollOptionAdapter extends RecyclerView.Adapter<FeedPollOptionAdapter.ViewHolder> {

    //region FeedPollOptionAdapter-Properties
    private static final String TAG = "FeedPollOptionAdapter";
    private Context context;
    private String pollId;
    private List<PollOption> pollOptions;
    private QxmApiService apiService;
    private String userId;
    private String token;
    private FeedData pollFeedData;
    private AppCompatTextView tvParticipantsQuantity;
    private int participantsQuantity;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;
    private String mySelectedOption;

    // Handling WrongVote
    private boolean isVoteSubmitNetworkCallFinished = true;
    private boolean isVoted = false;
    //private int onNextCalled = 0;
    //endregion

    //region FeedPollOptionAdapter-Constructor
    public FeedPollOptionAdapter(Context context,String feedPollId, QxmApiService apiService, String userId, String token, FeedData pollFeedData, AppCompatTextView tvParticipantsQuantity,
                                 QxmFragmentTransactionHelper qxmFragmentTransactionHelper) {
        this.pollId = feedPollId;
        this.pollOptions = pollFeedData.getPollOptions();
        this.apiService = apiService;
        this.userId = userId;
        this.token = token;
        this.pollFeedData = pollFeedData;
        this.tvParticipantsQuantity = tvParticipantsQuantity;
        this.qxmFragmentTransactionHelper = qxmFragmentTransactionHelper;
        this.context = context;

        for (int i = 0; i < pollOptions.size(); i++) {
            if(pollOptions.get(i).getVoter().contains(userId)){
                Log.d(TAG, "FeedPollOptionAdapter: " + pollOptions.get(i).getTitle() +", isMeParticipated: " + true);
                pollOptions.get(i).setMeParticipatedOnPoll(true);
            }else{
                pollOptions.get(i).setMeParticipatedOnPoll(false);
            }
            pollOptions.get(i).setVoteNum(pollOptions.get(i).getVoter().size());
        }
    }
    //endregion

    //region Override-onCreateViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_poll_single_option_item, parent, false);
        return new ViewHolder(view);
    }
    //endregion

    //region Override-onBindViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.d(TAG, "Poll Options: " + pollOptions.get(position));
        PollOption pollOption = pollOptions.get(position);

        // set poll option text
        holder.rbPollOption.setText(pollOption.getTitle());

        //region GetPollParticipatorQuantity
        participantsQuantity = 0;

        if (QxmStringIntegerChecker.isInt(pollFeedData.getPollParticipatorQuantity())) {
            participantsQuantity = Integer.parseInt(pollFeedData.getPollParticipatorQuantity());
        }
        Log.d(TAG, "participantsQuantity: " + participantsQuantity);
        //endregion

        // region CheckedTheRadioButton (if I've already given the vote)
        if (pollOption.isMeParticipatedOnPoll()) {
            holder.rbPollOption.setChecked(true);
            holder.rbPollOption.setSelected(true);
            mySelectedOption = pollOption.getTitle(); // save my older vote in a variable
            isVoted = true; // save flag
        } else {
            holder.rbPollOption.setChecked(false);
            holder.rbPollOption.setSelected(false);
        }

        // endregion

        //region SetProgressBar-progress
        if (participantsQuantity != 0) {

            float currentVote = pollOption.getVoteNum();

            float votePercentage = (currentVote / participantsQuantity) * 100;
            holder.progressBar.setProgress((int) votePercentage);
            Log.d("poll_progress", "onBindViewHolder: more vote--" + holder.progressBar.getProgress());
            Log.d(TAG, String.format("pollOption: %s, currentVote: %fl, percentage: %fl", pollOption.getTitle(), currentVote, votePercentage));
        } else{
            holder.progressBar.setProgress(0);
            Log.d("poll_progress", "onBindViewHolder: zero vote--" + holder.progressBar.getProgress());
        }
        //endregion

        //region SetPollOptionVotes
        if (pollOption.getVoteNum() == 0) {
            holder.tvVoteCount.setText(R.string.no_votes);
        } else if (pollOption.getVoteNum() == 1) {
            holder.tvVoteCount.setText(R.string.one_vote);
        } else {
            holder.tvVoteCount.setText(String.format("%s votes", pollOption.getVoteNum()));
        }
        //endregion

        //region PollOptionOnClickListener
        holder.rbPollOption.setOnClickListener(v -> {

            if (isVoteSubmitNetworkCallFinished) {

                if (!holder.rbPollOption.isSelected()) {
                    // newVoteOption = pollOption.getTitle();

                    holder.rbPollOption.setChecked(true);
                    holder.rbPollOption.setSelected(true);

                    for (int i = 0; i < pollOptions.size(); i++) {
                        if (!(i == position)) {

                            if (pollOptions.get(i).isMeParticipatedOnPoll()) {
                                int voteCount = pollOptions.get(i).getVoteNum();
                                pollOptions.get(i).setVoteNum(voteCount - 1);
                                pollOptions.get(i).setMeParticipatedOnPoll(false);
                                pollOptions.get(i).getVoter().remove(userId);

                                if (QxmStringIntegerChecker.isInt(pollFeedData.getPollParticipatorQuantity()))
                                    participantsQuantity = Integer.parseInt(pollFeedData.getPollParticipatorQuantity());
                                else participantsQuantity = 0;

                                if (participantsQuantity != 0)
                                    pollFeedData.setFeedParticipantsQuantity(String.valueOf(participantsQuantity - 1));
                            }
                        }
                    }

                    participantsQuantity = 0;
                    if (QxmStringIntegerChecker.isInt(pollFeedData.getPollParticipatorQuantity())) {
                        participantsQuantity = Integer.parseInt(pollFeedData.getPollParticipatorQuantity());
                    }


                    Log.d(TAG, "onBindViewHolder: participants quantity: " + (participantsQuantity + 1));

                    //Toast.makeText(context, "vote submit", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "total vote count: " + pollOption.getVoteNum());

                    int voteCountNow = pollOption.getVoteNum() + 1;
                    Log.d(TAG, "voteCountNow: " + voteCountNow);

                    updateVoteCount(holder.tvVoteCount, voteCountNow);
                    pollOption.setVoteNum(voteCountNow);

                    Log.d(TAG, "onBindViewHolder: checked: " + (voteCountNow / (participantsQuantity + 1)) * 100);

                    float votePercentage = (voteCountNow / (participantsQuantity + 1)) * 100;

                    Log.d(TAG, "onBindViewHolder: votePercentage: " + votePercentage);

                    // single progress bar -- poll percentage shows in blue line

                    holder.progressBar.setProgress((int) votePercentage);
//                  holder.progressBar.setProgress((voteCountNow / participantsQuantity) * 100);
                    Log.d(TAG, "vote percentage: " + (voteCountNow / (participantsQuantity + 1)) * 100);


                    // text view total poll count

                    if (!isVoted) {
                        pollOption.getVoter().add(userId);
                        pollFeedData.setPollParticipatorQuantity(String.valueOf(participantsQuantity + 1));
                        updateParticipantsQuantity(participantsQuantity + 1);
                    } else {
                        pollOption.getVoter().add(userId);
                        updateParticipantsQuantity(participantsQuantity);
                    }


                    isVoteSubmitNetworkCallFinished = false;
                    holder.itemView.setClickable(false);
                    pollParticipationNetworkCall(pollOption.getTitle(), mySelectedOption, holder);
                    mySelectedOption = pollOption.getTitle();
                    //delayedVoteCastWithRxJava(true, newVoteOption, olderVoteOption);
                    pollOption.setMeParticipatedOnPoll(true);

                    notifyDataSetChanged();

                } else {
                    //olderVoteOption = pollOption.getTitle();

                    holder.rbPollOption.setChecked(false);
                    holder.rbPollOption.setSelected(false);


                    //---
                    //Toast.makeText(context, "Vote canceled", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "vote submit");
                    Log.d(TAG, "total vote count: " + pollOption.getVoteNum());


                    int voteCountNow = pollOption.getVoteNum();

                    if (voteCountNow == 0) {
                        updateVoteCount(holder.tvVoteCount, 0);
                        pollOption.setVoteNum(0);
                        Log.d(TAG, "voteCountNow: " + voteCountNow);
                    } else {
                        voteCountNow -= 1;
                        updateVoteCount(holder.tvVoteCount, voteCountNow);
                        pollOption.setVoteNum(voteCountNow);
//                    Log.d(TAG, "voteCountNow: " + voteCountNow);
                    }

                    // set progressBar progress & call polOptionAdapterListener
                    if (participantsQuantity > 1) {
                        Log.d(TAG, "vote percentage: " + (voteCountNow / (participantsQuantity - 1)) * 100);

                        float currentVote = pollOption.getVoteNum();
                        float votePercentage = (currentVote / participantsQuantity) * 100;
                        Log.d(TAG, "onBindViewHolder: votePercentage: " + votePercentage);
                        holder.progressBar.setProgress((int) votePercentage);

                        decreaseParticipantsQuantity(participantsQuantity - 1);
                        pollOption.getVoter().remove(userId);
                        pollFeedData.setPollParticipatorQuantity(String.valueOf(participantsQuantity - 1));
                    } else {
                        holder.progressBar.setProgress(0);
                        decreaseParticipantsQuantity(participantsQuantity - 1);
                        pollOption.getVoter().remove(userId);
                        pollFeedData.setPollParticipatorQuantity(String.valueOf(participantsQuantity - 1));
                    }

                    pollOption.setMeParticipatedOnPoll(false);

                    isVoteSubmitNetworkCallFinished = false;
                    holder.itemView.setClickable(false);
                    undoPollParticipationNetworkCall(pollOption.getTitle(), holder);
                    //delayedVoteCastWithRxJava(false, newVoteOption, olderVoteOption);

                    notifyDataSetChanged();
                }
            } else {
                if (holder.rbPollOption.isChecked() && !mySelectedOption.equals(holder.rbPollOption.getText().toString()))
                    holder.rbPollOption.setChecked(false);
                else if (mySelectedOption.equals(holder.rbPollOption.getText().toString()))
                    holder.rbPollOption.setChecked(true);
                Toast.makeText(context, "Your submitted vote is being processed, please wait.", Toast.LENGTH_SHORT).show();
            }

        });
        //endregion

    }
    //endregion

    //region Override-getItemCount
    @Override
    public int getItemCount() {
        return pollOptions.size();
    }
    //endregion

    // region method-pollParticipationNetworkCall

    private void pollParticipationNetworkCall(String option, String olderOption, ViewHolder holder) {

        QxmProgressDialog qxmProgressDialog = new QxmProgressDialog(context);
        qxmProgressDialog.showProgressDialog("Vote Submit", "Casting vote, please wait...", false);

        Log.d(TAG, "pollParticipationNetworkCall: userId: " + userId);
        Log.d(TAG, "pollParticipationNetworkCall: pollId: " + pollId);
        Log.d(TAG, "pollParticipationNetworkCall: option: " + option);

        Call<QxmApiResponse> pollParticipation = apiService.pollParticipation(token, userId, pollId, option, olderOption);

        pollParticipation.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: pollParticipationNetworkCall");
                Log.d(TAG, "onResponse: response.code: " + response.code());
                Log.d(TAG, "onResponse: response.body: " + response.body());
                isVoteSubmitNetworkCallFinished = true;
                qxmProgressDialog.closeProgressDialog();
                holder.itemView.setClickable(true);
                isVoted = true;
                if (response.code() == 201) {
                    mySelectedOption = option;
                    Toast.makeText(context, "Vote success", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                isVoteSubmitNetworkCallFinished = true;
                Log.d(TAG, "onFailure: pollParticipationNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                qxmProgressDialog.closeProgressDialog();
                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // endregion

    // region method-undoPollParticipationNetworkCall

    private void undoPollParticipationNetworkCall(String option, ViewHolder holder) {
        Log.d(TAG, "undoPollParticipationNetworkCall: userId: " + userId);
        Log.d(TAG, "undoPollParticipationNetworkCall: pollId: " + pollId);
        Log.d(TAG, "undoPollParticipationNetworkCall: option: " + option);

        QxmProgressDialog qxmProgressDialog = new QxmProgressDialog(context);

        qxmProgressDialog.showProgressDialog("Undo Vote", "Undoing vote, please wait...", false);

        Call<QxmApiResponse> undoPollParticipation = apiService.undoPollParticipation(token, userId, pollId, option);

        undoPollParticipation.enqueue(new Callback<QxmApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<QxmApiResponse> call, @NonNull Response<QxmApiResponse> response) {
                Log.d(TAG, "onResponse: undoPollParticipationNetworkCall");
                Log.d(TAG, "onResponse: response.code: " + response.code());
                Log.d(TAG, "onResponse: response.body: " + response.body());
                isVoteSubmitNetworkCallFinished = true;
                qxmProgressDialog.closeProgressDialog();
                holder.itemView.setClickable(true);
                isVoted = false;

                if (response.code() == 201) {
                    Toast.makeText(context, "Vote undo success", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(context, "Your login session has expired, please login again.", Toast.LENGTH_SHORT).show();
                    qxmFragmentTransactionHelper.logout();
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QxmApiResponse> call, @NonNull Throwable t) {
                isVoteSubmitNetworkCallFinished = true;
                Log.d(TAG, "onFailure: undoPollParticipationNetworkCall");
                Log.d(TAG, "onFailure: : getMessage" + t.getMessage());
                Log.d(TAG, "onFailure: getStackTrace: " + Arrays.toString(t.getStackTrace()));
                qxmProgressDialog.closeProgressDialog();
                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // endregion

    //region method-updateParticipantsQuantity
    private void updateParticipantsQuantity(int participantsQuantity) {
        tvParticipantsQuantity.setTextColor(context.getResources().getColor(R.color.colorPrimary));

        if (participantsQuantity < 2)
            tvParticipantsQuantity.setText(String.format("%s participant", participantsQuantity));
        else
            tvParticipantsQuantity.setText(String.format("%s participants", participantsQuantity));
    }
    //endregion

    //region method-decreaseParticipantsQuantity
    private void decreaseParticipantsQuantity(int participantsQuantity) {
        tvParticipantsQuantity.setTextColor(context.getResources().getColor(R.color.icon_color_gray));

        if (participantsQuantity < 2)
            tvParticipantsQuantity.setText(String.format("%s participant", participantsQuantity));
        else
            tvParticipantsQuantity.setText(String.format("%s participants", participantsQuantity));
    }
    //endregion

    //region method-updateVoteCount
    private void updateVoteCount(AppCompatTextView tvVoteCount, int updatedTotalVote) {

        if (updatedTotalVote == 0) {
            tvVoteCount.setText(R.string.no_votes);
        } else if (updatedTotalVote == 1) {

            tvVoteCount.setText(R.string.one_vote);
        } else {
            tvVoteCount.setText(String.format("%s votes", updatedTotalVote));
        }


    }
    //endregion

    //region method(rxJava)-delayedVoteCastWithRxJava
    /*private void delayedVoteCastWithRxJava(boolean isVoted, String newVoteOption, String olderVoteOption) {

        compositeDisposable.add(behaviorSubject
                .debounce(2000, TimeUnit.MILLISECONDS)
                .takeLast(2)
                .switchMapSingle((Function<String, SingleSource<QxmApiResponse>>) s -> {
                    if (isVoted)
                        return apiService.pollParticipation(token, userId, pollId, newVoteOption, olderVoteOption)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    else
                        return apiService.undoPollParticipation(token, userId, pollId, olderVoteOption)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                }).subscribeWith(getPollVoteObserver()));

        behaviorSubject.onNext("");

    }*/
    //endregion

    //region method(rxJava)-getUserSearchObserver (it will observe the api call for user search)
    /*private DisposableObserver<QxmApiResponse> getPollVoteObserver() {

        return new DisposableObserver<QxmApiResponse>() {
            @Override
            public void onNext(QxmApiResponse qxmApiResponse) {

                Log.d(TAG, "onNext: called " + onNextCalled++);

            }

            @Override
            public void onError(Throwable e) {

                Log.d(TAG, "onError poll vote :" + e.getMessage());
                Toast.makeText(getApplicationContext(), "context.getResources().getString(R.string.network_error)work connection found, connect with internet!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete poll vote called " + onNextCalled);
            }
        };

    }*/
    //endregion

    //region Class-ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rbPollOption)
        RadioButton rbPollOption;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        @BindView(R.id.tvVoteCount)
        AppCompatTextView tvVoteCount;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
    //endregion
}
