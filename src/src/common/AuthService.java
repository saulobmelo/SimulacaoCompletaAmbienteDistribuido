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
            String data = nodeId + ":" + exp;
            Mac mac = Mac.getInstance(HMAC);
            mac.init(new SecretKeySpec(SECRET.getBytes(), HMAC));
            String sig = Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(data.getBytes()));
            return Base64.getUrlEncoder().withoutPadding().encodeToString((data + ":" + sig).getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean validateToken(String token, String expectedNode) {
        try {
            String decoded = new String(Base64.getUrlDecoder().decode(token));
            String[] parts = decoded.split(":");
            if (parts.length != 3) return false;
            String nodeId = parts[0];
            long exp = Long.parseLong(parts[1]);
            String sig = parts[2];
            String data = nodeId + ":" + exp;
            Mac mac = Mac.getInstance(HMAC);
            mac.init(new SecretKeySpec(SECRET.getBytes(), HMAC));
            String expected = Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(data.getBytes()));
            if (!expected.equals(sig)) return false;
            if (Instant.now().getEpochSecond() > exp) return false;
            if (expectedNode != null && !expectedNode.equals(nodeId)) return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}