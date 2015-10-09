package com.mgu.photoalbum.resource;

import java.util.HashMap;
import java.util.Map;

public class LinkScheme {

    private static final String DEFAULT_SCHEME = "http";

    private final String scheme;

    private final String hostname;

    private boolean usePort = false;

    private int port;

    public LinkScheme(final String hostname) {
        this(DEFAULT_SCHEME, hostname);
    }

    public LinkScheme(final String scheme, final String hostname) {

        if (hostname.contains(":")) {
            final String[] hostTokens = hostname.split(":");
            if (hostTokens.length > 2) {
                throw new IllegalArgumentException("Hostname can contain only one port delimiter.");
            }
            this.usePort = true;
            this.hostname = hostTokens[0];
            this.port = Integer.valueOf(hostTokens[1]);
        } else {
            this.hostname = hostname;
        }

        this.scheme = scheme;
    }

    protected String useTemplate(final String[] template) {
        final int index = this.usePort ? 1 : 0;
        return template[index];
    }

    protected Map<String, Object> staticPlaceholders() {
        final Map<String, Object> staticPlaceholders = new HashMap<>();
        if (this.usePort) {
            staticPlaceholders.put("port", String.valueOf(this.port));
        }
        staticPlaceholders.put("scheme", this.scheme);
        staticPlaceholders.put("host", this.hostname);
        return staticPlaceholders;
    }
}