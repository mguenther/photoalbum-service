package com.mgu.photoalbum.security;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * This POJO represents simple HTTP Basic Auth credentials. Passwords in HTTP
 * Basic Auth cross our system boundary unhashed, so we hash them (cf.
 * {@link Credentials#create(String, String)}) using a SHA-1 digest before
 * we process authentication requests any further. Passwords stored within an
 * instance of <code>Credentials</code> are always encrypted.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class Credentials {

    private final String userId;

    private final String hashedPassword;

    private Credentials(final String userId, final String hashedPassword) {
        this.userId = userId;
        this.hashedPassword = hashedPassword;
    }

    public String getUserId() {
        return userId;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * Determines whether the given <code>otherHashedPassword</code> is
     * identical to <code>Credentials.hashedPassword</code>.
     *
     * @param otherHashedPassword
     *      hashed password that ought to be compared to the stored
     *      hashed password within an instance of this class
     * @return
     *      <code>true</code> if both passwords match, <code>false</code>
     *      otherwise
     */
    public boolean matches(final String otherHashedPassword) {
        return this.hashedPassword.equals(otherHashedPassword);
    }

    /**
     * Constructs an instance of {@link Credentials} based on the given parameters.
     * Passwords are not hashed when they hit the system, so we hash them using a
     * SHA-1 digest on the server-side before going any further.
     *
     * @param userId
     *      identifies a user within the CREAM system
     * @param cleartextPassword
     *      associated password in cleartext; will be hashed using SHA-1
     * @return
     *      instance of {@link Credentials} containing the user ID along with
     *      the hashed password
     */
    public static Credentials create(final String userId, final String cleartextPassword) {
        final String hashedPassword = DigestUtils.sha1Hex(cleartextPassword);
        return new Credentials(userId, hashedPassword);
    }
}