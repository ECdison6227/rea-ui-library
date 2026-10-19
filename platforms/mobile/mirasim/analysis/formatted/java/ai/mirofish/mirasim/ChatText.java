package ai.mirofish.mirasim;

import android.content.Context;
import androidx.autofill.HintConstants;
import androidx.savedstate.serialization.ClassDiscriminatorModeKt;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
final class ChatText {
    private ChatText() {
    }

    static String str(JSONObject jSONObject, String str) {
        return (jSONObject == null || jSONObject.isNull(str)) ? "" : jSONObject.optString(str, "");
    }

    static String oneLine(String str) {
        String strTrim = str.replaceAll("\\s+", " ").trim();
        return strTrim.length() > 120 ? strTrim.substring(0, 120) : strTrim;
    }

    static String previewText(Context context, JSONObject jSONObject) {
        if (jSONObject == null) {
            return "";
        }
        if (jSONObject.optBoolean("retracted", false)) {
            return context.getString(R.string.chat_preview_retracted);
        }
        String strOneLine = oneLine(str(jSONObject, "text"));
        if (!strOneLine.isEmpty()) {
            return strOneLine;
        }
        JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("media");
        if (jSONObjectOptJSONObject != null) {
            return mediaLabel(context, str(jSONObjectOptJSONObject, "kind"), str(jSONObjectOptJSONObject, HintConstants.AUTOFILL_HINT_NAME));
        }
        return cardLabel(context, str(jSONObject, "card"));
    }

    static String previewOfLine(Context context, JSONObject jSONObject) {
        String strOneLine = oneLine(str(jSONObject, "text"));
        if (!strOneLine.isEmpty()) {
            return strOneLine;
        }
        JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("media");
        JSONObject jSONObjectOptJSONObject = jSONArrayOptJSONArray == null ? null : jSONArrayOptJSONArray.optJSONObject(0);
        if (jSONObjectOptJSONObject != null) {
            return mediaLabel(context, str(jSONObjectOptJSONObject, "kind"), str(jSONObjectOptJSONObject, HintConstants.AUTOFILL_HINT_NAME));
        }
        return cardLabel(context, str(jSONObject.optJSONObject("card"), ClassDiscriminatorModeKt.CLASS_DISCRIMINATOR_KEY));
    }

    static String withKind(Context context, String str, String str2) {
        str.hashCode();
        switch (str) {
            case "thread":
                return context.getString(R.string.chat_attention_thread, str2);
            case "keyword":
                return context.getString(R.string.chat_attention_keyword, str2);
            case "dm":
                return context.getString(R.string.chat_attention_dm, str2);
            case "nudge":
                return context.getString(R.string.chat_attention_nudge, str2);
            case "reply":
                return context.getString(R.string.chat_attention_reply, str2);
            case "mention":
                return context.getString(R.string.chat_attention_mention, str2);
            case "comment":
                return context.getString(R.string.chat_attention_comment, str2);
            case "session":
                return context.getString(R.string.chat_attention_session, str2);
            default:
                return str2;
        }
    }

    static String roomLabel(Context context, String str) {
        if ("general".equals(str)) {
            return context.getString(R.string.chat_room_general);
        }
        if (str.startsWith("s-")) {
            return context.getString(R.string.chat_room_session);
        }
        return (!str.startsWith("g-") || str.length() <= 2) ? str : str.substring(2);
    }

    static String mediaLabel(Context context, String str, String str2) {
        str.hashCode();
        if (str.equals("image")) {
            return context.getString(R.string.chat_preview_image);
        }
        if (str.equals("video")) {
            return context.getString(R.string.chat_preview_video);
        }
        return str2.isEmpty() ? context.getString(R.string.chat_preview_file_plain) : context.getString(R.string.chat_preview_file, str2);
    }

    static String cardLabel(Context context, String str) {
        str.hashCode();
        switch (str) {
            case "file":
                return context.getString(R.string.chat_preview_file_plain);
            case "turn":
                return context.getString(R.string.chat_preview_turn);
            case "issue":
                return context.getString(R.string.chat_preview_issue);
            case "session":
                return context.getString(R.string.chat_preview_session);
            default:
                return "";
        }
    }
}