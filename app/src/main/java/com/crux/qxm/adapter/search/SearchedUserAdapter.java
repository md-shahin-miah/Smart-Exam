package com.crux.qxm.adapter.search;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.search.user.SearchedUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchedUserAdapter extends RecyclerView.Adapter<SearchedUserAdapter.ViewHolder> {

    private Context context;
    private List<SearchedUser> searchedUserList;
    private Picasso picasso;
    private UserAdapterListener userAdapterListener;


    public SearchedUserAdapter(Context context,List<SearchedUser> searchedUserList,Picasso picasso,UserAdapterListener userAdapterListener){

        this.context = context;
        this.searchedUserList = searchedUserList;
        this.picasso = picasso;
        this.userAdapterListener = userAdapterListener;
    }


    @NonNull
    @Override
    public SearchedUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View searchedUserListView = inflater.inflate(R.layout.single_searched_user_row,parent,false);
        return new SearchedUserAdapter.ViewHolder(searchedUserListView);

    }

    @Override
    public void onBindViewHolder(@NonNull SearchedUserAdapter.ViewHolder holder, int position) {

        if(!TextUtils.isEmpty(searchedUserList.get(position).getFullName()))
            holder.tvUserFullName.setText(searchedUserList.get(position).getFullName());
        if(!TextUtils.isEmpty(searchedUserList.get(position).getModifiedProfileImage()))
            picasso.load(searchedUserList.get(position).getModifiedProfileImage()).into(holder.ivUserImage);
        else picasso.load(R.drawable.ic_user_default).into(holder.ivUserImage);
    }

    @Override
    public int getItemCount() {
        return searchedUserList.size();
    }


    //region viewHolder
    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.ivUserImage)
        AppCompatImageView ivUserImage;
        @BindView(R.id.tvUserFullName)
        AppCompatTextView tvUserFullName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(v->{

                userAdapterListener.onUserSelected(searchedUserList.get(getAdapterPosition()));

            });

        }
    }
    //endregion

    // region UserAdapterListener
    public interface UserAdapterListener{
        void onUserSelected(SearchedUser searchedUser);
    }
    //endregion
}
