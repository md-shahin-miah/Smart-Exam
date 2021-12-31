package com.crux.qxm.utils.inputValidations;

import android.content.Context;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import mabbas007.tagsedittext.TagsEditText;

public class InputValidationHelper {
    private static final String TAG = "InputValidationHelper";
    private Context context;

    public InputValidationHelper(Context context) {
        this.context = context;
    }

    public void showEditTextMaximumCharacterReachedMessage(AppCompatEditText editText, int maxLength, String message) {

        Log.d(TAG, "showEditTextMaximumCharacterReachedMessage: called");
        Log.d(TAG, "maxLength: " + maxLength);
        Log.d(TAG, "message: " + message);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (!TextUtils.isEmpty(text)) {
                    Log.d(TAG, "afterTextChanged: text.length = " + text.length());
                    if (text.length() >= maxLength) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void showEditTextMaximumCharacterReachedMessage(TagsEditText editText, int maxLength, String message) {

        Log.d(TAG, "showEditTextMaximumCharacterReachedMessage: called");
        Log.d(TAG, "maxLength: " + maxLength);
        Log.d(TAG, "message: " + message);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (!TextUtils.isEmpty(text)) {
                    Log.d(TAG, "afterTextChanged: text.length = " + text.length());
                    if (text.length() >= maxLength) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
