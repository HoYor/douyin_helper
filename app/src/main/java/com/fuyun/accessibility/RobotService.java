package com.fuyun.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by yym on 2018/8/20.
 */

public class RobotService extends AccessibilityService {

    private final String TAG = "RobotService";
    public static String mSendMsg = "老夫从未见过如此精彩的视频 ~ ";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        int eventType = accessibilityEvent.getEventType();
        Log.d(TAG, "onAccessibilityEvent-eventType:"+eventType);
        switch (eventType){
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                Log.d(TAG, "onAccessibilityEvent: TYPE_NOTIFICATION_STATE_CHANGED");
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                Log.d(TAG, "onAccessibilityEvent: TYPE_WINDOW_STATE_CHANGED");
                ComponentName activityComponentName = new ComponentName(
                        accessibilityEvent.getPackageName().toString(),
                        accessibilityEvent.getClassName().toString());
                try {
                    String activityName = getPackageManager().getActivityInfo
                            (activityComponentName,0).name;
                    Log.d(TAG, "onAccessibilityEvent: "+activityName);
                    if(activityName != null &&
                            "com.ss.android.ugc.aweme.main.MainActivity".equals(activityName)){
                        sendComment();
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                Log.d(TAG, "onAccessibilityEvent: TYPE_WINDOW_CONTENT_CHANGED");
//                String className = accessibilityEvent.getClassName().toString();
//                Log.d(TAG, "className: "+className);
                break;
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                Log.d(TAG, "onAccessibilityEvent: TYPE_VIEW_CLICKED");
                break;
        }
    }

    private void sendComment() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> commentBtns = nodeInfo.findAccessibilityNodeInfosByViewId(
                "com.ss.android.ugc.aweme:id/wi");
        if(commentBtns != null && commentBtns.size()>0){
            AccessibilityNodeInfo commentBtn = commentBtns.get(0);
            commentBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
        List<AccessibilityNodeInfo> editInfos = nodeInfo.findAccessibilityNodeInfosByViewId(
                "com.ss.android.ugc.aweme:id/wf");
        if(editInfos != null && editInfos.size()>0){
            AccessibilityNodeInfo editInfo = editInfos.get(0);
            Bundle bundle = new Bundle();
            bundle.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,mSendMsg);
            editInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            editInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT,bundle);
        }
        List<AccessibilityNodeInfo> sendInfos = nodeInfo.findAccessibilityNodeInfosByViewId(
                "com.ss.android.ugc.aweme:id/wh");
        if(sendInfos != null && sendInfos.size()>0){
            AccessibilityNodeInfo sendInfo = sendInfos.get(0);
            sendInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
//        List<AccessibilityNodeInfo> closeBtns = nodeInfo.findAccessibilityNodeInfosByViewId(
//                "com.ss.android.ugc.aweme:id/aby");
//        if(closeBtns != null && closeBtns.size()>0){
//            AccessibilityNodeInfo closeBtn = closeBtns.get(0);
//            closeBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//        }
        performGlobalAction(GLOBAL_ACTION_BACK);
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt: ");
    }
}
