package com.mgu.photoalbum.webapp.resource;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LinkSchemeTest {

    private final LinkScheme linkScheme = new LinkScheme(); // object-under-test

    @Test
    public void toGalleryShouldYieldCorrectUrl() {
        assertThat(linkScheme.toGallery().toString(), is("/albums"));
    }

    @Test
    public void toAlbumShouldYieldUrlWithCorrectlySubstitutedPathParameters() {
        assertThat(linkScheme.toAlbum("Aa3b").toString(), is("/albums/Aa3b"));
    }

    @Test
    public void toPhotoShouldYieldUrlWithCorrectlySubstitutedPathParameters() {
        assertThat(linkScheme.toPhoto("Aa3b", "Ph0t0").toString(), is("/albums/Aa3b/Ph0t0"));
    }

    @Test
    public void toThumbnailShouldYieldUrlWithCorrectlySubstitutedPathParameters() {
        assertThat(linkScheme.toThumbnail("Aa3b", "Ph0t0").toString(), is("/albums/Aa3b/Ph0t0/thumbnail"));
    }

    @Test
    public void toDownloadShouldYieldUrlWithCorrectlySubstitutedPathAndQueryParameters() {
        assertThat(linkScheme.toDownload("Aa3b", "Ph0t0").toString(), is("/albums/Aa3b/Ph0t0?download=true"));
    }
}