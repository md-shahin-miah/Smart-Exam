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
import com.crux.qxm.db.models.search.list.SearchedList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchedListAdapter extends RecyclerView.Adapter<SearchedListAdapter.ViewHolder> {

    private Context context;
    private List<SearchedList> searchedListLists;
    private SearchedListAdapterListener searchedListAdapterListener;

    public SearchedListAdapter(Context context, List<SearchedList> searchedListLists, SearchedListAdapterListener searchedListAdapterListener){

        this.context = context;
        this.searchedListLists = searchedListLists;
        this.searchedListAdapterListener = searchedListAdapterListener;
    }

    @NonNull
    @Override
    public SearchedListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View searchedListView = inflater.inflate(R.layout.single_searched_list_row,parent,false);
        return new SearchedListAdapter.ViewHolder(searchedListView);

    }

    @Override
    public void onBindViewHolder(@NonNull SearchedListAdapter.ViewHolder holder, int position) {

        SearchedList searchedList = searchedListLists.get(position);

        if(!TextUtils.isEmpty(searchedList.getListName()))
            holder.tvListName.setText(searchedList.getListName());
        if (!TextUtils.isEmpty(searchedList.getListPrivacy()))
            holder.tvListPrivacy.setText(searchedList.getListPrivacy());
    }

    @Override
    public int getItemCount() {
        return searchedListLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvListName)
        AppCompatTextView tvListName;

        @BindView(R.id.tvListPrivacy)
        AppCompatTextView tvListPrivacy;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(v->{
                searchedListAdapterListener.onListSelected(searchedListLists.get(getAdapterPosition()));
            });
        }
    }

    public interface SearchedListAdapterListener {

        void onListSelected(SearchedList searchedList);
    }
}
