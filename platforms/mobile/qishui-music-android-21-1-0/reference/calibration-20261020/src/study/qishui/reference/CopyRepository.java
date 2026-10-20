package study.qishui.reference;
import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

final class CopyRepository {
    final JSONObject root;
    CopyRepository(Context context, String asset) {
        try (InputStream input = context.getAssets().open(asset); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096]; int count;
            while ((count = input.read(buffer)) != -1) output.write(buffer, 0, count);
            root = new JSONObject(output.toString("UTF-8"));
        } catch (Exception error) { throw new IllegalStateException("Missing local copy asset: " + asset, error); }
    }
    JSONObject section(String key) { return root.optJSONObject(key); }
    static String text(JSONObject section, String key) { return section == null ? "" : section.optString(key); }
    static String[] bold(JSONObject section) {
        JSONArray values = section == null ? null : section.optJSONArray("bold");
        if (values == null) return new String[0];
        String[] result = new String[values.length()]; for (int i=0;i<result.length;i++) result[i]=values.optString(i); return result;
    }
}
