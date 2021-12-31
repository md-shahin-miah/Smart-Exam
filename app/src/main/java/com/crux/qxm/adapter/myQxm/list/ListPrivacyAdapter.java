package com.crux.qxm.adapter.myQxm.list;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.crux.qxm.R;

import java.util.List;

import static com.crux.qxm.utils.StaticValues.LIST_PRIVACY_PRIVATE;
import static com.crux.qxm.utils.StaticValues.LIST_PRIVACY_PUBLIC;

public class ListPrivacyAdapter extends ArrayAdapter<String> {


    private LayoutInflater layoutInflater;
    private int resourceId;
    private List<String> privacyList;
    private Context context;

    public ListPrivacyAdapter(@NonNull Context context, int resourceId, @NonNull List<String> privacyList) {


        super(context, resourceId, privacyList);
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.privacyList = privacyList;
        this.resourceId = resourceId;
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);


    }


    private View createItemView(int position, View convertView, ViewGroup parent){

        if(convertView == null){
            convertView = layoutInflater.inflate(resourceId,parent, false);
        }
        AppCompatTextView tvListPrivacyTitle = convertView.findViewById(R.id.tvListPrivacyTitle);
        AppCompatTextView tvListPrivacyExplanation = convertView.findViewById(R.id.tvListPrivacyExplanation);
        AppCompatImageView ivListPrivacy = convertView.findViewById(R.id.ivListPrivacy);

        String singleListSettings = privacyList.get(position);

        if(singleListSettings.equals(LIST_PRIVACY_PUBLIC)){

            tvListPrivacyTitle.setText(singleListSettings);
            ivListPrivacy.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_privacy_public));
            tvListPrivacyExplanation.setText("Anyone can view");
        }else if(singleListSettings.equals(LIST_PRIVACY_PRIVATE)){

            tvListPrivacyTitle.setText(singleListSettings);
            ivListPrivacy.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_privacy_private));
            tvListPrivacyExplanation.setText("Only you can view");
        }

        return convertView;
    }
}
