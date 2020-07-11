package com.eztv.mud.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.RequiresApi;

import com.alibaba.fastjson.JSONObject;
import com.eztv.mud.bean.Enum;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.util.callback.IAnimatorListener;

public class Util {
    public static View getContentView(Activity activity) {
        return activity.findViewById(android.R.id.content);
    }

    public static Rect getViewAbsRect(View view)
    {
        Rect localRect = new Rect();
        int[] arrayOfInt = new int[2];
        view.getLocationOnScreen(arrayOfInt);
        localRect.left = arrayOfInt[0];
        localRect.top = arrayOfInt[1];
        localRect.right = (localRect.left + view.getWidth());
        localRect.bottom = (localRect.top + view.getHeight());
        return localRect;
    }


    public static void fightAnim(Object layout,View showView, final View fromView, final View toView, final IAnimatorListener iAnimatorListener)
    {
        if(layout!=null&&iAnimatorListener!=null)
            iAnimatorListener.onAnimationPrepare(layout,showView,fromView,toView);
        showView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @RequiresApi(api=16)
            public void onGlobalLayout()
            {
                showView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                AnimatorSet localAnimatorSet = new AnimatorSet();
                int fromX = getViewAbsRect(fromView).centerX();
                int fromY = getViewAbsRect(fromView).centerY();
                int toX = getViewAbsRect(toView).centerX();
                int toY = getViewAbsRect(toView).centerY();
                double distance = Math.atan2(toX - fromX, toY - fromY);
                localAnimatorSet.playTogether(new Animator[] {
                        ObjectAnimator.ofFloat(showView, "translationX", new float[] {
                                fromX - showView.getWidth() / 2, toX - showView.getWidth() / 2
                        }).setDuration(1000L),
                        ObjectAnimator.ofFloat(showView, "translationY", new float[] {
                                fromY, toY }).setDuration(1000L),
                        ObjectAnimator.ofFloat(showView, "rotation", new float[] {
                                0.0F, (float)(180.0D - distance * 57.3) }).setDuration(0L)
                });
                if (iAnimatorListener != null) {
                    localAnimatorSet.addListener(new Animator.AnimatorListener()
                    {
                        public void onAnimationCancel(Animator paramAnonymous2Animator) {}
                        public void onAnimationEnd(Animator paramAnonymous2Animator)
                        {
                            iAnimatorListener.onAnimationFinish(layout,showView,fromView,toView);
                        }
                        public void onAnimationRepeat(Animator paramAnonymous2Animator) {}
                        public void onAnimationStart(Animator paramAnonymous2Animator) {}
                    });
                }
                localAnimatorSet.start();
            }
        });
    }


    public static void calculateAnim(Object layout,View showView, final Rect fromRect, final Rect toRect, final IAnimatorListener iAnimatorListener)
    {
        if(layout!=null&&iAnimatorListener!=null)
            iAnimatorListener.onAnimationPrepare(layout,showView,null,null);
        showView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @RequiresApi(api=16)
            public void onGlobalLayout()
            {
                showView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                AnimatorSet localAnimatorSet = new AnimatorSet();
                int fromX = fromRect.centerX();
                int fromY = fromRect.centerY();
                int toX = toRect.centerX();
                int toY = toRect.centerY();
                localAnimatorSet.playTogether(new Animator[] {
                        ObjectAnimator.ofFloat(showView, "translationX", new float[] {
                                fromX - showView.getWidth() / 2, toX - showView.getWidth() / 2
                        }).setDuration(2000L),
                        ObjectAnimator.ofFloat(showView, "translationY", new float[] {
                                fromY, toY
                        }).setDuration(2000L)
                });
                if (iAnimatorListener != null) {
                    localAnimatorSet.addListener(new Animator.AnimatorListener()
                    {
                        public void onAnimationCancel(Animator paramAnonymous2Animator) {}
                        public void onAnimationEnd(Animator paramAnonymous2Animator)
                        {
                            iAnimatorListener.onAnimationFinish(layout,showView,null,null);
                        }
                        public void onAnimationRepeat(Animator paramAnonymous2Animator) {}
                        public void onAnimationStart(Animator paramAnonymous2Animator) {}
                    });
                }
                localAnimatorSet.start();
            }
        });
    }





    public static String msgBuildStr(Enum.messageType type, String cmd, String msgstr, String role){
        Msg msg = new Msg();
        msg.setCmd(cmd);
        msg.setMsg(msgstr);
        msg.setRole(role);
        msg.setType(type);
        return object2JsonStr(msg);
    }
    public static Msg msgBuild(Enum.messageType type, String cmd, String msgstr, String role){
        Msg msg = new Msg();
        msg.setCmd(cmd);
        msg.setMsg(msgstr);
        msg.setRole(role);
        msg.setType(type);
        return msg;
    }

    public static String object2JsonStr(Object obj){
        return JSONObject.parseObject(JSONObject.toJSON(obj).toString()).toJSONString();
    }

    public static JSONObject jsonStr2Json(String obj){
        return JSONObject.parseObject(JSONObject.toJSON(obj).toString());
    }

}
