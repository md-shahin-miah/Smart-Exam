package com.crux.qxm.adapter.search;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crux.qxm.R;
import com.crux.qxm.db.models.search.group.SearchedGroup;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchedGroupAdapter extends RecyclerView.Adapter<SearchedGroupAdapter.ViewHolder> {

    private Context context;
    private List<SearchedGroup> searchedGroupList;
    private Picasso picasso;
    private GroupAdapterListener groupAdapterListener;

    public SearchedGroupAdapter(Context context, List<SearchedGroup> searchedGroupList,Picasso picasso,GroupAdapterListener groupAdapterListener){

        this.context = context;
        this.searchedGroupList = searchedGroupList;
        this.picasso = picasso;
        this.groupAdapterListener = groupAdapterListener;
    }

    @NonNull
    @Override
    public SearchedGroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View searchedGroupListView = inflater.inflate(R.layout.single_searched_group_row,parent,false);
        return new SearchedGroupAdapter.ViewHolder(searchedGroupListView);

    }

    @Override
    public void onBindViewHolder(@NonNull SearchedGroupAdapter.ViewHolder holder, int position) {

        SearchedGroup searchedGroup = searchedGroupList.get(position);

        if(!TextUtils.isEmpty(searchedGroup.getSearchedGroupInfo().getGroupName()))
            holder.tvGroupName.setText(searchedGroup.getSearchedGroupInfo().getGroupName());
        if(!TextUtils.isEmpty(searchedGroup.getSearchedGroupInfo().getGroupThumbnail()))
            picasso.load(searchedGroup.getSearchedGroupInfo().getGroupThumbnail()).into(holder.ivGroupThumbnail);
    }

    @Override
    public int getItemCount() {
        return searchedGroupList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvGroupName)
        AppCompatTextView tvGroupName;
        @BindView(R.id.ivGroupThumbnail)
        AppCompatImageView ivGroupThumbnail;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(v->{

                groupAdapterListener.onGroupSelected(searchedGroupList.get(getAdapterPosition()));

            });
        }
    }

    public interface GroupAdapterListener{
        void onGroupSelected(SearchedGroup searchedGroup);
    }
}
