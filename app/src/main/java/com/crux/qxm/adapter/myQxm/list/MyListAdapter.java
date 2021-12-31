package com.crux.qxm.adapter.myQxm.list;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crux.qxm.R;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.networkLayer.QxmApiService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.crux.qxm.utils.StaticValues.LIST_PRIVACY_PRIVATE;
import static com.crux.qxm.utils.StaticValues.LIST_PRIVACY_PUBLIC;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {


    private Context context;
    private List<com.crux.qxm.db.models.myQxm.list.List> myLists;
    private MyListAdapterListener myListAdapterListener;
    private QxmToken token;
    private QxmApiService apiService;

    public MyListAdapter(Context context, List<com.crux.qxm.db.models.myQxm.list.List> myLists,QxmToken token,MyListAdapterListener myListAdapterListener){

        this.context = context;
        this.myLists = myLists;
        this.token = token;
        this.myListAdapterListener = myListAdapterListener;
    }

    @NonNull
    @Override
    public MyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItemView = layoutInflater.inflate(R.layout.single_my_list_item,parent,false);
        return new ViewHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyListAdapter.ViewHolder holder, int position) {

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
                myListAdapterListener.onMyListSelected(myLists.get(getAdapterPosition()));
            });


            itemView.setOnLongClickListener(view -> {

                myListAdapterListener.onMyListLongClickListener(myLists.get(getAdapterPosition()));
                return false;
            });
        }
    }

    public interface MyListAdapterListener{

        void onMyListSelected(com.crux.qxm.db.models.myQxm.list.List list);
        void onMyListLongClickListener(com.crux.qxm.db.models.myQxm.list.List list);
    }

}
