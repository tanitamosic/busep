package securityproject.model.webtokens;


import java.util.HashSet;
import java.util.Set;

public class JWTBlacklist {

    private static final Set<String> blacklist = new HashSet<>();

    public static synchronized void invalidateToken(String token) {
        blacklist.add(token);
    }

    public static synchronized Boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }
}
