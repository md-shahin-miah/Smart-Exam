package com.crux.qxm.utils.qxmDialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.crux.qxm.R;
import com.crux.qxm.utils.QxmFragmentTransactionHelper;
import com.crux.qxm.views.activities.CreateQxmActivity;

import static com.crux.qxm.utils.StaticValues.CREATE_QXM_REQUEST;

public class QxmCreateNewQxmOrPollDialog {
    private static final String TAG = "QxmCreateNewQxmOrPollDi";

    private FragmentActivity fragmentActivity;
    private Context context;
    private QxmFragmentTransactionHelper qxmFragmentTransactionHelper;


    public QxmCreateNewQxmOrPollDialog(FragmentActivity fragmentActivity, Context context) {
        this.fragmentActivity = fragmentActivity;
        this.context = context;
        qxmFragmentTransactionHelper = new QxmFragmentTransactionHelper(fragmentActivity);
    }

    public void showDialog() {
        LayoutInflater layoutInflater = fragmentActivity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_create_qxm_or_poll_selection, null);

        AppCompatButton btnCreateQxm = view.findViewById(R.id.btnCreateQxm);
        AppCompatButton btnCreatePoll = view.findViewById(R.id.btnCreatePoll);

        Dialog dialog = new Dialog(context);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);

        btnCreateQxm.setOnClickListener(v -> {
            fragmentActivity.startActivityForResult(
                    new Intent(context, CreateQxmActivity.class),
                    CREATE_QXM_REQUEST
            );

            dialog.dismiss();
        });

        btnCreatePoll.setOnClickListener(v -> {
            qxmFragmentTransactionHelper.loadCreatePollFragment();
            dialog.dismiss();
        });

        dialog.show();

    }
}
