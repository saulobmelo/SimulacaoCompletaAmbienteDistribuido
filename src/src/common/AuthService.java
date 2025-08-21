package common;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.time.Instant;

public class AuthService {
    private static final String SECRET = "verysecretkey";
    private static final String HMAC = "HmacSHA256";

    public static String issueToken(String nodeId, long ttlSeconds) {
        try {
            long exp = Instant.now().getEpochSecond() + ttlSeconds;
            String payload = nodeId + ":" + exp;
            Mac mac = Mac.getInstance(HMAC);
            mac.init(new SecretKeySpec(SECRET.getBytes(), HMAC));
            byte[] sig = mac.doFinal(payload.getBytes());
            return Base64.getUrlEncoder().withoutPadding().encodeToString((payload + ":" + Base64.getUrlEncoder().encodeToString(sig)).getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean validateToken(String token) {
        try {
            String decoded = new String(Base64.getUrlDecoder().decode(token));
            String[] parts = decoded.split(":");
            if (parts.length < 3) return false;
            String nodeId = parts[0];
            long exp = Long.parseLong(parts[1]);
            String sig = parts[2];
            if (Instant.now().getEpochSecond() > exp) return false;
            String payload = nodeId + ":" + exp;
            Mac mac = Mac.getInstance(HMAC);
            mac.init(new SecretKeySpec(SECRET.getBytes(), HMAC));
            byte[] expected = mac.doFinal(payload.getBytes());
            String expectedSig = Base64.getUrlEncoder().encodeToString(expected);
            return expectedSig.equals(sig);
        } catch (Exception e) {
            return false;
        }
    }
}