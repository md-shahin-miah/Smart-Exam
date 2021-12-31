package com.crux.qxm.utils.deepLinkingHelper;

import android.net.Uri;
import android.util.Log;

public class IdExtractorFromUrl {

    private static final String TAG = "IdExtractorFromUrl";

    public static String getId(String url) {

        Log.d(TAG, "getId: url: " + url);

        Uri uri = Uri.parse(url);
        String id = uri.getLastPathSegment();

        if (id != null) {
            Log.d(TAG, "getId: id = " + id);
            return id;

        } else {
            Log.d(TAG, "getId: id is null");
            return "";
        }
    }
}
