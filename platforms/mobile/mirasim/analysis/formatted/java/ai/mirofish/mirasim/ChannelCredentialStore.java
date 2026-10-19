package ai.mirofish.mirasim;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.util.Base64;
import android.util.Log;
import java.security.KeyStore;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
final class ChannelCredentialStore {
    private static final String KEYSTORE_ALIAS = "mirasim.channel.credential";
    private static final String KEY_ATTENTION_TICKET = "attentionTicket.v1";
    private static final String KEY_CREDENTIAL = "credential.v1";
    private static final String KEY_LAST_EDGE_SEQ = "lastEdgeSeq.v1";
    private static final String PREFS = "mirasim.channel";
    private static final String SEP = ":";

    private ChannelCredentialStore() {
    }

    static void save(Context context, JSONObject jSONObject) {
        seal(context, KEY_CREDENTIAL, jSONObject);
    }

    static JSONObject load(Context context) {
        return open(context, KEY_CREDENTIAL);
    }

    static void clear(Context context) {
        prefs(context).edit().remove(KEY_CREDENTIAL).apply();
    }

    static void saveAttentionTicket(Context context, JSONObject jSONObject) {
        seal(context, KEY_ATTENTION_TICKET, jSONObject);
    }

    static JSONObject loadAttentionTicket(Context context) {
        return open(context, KEY_ATTENTION_TICKET);
    }

    static void clearAttentionTicket(Context context) {
        prefs(context).edit().remove(KEY_ATTENTION_TICKET).apply();
    }

    private static void seal(Context context, String str, JSONObject jSONObject) {
        try {
            SecretKey secretKeyObtainKey = obtainKey();
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(1, secretKeyObtainKey);
            prefs(context).edit().putString(str, Base64.encodeToString(cipher.getIV(), 2) + SEP + Base64.encodeToString(cipher.doFinal(jSONObject.toString().getBytes("UTF-8")), 2)).apply();
        } catch (Exception e) {
            Log.w(PREFS, str + " save failed", e);
        }
    }

    private static JSONObject open(Context context, String str) {
        int iIndexOf;
        try {
            String string = prefs(context).getString(str, null);
            if (string == null || (iIndexOf = string.indexOf(SEP)) <= 0) {
                return null;
            }
            byte[] bArrDecode = Base64.decode(string.substring(0, iIndexOf), 2);
            byte[] bArrDecode2 = Base64.decode(string.substring(iIndexOf + 1), 2);
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            KeyStore.Entry entry = keyStore.getEntry(KEYSTORE_ALIAS, null);
            if (!(entry instanceof KeyStore.SecretKeyEntry)) {
                return null;
            }
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(2, ((KeyStore.SecretKeyEntry) entry).getSecretKey(), new GCMParameterSpec(128, bArrDecode));
            return new JSONObject(new String(cipher.doFinal(bArrDecode2), "UTF-8"));
        } catch (Exception unused) {
            return null;
        }
    }

    static long lastEdgeSeq(Context context) {
        return prefs(context).getLong(KEY_LAST_EDGE_SEQ, 0L);
    }

    static void saveLastEdgeSeq(Context context, long j) {
        prefs(context).edit().putLong(KEY_LAST_EDGE_SEQ, j).apply();
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS, 0);
    }

    private static SecretKey obtainKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        KeyStore.Entry entry = keyStore.getEntry(KEYSTORE_ALIAS, null);
        if (entry instanceof KeyStore.SecretKeyEntry) {
            return ((KeyStore.SecretKeyEntry) entry).getSecretKey();
        }
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", "AndroidKeyStore");
        keyGenerator.init(new KeyGenParameterSpec.Builder(KEYSTORE_ALIAS, 3).setBlockModes("GCM").setEncryptionPaddings("NoPadding").setKeySize(256).build());
        return keyGenerator.generateKey();
    }
}