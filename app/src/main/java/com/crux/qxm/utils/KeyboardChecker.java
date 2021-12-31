package com.crux.qxm.utils;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.crux.qxm.utils.eventBus.Events;

import org.greenrobot.eventbus.EventBus;

public class KeyboardChecker {


    private static final String TAG = "KeyboardChecker";

    public static void isKeyboardOpen(View contentView){

        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                contentView.getWindowVisibleDisplayFrame(r);
                int screenHeight = contentView.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                Log.d(TAG, "keypadHeight = " + keypadHeight);

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    EventBus.getDefault().post(new Events.KeyboardVisibilityEvent(true));

                }
                else {
                    // keyboard is closed
                    EventBus.getDefault().post(new Events.KeyboardVisibilityEvent(false));
                }
            }
        });
    }
}
