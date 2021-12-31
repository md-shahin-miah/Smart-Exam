package com.crux.qxm.utils.qxmDialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.crux.qxm.R;
import com.crux.qxm.utils.QxmCreateQRCode;
import com.crux.qxm.utils.QxmShareQRCode;
import com.google.zxing.WriterException;

import static com.crux.qxm.utils.StaticValues.QR_CODE_FOR_GROUP;
import static com.crux.qxm.utils.StaticValues.QR_CODE_FOR_QXM;
import static com.crux.qxm.utils.StaticValues.QR_CODE_FOR_USER;
import static com.crux.qxm.utils.StaticValues.QR_CODE_HEIGHT;
import static com.crux.qxm.utils.StaticValues.QR_CODE_WIDTH;

public class QxmShowQRCodeDialog {
    private static final String TAG = "QxmShowQRCodeDialog";
    private Context context;
    private FragmentActivity fragmentActivity;

    public QxmShowQRCodeDialog(Context context, FragmentActivity fragmentActivity) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
    }

    public void showGroupQRCode(String showQRCodeFor, String name, String url) {

        if (!TextUtils.isEmpty(url)) {
            QxmCreateQRCode qxmCreateQRCode = new QxmCreateQRCode();

            Log.d(TAG, "showGroupQRCode: groupName = " + name);
            Log.d(TAG, "showGroupQRCode: groupUrl = " + url);

            try {
                Bitmap bitmap = qxmCreateQRCode.textToImage(url, QR_CODE_WIDTH, QR_CODE_HEIGHT);
                if (bitmap != null) {
                    LayoutInflater layoutInflater = fragmentActivity.getLayoutInflater();
                    View view = layoutInflater.inflate(R.layout.dialog_show_qr_code, null);

                    AppCompatTextView tvDialogTitle = view.findViewById(R.id.tvDialogTitle);
                    AppCompatImageView ivShare = view.findViewById(R.id.ivShare);
                    AppCompatTextView tvQRCodeFor = view.findViewById(R.id.tvQRCodeFor);
                    AppCompatImageView ivQrCode = view.findViewById(R.id.ivQrCode);
                    AppCompatTextView tvClose = view.findViewById(R.id.tvClose);

                    Dialog dialog = new Dialog(context);
                    dialog.getWindow();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(view);
                    dialog.setCanceledOnTouchOutside(true);

                    switch (showQRCodeFor) {
                        case QR_CODE_FOR_USER:
                            tvDialogTitle.setText(context.getResources().getString(R.string.user_qr_code));
                            tvQRCodeFor.setText(String.format("User Name: %s", name));

                            break;
                        case QR_CODE_FOR_QXM:
                            tvDialogTitle.setText(context.getResources().getString(R.string.qxm_qr_code));
                            tvQRCodeFor.setText(String.format("Qxm Title: %s", name));

                            break;
                        case QR_CODE_FOR_GROUP:
                            tvDialogTitle.setText(context.getResources().getString(R.string.group_qr_code));
                            tvQRCodeFor.setText(String.format("Group Name: %s", name));
                            break;
                    }

                    ivQrCode.setImageBitmap(bitmap);

                    ivShare.setOnClickListener(v -> {

                        QxmShareQRCode qxmShareQRCode = new QxmShareQRCode(fragmentActivity, context);
                        String qrCodeText = String.format("The QR code of Qxm App's %s - %s", showQRCodeFor, name);
                        qxmShareQRCode.shareQRCode(bitmap, qrCodeText);

                    });

                    tvClose.setOnClickListener(v -> dialog.dismiss());

                    dialog.show();
                }
            } catch (WriterException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "showGroupQRCode: failed. groupUrl is null");
        }
    }
}
