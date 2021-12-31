package com.crux.qxm.adapter.feedMenu;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crux.qxm.R;
import com.crux.qxm.db.models.menu.group.MyGroupDataInMenu;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyGroupAdapterInMenu extends RecyclerView.Adapter<MyGroupAdapterInMenu.ViewHolder> {
    private static final String TAG = "MyGroupAdapter";
    private Context context;
    private Picasso picasso;
    private List<MyGroupDataInMenu> groupDataInMenuList;
    private MyGroupAdapterInMenuListener myGroupAdapterInMenuListener;
    private AppCompatDialog dialogShareToGroup;

    public MyGroupAdapterInMenu(AppCompatDialog dialogShareToGroup, Context context, List<MyGroupDataInMenu> groupDataInMenuList, Picasso picasso, MyGroupAdapterInMenuListener myGroupAdapterInMenuListener) {
        this.dialogShareToGroup = dialogShareToGroup;
        this.context = context;
        this.groupDataInMenuList = groupDataInMenuList;
        this.picasso = picasso;
        this.myGroupAdapterInMenuListener = myGroupAdapterInMenuListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myGroupSingleItemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.rec_my_group_list_single_item_layout, parent, false
        );

        return new MyGroupAdapterInMenu.ViewHolder(myGroupSingleItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MyGroupDataInMenu myGroupDataInMenu = groupDataInMenuList.get(position);

        Log.d(TAG, "onBindViewHolder: GroupData = " + myGroupDataInMenu.toString());

        if (!TextUtils.isEmpty(myGroupDataInMenu.getGroup().getGroupInfo().getGroupImage()))
            picasso.load(myGroupDataInMenu.getGroup().getGroupInfo().getGroupImage()).into(holder.ivGroupImage);

        if (!TextUtils.isEmpty(myGroupDataInMenu.getGroup().getGroupInfo().getGroupName()))
            holder.tvGroupName.setText(myGroupDataInMenu.getGroup().getGroupInfo().getGroupName());
        else
            holder.tvGroupName.setText("");

        if (!TextUtils.isEmpty(myGroupDataInMenu.getGroup().getMemberCount().toString())) {
            if ((myGroupDataInMenu.getGroup().getMemberCount()) > 1)
                holder.tvMemberCount.setText(String.format("%s Members", myGroupDataInMenu.getGroup().getMemberCount()));
            else
                holder.tvMemberCount.setText(String.format("%s Member", myGroupDataInMenu.getGroup().getMemberCount()));
        } else
            holder.tvMemberCount.setText(R.string.zero_member);
    }


    @Override
    public int getItemCount() {
        return groupDataInMenuList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rlMyGroup)
        RelativeLayout rlMyGroup;
        @BindView(R.id.ivGroupImage)
        AppCompatImageView ivGroupImage;
        @BindView(R.id.tvGroupName)
        AppCompatTextView tvGroupName;
        @BindView(R.id.tvMemberCount)
        AppCompatTextView tvMemberCount;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                Toast.makeText(context, "The Qxm is being shared with the group \""
                        + groupDataInMenuList.get(getAdapterPosition()).getGroup().getGroupInfo().getGroupName()
                        +"\""
                        , Toast.LENGTH_SHORT).show();
                myGroupAdapterInMenuListener.onGroupInMenuSelected(groupDataInMenuList.get(getAdapterPosition()));
                dialogShareToGroup.dismiss();
            });
        }
    }

    // Clean all elements of the recycler
    public void clear() {

        groupDataInMenuList.clear();
        notifyDataSetChanged();

    }

    //region MyGroupAdapterInMenuListener
    public interface MyGroupAdapterInMenuListener {

        void onGroupInMenuSelected(MyGroupDataInMenu myGroupDataInMenu);
    }
    //endregion
}
