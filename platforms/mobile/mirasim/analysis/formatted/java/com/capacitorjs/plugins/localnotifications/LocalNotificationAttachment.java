package com.capacitorjs.plugins.localnotifications;

import com.getcapacitor.JSObject;
import com.google.android.gms.common.internal.ImagesContract;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class LocalNotificationAttachment {
    private String id;
    private JSONObject options;
    private String url;

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public JSONObject getOptions() {
        return this.options;
    }

    public void setOptions(JSONObject jSONObject) {
        this.options = jSONObject;
    }

    public static List<LocalNotificationAttachment> getAttachments(JSObject jSObject) {
        JSONArray jSONArray;
        JSONObject jSONObject;
        JSObject jSObjectFromJSONObject;
        ArrayList arrayList = new ArrayList();
        try {
            jSONArray = jSObject.getJSONArray("attachments");
        } catch (Exception unused) {
            jSONArray = null;
        }
        if (jSONArray != null) {
            for (int i = 0; i < jSONArray.length(); i++) {
                LocalNotificationAttachment localNotificationAttachment = new LocalNotificationAttachment();
                try {
                    jSONObject = jSONArray.getJSONObject(i);
                } catch (JSONException unused2) {
                    jSONObject = null;
                }
                if (jSONObject != null) {
                    try {
                        jSObjectFromJSONObject = JSObject.fromJSONObject(jSONObject);
                    } catch (JSONException unused3) {
                        jSObjectFromJSONObject = null;
                    }
                    localNotificationAttachment.setId(jSObjectFromJSONObject.getString("id"));
                    localNotificationAttachment.setUrl(jSObjectFromJSONObject.getString(ImagesContract.URL));
                    try {
                        localNotificationAttachment.setOptions(jSObjectFromJSONObject.getJSONObject("options"));
                    } catch (JSONException unused4) {
                    }
                    arrayList.add(localNotificationAttachment);
                }
            }
        }
        return arrayList;
    }
}