package com.crux.qxm.adapter.feedMenu;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.crux.qxm.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.crux.qxm.utils.StaticValues.LIST_PRIVACY_PRIVATE;
import static com.crux.qxm.utils.StaticValues.LIST_PRIVACY_PUBLIC;

public class MyListAdapterInMenu extends RecyclerView.Adapter<MyListAdapterInMenu.ViewHolder> {

    private AppCompatDialog dialogAddToList;
    private Context context;
    private List<com.crux.qxm.db.models.myQxm.list.List> myLists;
    private MyListAdapterInMenuListener myListAdapterInMenuListener;

    public MyListAdapterInMenu(AppCompatDialog dialogAddToList, Context context, List<com.crux.qxm.db.models.myQxm.list.List> myLists, MyListAdapterInMenuListener myListAdapterInMenuListener){
        this.dialogAddToList = dialogAddToList;
        this.context = context;
        this.myLists = myLists;
        this.myListAdapterInMenuListener = myListAdapterInMenuListener;
    }

    @NonNull
    @Override
    public MyListAdapterInMenu.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItemView = layoutInflater.inflate(R.layout.feed_menu_single_list_item,parent,false);
        return new ViewHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyListAdapterInMenu.ViewHolder holder, int position) {

        com.crux.qxm.db.models.myQxm.list.List myList = myLists.get(position);

        holder.tvListName.setText(myList.getListSettings().getListName());

        String listPrivacy = myList.getListSettings().getListPrivacy();
        holder.tvListPrivacy.setText(myList.getListSettings().getListPrivacy());

        //setting suitable privacy icon based on privacy
        if(listPrivacy.equals(LIST_PRIVACY_PUBLIC))
            holder.tvListPrivacy.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_privacy_public),null,null,null);
        else if(listPrivacy.equals(LIST_PRIVACY_PRIVATE))
            holder.tvListPrivacy.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_privacy_private),null,null,null);

    }

    @Override
    public int getItemCount() {
        return myLists.size();
    }

    public void clear() {
        myLists.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvListName)
        AppCompatTextView tvListName;
        @BindView(R.id.tvListPrivacy)
        AppCompatTextView tvListPrivacy;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(v->{
                Toast.makeText(context, "The Qxm is being added to list \""
                        + myLists.get(getAdapterPosition()).getListSettings().getListName()
                        + "\".", Toast.LENGTH_SHORT).show();
                myListAdapterInMenuListener.onMyListSelected(myLists.get(getAdapterPosition()));
                dialogAddToList.dismiss();
            });
        }
    }


    public interface MyListAdapterInMenuListener {

        void onMyListSelected(com.crux.qxm.db.models.myQxm.list.List list);
    }
}
