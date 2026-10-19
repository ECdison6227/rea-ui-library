package ai.mirofish.mirasim;

import android.util.Base64;
import androidx.savedstate.serialization.ClassDiscriminatorModeKt;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.util.zip.GZIPInputStream;
import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
final class MirachannelE2e {
    private static final byte[] INFO_C2S = "mirachannel-e2e/c2s".getBytes();
    private static final byte[] INFO_S2C = "mirachannel-e2e/s2c".getBytes();
    private byte[] c2sKey;
    private final KeyPair ephemeral;
    private byte[] s2cKey;
    private final byte[] saltC;
    private long nOut = 0;
    private long nInSeen = 0;

    MirachannelE2e() throws Exception {
        byte[] bArr = new byte[16];
        this.saltC = bArr;
        new SecureRandom().nextBytes(bArr);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(new ECGenParameterSpec("secp256r1"));
        this.ephemeral = keyPairGenerator.generateKeyPair();
    }

    String sec1() throws Exception {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(ClassDiscriminatorModeKt.CLASS_DISCRIMINATOR_KEY, "sec1");
        jSONObject.put("epk", b64url(uncompressedPoint((ECPublicKey) this.ephemeral.getPublic())));
        jSONObject.put("salt", b64url(this.saltC));
        return jSONObject.toString();
    }

    void onSec2(JSONObject jSONObject, String str) throws Exception {
        byte[] bArrB64urlDecode = b64urlDecode(jSONObject.getString("salt"));
        byte[] bArrEcdh = ecdh(str);
        byte[] bArr = new byte[32];
        System.arraycopy(this.saltC, 0, bArr, 0, 16);
        System.arraycopy(bArrB64urlDecode, 0, bArr, 16, 16);
        this.c2sKey = hkdfSha256(bArrEcdh, bArr, INFO_C2S);
        this.s2cKey = hkdfSha256(bArrEcdh, bArr, INFO_S2C);
    }

    boolean ready() {
        return this.c2sKey != null;
    }

    String seal(String str) throws Exception {
        long j = this.nOut + 1;
        this.nOut = j;
        byte[] bArr = new byte[12];
        new SecureRandom().nextBytes(bArr);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(1, new SecretKeySpec(this.c2sKey, "AES"), new GCMParameterSpec(128, bArr));
        cipher.updateAAD(("mc:" + j).getBytes("UTF-8"));
        byte[] bArrDoFinal = cipher.doFinal(str.getBytes("UTF-8"));
        byte[] bArr2 = new byte[bArrDoFinal.length + 12];
        System.arraycopy(bArr, 0, bArr2, 0, 12);
        System.arraycopy(bArrDoFinal, 0, bArr2, 12, bArrDoFinal.length);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(ClassDiscriminatorModeKt.CLASS_DISCRIMINATOR_KEY, "sec");
        jSONObject.put("n", j);
        jSONObject.put("ct", b64url(bArr2));
        return jSONObject.toString();
    }

    String open(JSONObject jSONObject) throws Exception {
        long j = jSONObject.getLong("n");
        if (j <= this.nInSeen) {
            throw new Exception("e2e: replayed counter " + j);
        }
        byte[] bArrB64urlDecode = b64urlDecode(jSONObject.getString("ct"));
        if (bArrB64urlDecode.length < 28) {
            throw new Exception("e2e: short envelope");
        }
        byte[] bArr = new byte[12];
        System.arraycopy(bArrB64urlDecode, 0, bArr, 0, 12);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(2, new SecretKeySpec(this.s2cKey, "AES"), new GCMParameterSpec(128, bArr));
        cipher.updateAAD(("mc:" + j).getBytes("UTF-8"));
        byte[] bArrDoFinal = cipher.doFinal(bArrB64urlDecode, 12, bArrB64urlDecode.length - 12);
        this.nInSeen = j;
        if (jSONObject.optInt("z", 0) == 1) {
            bArrDoFinal = gunzip(bArrDoFinal);
        }
        return new String(bArrDoFinal, "UTF-8");
    }

    private byte[] ecdh(String str) throws Exception {
        byte[] bArrB64urlDecode = b64urlDecode(str);
        if (bArrB64urlDecode.length != 65 || bArrB64urlDecode[0] != 4) {
            throw new Exception("e2e: bad host key");
        }
        AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("EC");
        algorithmParameters.init(new ECGenParameterSpec("secp256r1"));
        ECPublicKey eCPublicKey = (ECPublicKey) KeyFactory.getInstance("EC").generatePublic(new ECPublicKeySpec(new ECPoint(new BigInteger(1, slice(bArrB64urlDecode, 1, 33)), new BigInteger(1, slice(bArrB64urlDecode, 33, 65))), (ECParameterSpec) algorithmParameters.getParameterSpec(ECParameterSpec.class)));
        KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH");
        keyAgreement.init(this.ephemeral.getPrivate());
        keyAgreement.doPhase(eCPublicKey, true);
        return keyAgreement.generateSecret();
    }

    private static byte[] hkdfSha256(byte[] bArr, byte[] bArr2, byte[] bArr3) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(bArr2, "HmacSHA256"));
        mac.init(new SecretKeySpec(mac.doFinal(bArr), "HmacSHA256"));
        mac.update(bArr3);
        mac.update((byte) 1);
        return mac.doFinal();
    }

    private static byte[] uncompressedPoint(ECPublicKey eCPublicKey) {
        byte[] bArrFixed32 = fixed32(eCPublicKey.getW().getAffineX());
        byte[] bArrFixed33 = fixed32(eCPublicKey.getW().getAffineY());
        byte[] bArr = new byte[65];
        bArr[0] = 4;
        System.arraycopy(bArrFixed32, 0, bArr, 1, 32);
        System.arraycopy(bArrFixed33, 0, bArr, 33, 32);
        return bArr;
    }

    private static byte[] fixed32(BigInteger bigInteger) {
        byte[] byteArray = bigInteger.toByteArray();
        byte[] bArr = new byte[32];
        if (byteArray.length >= 32) {
            System.arraycopy(byteArray, byteArray.length - 32, bArr, 0, 32);
            return bArr;
        }
        System.arraycopy(byteArray, 0, bArr, 32 - byteArray.length, byteArray.length);
        return bArr;
    }

    private static byte[] slice(byte[] bArr, int i, int i2) {
        int i3 = i2 - i;
        byte[] bArr2 = new byte[i3];
        System.arraycopy(bArr, i, bArr2, 0, i3);
        return bArr2;
    }

    private static byte[] gunzip(byte[] bArr) throws Exception {
        GZIPInputStream gZIPInputStream = new GZIPInputStream(new ByteArrayInputStream(bArr));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr2 = new byte[4096];
        while (true) {
            int i = gZIPInputStream.read(bArr2);
            if (i <= 0) {
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr2, 0, i);
        }
    }

    static String b64url(byte[] bArr) {
        return Base64.encodeToString(bArr, 11);
    }

    static byte[] b64urlDecode(String str) {
        return Base64.decode(str, 10);
    }
}