package ai.mirofish.mirasim;

import android.content.Intent;
import android.net.Uri;
import androidx.core.content.FileProvider;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.google.android.gms.common.internal.ImagesContract;
import java.io.File;

/* JADX INFO: loaded from: classes.dex */
@CapacitorPlugin(name = "ApkUpdater")
public class ApkUpdaterPlugin extends Plugin {
    private static final int BUFFER = 65536;
    private static final long MAX_APK_BYTES = 536870912;
    private static final String UPDATES_DIR = "updates";

    @PluginMethod
    public void canInstall(PluginCall pluginCall) {
        JSObject jSObject = new JSObject();
        jSObject.put("allowed", getContext().getPackageManager().canRequestPackageInstalls());
        pluginCall.resolve(jSObject);
    }

    @PluginMethod
    public void requestInstallPermission(PluginCall pluginCall) {
        Intent intent = new Intent("android.settings.MANAGE_UNKNOWN_APP_SOURCES");
        intent.setData(Uri.parse("package:" + getContext().getPackageName()));
        getActivity().startActivity(intent);
        pluginCall.resolve();
    }

    @PluginMethod
    public void download(final PluginCall pluginCall) {
        final String string = pluginCall.getString(ImagesContract.URL);
        final String string2 = pluginCall.getString("sha256");
        final String string3 = pluginCall.getString("version");
        if (string == null || string2 == null || string3 == null) {
            pluginCall.reject("url, sha256 and version are required", "storage");
        } else {
            new Thread(new Runnable() { // from class: ai.mirofish.mirasim.ApkUpdaterPlugin$$ExternalSyntheticLambda0
                /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 0 */
                @Override // java.lang.Runnable
                public final void run() throws Throwable {
                    this.f$0.lambda$download$0(string3, pluginCall, string, string2);
                }
            }).start();
        }
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 0 */
    /* JADX INFO: Access modifiers changed from: private */
    /*  JADX ERROR: Type inference failed
        jadx.core.utils.exceptions.JadxOverflowException: Type inference error: updates count limit reached with updateSeq = 48500001. Try increasing type updates limit count.
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:79)
        */
    public /* synthetic */ void lambda$download$0(java.lang.String r22, com.getcapacitor.PluginCall r23, java.lang.String r24, java.lang.String r25) throws java.lang.Throwable {
        /*
            r21 = this;
            r1 = r23
            java.lang.String r2 = "network"
            java.lang.String r3 = "storage"
            java.lang.String r0 = "update download failed: HTTP "
            java.io.File r4 = new java.io.File
            android.content.Context r5 = r21.getContext()
            java.io.File r5 = r5.getCacheDir()
            java.lang.String r6 = "updates"
            r4.<init>(r5, r6)
            java.io.File r5 = new java.io.File
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            java.lang.String r7 = "mirasim-"
            r6.<init>(r7)
            r7 = r22
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r7 = ".apk"
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r6 = r6.toString()
            r5.<init>(r4, r6)
            r6 = 0
            boolean r7 = r4.exists()     // Catch: java.lang.Throwable -> L1ad java.lang.Exception -> L1b1 java.io.IOException -> L1c6
            if (r7 != 0) goto L49
            boolean r7 = r4.mkdirs()     // Catch: java.lang.Throwable -> L1ad java.lang.Exception -> L1b1 java.io.IOException -> L1c6
            if (r7 != 0) goto L49
            java.lang.String r0 = "cannot create update cache dir"
            r1.reject(r0, r3)     // Catch: java.lang.Throwable -> L1ad java.lang.Exception -> L1b1 java.io.IOException -> L1c6
            return
        L49:
            purge(r4)     // Catch: java.lang.Throwable -> L1ad java.lang.Exception -> L1b1 java.io.IOException -> L1c6
            java.net.URL r4 = new java.net.URL     // Catch: java.lang.Throwable -> L1ad java.lang.Exception -> L1b1 java.io.IOException -> L1c6
            r7 = r24
            r4.<init>(r7)     // Catch: java.lang.Throwable -> L1ad java.lang.Exception -> L1b1 java.io.IOException -> L1c6
            java.net.URLConnection r4 = r4.openConnection()     // Catch: java.lang.Throwable -> L1ad java.lang.Exception -> L1b1 java.io.IOException -> L1c6
            java.net.HttpURLConnection r4 = (java.net.HttpURLConnection) r4     // Catch: java.lang.Throwable -> L1ad java.lang.Exception -> L1b1 java.io.IOException -> L1c6
            r6 = 30000(0x7530, float:4.2039E-41)
            r4.setConnectTimeout(r6)     // Catch: java.lang.Throwable -> L193 java.lang.Exception -> L19b java.io.IOException -> L1a4
            r6 = 60000(0xea60, float:8.4078E-41)
            r4.setReadTimeout(r6)     // Catch: java.lang.Throwable -> L193 java.lang.Exception -> L19b java.io.IOException -> L1a4
            r6 = 1
            r4.setInstanceFollowRedirects(r6)     // Catch: java.lang.Throwable -> L193 java.lang.Exception -> L19b java.io.IOException -> L1a4
            int r6 = r4.getResponseCode()     // Catch: java.lang.Throwable -> L193 java.lang.Exception -> L19b java.io.IOException -> L1a4
            r7 = 200(0xc8, float:2.8E-43)
            if (r6 < r7) goto L172
            r7 = 300(0x12c, float:4.2E-43)
            if (r6 < r7) goto L76
            goto L172
        L76:
            long r6 = r4.getContentLengthLong()     // Catch: java.lang.Throwable -> L193 java.lang.Exception -> L19b java.io.IOException -> L1a4
            r8 = 536870912(0x20000000, double:2.65249474E-315)
            int r0 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r0 <= 0) goto L9f
            java.lang.String r0 = "refusing an implausibly large APK"
            r1.reject(r0, r3)     // Catch: java.lang.Throwable -> L91 java.lang.Exception -> L97 java.io.IOException -> L9b
            if (r4 == 0) goto L8d
            r4.disconnect()
            return
        L8d:
            r5 = r21
            goto L1dd
        L91:
            r0 = move-exception
            r5 = r21
            r6 = r4
            goto L1df
        L97:
            r0 = move-exception
            r6 = r4
            goto L1b2
        L9b:
            r0 = move-exception
            r6 = r4
            goto L1c7
        L9f:
            java.lang.String r0 = "SHA-256"
            java.security.MessageDigest r0 = java.security.MessageDigest.getInstance(r0)     // Catch: java.lang.Throwable -> L193 java.lang.Exception -> L19b java.io.IOException -> L1a4
            r10 = 65536(0x10000, float:9.1835E-41)
            byte[] r10 = new byte[r10]     // Catch: java.lang.Throwable -> L193 java.lang.Exception -> L19b java.io.IOException -> L1a4
            java.io.InputStream r11 = r4.getInputStream()     // Catch: java.lang.Throwable -> L193 java.lang.Exception -> L19b java.io.IOException -> L1a4
            java.io.FileOutputStream r12 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L160
            r12.<init>(r5)     // Catch: java.lang.Throwable -> L160
            r13 = 0
            r17 = r8
            r15 = r13
        Lb7:
            int r8 = r11.read(r10)     // Catch: java.lang.Throwable -> L150
            r9 = -1
            if (r8 == r9) goto L109
            r9 = 0
            r12.write(r10, r9, r8)     // Catch: java.lang.Throwable -> L150
            r0.update(r10, r9, r8)     // Catch: java.lang.Throwable -> L150
            long r8 = (long) r8     // Catch: java.lang.Throwable -> L150
            long r8 = r8 + r15
            int r15 = (r8 > r17 ? 1 : (r8 == r17 ? 0 : -1))
            if (r15 > 0) goto Lfa
            int r15 = (r6 > r13 ? 1 : (r6 == r13 ? 0 : -1))
            if (r15 <= 0) goto Lee
            com.getcapacitor.JSObject r15 = new com.getcapacitor.JSObject     // Catch: java.lang.Throwable -> L150
            r15.<init>()     // Catch: java.lang.Throwable -> L150
            java.lang.String r13 = "progress"
            r22 = r4
            r14 = r5
            double r4 = (double) r8
            r19 = r4
            double r4 = (double) r6
            double r4 = r19 / r4
            r15.put(r13, r4)     // Catch: java.lang.Throwable -> Leb
            java.lang.String r4 = "downloadProgress"
            r5 = r21
            r5.notifyListeners(r4, r15)     // Catch: java.lang.Throwable -> L107
            goto Lf3
        Leb:
            r0 = move-exception
            goto L154
        Lee:
            r22 = r4
            r14 = r5
            r5 = r21
        Lf3:
            r4 = r22
            r15 = r8
            r5 = r14
            r13 = 0
            goto Lb7
        Lfa:
            r22 = r4
            r14 = r5
            r5 = r21
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException     // Catch: java.lang.Throwable -> L107
            java.lang.String r4 = "APK exceeded the size ceiling mid-stream"
            r0.<init>(r4)     // Catch: java.lang.Throwable -> L107
            throw r0     // Catch: java.lang.Throwable -> L107
        L107:
            r0 = move-exception
            goto L156
        L109:
            r22 = r4
            r14 = r5
            r5 = r21
            r12.close()     // Catch: java.lang.Throwable -> L14e
            if (r11 == 0) goto L116
            r11.close()     // Catch: java.lang.Throwable -> L18d java.lang.Exception -> L18f java.io.IOException -> L191
        L116:
            byte[] r0 = r0.digest()     // Catch: java.lang.Throwable -> L18d java.lang.Exception -> L18f java.io.IOException -> L191
            java.lang.String r0 = hex(r0)     // Catch: java.lang.Throwable -> L18d java.lang.Exception -> L18f java.io.IOException -> L191
            r4 = r25
            boolean r0 = r0.equalsIgnoreCase(r4)     // Catch: java.lang.Throwable -> L18d java.lang.Exception -> L18f java.io.IOException -> L191
            if (r0 != 0) goto L136
            deleteQuietly(r14)     // Catch: java.lang.Throwable -> L18d java.lang.Exception -> L18f java.io.IOException -> L191
            java.lang.String r0 = "checksum mismatch"
            java.lang.String r4 = "checksum"
            r1.reject(r0, r4)     // Catch: java.lang.Throwable -> L18d java.lang.Exception -> L18f java.io.IOException -> L191
            if (r22 == 0) goto L1dd
            r22.disconnect()
            return
        L136:
            com.getcapacitor.JSObject r0 = new com.getcapacitor.JSObject     // Catch: java.lang.Throwable -> L18d java.lang.Exception -> L18f java.io.IOException -> L191
            r0.<init>()     // Catch: java.lang.Throwable -> L18d java.lang.Exception -> L18f java.io.IOException -> L191
            java.lang.String r4 = "path"
            java.lang.String r6 = r14.getAbsolutePath()     // Catch: java.lang.Throwable -> L18d java.lang.Exception -> L18f java.io.IOException -> L191
            r0.put(r4, r6)     // Catch: java.lang.Throwable -> L18d java.lang.Exception -> L18f java.io.IOException -> L191
            r1.resolve(r0)     // Catch: java.lang.Throwable -> L18d java.lang.Exception -> L18f java.io.IOException -> L191
            if (r22 == 0) goto L1dd
            r22.disconnect()
            return
        L14e:
            r0 = move-exception
            goto L166
        L150:
            r0 = move-exception
            r22 = r4
            r14 = r5
        L154:
            r5 = r21
        L156:
            r4 = r0
            r12.close()     // Catch: java.lang.Throwable -> L15b
            goto L15f
        L15b:
            r0 = move-exception
            r4.addSuppressed(r0)     // Catch: java.lang.Throwable -> L14e
        L15f:
            throw r4     // Catch: java.lang.Throwable -> L14e
        L160:
            r0 = move-exception
            r22 = r4
            r14 = r5
            r5 = r21
        L166:
            r4 = r0
            if (r11 == 0) goto L171
            r11.close()     // Catch: java.lang.Throwable -> L16d
            goto L171
        L16d:
            r0 = move-exception
            r4.addSuppressed(r0)     // Catch: java.lang.Throwable -> L18d java.lang.Exception -> L18f java.io.IOException -> L191
        L171:
            throw r4     // Catch: java.lang.Throwable -> L18d java.lang.Exception -> L18f java.io.IOException -> L191
        L172:
            r22 = r4
            r14 = r5
            r5 = r21
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L18d java.lang.Exception -> L18f java.io.IOException -> L191
            r4.<init>(r0)     // Catch: java.lang.Throwable -> L18d java.lang.Exception -> L18f java.io.IOException -> L191
            java.lang.StringBuilder r0 = r4.append(r6)     // Catch: java.lang.Throwable -> L18d java.lang.Exception -> L18f java.io.IOException -> L191
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Throwable -> L18d java.lang.Exception -> L18f java.io.IOException -> L191
            r1.reject(r0, r2)     // Catch: java.lang.Throwable -> L18d java.lang.Exception -> L18f java.io.IOException -> L191
            if (r22 == 0) goto L1dd
            r22.disconnect()
            return
        L18d:
            r0 = move-exception
            goto L198
        L18f:
            r0 = move-exception
            goto L1a1
        L191:
            r0 = move-exception
            goto L1aa
        L193:
            r0 = move-exception
            r5 = r21
            r22 = r4
        L198:
            r6 = r22
            goto L1df
        L19b:
            r0 = move-exception
            r22 = r4
            r14 = r5
            r5 = r21
        L1a1:
            r6 = r22
            goto L1b5
        L1a4:
            r0 = move-exception
            r22 = r4
            r14 = r5
            r5 = r21
        L1aa:
            r6 = r22
            goto L1ca
        L1ad:
            r0 = move-exception
            r5 = r21
            goto L1df
        L1b1:
            r0 = move-exception
        L1b2:
            r14 = r5
            r5 = r21
        L1b5:
            deleteQuietly(r14)     // Catch: java.lang.Throwable -> L1de
            java.lang.String r0 = r0.getMessage()     // Catch: java.lang.Throwable -> L1de
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch: java.lang.Throwable -> L1de
            r1.reject(r0, r3)     // Catch: java.lang.Throwable -> L1de
            if (r6 == 0) goto L1dd
            goto L1da
        L1c6:
            r0 = move-exception
        L1c7:
            r14 = r5
            r5 = r21
        L1ca:
            deleteQuietly(r14)     // Catch: java.lang.Throwable -> L1de
            java.lang.String r0 = r0.getMessage()     // Catch: java.lang.Throwable -> L1de
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch: java.lang.Throwable -> L1de
            r1.reject(r0, r2)     // Catch: java.lang.Throwable -> L1de
            if (r6 == 0) goto L1dd
        L1da:
            r6.disconnect()
        L1dd:
            return
        L1de:
            r0 = move-exception
        L1df:
            if (r6 == 0) goto L1e4
            r6.disconnect()
        L1e4:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: ai.mirofish.mirasim.ApkUpdaterPlugin.lambda$download$0(java.lang.String, com.getcapacitor.PluginCall, java.lang.String, java.lang.String):void");
    }

    @PluginMethod
    public void install(PluginCall pluginCall) {
        String string = pluginCall.getString("path");
        if (string == null) {
            pluginCall.reject("path is required", "install");
            return;
        }
        File file = new File(string);
        if (!file.isFile()) {
            pluginCall.reject("no such APK", "install");
            return;
        }
        try {
            Uri uriForFile = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".fileprovider", file);
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");
            intent.addFlags(1);
            intent.addFlags(268435456);
            getActivity().startActivity(intent);
            pluginCall.resolve();
        } catch (Exception e) {
            pluginCall.reject(String.valueOf(e.getMessage()), "install");
        }
    }

    private static void purge(File file) {
        File[] fileArrListFiles = file.listFiles();
        if (fileArrListFiles == null) {
            return;
        }
        for (File file2 : fileArrListFiles) {
            deleteQuietly(file2);
        }
    }

    private static void deleteQuietly(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        file.delete();
    }

    private static String hex(byte[] bArr) {
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        for (byte b : bArr) {
            sb.append(String.format("%02x", Byte.valueOf(b)));
        }
        return sb.toString();
    }
}