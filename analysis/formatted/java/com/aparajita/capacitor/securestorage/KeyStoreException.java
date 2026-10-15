package com.aparajita.capacitor.securestorage;

import com.getcapacitor.PluginCall;
import java.util.HashMap;

/* JADX INFO: loaded from: classes2.dex */
public class KeyStoreException extends Throwable {
    private static final HashMap<ErrorKind, String> errorMap;
    private String message = "";
    private String code = "";

    public enum ErrorKind {
        missingKey,
        invalidData,
        osError,
        unknownError
    }

    static {
        HashMap<ErrorKind, String> map = new HashMap<>();
        errorMap = map;
        map.put(ErrorKind.missingKey, "Empty key or missing key param");
        map.put(ErrorKind.invalidData, "The data in the store is in an invalid format");
        map.put(ErrorKind.osError, "An OS error occurred (%s)");
        map.put(ErrorKind.unknownError, "An unknown error occurred: %s");
    }

    KeyStoreException(ErrorKind errorKind) {
        init(errorKind, null);
    }

    KeyStoreException(ErrorKind errorKind, Throwable th) {
        init(errorKind, th);
    }

    public static void reject(PluginCall pluginCall, ErrorKind errorKind) {
        reject(pluginCall, errorKind, null);
    }

    public static void reject(PluginCall pluginCall, ErrorKind errorKind, Throwable th) {
        KeyStoreException keyStoreException = new KeyStoreException(errorKind, th);
        pluginCall.reject(keyStoreException.message, keyStoreException.code);
    }

    void init(ErrorKind errorKind, Throwable th) {
        String str = errorMap.get(errorKind);
        if (str != null) {
            int iOrdinal = errorKind.ordinal();
            if (iOrdinal != 2 && iOrdinal != 3) {
                this.message = str;
            } else if (th != null) {
                this.message = String.format(str, th.getClass().getSimpleName());
            }
            this.code = errorKind.toString();
        }
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return this.code;
    }

    public void rejectCall(PluginCall pluginCall) {
        pluginCall.reject(this.message, this.code);
    }
}