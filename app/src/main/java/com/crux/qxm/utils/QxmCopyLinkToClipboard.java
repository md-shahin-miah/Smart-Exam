package com.crux.qxm.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;

import java.util.Objects;

import static android.content.Context.CLIPBOARD_SERVICE;

public class QxmCopyLinkToClipboard {

    private static final String TAG = "QxmCopyLinkToClipboard";
    private FragmentActivity fragmentActivity;

    public QxmCopyLinkToClipboard(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    public boolean copyToClipboard(String url) {
        ClipboardManager myClipboard;
        myClipboard = (ClipboardManager) Objects.requireNonNull(fragmentActivity).getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData;
        clipData = ClipData.newPlainText("url", url);
        if (myClipboard != null) {
            myClipboard.setPrimaryClip(clipData);
            return true;
        } else {
            Log.d(TAG, "Failed to copy link in clipboard.");
            return false;
        }
    }
}
