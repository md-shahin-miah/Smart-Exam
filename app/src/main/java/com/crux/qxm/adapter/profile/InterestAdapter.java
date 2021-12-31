package com.crux.qxm.adapter.profile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.networkLayer.userInfoUpdate.InfoUpdater;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;


public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.ViewHolder> {

    private static final String TAG = "InterestAdapter";

    private Context context;
    private List<String> interestList;
    private QxmApiService apiService;
    private QxmToken token;
    private String newInterest;

    public InterestAdapter(Context context, List<String> interestList, QxmApiService apiService, QxmToken token) {
        this.context = context;
        this.interestList = interestList;
        this.apiService = apiService;
        this.token = token;

    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return context;
    }



    @NonNull
    @Override
    public InterestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View interestView = inflater.inflate(R.layout.single_interest_row,parent,false);
        return new InterestAdapter.ViewHolder(interestView);

    }

    @Override
    public void onBindViewHolder(@NonNull InterestAdapter.ViewHolder holder, int position) {

        holder.interestTV.setText(interestList.get(position));

        holder.editInterestIV.setOnClickListener(v->{


            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
            LayoutInflater inflater = LayoutInflater.from(context);
            View bottomSheetView = inflater.inflate(R.layout.layout_interest,null);
            bottomSheetDialog.setContentView(bottomSheetView);
            //bottomSheetDialog.setCancelable(false);

            RecyclerView rvQxmInterestCategory = bottomSheetView.findViewById(R.id.rvQxmInterestCategory);
            ImageView doneIV = bottomSheetView.findViewById(R.id.doneIV);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),
                    3, GridLayoutManager.HORIZONTAL, false);

            rvQxmInterestCategory.setLayoutManager(gridLayoutManager);
            rvQxmInterestCategory.setHasFixedSize(true);
            rvQxmInterestCategory.setNestedScrollingEnabled(false);


            String currentInterest =  holder.interestTV.getText().toString().trim();
            QxmInterestAdapter qxmInterestAdapter = new QxmInterestAdapter(context, getAllInterests(),currentInterest, (interestMainContent, selectedInterest, position1) -> {

                newInterest = selectedInterest;

            });
            rvQxmInterestCategory.setAdapter(qxmInterestAdapter);

            bottomSheetDialog.show();


            doneIV.setOnClickListener(vDone->{


                if(interestList.contains(newInterest)){

                    Toast.makeText(context, "This interest is already in your interests!", Toast.LENGTH_SHORT).show();
                }else{

                    interestList.set(holder.getAdapterPosition(), newInterest);
                    notifyDataSetChanged();

                    Log.d(TAG, "onBindViewHolder: edited Interest list: "+interestList);

                    InfoUpdater.updateInterest(getContext(),apiService,token,currentInterest, newInterest);
                    bottomSheetDialog.dismiss();
                }

                Log.d(TAG, "onBindViewHolder: doneIv Called");

            });

        });

        // deleting single interest
        holder.rlInterest.setOnLongClickListener(v -> {

            AppCompatDialog dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Do you really want to delete this item ?");

            builder.setPositiveButton("Yes", (dialogInterface, i) -> {


                String deletedItem = interestList.get(position);
                interestList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,getItemCount());

                Log.d(TAG, "onBindViewHolder: interestList: "+interestList);
                Log.d(TAG, "onBindViewHolder: deleted interest : "+ deletedItem);
                InfoUpdater.deleteInterest(getContext(),apiService,token,deletedItem);
                Log.d("InterestAfterRemoveOne", interestList.toString());


            });

            builder.setNegativeButton("No", (dialog1, which) -> {
                dialog1.dismiss();
            });
            dialog = builder.create();

            dialog.show();


            return false;
        });


    }

    @Override
    public int getItemCount() {
        return interestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlInterest;
        TextView interestTV;
        ImageView editInterestIV;
        public ViewHolder(View itemView) {
            super(itemView);
            interestTV = itemView.findViewById(R.id.tvInterest);
            editInterestIV =itemView.findViewById(R.id.editInterestIV);
            rlInterest = itemView.findViewById(R.id.rlInterest);
        }
    }


    private ArrayList<String> getAllInterests() {

        ArrayList<String> qxmCategoryList = new ArrayList<>();

        qxmCategoryList.add("Science & Technology");
        qxmCategoryList.add("General Knowledge");
        qxmCategoryList.add("Arts & Culture");
        qxmCategoryList.add("Environment");
        qxmCategoryList.add("Entertainment");
        qxmCategoryList.add("Food");
        qxmCategoryList.add("History");
        qxmCategoryList.add("Education");
        qxmCategoryList.add("Fun");
        qxmCategoryList.add("Globalization");
        qxmCategoryList.add("Books");
        qxmCategoryList.add("Film & Animation");
        qxmCategoryList.add("Professional or Training Exam");
        qxmCategoryList.add("Class Related Exam");
        qxmCategoryList.add("Travel & Events");
        qxmCategoryList.add("Game");
        qxmCategoryList.add("News & Politics");
        qxmCategoryList.add("Music");
        qxmCategoryList.add("Sport");
        qxmCategoryList.add("Society");
        qxmCategoryList.add("Opinion");
        qxmCategoryList.add("Health");
        qxmCategoryList.add("Gadget & Product");
        qxmCategoryList.add("Engineering");
        qxmCategoryList.add("Design & Photography");
        qxmCategoryList.add("Tutorial");
        qxmCategoryList.add("Quiz");

        return qxmCategoryList;

    }

    // endregion
}
