package com.crux.qxm.linkclickable;

import android.view.View;

public  class InternalURLSpan extends android.text.style.ClickableSpan {

    private String text;
    private HandleLinkClickInsideTextView clickInterface;

    @Override
    public void onClick(View widget) {
        getClickInterface().onLinkClicked(getText());
    }

    public void setText(String textString) {
        this.text = textString;
    }

    public String getText() {
        return this.text;
    }

    public void setClickInterface(HandleLinkClickInsideTextView clickInterface) {
        this.clickInterface = clickInterface;
    }

    public HandleLinkClickInsideTextView getClickInterface() {
        return this.clickInterface;
    }

}
