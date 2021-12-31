package com.crux.qxm.adapter.preferableLanguagePicker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.crux.qxm.R;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreferableLanguagePickerAdapter extends RecyclerView.Adapter<PreferableLanguagePickerAdapter.ViewHolder> {
    private static final String TAG = "PreferableLanguagePicke";
    private Context context;
    private List<String> usersPreferableLanguages;
    private List<String> languages;

    public PreferableLanguagePickerAdapter(Context context, List<String> usersPreferableLanguages) {

        this.context = context;
        this.usersPreferableLanguages = usersPreferableLanguages;
        languages = new ArrayList<>();
        for (String languageName : context.getResources().getStringArray(R.array.language_names)) {
            Log.d(TAG, "Language name: " + languageName);
            languages.add(languageName);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View preferableLanguagePickerSingleItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_dialog_preferable_language_single_item, parent, false);
        return new ViewHolder(preferableLanguagePickerSingleItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Log.d(TAG, "onBindViewHolder: language name: " + languages.get(position));

        holder.tvLanguageName.setText(languages.get(position));

        if (usersPreferableLanguages != null) {
            if (usersPreferableLanguages.contains(languages.get(position)))
                holder.checkboxLanguageSelect.setChecked(true);
            else
                holder.checkboxLanguageSelect.setChecked(false);
        }

        holder.checkboxLanguageSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                usersPreferableLanguages.add(languages.get(position));
            } else {
                usersPreferableLanguages.remove(languages.get(position));
            }

        });
    }

    @Override
    public int getItemCount() {

        Log.d(TAG, "getItemCount: " + languages.size());
        return languages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvLanguageName)
        AppCompatTextView tvLanguageName;
        @BindView(R.id.checkboxLanguageSelect)
        MaterialCheckBox checkboxLanguageSelect;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
