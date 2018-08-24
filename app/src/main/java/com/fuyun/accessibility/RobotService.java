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

import java.util.Arrays;
import java.util.List;

/**
 * Created by yym on 2018/8/20.
 */

public class RobotService extends AccessibilityService {

    private final String TAG = "RobotService";
    public static String mSendMsg = "老夫从未见过如此精彩的视频 ~ ";
    public static boolean isAllowPlay = true;
    public static boolean isPlaying = false;
    private int step = 0;

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
//                printPacketInfo(getRootInActiveWindow());
//                ComponentName activityComponentName = new ComponentName(
//                        accessibilityEvent.getPackageName().toString(),
//                        accessibilityEvent.getClassName().toString());
//                if(!isPlaying) {
//                    try {
//                        String activityName = getPackageManager().getActivityInfo
//                                (activityComponentName, 0).name;
//                        Log.d(TAG, "onAccessibilityEvent: " + activityName);
//                        if (activityName != null &&
//                                "com.ss.android.ugc.aweme.main.MainActivity".equals(activityName)) {
//                            isPlaying = true;
                            sendComment();
//                        }
//                    } catch (PackageManager.NameNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
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
        if(!isAllowPlay)return;
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> commentBtns = nodeInfo.findAccessibilityNodeInfosByViewId(
                "com.ss.android.ugc.aweme:id/wl");
        if(commentBtns != null && commentBtns.size()>0){
            if(step == 0) {
                AccessibilityNodeInfo commentBtn = commentBtns.get(0);
                commentBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                step = 1;
                return;
            }else if(step == 3){
                step = 0;
            }
        }
        List<AccessibilityNodeInfo> editInfos = nodeInfo.findAccessibilityNodeInfosByViewId(
                "com.ss.android.ugc.aweme:id/wh");
        if(step == 1 && editInfos != null && editInfos.size()>0){
            AccessibilityNodeInfo editInfo = editInfos.get(0);
            Bundle bundle = new Bundle();
            bundle.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,mSendMsg);
//            editInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            editInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            editInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT,bundle);
            step = 2;
            return;
        }
        List<AccessibilityNodeInfo> sendInfos = nodeInfo.findAccessibilityNodeInfosByViewId(
                "com.ss.android.ugc.aweme:id/wk");
        if(sendInfos != null && sendInfos.size()>0){
            AccessibilityNodeInfo sendInfo = sendInfos.get(0);
            sendInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            step = 3;
        }
//        List<AccessibilityNodeInfo> closeBtns = nodeInfo.findAccessibilityNodeInfosByViewId(
//                "com.ss.android.ugc.aweme:id/aby");
//        if(closeBtns != null && closeBtns.size()>0){
//            AccessibilityNodeInfo closeBtn = closeBtns.get(0);
//            closeBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//        }
        performGlobalAction(GLOBAL_ACTION_BACK);
        performGlobalAction(GLOBAL_ACTION_BACK);
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt: ");
    }


    private static int tabcount = -1;
    private static StringBuilder sb;

    public static void printPacketInfo(AccessibilityNodeInfo root) {
        sb = new StringBuilder();
        tabcount = 0;
        int[] is = {};
        analysisPacketInfo(root, is);
        Log.d("RobotService",sb.toString());
    }

    //打印此时的界面状况,便于分析
    private static void analysisPacketInfo(AccessibilityNodeInfo info, int... ints) {
        if (info == null) {
            return;
        }
        if (tabcount > 0) {
            for (int i = 0; i < tabcount; i++) {
                sb.append("\t\t");
            }
        }
        if (ints != null && ints.length > 0) {
            StringBuilder s = new StringBuilder();
            for (int j = 0; j < ints.length; j++) {
                s.append(ints[j]).append(".");
            }
            sb.append(s).append(" ");
        }
        String name = info.getClassName().toString();
        String[] split = name.split("\\.");
        name = split[split.length - 1];
        if ("TextView".equals(name)) {
            CharSequence text = info.getText();
            sb.append("text:").append(text);
        } else if ("Button".equals(name)) {
            CharSequence text = info.getText();
            sb.append("Button:").append(text);
        } else {
            sb.append(name);
        }
        sb.append("\n");

        int count = info.getChildCount();
        if (count > 0) {
            tabcount++;
            int len = ints.length + 1;
            int[] newInts = Arrays.copyOf(ints, len);

            for (int i = 0; i < count; i++) {
                newInts[len - 1] = i;
                analysisPacketInfo(info.getChild(i), newInts);
            }
            tabcount--;
        }

    }
}
