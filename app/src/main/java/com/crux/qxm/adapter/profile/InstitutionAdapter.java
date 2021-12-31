package com.crux.qxm.adapter.profile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.users.Institution;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.networkLayer.userInfoUpdate.InfoUpdater;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Collections;
import java.util.List;



/**
 * Created by frshafi on 1/31/18.
 */

public class InstitutionAdapter extends RecyclerView.Adapter<InstitutionAdapter.ViewHolder> {


    private static final String TAG = "InstitutionAdapter";


    private List<Institution> institutionList;
    private Context context;
    private QxmApiService apiService;
    private QxmToken token;


    public InstitutionAdapter(Context context, List<Institution> institutions, QxmApiService apiService, QxmToken token){
        this.context = context;
        this.institutionList = institutions;
        this.apiService = apiService;
        this.token = token;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return context;
    }


    // connecting viewHolder

    @Override
    public InstitutionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // inflate the custom layout
        View InstitutionView = inflater.inflate(R.layout.single_institute_row,parent,false);

        return new ViewHolder(InstitutionView);

    }

    // setting value from Institution list

    @Override
    public void onBindViewHolder(final InstitutionAdapter.ViewHolder holder, final int position) {

        final com.crux.qxm.db.models.users.Institution institution = institutionList.get(position);

        holder.nameInstTV.setText(institution.getInstituteName());

        if(!institution.getFieldOfStudy().isEmpty()){

            holder.fieldOfStudyTV.setVisibility(View.VISIBLE);
            holder.fieldOfStudyTV.setText(institution.getFieldOfStudy());
        }else holder.fieldOfStudyTV.setVisibility(View.GONE);

        if(!institution.getDegree().isEmpty() ){

            holder.degreeTV.setVisibility(View.VISIBLE);
            holder.degreeTV.setText(institution.getDegree());
        }else holder.degreeTV.setVisibility(View.GONE);


        if(!institution.getGrade().isEmpty()){

            holder.gradeTV.setVisibility(View.VISIBLE);
            holder.gradeTV.setText(institution.getGrade());
        }
        else holder.gradeTV.setVisibility(View.GONE);
        holder.passYearTV.setText(institution.getFromTo());
        holder.iconEditInstIV.setOnClickListener(view -> {

            Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
            LayoutInflater inflater = LayoutInflater.from(context);
            View bottomSheetView = inflater.inflate(R.layout.instituition_edit_bottom_dialog,null);
            bottomSheetDialog.setContentView(bottomSheetView);
            //bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();

            final EditText institutionET = bottomSheetView.findViewById(R.id.institutionET);
            final EditText degreeET = bottomSheetView.findViewById(R.id.degreeET);
            final EditText fieldOfStudyET = bottomSheetView.findViewById(R.id.fieldOfStudyET);
            final EditText gradeET = bottomSheetView.findViewById(R.id.gradeET);
            final EditText fromET = bottomSheetView.findViewById(R.id.fromET);
            final EditText toET = bottomSheetView.findViewById(R.id.toET);
            ImageView doneIV = bottomSheetView.findViewById(R.id.doneIV);


            if(holder.nameInstTV!=null)
                institutionET.setText(holder.nameInstTV.getText());
            else institutionET.setText("");

            if(holder.fieldOfStudyTV!=null)
                fieldOfStudyET.setText(holder.fieldOfStudyTV.getText());
            else fieldOfStudyET.setText("");

            if(holder.degreeTV.getText()!=null)
                degreeET.setText(holder.degreeTV.getText());
            else degreeET.setText("");
            if(holder.gradeTV!=null)
                gradeET.setText(holder.gradeTV.getText());
            else gradeET.setText("");

            String[] durationOfStudy = holder.passYearTV.getText().toString().split("-");

            fromET.setText(durationOfStudy[0]);
            fromET.setOnClickListener(view12 -> {

                AppCompatDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater12 = LayoutInflater.from(context);
                View timePickerView = inflater12.inflate(R.layout.time_picker,null);
                builder.setView(timePickerView);

                final NumberPicker studyDurationPicker = timePickerView.findViewById(R.id.studyDurationNP);
                studyDurationPicker.setMinValue(1970);
                studyDurationPicker.setMaxValue(2040);
                builder.setPositiveButton("OK", (dialogInterface, i) -> {


                    Log.d("FROM", String.valueOf(studyDurationPicker.getValue()));
                    fromET.setText(String.valueOf(studyDurationPicker.getValue()));

                });

                dialog = builder.create();
                dialog.show();


            });
            toET.setText(durationOfStudy[1]);
            toET.setOnClickListener(view1 -> {

                AppCompatDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater1 = LayoutInflater.from(context);
                View timePickerView = inflater1.inflate(R.layout.time_picker,null);
                builder.setView(timePickerView);

                final NumberPicker studyDurationPicker = timePickerView.findViewById(R.id.studyDurationNP);
                studyDurationPicker.setMinValue(1970);
                studyDurationPicker.setMaxValue(2040);

                builder.setPositiveButton("OK", (dialogInterface, i) -> {

                    Log.d("TO", String.valueOf(studyDurationPicker.getValue()));
                    toET.setText(String.valueOf(studyDurationPicker.getValue()));
                });
                dialog = builder.create();
                dialog.show();
            });


            // done editing educational institution
            doneIV.setOnClickListener(view13 -> {

                // institution name is mandatory rest institution info is optional
                if(!institutionET.getText().toString().isEmpty()){

                    Institution editedInstitution = new Institution();

                    // setting institution id as before institution id
                    // because we are editing institution with this id
                    editedInstitution.setId(institution.getId());

                    holder.nameInstTV.setText(institutionET.getText().toString());
                    editedInstitution.setInstituteName(institutionET.getText().toString());

                    if(fieldOfStudyET.getText().toString().isEmpty()){

                        holder.fieldOfStudyTV.setVisibility(View.GONE);
                        editedInstitution.setFieldOfStudy("");

                    }else {
                        holder.fieldOfStudyTV.setVisibility(View.VISIBLE);
                        holder.fieldOfStudyTV.setText(fieldOfStudyET.getText().toString());
                        editedInstitution.setFieldOfStudy(fieldOfStudyET.getText().toString());
                    }
                    if(degreeET.getText().toString().isEmpty()){
                        holder.degreeTV.setVisibility(View.GONE);
                        editedInstitution.setDegree("");
                    }else {
                        holder.degreeTV.setVisibility(View.VISIBLE);
                        holder.degreeTV.setText(degreeET.getText().toString());
                        editedInstitution.setDegree(degreeET.getText().toString());
                    }
                    if(gradeET.getText().toString().isEmpty()){

                        holder.gradeTV.setVisibility(View.GONE);
                        editedInstitution.setGrade("");
                    }else {
                        holder.gradeTV.setVisibility(View.VISIBLE);
                        holder.gradeTV.setText(gradeET.getText().toString());
                        editedInstitution.setGrade(gradeET.getText().toString());
                    }

                    String studyDuration = fromET.getText().toString().trim()+"-"+toET.getText().toString().trim();
                    holder.passYearTV.setText(studyDuration);
                    editedInstitution.setFromTo(studyDuration);

                    institutionList.set(position,editedInstitution);
                    Collections.sort(institutionList);
                    notifyDataSetChanged();

                    // sending edited education list in database
                    Log.d(TAG, "setClickListeners newInstitution : "+editedInstitution);
                    InfoUpdater.addOrUpdateEducationalInstitute(getContext(),apiService,token,editedInstitution,false,"Institution edited successfully!");
                    bottomSheetDialog.hide();
                    Log.d("InstitutionList",institutionList.toString());
                }else{

                    Toast.makeText(context, "You must provide institution name", Toast.LENGTH_SHORT).show();
                }

//                Institution institution1 = new Institution(institutionET.getText().toString(),fieldOfStudyET.getText().toString()
//                        ,degreeET.getText().toString(),gradeET.getText().toString(),studyDuration);
//

//                JSONObject uniObj = new JSONObject();
//                try {
//                    uniObj.put("name",institutionET.getText().toString());
//                    uniObj.put("fieldOfStudy",fieldOfStudyET.getText().toString());
//                    uniObj.put("degree",degreeET.getText().toString());
//                    if(!gradeET.getText().toString().isEmpty()){
//
//                        uniObj.put("grade",gradeET.getText().toString());
//
//                    }
////
//                    if (institution.getInstitutionType().equals("university")){
//                        EditProfileFragment.universityArrayList.set(1,uniObj);
//                        Log.d("UniversityArrayList",EditProfileFragment.universityArrayList.toString());
//                    }
//                    uniObj.put("passingYear",studyDuration);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }





            });

        });

        // deleting single institution
        holder.rlInstitutionContainer.setOnLongClickListener(v -> {

            AppCompatDialog dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Do you really want to delete this item ?");

            builder.setPositiveButton("Yes", (dialogInterface, i) -> {

                institutionList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,getItemCount());
                InfoUpdater.addOrUpdateEducationalInstitute(getContext(),apiService,token,institution,true,"Institution deleted successfully!");
                Log.d("InstListAfterRemoveOne", institutionList.toString());

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
        return institutionList.size();
    }


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView nameInstTV;
        TextView fieldOfStudyTV;
        TextView degreeTV;
        TextView gradeTV;
        TextView passYearTV;
        ImageView iconEditInstIV;
        RelativeLayout rlInstitutionContainer;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any FollowingViewHolder instance.
            super(itemView);
            nameInstTV = itemView.findViewById(R.id.nameInstTV);
            fieldOfStudyTV = itemView.findViewById(R.id.fieldOfStudyTV);
            degreeTV = itemView.findViewById(R.id.degreeTV);
            gradeTV = itemView.findViewById(R.id.gradeTV);
            passYearTV = itemView.findViewById(R.id.passYearTV);
            iconEditInstIV = itemView.findViewById(R.id.iconEditInstIV);
            rlInstitutionContainer = itemView.findViewById(R.id.rlInstitutionContainer);


        }
    }
}
