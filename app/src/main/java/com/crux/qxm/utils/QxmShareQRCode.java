package com.crux.qxm.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

public class QxmShareQRCode {
    // region Share QR Code
    private static final String TAG = "QxmShareQRCode";
    private FragmentActivity fragmentActivity;
    private Context context;

    public QxmShareQRCode(FragmentActivity fragmentActivity, Context context) {
        this.fragmentActivity = fragmentActivity;
        this.context = context;
    }

    public void shareQRCode(Bitmap bitmap, String qrCodeText) {

        try {
            File file = new File(context.getExternalCacheDir(), "QRCode.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_TEXT, qrCodeText);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            fragmentActivity.startActivity(Intent.createChooser(intent, "Share QR code via"));
        } catch (Exception e) {
            Log.d(TAG, "showGroupQRCode: ivShare catch error: " + e.getLocalizedMessage());
            e.printStackTrace();
        }


    }

    // endregion
}
