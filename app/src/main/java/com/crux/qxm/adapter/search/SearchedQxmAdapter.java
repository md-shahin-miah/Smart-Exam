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
import com.crux.qxm.db.models.search.qxm.SearchedQxm;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchedQxmAdapter extends RecyclerView.Adapter<SearchedQxmAdapter.ViewHolder> {

    private Context context;
    private List<SearchedQxm> searchedQxmList;
    private QxmAdapterListener qxmAdapterListener;

    public SearchedQxmAdapter(Context context,List<SearchedQxm> searchedUserList,QxmAdapterListener qxmAdapterListener){

        this.context = context;
        this.searchedQxmList = searchedUserList;
        this.qxmAdapterListener = qxmAdapterListener;
    }

    @NonNull
    @Override
    public SearchedQxmAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View searchedQxmListView = inflater.inflate(R.layout.single_searched_qxm_row,parent,false);
        return new SearchedQxmAdapter.ViewHolder(searchedQxmListView);

    }

    @Override
    public void onBindViewHolder(@NonNull SearchedQxmAdapter.ViewHolder holder, int position) {

        SearchedQxm searchedQxm = searchedQxmList.get(position);

        if(!TextUtils.isEmpty(searchedQxm.getQxmTitle()))
            holder.tvQxmTitle.setText(searchedQxm.getQxmTitle());
        if (!TextUtils.isEmpty(searchedQxm.getQxmCreatorName()))
            holder.tvQxmCreator.setText(searchedQxm.getQxmCreatorName());
    }

    @Override
    public int getItemCount() {
        return searchedQxmList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvQxmTitle)
        AppCompatTextView tvQxmTitle;

        @BindView(R.id.tvQxmCreator)
        AppCompatTextView tvQxmCreator;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(v->{
                qxmAdapterListener.onQxmSelected(searchedQxmList.get(getAdapterPosition()));
            });
        }
    }

    public interface QxmAdapterListener{

        void onQxmSelected(SearchedQxm searchedQxm);
    }
}
