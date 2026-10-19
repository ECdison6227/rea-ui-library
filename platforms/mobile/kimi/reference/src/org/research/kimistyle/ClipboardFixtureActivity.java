package org.research.kimistyle;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import java.nio.ByteBuffer;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;
import org.json.JSONObject;

/** D research helper: writes only its explicitly supplied synthetic fixture. */
public final class ClipboardFixtureActivity extends Activity {
    private static final String TAG = "ResearchClipboardFixture";
    private final String attemptId = UUID.randomUUID().toString();
    private String fixture;
    private String sha256;
    private int characters = 0, utf8Bytes = 0;
    private boolean attempted = false;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        TextView view = new TextView(this);
        view.setGravity(Gravity.CENTER);
        view.setText("Research fixture");
        setContentView(view);
        try {
            Bundle extras = getIntent().getExtras();
            if (extras == null || extras.size() != 1 || !extras.containsKey("fixtureBase64")) {
                reject("rejected-extra-contract"); return;
            }
            String encoded = extras.getString("fixtureBase64");
            if (encoded == null || encoded.isEmpty() || encoded.length() > 12000 ||
                    !encoded.matches("(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?")) {
                reject("rejected-base64"); return;
            }
            byte[] bytes = Base64.decode(encoded, Base64.NO_WRAP);
            fixture = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)
                .decode(ByteBuffer.wrap(bytes)).toString();
            characters = fixture.codePointCount(0, fixture.length());
            utf8Bytes = bytes.length;
            StringBuilder digest = new StringBuilder();
            for (byte value : MessageDigest.getInstance("SHA-256").digest(bytes))
                digest.append(String.format(java.util.Locale.ROOT, "%02x", value & 255));
            sha256 = digest.toString();
            if (!fixture.startsWith("Research Run03") || characters > 2000) {
                reject("rejected-fixture-boundary"); return;
            }
            record("accepted-awaiting-focus");
        } catch (Exception ignored) {
            reject("rejected-decoding");
        }
    }

    @Override public void onWindowFocusChanged(boolean focused) {
        super.onWindowFocusChanged(focused);
        if (!focused || attempted || fixture == null) return;
        attempted = true;
        try {
            ClipboardManager clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
            // No getPrimaryClip/hasPrimaryClip/guest clipboard read is performed.
            clipboard.setPrimaryClip(ClipData.newPlainText("Research Run03 fixture", fixture));
            record("written");
        } catch (Exception ignored) {
            record("write-failed");
        } finally {
            fixture = null;
            finish();
        }
    }

    private void reject(String status) {
        attempted = true; fixture = null; record(status); finish();
    }
    private void record(String status) {
        try {
            JSONObject entry = new JSONObject();
            entry.put("classification", "D");
            entry.put("helper", "ClipboardFixtureActivity");
            entry.put("attemptId", attemptId);
            entry.put("status", status);
            entry.put("characters", characters);
            entry.put("utf8Bytes", utf8Bytes);
            entry.put("sha256", sha256 == null ? JSONObject.NULL : sha256);
            entry.put("recordedAtDeviceMillis", System.currentTimeMillis());
            entry.put("pid", android.os.Process.myPid());
            entry.put("clipboardRead", false);
            entry.put("hostClipboardAccess", false);
            String metadata = entry.toString();
            getSharedPreferences("clipboard-fixture-status", MODE_PRIVATE).edit()
                .putString("lastStatusMetadata", metadata).commit();
            Log.i(TAG, metadata);
        } catch (Exception ignored) {
            Log.e(TAG, "metadata-record-failed");
        }
    }
}
