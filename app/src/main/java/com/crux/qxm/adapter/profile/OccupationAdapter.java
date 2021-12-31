package com.crux.qxm.adapter.profile;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.users.Occupation;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.networkLayer.userInfoUpdate.InfoUpdater;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by frshafi on 2/9/18.
 */

public class OccupationAdapter extends RecyclerView.Adapter<OccupationAdapter.ViewHolder> {


    private static final String TAG = "OccupationAdapter";

    private Context context;
    private List<Occupation> occupationList;
    private QxmApiService apiService;
    private QxmToken token;

    public OccupationAdapter(Context context, List<Occupation> occupationList, QxmApiService apiService, QxmToken token) {
        this.context = context;
        this.occupationList = occupationList;
        this.apiService = apiService;
        this.token = token;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return context;
    }


    @NonNull
    @Override
    public OccupationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View occupationView = inflater.inflate(R.layout.single_occupation_row,parent,false);
        return new ViewHolder(occupationView);

    }

    @Override
    public void onBindViewHolder(@NonNull OccupationAdapter.ViewHolder holder, int position) {

        holder.tvOccupationTitle.setText(occupationList.get(position).getDesignation());
        holder.tvOrganizationName.setText(occupationList.get(position).getOrganization());
        holder.tvWorkDuration.setText(occupationList.get(position).getWorkDuration());

        holder.ivEditOccupation.setOnClickListener(v->{


            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
            LayoutInflater inflater = LayoutInflater.from(context);
            View bottomSheetView = inflater.inflate(R.layout.bottom_sheet_occupation_edit,null);
            bottomSheetDialog.setContentView(bottomSheetView);
            //bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();

            AppCompatEditText etDesignation = bottomSheetView.findViewById(R.id.etDesignation);
            AppCompatEditText etOrganization = bottomSheetView.findViewById(R.id.etOrganization);
            AppCompatEditText etWorkDuration = bottomSheetView.findViewById(R.id.etWorkDuration);

            etDesignation.setText(holder.tvOccupationTitle.getText());
            etOrganization.setText(holder.tvOrganizationName.getText());
            etWorkDuration.setText(holder.tvWorkDuration.getText());


            ImageView doneIV = bottomSheetView.findViewById(R.id.doneIV);
            doneIV.setOnClickListener(vDone->{

                if(TextUtils.isEmpty(etDesignation.getText()) || TextUtils.isEmpty(etOrganization.getText())
//                        || TextUtils.isEmpty(etWorkDuration.getText())

                ){

                    Toast.makeText(context, "Some fields my be empty", Toast.LENGTH_SHORT).show();

                }else{

                    Occupation editedOccupation = new Occupation();

                    editedOccupation.setId(occupationList.get(0).getId());
                    editedOccupation.setDesignation(etDesignation.getText().toString());
                    editedOccupation.setOrganization(etOrganization.getText().toString());
                    editedOccupation.setWorkDuration(etWorkDuration.getText().toString());

                    Log.d(TAG, "onBindViewHolder editedOccupation: "+editedOccupation);

                    occupationList.set(position,editedOccupation);

                    notifyDataSetChanged();
                    // sending edited data to database
                    InfoUpdater.addEditOrDeleteOccupation(getContext(),apiService,token,editedOccupation,false,"Occupation edited successfully!");
                    bottomSheetDialog.hide();
                }

            });


        });

        // deleting single occupation
        holder.rlOccupationContainer.setOnLongClickListener(v -> {

            AppCompatDialog dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Do you really want to delete this item ?");

            builder.setPositiveButton("Yes", (dialogInterface, i) -> {

                Occupation deletedOccupation = occupationList.get(holder.getAdapterPosition());

                occupationList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,getItemCount());



                Log.d(TAG, "onBindViewHolder deletedOccupation: "+deletedOccupation);
                Log.d("OccuListAfterRemoveOne", occupationList.toString());

                InfoUpdater.addEditOrDeleteOccupation(getContext(),apiService,token,deletedOccupation,true,"Occupation deleted successfully!");

            });

            builder.setNegativeButton("No", (dialog1, which) -> {

            });
            dialog = builder.create();

            dialog.show();


            return false;
        });
    }

    @Override
    public int getItemCount() {
        return occupationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rlOccupationContainer)
        RelativeLayout rlOccupationContainer;
        @BindView(R.id.tvOccupationTitle)
        AppCompatTextView tvOccupationTitle;
        @BindView(R.id.ivEditOccupation)
        AppCompatImageView ivEditOccupation;
        @BindView(R.id.tvOrganizationName)
        AppCompatTextView tvOrganizationName;
        @BindView(R.id.tvWorkDuration)
        AppCompatTextView tvWorkDuration;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
