package com.crux.qxm.adapter.search;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crux.qxm.R;
import com.crux.qxm.db.models.search.poll.SearchedPoll;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchedPollAdapter extends RecyclerView.Adapter<SearchedPollAdapter.ViewHolder> {

    private Context context;
    private List<SearchedPoll> searchedPollList;
    private SinglePollAdapterListener singlePollAdapterListener;

    public SearchedPollAdapter(Context context, List<SearchedPoll> searchedUserList, SinglePollAdapterListener singlePollAdapterListener){

        this.context = context;
        this.searchedPollList = searchedUserList;
        this.singlePollAdapterListener = singlePollAdapterListener;
    }

    @NonNull
    @Override
    public SearchedPollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View searchedPollListView = inflater.inflate(R.layout.single_searched_poll_row,parent,false);
        return new SearchedPollAdapter.ViewHolder(searchedPollListView);

    }

    @Override
    public void onBindViewHolder(@NonNull SearchedPollAdapter.ViewHolder holder, int position) {

        SearchedPoll searchedPoll = searchedPollList.get(position);

        if(!TextUtils.isEmpty(searchedPoll.getFeedTitle()))
            holder.tvPollTitle.setText(searchedPoll.getFeedTitle());
        if (!TextUtils.isEmpty(searchedPoll.getFeedCreatorName()))
            holder.tvPollCreator.setText(searchedPoll.getFeedCreatorName());
    }

    @Override
    public int getItemCount() {
        return searchedPollList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvPollTitle)
        AppCompatTextView tvPollTitle;

        @BindView(R.id.tvPollCreator)
        AppCompatTextView tvPollCreator;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(v->{
                singlePollAdapterListener.onPollSelected(searchedPollList.get(getAdapterPosition()));
            });
        }
    }

    public interface SinglePollAdapterListener {

        void onPollSelected(SearchedPoll searchedPoll);
    }
}
