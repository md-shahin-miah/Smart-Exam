package com.crux.qxm.utils;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.FragmentActivity;

import com.crux.qxm.R;

import static com.crux.qxm.utils.StaticValues.LINK_CATEGORY_GROUP;
import static com.crux.qxm.utils.StaticValues.LINK_CATEGORY_POLL;
import static com.crux.qxm.utils.StaticValues.LINK_CATEGORY_QXM;
import static com.crux.qxm.utils.StaticValues.LINK_CATEGORY_SINGLE_MCQ;

public class QxmActionShareLink {

    private static final String TAG = "QxmActionShareLink";
    private FragmentActivity fragmentActivity;
    private Context context;

    public QxmActionShareLink(FragmentActivity fragmentActivity, Context context) {
        this.fragmentActivity = fragmentActivity;
        this.context = context;
    }

    public void shareUserLink(String userUrl) {
        shareUrl(LINK_CATEGORY_QXM, userUrl);
    }

    public void shareQxmLink(String qxmUrl) {
        shareUrl(LINK_CATEGORY_QXM, qxmUrl);
    }

    public void shareGroupLink(String groupUrl) {
        shareUrl(LINK_CATEGORY_GROUP, groupUrl);
    }

    public void sharePollLink(String pollUrl) {
        shareUrl(LINK_CATEGORY_POLL, pollUrl);
    }

    public void shareSingleMCQLink(String singleMCQUrl) {
        shareUrl(LINK_CATEGORY_SINGLE_MCQ, singleMCQUrl);
    }


    private void shareUrl(String linkCategory, String url){
        final Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share_this_qxm_link_with_others));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, url);

        fragmentActivity.startActivity(Intent.createChooser(sharingIntent, String.format("Share %s via", linkCategory)));
    }
}
