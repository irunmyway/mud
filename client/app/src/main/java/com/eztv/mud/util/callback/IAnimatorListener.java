package com.eztv.mud.util.callback;

import android.view.View;

public interface IAnimatorListener {
    void onAnimationPrepare(Object Layout, View showView,View fromView,View toView);
    void onAnimationFinish(Object Layout, View showView,View fromView,View toView);
}
