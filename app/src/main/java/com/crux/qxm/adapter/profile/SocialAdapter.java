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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.crux.qxm.db.models.enroll.SocialAddress;
import com.crux.qxm.db.realmModels.apiToken.QxmToken;
import com.crux.qxm.networkLayer.QxmApiService;
import com.crux.qxm.networkLayer.userInfoUpdate.InfoUpdater;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;


public class SocialAdapter extends RecyclerView.Adapter<SocialAdapter.ViewHolder> {

    private static final String TAG = "SocialAdapter";
    private List<SocialAddress> socialAddresses;
    private Context context;
    private QxmApiService apiService;
    private QxmToken token;

    public SocialAdapter(Context context, List<SocialAddress> socialAddresses, QxmApiService apiService, QxmToken token) {

        this.context = context;
        this.socialAddresses = socialAddresses;
        this.apiService = apiService;
        this.token = token;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return context;
    }

    @NonNull
    @Override
    public SocialAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View socialView = inflater.inflate(R.layout.single_social_layout, parent, false);
        return new SocialAdapter.ViewHolder(socialView);

    }

    @Override
    public void onBindViewHolder(@NonNull SocialAdapter.ViewHolder holder, int position) {

        SocialAddress socialAddress = socialAddresses.get(position);

        holder.socialLinkTV.setText(socialAddress.getSocialLink());

        switch (socialAddress.getSocialType()) {

            case YOUTUBE:
                holder.iconSocialIV.setImageResource(R.drawable.ic_youtube_vector);
                break;
            case FACEBOOK:
                holder.iconSocialIV.setImageResource(R.drawable.ic_facebook_vetor);
                break;
            case LINKEDIN:
                holder.iconSocialIV.setImageResource(R.drawable.ic_linkedin_vector);
                break;
            case INSTAGRAM:
                holder.iconSocialIV.setImageResource(R.drawable.ic_insta_vector);
                break;
            case TWITTER:
                holder.iconSocialIV.setImageResource(R.drawable.ic_twitter_vector);
                break;

        }

        holder.iconEditSocialIV.setOnClickListener(v -> {

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
            LayoutInflater inflater = LayoutInflater.from(context);
            View bottomSheetView = inflater.inflate(R.layout.bottom_dialog_social_address_edit, null);
            bottomSheetDialog.setContentView(bottomSheetView);
            //bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();

            // taking social site name and currentAddress to pass in network call
            String socialSiteName;
            String previousId = holder.socialLinkTV.getText().toString().trim();


            TextView socialHintTV = bottomSheetView.findViewById(R.id.socialHintTV);

            switch (socialAddress.getSocialType()) {

                case YOUTUBE:

                    socialSiteName = "youtube";
                    socialHintTV.setText(R.string.new_youtube_link);
                    break;
                case FACEBOOK:
                    socialSiteName = "facebook";
                    socialHintTV.setText(R.string.new_facebook_link);
                    break;
                case LINKEDIN:
                    socialSiteName = "linkedin";
                    socialHintTV.setText(R.string.new_linkedin_link);
                    break;
                case INSTAGRAM:
                    socialSiteName = "instagram";
                    socialHintTV.setText(R.string.new_instagram_link);
                    break;
                case TWITTER:
                    socialSiteName = "twitter";
                    socialHintTV.setText(R.string.new_twitter_link);
                    break;
                default:
                    socialSiteName = "";
            }

            String[] socialArr = holder.socialLinkTV.getText().toString().split("/");
            EditText socialLinkET = bottomSheetView.findViewById(R.id.socialLinkET);
            socialLinkET.setText(socialArr[1]);


            ImageView doneIV = bottomSheetView.findViewById(R.id.doneIV);
            doneIV.setOnClickListener(vDone -> {

                if (!socialLinkET.getText().toString().isEmpty()) {

                    String socialAdd = socialArr[0] + "/" + socialLinkET.getText();
                    holder.socialLinkTV.setText(socialAdd);

                    SocialAddress newSocialAddress = new SocialAddress();
                    switch (socialAddress.getSocialType()) {

                        case YOUTUBE:
                            newSocialAddress.setSocialType(SocialAddress.SocialType.YOUTUBE);
                            newSocialAddress.setSocialLink(socialAdd);
                            socialAddresses.set(position, newSocialAddress);
                            notifyDataSetChanged();
                            break;
                        case FACEBOOK:
                            newSocialAddress.setSocialType(SocialAddress.SocialType.FACEBOOK);
                            newSocialAddress.setSocialLink(socialAdd);
                            socialAddresses.set(position, newSocialAddress);
                            notifyDataSetChanged();
                            break;
                        case LINKEDIN:
                            newSocialAddress.setSocialType(SocialAddress.SocialType.LINKEDIN);
                            newSocialAddress.setSocialLink(socialAdd);
                            socialAddresses.set(position, newSocialAddress);
                            notifyDataSetChanged();
                            break;
                        case INSTAGRAM:
                            newSocialAddress.setSocialType(SocialAddress.SocialType.INSTAGRAM);
                            newSocialAddress.setSocialLink(socialAdd);
                            socialAddresses.set(position, newSocialAddress);
                            notifyDataSetChanged();
                            break;
                        case TWITTER:
                            newSocialAddress.setSocialType(SocialAddress.SocialType.TWITTER);
                            newSocialAddress.setSocialLink(socialAdd);
                            socialAddresses.set(position, newSocialAddress);
                            notifyDataSetChanged();
                            break;
                    }

                    Log.d(TAG, "onBindViewHolder: previous social address: " + previousId);
                    Log.d(TAG, "onBindViewHolder: new social address: " + socialAdd);
                    Log.d(TAG, "onBindViewHolder: social site type " + socialSiteName);

                    InfoUpdater.updateSocialSite(context, apiService, token, socialSiteName, previousId, socialAdd, "Successfully Updated New Social Site!");
                    bottomSheetDialog.hide();
                } else Toast.makeText(context, "Link can not be empty", Toast.LENGTH_SHORT).show();

            });

        });

        // deleting single social site
        holder.rlSocialContainer.setOnLongClickListener(v -> {

            AppCompatDialog dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Do you really want to delete this item ?");

            builder.setPositiveButton("Yes", (dialogInterface, i) -> {

                socialAddresses.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());

                String socialSiteName;

                switch (socialAddress.getSocialType()) {

                    case FACEBOOK:
                        socialSiteName = "facebook";
                        break;
                    case YOUTUBE:
                        socialSiteName = "youtube";
                        break;
                    case TWITTER:
                        socialSiteName = "twitter";
                        break;
                    case INSTAGRAM:
                        socialSiteName = "instagram";
                        break;
                    case LINKEDIN:
                        socialSiteName = "linkedin";
                        break;
                    default:
                        socialSiteName = "";
                        break;

                }

                // new id must be empty string when we are deleting a social site
                String previousId = holder.socialLinkTV.getText().toString().trim();
                String newId = "";

                InfoUpdater.updateSocialSite(getContext(), apiService, token,socialSiteName,previousId,newId,"Social site deleted successfully!");
                Log.d("SocialListAfterRmvOne", socialAddresses.toString());

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
        return socialAddresses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView iconSocialIV;
        TextView socialLinkTV;
        ImageView iconEditSocialIV;
        RelativeLayout rlSocialContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            iconSocialIV = itemView.findViewById(R.id.iconSocialIV);
            socialLinkTV = itemView.findViewById(R.id.socialLinkTV);
            iconEditSocialIV = itemView.findViewById(R.id.iconEditSocialIV);
            rlSocialContainer = itemView.findViewById(R.id.rlSocialContainer);
        }
    }

    // method for updataing users info which contains array
//    public  void updateSocialSite(List<SocialAddress> socialAddresses,Context context,String message) {
//
//        List<String> facebookList = new ArrayList<>();
//        List<String> instagramList = new ArrayList<>();
//        List<String> youtubeList = new ArrayList<>();
//        List<String> linkedinList = new ArrayList<>();
//
//        for (SocialAddress socialAddress:socialAddresses){
//
//            switch (socialAddress.getSocialType()){
//                case YOUTUBE:
//                    youtubeList.add(socialAddress.getSocialLink());
//                    break;
//                case FACEBOOK:
//                    facebookList.add(socialAddress.getSocialLink());
//                    break;
//                case LINKEDIN:
//                    linkedinList.add(socialAddress.getSocialLink());
//                    break;
//                case INSTAGRAM:
//                    instagramList.add(socialAddress.getSocialLink());
//                    break;
//            }
//
//        }
//        ProfileFragment.user.getSocialSite().get(0).setFacebookList(facebookList);
//        ProfileFragment.user.getSocialSite().get(0).setYoutubeList(youtubeList);
//        ProfileFragment.user.getSocialSite().get(0).setInstragramList(instagramList);
//        ProfileFragment.user.getSocialSite().get(0).setLinkedinList(linkedinList);
//
//        Gson gson = new GsonBuilder().create();
//        String socialJson = gson.toJson(ProfileFragment.user.getSocialSite());
//        Log.d("SocialJson",socialJson);
//
//
//        StringRequest userSocialSiteUpdate = new StringRequest(Request.Method.PATCH, Config.url + FeedFragment.userId + "/editProfile", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                Log.d("BasicInfoUpSuccess", response);
//                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Log.d("BasicInfoUpError", error.toString());
//                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//
//                    Map<String, String> params = new HashMap<>();
//
//                    params.put("socialSite", socialJson);
//                    Log.d("OK", "Ture");
//                    Log.d("Params", params.toString());
//                    return params;
//            }
//        };
//        AppController.getInstance().addToRequestQueue(userSocialSiteUpdate);
//    }


}
