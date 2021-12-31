package com.crux.qxm.adapter.profile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.networkLayer.userInfoUpdate.InfoUpdater;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

/**
 * Created by frshafi on 2/8/18.
 */

public class WebsiteAdapter extends RecyclerView.Adapter<WebsiteAdapter.ViewHolder> {

    private static final String TAG = "WebsiteAdapter";

    private Context context;
    private List<String> websiteList;
    private QxmApiService apiService;
    private QxmToken token;

    public WebsiteAdapter(Context context,List<String> websiteList,QxmApiService apiService,QxmToken token) {
        this.context = context;
        this.websiteList = websiteList;
        this.apiService = apiService;
        this.token = token;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return context;
    }

    @Override
    public WebsiteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View socialView = inflater.inflate(R.layout.single_website_layout,null);
        return new WebsiteAdapter.ViewHolder(socialView);

    }

    @Override
    public void onBindViewHolder(WebsiteAdapter.ViewHolder holder, int position) {

        holder.websiteLinkTV.setText(websiteList.get(position));


        // editing website link
        holder.iconEditWebsiteIV.setOnClickListener(v->{

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
            LayoutInflater inflater = LayoutInflater.from(context);
            View bottomSheetView = inflater.inflate(R.layout.bottom_dialog_edit_website,null);
            bottomSheetDialog.setContentView(bottomSheetView);
            //bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();

            EditText websiteLinkET = bottomSheetView.findViewById(R.id.websiteLinkET);
            websiteLinkET.setText(holder.websiteLinkTV.getText());
            String previousWebsite = holder.websiteLinkTV.getText().toString().trim();


            ImageView doneIV = bottomSheetView.findViewById(R.id.doneIV);
            doneIV.setOnClickListener(vDone->{

                if(!websiteLinkET.getText().toString().isEmpty()){
                    holder.websiteLinkTV.setText(websiteLinkET.getText());
                    websiteList.set(position,websiteLinkET.getText().toString().trim());


                    String newWebsite = websiteLinkET.getText().toString().trim();
                    Log.d(TAG, "onBindViewHolder previousWebsite"+previousWebsite);
                    Log.d(TAG, "onBindViewHolder newWebsite"+newWebsite);
                    Log.d(TAG, "onBindViewHolder linkType website");

                    // sending edited data in the database
                    InfoUpdater.editUpdateOrDeleteWebsite(getContext(),apiService,token,previousWebsite,newWebsite,"Website edited successfully!");
                    notifyDataSetChanged();
                    bottomSheetDialog.hide();
                }else Toast.makeText(getContext(), "Link can not be empty", Toast.LENGTH_SHORT).show();

            });
        });

        // deleting single website
        holder.rlWebsiteContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AppCompatDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Do you really want to delete this item ?");

                builder.setPositiveButton("Yes", (dialogInterface, i) -> {

                    websiteList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,getItemCount());

                    String previousWebsite = holder.websiteLinkTV.getText().toString().trim();
                    // newWebSite name will be left blank when delete an item
                    String newWebsite = "";


                    Log.d(TAG, "onBindViewHolder previousWebsite: "+previousWebsite);
                    Log.d(TAG, "onBindViewHolder newWebsite: "+newWebsite);
                    Log.d(TAG, "onBindViewHolder linkType website");

                    InfoUpdater.editUpdateOrDeleteWebsite(context,apiService,token,previousWebsite,newWebsite,"Website deleted successfully!");
                    Log.d("WebsiteList", websiteList.toString());

                });

                builder.setNegativeButton("No", (dialog1, which) -> {

                });
                dialog = builder.create();

                dialog.show();


                return false;
            }
        });



    }

    @Override
    public int getItemCount() {
        return websiteList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView websiteLinkTV;
        ImageView iconEditWebsiteIV;
        RelativeLayout rlWebsiteContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            websiteLinkTV = itemView.findViewById(R.id.websiteLinkTV);
            iconEditWebsiteIV =itemView.findViewById(R.id.iconEditWebsiteIV);
            rlWebsiteContainer = itemView.findViewById(R.id.rlWebsiteContainer);
        }
    }

}
