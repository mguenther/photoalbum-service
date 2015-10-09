package com.mgu.photoalbum.resource;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class PhotoalbumLinkScheme extends LinkScheme {

    private static final String[] ALBUM_URI_TEMPLATE = new String[] {
            "{scheme}://{host}/albums/{albumId}",
            "{scheme}://{host}:{port}/albums/{albumId}"
    };

    public PhotoalbumLinkScheme(final String hostname) {
        super(hostname);
    }

    public PhotoalbumLinkScheme(final String scheme, final String hostname) {
        super(scheme, hostname);
    }

    public URI toAlbum(final String albumId) {
        return UriBuilder
                .fromUri(useTemplate(ALBUM_URI_TEMPLATE))
                .resolveTemplates(staticPlaceholders())
                .resolveTemplate("albumId", albumId)
                .build();
    }
}