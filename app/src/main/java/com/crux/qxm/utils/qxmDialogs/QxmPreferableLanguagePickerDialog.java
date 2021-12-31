package com.crux.qxm.utils.qxmDialogs;

import android.app.Dialog;
import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.crux.qxm.R;
import com.crux.qxm.adapter.preferableLanguagePicker.PreferableLanguagePickerAdapter;

import java.util.List;

public class QxmPreferableLanguagePickerDialog {
    private static final String TAG = "QxmUsersPreferableLangu";
    private View rootView;
    private FragmentActivity fragmentActivity;
    private Context context;
    private List<String> usersPreferableLanguages;
    private AppCompatTextView tvChoosePreferableLanguageHint;
    private AppCompatTextView tvSelectedPreferableLanguages;


    public QxmPreferableLanguagePickerDialog(View rootView, FragmentActivity fragmentActivity, Context context, List<String> usersPreferableLanguages, AppCompatTextView tvChoosePreferableLanguageHint, AppCompatTextView tvSelectedPreferableLanguages) {
        this.rootView = rootView;
        this.fragmentActivity = fragmentActivity;
        this.context = context;
        this.usersPreferableLanguages = usersPreferableLanguages;
        this.tvChoosePreferableLanguageHint = tvChoosePreferableLanguageHint;
        this.tvSelectedPreferableLanguages = tvSelectedPreferableLanguages;
    }

    public void showPreferableLanguagePickerDialog() {
        if (usersPreferableLanguages != null && usersPreferableLanguages.size() != 0)
            Log.d(TAG, "showPreferableLanguagePickerDialog: UserPreferableLanguages = ");

        LayoutInflater layoutInflater = fragmentActivity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_preferable_language_picker, (ViewGroup) rootView, false);

        RecyclerView rvPreferableLanguages = view.findViewById(R.id.rvPreferableLanguages);
        AppCompatTextView tvOk = view.findViewById(R.id.tvOk);

        Dialog dialog = new Dialog(context);
        Window window = dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        if (window != null) {
            Log.d(TAG, "showPreferableLanguagePickerDialog: window not null");
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        dialog.setCanceledOnTouchOutside(true);

        PreferableLanguagePickerAdapter languagePickerAdapter = new PreferableLanguagePickerAdapter(context, usersPreferableLanguages);
        rvPreferableLanguages.setAdapter(languagePickerAdapter);
        rvPreferableLanguages.setLayoutManager(new LinearLayoutManager(context));

        tvOk.setOnClickListener(v -> {

            if(usersPreferableLanguages.size() != 0){
                tvChoosePreferableLanguageHint.setText(context.getString(R.string.you_have_chosen_the_following_languages));

                StringBuilder selectedLanguages;
                selectedLanguages = new StringBuilder();


                for (int i = 0; i < usersPreferableLanguages.size(); i++ ) {
                        selectedLanguages.append(usersPreferableLanguages.get(i));
                    if(i != usersPreferableLanguages.size()-1)
                        selectedLanguages.append(", ");
                }

                tvSelectedPreferableLanguages.setText(selectedLanguages.toString());
                tvSelectedPreferableLanguages.setVisibility(View.VISIBLE);
            }else{
                tvChoosePreferableLanguageHint.setText(context.getString(R.string.add_your_preferred_language_by_tapping_the_following_button));
                tvSelectedPreferableLanguages.setVisibility(View.GONE);
            }

            Log.d(TAG, "showPreferableLanguagePickerDialog: userPreferableLanguages: " + usersPreferableLanguages.toString());

            dialog.dismiss();
        });

        dialog.show();

    }
}
