package org.ttn.ecommerce.security;

public class SecurityConstants {
    public static final long JWT_EXPIRATION =9000000;

    public static final String JWT_SECRET = "secret";

    public static final int LOGIN_ATTEMPTS = 2;

    public static final long REGISTER_TOKEN_EXPIRE_MIN = 180;

    public static final long REFRESH_TOKEN_EXPIRE_HOURS =24;

    public static final long RESET_PASS_EXPIRE_MINUTES = 15;

    public static final String ADMIN_EMAIL_ADDRESS = "kamlesh.singh@tothenew.com";

    public static final int MAX_LOGIN_ATTEMPT = 3;

    public static String FILE_UPLOAD_URL ="/home/kamlesh/Pictures/ecommerce_image";

}
