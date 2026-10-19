package com.aparajita.capacitor.securestorage;

import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.util.Base64;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import org.json.JSONArray;

/* JADX INFO: loaded from: classes2.dex */
@CapacitorPlugin(name = "SecureStorage")
public class SecureStorage extends Plugin {
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private static final int BASE64_FLAGS = 3;
    private static final String CIPHER_TRANSFORMATION = "AES/GCM/NoPadding";
    private static final Character DATA_IV_SEPARATOR = 16;
    private static final String SHARED_PREFERENCES = "WSSecureStorageSharedPreferences";
    private KeyStore keyStore;

    @PluginMethod
    public void internalSetItem(final PluginCall pluginCall) {
        final String dataParam;
        final String keyParam = getKeyParam(pluginCall);
        if (keyParam == null || (dataParam = getDataParam(pluginCall)) == null) {
            return;
        }
        tryStorageOp(pluginCall, new StorageOp() { // from class: com.aparajita.capacitor.securestorage.SecureStorage$$ExternalSyntheticLambda2
            @Override // com.aparajita.capacitor.securestorage.StorageOp
            public final void run() throws GeneralSecurityException, KeyStoreException, IOException {
                this.f$0.lambda$internalSetItem$0(keyParam, dataParam, pluginCall);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$internalSetItem$0(String str, String str2, PluginCall pluginCall) throws GeneralSecurityException, KeyStoreException, IOException {
        storeDataInKeyStore(str, str2);
        pluginCall.resolve();
    }

    @PluginMethod
    public void internalGetItem(final PluginCall pluginCall) {
        final String keyParam = getKeyParam(pluginCall);
        if (keyParam == null) {
            return;
        }
        tryStorageOp(pluginCall, new StorageOp() { // from class: com.aparajita.capacitor.securestorage.SecureStorage$$ExternalSyntheticLambda0
            @Override // com.aparajita.capacitor.securestorage.StorageOp
            public final void run() throws KeyStoreException, GeneralSecurityException, IOException {
                this.f$0.lambda$internalGetItem$1(keyParam, pluginCall);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$internalGetItem$1(String str, PluginCall pluginCall) throws KeyStoreException, GeneralSecurityException, IOException {
        Object dataFromKeyStore = getDataFromKeyStore(str);
        JSObject jSObject = new JSObject();
        if (dataFromKeyStore == null) {
            dataFromKeyStore = JSObject.NULL;
        }
        jSObject.put("data", dataFromKeyStore);
        pluginCall.resolve(jSObject);
    }

    @PluginMethod
    public void internalRemoveItem(final PluginCall pluginCall) {
        final String keyParam = getKeyParam(pluginCall);
        if (keyParam == null) {
            return;
        }
        tryStorageOp(pluginCall, new StorageOp() { // from class: com.aparajita.capacitor.securestorage.SecureStorage$$ExternalSyntheticLambda3
            @Override // com.aparajita.capacitor.securestorage.StorageOp
            public final void run() throws GeneralSecurityException, KeyStoreException, IOException {
                this.f$0.lambda$internalRemoveItem$2(keyParam, pluginCall);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$internalRemoveItem$2(String str, PluginCall pluginCall) throws GeneralSecurityException, KeyStoreException, IOException {
        boolean zRemoveDataFromKeyStore = removeDataFromKeyStore(str);
        JSObject jSObject = new JSObject();
        jSObject.put("success", zRemoveDataFromKeyStore);
        pluginCall.resolve(jSObject);
    }

    @PluginMethod
    public void clearItemsWithPrefix(final PluginCall pluginCall) {
        tryStorageOp(pluginCall, new StorageOp() { // from class: com.aparajita.capacitor.securestorage.SecureStorage$$ExternalSyntheticLambda4
            @Override // com.aparajita.capacitor.securestorage.StorageOp
            public final void run() throws GeneralSecurityException, KeyStoreException, IOException {
                this.f$0.lambda$clearItemsWithPrefix$3(pluginCall);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearItemsWithPrefix$3(PluginCall pluginCall) throws GeneralSecurityException, KeyStoreException, IOException {
        clearKeyStore(pluginCall.getString("_prefix", ""));
        pluginCall.resolve();
    }

    @PluginMethod
    public void getPrefixedKeys(final PluginCall pluginCall) {
        tryStorageOp(pluginCall, new StorageOp() { // from class: com.aparajita.capacitor.securestorage.SecureStorage$$ExternalSyntheticLambda1
            @Override // com.aparajita.capacitor.securestorage.StorageOp
            public final void run() throws KeyStoreException, GeneralSecurityException, IOException {
                this.f$0.lambda$getPrefixedKeys$4(pluginCall);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getPrefixedKeys$4(PluginCall pluginCall) throws KeyStoreException, GeneralSecurityException, IOException {
        JSONArray jSONArray = new JSONArray((Collection) getKeysWithPrefix(pluginCall.getString("prefix", "")));
        JSObject jSObject = new JSObject();
        jSObject.put("keys", (Object) jSONArray);
        pluginCall.resolve(jSObject);
    }

    private SharedPreferences getPrefs() {
        return getContext().getSharedPreferences(SHARED_PREFERENCES, 0);
    }

    private void storeDataInKeyStore(String str, String str2) throws GeneralSecurityException, IOException {
        getPrefs().edit().putString(str, encryptString(str2, str)).apply();
    }

    private String getDataFromKeyStore(String str) throws KeyStoreException, GeneralSecurityException, IOException {
        try {
            String string = getPrefs().getString(str, null);
            if (string != null) {
                return decryptString(string, str);
            }
            return null;
        } catch (ClassCastException unused) {
            throw new KeyStoreException(KeyStoreException.ErrorKind.invalidData);
        }
    }

    private boolean removeDataFromKeyStore(String str) throws GeneralSecurityException, IOException {
        return removeAlias(getKeyStore(), str);
    }

    private boolean removeAlias(KeyStore keyStore, String str) throws java.security.KeyStoreException {
        if (!keyStore.containsAlias(str)) {
            return false;
        }
        keyStore.deleteEntry(str);
        getPrefs().edit().remove(str).apply();
        return true;
    }

    private ArrayList<String> getKeysWithPrefix(String str) throws GeneralSecurityException, IOException {
        ArrayList<String> arrayList = new ArrayList<>();
        Enumeration<String> enumerationAliases = getKeyStore().aliases();
        while (enumerationAliases.hasMoreElements()) {
            String strNextElement = enumerationAliases.nextElement();
            if (strNextElement.startsWith(str)) {
                arrayList.add(strNextElement);
            }
        }
        return arrayList;
    }

    private void clearKeyStore(String str) throws GeneralSecurityException, IOException {
        ArrayList<String> keysWithPrefix = getKeysWithPrefix(str);
        KeyStore keyStore = getKeyStore();
        Iterator<String> it = keysWithPrefix.iterator();
        while (it.hasNext()) {
            removeAlias(keyStore, it.next());
        }
    }

    private void tryStorageOp(PluginCall pluginCall, StorageOp storageOp) {
        KeyStoreException e;
        try {
            storageOp.run();
        } catch (KeyStoreException e2) {
            e = e2;
            e.rejectCall(pluginCall);
        } catch (IOException e3) {
            e = e3;
            e = new KeyStoreException(KeyStoreException.ErrorKind.osError, e);
            e.rejectCall(pluginCall);
        } catch (GeneralSecurityException e4) {
            e = e4;
            e = new KeyStoreException(KeyStoreException.ErrorKind.osError, e);
            e.rejectCall(pluginCall);
        } catch (Exception e5) {
            e = new KeyStoreException(KeyStoreException.ErrorKind.unknownError, e5);
            e.rejectCall(pluginCall);
        }
    }

    private String getKeyParam(PluginCall pluginCall) {
        String string = pluginCall.getString("prefixedKey");
        if (string != null && !string.isEmpty()) {
            return string;
        }
        KeyStoreException.reject(pluginCall, KeyStoreException.ErrorKind.missingKey);
        return null;
    }

    private String getDataParam(PluginCall pluginCall) {
        String string = pluginCall.getString("data");
        if (string != null) {
            return string;
        }
        KeyStoreException.reject(pluginCall, KeyStoreException.ErrorKind.invalidData);
        return null;
    }

    private String encryptString(String str, String str2) throws GeneralSecurityException, IOException {
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(1, getSecretKey(str2));
        byte[] iv = cipher.getIV();
        return Base64.encodeToString(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)), 3) + DATA_IV_SEPARATOR + Base64.encodeToString(iv, 3);
    }

    private String decryptString(String str, String str2) throws KeyStoreException, GeneralSecurityException, IOException {
        String[] strArrSplit = str.split(DATA_IV_SEPARATOR.toString());
        if (strArrSplit.length != 2) {
            throw new KeyStoreException(KeyStoreException.ErrorKind.invalidData);
        }
        byte[] bArrDecode = Base64.decode(strArrSplit[0], 3);
        byte[] bArrDecode2 = Base64.decode(strArrSplit[1], 3);
        KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) getKeyStore().getEntry(str2, null);
        if (secretKeyEntry == null) {
            return null;
        }
        SecretKey secretKey = secretKeyEntry.getSecretKey();
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(2, secretKey, new GCMParameterSpec(128, bArrDecode2));
        return new String(cipher.doFinal(bArrDecode), StandardCharsets.UTF_8);
    }

    private SecretKey getSecretKey(String str) throws GeneralSecurityException, IOException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", ANDROID_KEY_STORE);
        KeyStore.SecretKeyEntry secretKeyEntry = null;
        try {
            secretKeyEntry = (KeyStore.SecretKeyEntry) getKeyStore().getEntry(str, null);
        } catch (UnrecoverableKeyException unused) {
        }
        if (secretKeyEntry == null) {
            keyGenerator.init(new KeyGenParameterSpec.Builder(str, 3).setBlockModes("GCM").setEncryptionPaddings("NoPadding").build());
            return keyGenerator.generateKey();
        }
        return secretKeyEntry.getSecretKey();
    }

    private KeyStore getKeyStore() throws GeneralSecurityException, IOException {
        if (this.keyStore == null) {
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
            this.keyStore = keyStore;
            keyStore.load(null);
        }
        return this.keyStore;
    }
}