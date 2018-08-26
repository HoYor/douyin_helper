package com.fuyun.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by yym on 2018/8/20.
 */

public class RobotService extends AccessibilityService {

    private final String TAG = "RobotService";
    public static String mSendMsg = "我把抖音上所有视频都评论了一遍 ~ ";
    public static boolean isAllowPlay = true;
    public static boolean isPlaying = false;
    private int step = 0;
    private int step2 = 0;
    public static int page = 0;
    private int count = 0;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        int eventType = accessibilityEvent.getEventType();
        Log.d(TAG, "onAccessibilityEvent-eventType:"+eventType);
        if(eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
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
            Observable.timer(5,TimeUnit.SECONDS)
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            count++;
                            if(page == 0) {
                                Log.d(TAG, "step："+step);
                                sendComment();
                            }else if(page == 1){
                                Log.d(TAG, "step2："+step2);
                                sendComment2();
                            }
                        }
                    });
//                        }
//                    } catch (PackageManager.NameNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
        }
    }

    private void sendComment2() {
        if(!isAllowPlay)return;
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if(nodeInfo == null)return;
        List<AccessibilityNodeInfo> editInfos = nodeInfo.findAccessibilityNodeInfosByViewId(
                "com.ss.android.ugc.aweme:id/wh");
        if(step2 == 0 && editInfos != null && editInfos.size()>0){
            AccessibilityNodeInfo editInfo = editInfos.get(editInfos.size()-1);
            Bundle bundle = new Bundle();
            bundle.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                    mSendMsg+count);
            editInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT,bundle);
            editInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            step2 = 1;
            return;
        }
        List<AccessibilityNodeInfo> sendInfos = nodeInfo.findAccessibilityNodeInfosByViewId(
                "com.ss.android.ugc.aweme:id/wk");
        if(step2 == 1 && sendInfos != null && sendInfos.size()>0){
            final AccessibilityNodeInfo sendInfo = sendInfos.get(sendInfos.size()-1);
            Observable.timer(500, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            if(step2 == 1) {
                                sendInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                step2 = 2;
                                Observable.timer(500, TimeUnit.MILLISECONDS)
                                        .subscribe(new Consumer<Long>() {
                                            @Override
                                            public void accept(Long aLong) throws Exception {
                                                slideUp();
                                            }
                                        });
                            }
                        }
                    });
        }
    }

    private void sendComment() {
        if(!isAllowPlay)return;
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if(nodeInfo == null)return;
        List<AccessibilityNodeInfo> commentBtns = nodeInfo.findAccessibilityNodeInfosByViewId(
                "com.ss.android.ugc.aweme:id/wl");
        if(commentBtns != null && commentBtns.size()>0){
            if(step == 0) {
                AccessibilityNodeInfo commentBtn = commentBtns.get(commentBtns.size()-1);
                commentBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                step = 1;
                return;
            }
//            else if(step == 3){
//                step = 0;
//            }
        }
        List<AccessibilityNodeInfo> editInfos = nodeInfo.findAccessibilityNodeInfosByViewId(
                "com.ss.android.ugc.aweme:id/wh");
        if(step == 1 && editInfos != null && editInfos.size()>0){
            AccessibilityNodeInfo editInfo = editInfos.get(editInfos.size()-1);
            Bundle bundle = new Bundle();
            bundle.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                    mSendMsg+count);
//            editInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            editInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT,bundle);
            editInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            step = 2;
            return;
        }
        List<AccessibilityNodeInfo> sendInfos = nodeInfo.findAccessibilityNodeInfosByViewId(
                "com.ss.android.ugc.aweme:id/wk");
        if(step == 2 && sendInfos != null && sendInfos.size()>0){
            final AccessibilityNodeInfo sendInfo = sendInfos.get(sendInfos.size()-1);
            Observable.timer(500, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            if(step == 2) {
                                sendInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                step = 3;
                                performGlobalAction(GLOBAL_ACTION_BACK);
                                Observable.timer(500, TimeUnit.MILLISECONDS)
                                        .subscribe(new Consumer<Long>() {
                                            @Override
                                            public void accept(Long aLong) throws Exception {
                                                performGlobalAction(GLOBAL_ACTION_BACK);
                                                slideUp();
                                            }
                                        });
                            }
                        }
                    });
        }
//        List<AccessibilityNodeInfo> closeBtns = nodeInfo.findAccessibilityNodeInfosByViewId(
//                "com.ss.android.ugc.aweme:id/aby");
//        if(closeBtns != null && closeBtns.size()>0){
//            AccessibilityNodeInfo closeBtn = closeBtns.get(0);
//            closeBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//        }
    }

    private void slideUp() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Path path = new Path();
            path.moveTo(400,1000);
            path.lineTo(400,100);
            GestureDescription gestureDescription = new GestureDescription.Builder()
                    .addStroke(new GestureDescription.StrokeDescription(path,100,300))
                    .build();
            if(dispatchGesture(gestureDescription, new GestureResultCallback() {
                @Override
                public void onCancelled(GestureDescription gestureDescription) {
                    Log.d(TAG, "onCancelled: ");
                    performGlobalAction(GLOBAL_ACTION_BACK);
                    Observable.timer(1,TimeUnit.SECONDS)
                            .subscribe(new Consumer<Long>() {
                                @Override
                                public void accept(Long aLong) throws Exception {
                                    slideUp();
                                }
                            });
                }

                @Override
                public void onCompleted(GestureDescription gestureDescription) {
                    if(page == 0) {
                        step = 0;
                        sendComment();
                    }else if(page == 1){
                        step2 = 0;
                        sendComment2();
                    }
                }
            },null)){
//                isAllowPlay = false;
            }
        }
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
