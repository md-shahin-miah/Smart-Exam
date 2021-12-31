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
import com.crux.qxm.db.models.search.singleMCQ.SearchedSingleMcq;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchedSingleMCQAdapter extends RecyclerView.Adapter<SearchedSingleMCQAdapter.ViewHolder> {

    private Context context;
    private List<SearchedSingleMcq> searchedQxmList;
    private SingleMcqAdapterListener singleMcqAdapterListener;

    public SearchedSingleMCQAdapter(Context context, List<SearchedSingleMcq> searchedUserList, SingleMcqAdapterListener singleMcqAdapterListener){

        this.context = context;
        this.searchedQxmList = searchedUserList;
        this.singleMcqAdapterListener = singleMcqAdapterListener;
    }

    @NonNull
    @Override
    public SearchedSingleMCQAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View searchedQxmListView = inflater.inflate(R.layout.single_searched_single_mcq_row,parent,false);
        return new SearchedSingleMCQAdapter.ViewHolder(searchedQxmListView);

    }

    @Override
    public void onBindViewHolder(@NonNull SearchedSingleMCQAdapter.ViewHolder holder, int position) {

        SearchedSingleMcq searchedQxm = searchedQxmList.get(position);

        if(!TextUtils.isEmpty(searchedQxm.getFeedTitle()))
            holder.tvSingleMCQTitle.setText(searchedQxm.getFeedTitle());
        if (!TextUtils.isEmpty(searchedQxm.getFeedCreatorName()))
            holder.tvSingleMCQCreator.setText(searchedQxm.getFeedCreatorName());
    }

    @Override
    public int getItemCount() {
        return searchedQxmList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvSingleMCQTitle)
        AppCompatTextView tvSingleMCQTitle;

        @BindView(R.id.tvPollCreator)
        AppCompatTextView tvSingleMCQCreator;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(v->{
                singleMcqAdapterListener.onSingleMCQSelected(searchedQxmList.get(getAdapterPosition()));
            });
        }
    }

    public interface SingleMcqAdapterListener {

        void onSingleMCQSelected(SearchedSingleMcq searchedSingleMcq);
    }
}
