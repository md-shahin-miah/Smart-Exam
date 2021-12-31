package com.crux.qxm.utils;

public class Model {
    private String text;
    private int image;
    private boolean isSelect;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }



    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
