package com.mgu.photoalbum.identity;

/**
 * IDs are generally generated at the server-side upon client request over the alphabet [A-Za-z0-9]. This
 * generator can be parameterized with an optional prefix and a length that the generated ID must adhere
 * to.
 *
 * Most of the properties of strong passwords should be applied to IDs as well. Some IDs - for instance user IDs -
 * should have a high level of entropy. With regard to strong passwords, NIST recommends an entropy of at least
 * 78 bits. If we apply this recommendation to an ID within the context of the given the alphabet [A-Za-z0-9], a minimum
 * length of 14 characters is advised. Thus, if the caller does not supply the length parameter, the default
 * length is exactly 14 characters.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class IdGenerator {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVW" + "ABCDEFGHIJKLMNOPQRSTUVW".toLowerCase() + "0123456789";

    private static final String WITHOUT_PREFIX = "";

    private static final int DEFAULT_LENGTH = 14;

    public String generateId() {
        return generateId(WITHOUT_PREFIX, DEFAULT_LENGTH);
    }

    public String generateId(final int length) {
        return generateId(WITHOUT_PREFIX, length);
    }

    public String generateId(final String prefix) {
        return generateId(prefix, DEFAULT_LENGTH);
    }

    /**
     * Generates an ID that adheres to the given parameters {@code prefix} and {@code length}. The prefix
     * segment is separated using a '-' from the randomized segment.
     *
     * Example: Given the prefix 'CU' and a target length of 5, the ID 'CU-AbjEk' would be in the language
     * that both parameters span in conjunction with the alphabet.
     *
     * @param prefix
     *      Optional prefix for the ID
     * @param length
     *      Length of the randomized segment
     * @return
     *      Generated ID with an optional prefix segment followed by a randomized segment, where both segments
     *      are delimited by a dash ('-')
     */
    public String generateId(final String prefix, final int length) {

        final StringBuilder sb = new StringBuilder();

        if (!prefix.equals(WITHOUT_PREFIX)) {
            sb.append(prefix).append("-");
        }

        for (int i = 0; i < length; i++) {
            final int randomIndex = (int) (Math.random() * ALPHABET.length());
            sb.append(ALPHABET.charAt(randomIndex));
        }

        return sb.toString();
    }
}