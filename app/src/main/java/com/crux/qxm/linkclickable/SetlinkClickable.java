package com.crux.qxm.linkclickable;

import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetlinkClickable {

    public static void setLinkclickEvent(TextView tv, HandleLinkClickInsideTextView clickInterface) {
        String text = tv.getText().toString();
        String str = "([Hh][tT][tT][pP][sS]?:\\/\\/[^ ,'\">\\]\\)]*[^\\. ,'\">\\]\\)])";
        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(tv.getText());
        while (matcher.find()) {
            int x = matcher.start();
            int y = matcher.end();
            final android.text.SpannableString f = new android.text.SpannableString(
                    tv.getText());
            InternalURLSpan span = new InternalURLSpan();
            span.setText(text.substring(x, y));
            span.setClickInterface(clickInterface);
            f.setSpan(span, x, y,
                    android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv.setText(f);
        }
        tv.setLinksClickable(true);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setFocusable(false);
    }

}
