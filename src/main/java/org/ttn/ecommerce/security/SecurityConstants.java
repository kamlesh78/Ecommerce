package org.ttn.ecommerce.security;

public class SecurityConstants {
    public static final long JWT_EXPIRATION =900000;

    public static final String JWT_SECRET = "secret";

    public static final int LOGIN_ATTEMPTS = 2;

    public static final long REGISTER_TOKEN_EXPIRE_MIN = 180;

    public static final long REFRESH_TOKEN_EXPIRE_HOURS =24;

}
