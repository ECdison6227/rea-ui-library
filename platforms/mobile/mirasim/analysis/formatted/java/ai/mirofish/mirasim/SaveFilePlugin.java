package ai.mirofish.mirasim;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import androidx.autofill.HintConstants;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes.dex */
@CapacitorPlugin(name = "SaveFile")
public class SaveFilePlugin extends Plugin {
    @PluginMethod
    public void save(final PluginCall pluginCall) {
        final String string = pluginCall.getString(HintConstants.AUTOFILL_HINT_NAME);
        final String string2 = pluginCall.getString("data");
        final String string3 = pluginCall.getString("mime", "application/octet-stream");
        if (string == null || string2 == null) {
            pluginCall.reject("name and data are required", "input");
        } else {
            if (Build.VERSION.SDK_INT < 29) {
                JSObject jSObject = new JSObject();
                jSObject.put("saved", false);
                pluginCall.resolve(jSObject);
                return;
            }
            new Thread(new Runnable() { // from class: ai.mirofish.mirasim.SaveFilePlugin$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$save$0(pluginCall, string, string2, string3);
                }
            }).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: saveToDownloads, reason: merged with bridge method [inline-methods] */
    public void lambda$save$0(PluginCall pluginCall, String str, String str2, String str3) {
        Uri uriInsert;
        ContentResolver contentResolver = getContext().getContentResolver();
        try {
            byte[] bArrDecode = Base64.decode(str2, 0);
            String strFreeName = freeName(contentResolver, str.replace('/', '_').replace('\\', '_'));
            ContentValues contentValues = new ContentValues();
            contentValues.put("_display_name", strFreeName);
            contentValues.put("mime_type", str3);
            contentValues.put("is_pending", (Integer) 1);
            uriInsert = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
            try {
                if (uriInsert == null) {
                    pluginCall.reject("the system refused a Downloads entry", "storage");
                    return;
                }
                OutputStream outputStreamOpenOutputStream = contentResolver.openOutputStream(uriInsert);
                try {
                    if (outputStreamOpenOutputStream == null) {
                        throw new IOException("no output stream for " + uriInsert);
                    }
                    outputStreamOpenOutputStream.write(bArrDecode);
                    if (outputStreamOpenOutputStream != null) {
                        outputStreamOpenOutputStream.close();
                    }
                    ContentValues contentValues2 = new ContentValues();
                    contentValues2.put("is_pending", (Integer) 0);
                    contentResolver.update(uriInsert, contentValues2, null, null);
                    JSObject jSObject = new JSObject();
                    jSObject.put("saved", true);
                    jSObject.put("uri", uriInsert.toString());
                    jSObject.put("folder", Environment.DIRECTORY_DOWNLOADS);
                    jSObject.put(HintConstants.AUTOFILL_HINT_NAME, strFreeName);
                    pluginCall.resolve(jSObject);
                } catch (Throwable th) {
                    if (outputStreamOpenOutputStream != null) {
                        try {
                            outputStreamOpenOutputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                if (uriInsert != null) {
                    try {
                        contentResolver.delete(uriInsert, null, null);
                    } catch (Throwable unused) {
                    }
                }
                pluginCall.reject(th.getMessage() == null ? "could not save the file" : th.getMessage(), "storage");
            }
        } catch (Throwable th4) {
            th = th4;
            uriInsert = null;
        }
    }

    private String freeName(ContentResolver contentResolver, String str) {
        String strIndexedName = str;
        for (int i = 1; i <= 999 && nameTaken(contentResolver, strIndexedName); i++) {
            strIndexedName = indexedName(str, i);
        }
        return strIndexedName;
    }

    private static String indexedName(String str, int i) {
        int iLastIndexOf = str.lastIndexOf(46);
        return iLastIndexOf <= 0 ? str + " (" + i + ")" : str.substring(0, iLastIndexOf) + " (" + i + ")" + str.substring(iLastIndexOf);
    }

    /* JADX WARN: Code duplicated, block: B:17:0x0045  */
    private boolean nameTaken(ContentResolver contentResolver, String str) {
        boolean z;
        try {
            Cursor cursorQuery = contentResolver.query(MediaStore.Downloads.EXTERNAL_CONTENT_URI, new String[]{"_id"}, "_display_name = ? AND relative_path = ?", new String[]{str, Environment.DIRECTORY_DOWNLOADS + "/"}, null);
            if (cursorQuery != null) {
                try {
                    z = cursorQuery.getCount() > 0;
                } catch (Throwable th) {
                    if (cursorQuery == null) {
                        throw th;
                    }
                    try {
                        cursorQuery.close();
                        throw th;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        throw th;
                    }
                }
            }
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            return z;
        } catch (Throwable unused) {
            return false;
        }
    }
}