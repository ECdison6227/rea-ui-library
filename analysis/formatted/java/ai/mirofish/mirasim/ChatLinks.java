package ai.mirofish.mirasim;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
final class ChatLinks {
    private static final Pattern LINK = Pattern.compile("^mirasim://chat/([^/?#]+)(?:/(\\d+))?(?:\\?(.*))?$");

    private ChatLinks() {
    }

    static final class Link {
        final String machine;
        final String room;
        final long seq;

        Link(String str, long j, String str2) {
            this.room = str;
            this.seq = j;
            this.machine = str2;
        }
    }

    static String deepLink(String str, long j, String str2) {
        String str3 = "mirasim://chat/" + encode(str) + (j > 0 ? "/" + j : "");
        return (str2 == null || str2.isEmpty()) ? str3 : str3 + "?machine=" + encode(str2);
    }

    static Link parse(String str) {
        String strDecode;
        if (str == null) {
            return null;
        }
        Matcher matcher = LINK.matcher(str.trim());
        if (!matcher.matches()) {
            return null;
        }
        try {
            String strDecode2 = URLDecoder.decode(matcher.group(1).replace("+", "%2B"), "UTF-8");
            long j = matcher.group(2) == null ? 0L : Long.parseLong(matcher.group(2));
            String strGroup = matcher.group(3);
            String[] strArrSplit = strGroup == null ? new String[0] : strGroup.split("&");
            int length = strArrSplit.length;
            int i = 0;
            while (true) {
                strDecode = "";
                if (i < length) {
                    String str2 = strArrSplit[i];
                    int iIndexOf = str2.indexOf(61);
                    if ("machine".equals(iIndexOf < 0 ? str2 : str2.substring(0, iIndexOf))) {
                        if (iIndexOf >= 0) {
                            strDecode = URLDecoder.decode(str2.substring(iIndexOf + 1), "UTF-8");
                            break;
                        }
                        break;
                    }
                    i++;
                } else {
                    break;
                }
            }
            if (strDecode2.isEmpty()) {
                return null;
            }
            return new Link(strDecode2, j, strDecode);
        } catch (UnsupportedEncodingException | IllegalArgumentException unused) {
            return null;
        }
    }

    private static String encode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8").replace("+", "%20").replace("%21", "!").replace("%27", "'").replace("%28", "(").replace("%29", ")").replace("%7E", "~");
        } catch (UnsupportedEncodingException unused) {
            return str;
        }
    }
}