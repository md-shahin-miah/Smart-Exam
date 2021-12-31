package com.crux.qxm.utils;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Objects;

import static com.facebook.FacebookSdk.getApplicationContext;

public class QxmKeyboardHelper {

    private FragmentActivity fragmentActivity;

    // region Class-Constructor

    public QxmKeyboardHelper(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    // endregion

    // region CloseKeyboard

    public void closeKeyboard(){

        View view = Objects.requireNonNull(fragmentActivity.getCurrentFocus());
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    // endregion
}
