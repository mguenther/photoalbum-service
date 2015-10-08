package com.mgu.photoalbum.service;

import com.mgu.photoalbum.identity.IdGenerator;
import org.junit.Test;

import java.nio.file.Path;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Set of unit tests for {@link PathScheme}.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class PathSchemeTest {

    private static final IdGenerator idGenerator = new IdGenerator();

    private static final String PATH_TO_ARCHIVE = "/archive";

    private static final String OWNER_ID = idGenerator.generateId(8);

    private static final String ALBUM_ID = idGenerator.generateId("AL", 14);

    private static final String PHOTO_ID = idGenerator.generateId(14);

    private final PathScheme pathScheme = new PathScheme(PATH_TO_ARCHIVE); // object-under-test

    @Test
    public void pathToAlbumFolderShouldYieldCorrectPath() {
        final Path albumPath = pathScheme.constructPathToAlbumFolder(OWNER_ID, ALBUM_ID);
        assertThat(albumPath.getNameCount(), is(3));
        assertThat(albumPath.getName(0).toString(), is(PATH_TO_ARCHIVE.substring(1)));
        assertThat(albumPath.getName(1).toString(), is(OWNER_ID));
        assertThat(albumPath.getName(2).toString(), is(ALBUM_ID));
    }

    @Test
    public void pathToPhotoFolderShouldYieldCorrectPath() {
        final Path photoPath = pathScheme.constructPathToPhotoFolder(OWNER_ID, ALBUM_ID, PHOTO_ID);
        assertThat(photoPath.getNameCount(), is(5));
        assertThat(photoPath.getName(0).toString(), is(PATH_TO_ARCHIVE.substring(1)));
        assertThat(photoPath.getName(1).toString(), is(OWNER_ID));
        assertThat(photoPath.getName(2).toString(), is(ALBUM_ID));
        assertThat(photoPath.getName(3).toString(), is(String.valueOf(PHOTO_ID.charAt(0))));
        assertThat(photoPath.getName(4).toString(), is(String.valueOf(PHOTO_ID.charAt(1))));
    }

    @Test
    public void pathToOriginalShouldYieldCorrectPath() {
        final Path photoPath = pathScheme.constructPathToOriginal(OWNER_ID, ALBUM_ID, PHOTO_ID);
        assertThat(photoPath.getNameCount(), is(6));
        assertThat(photoPath.getName(0).toString(), is(PATH_TO_ARCHIVE.substring(1)));
        assertThat(photoPath.getName(1).toString(), is(OWNER_ID));
        assertThat(photoPath.getName(2).toString(), is(ALBUM_ID));
        assertThat(photoPath.getName(3).toString(), is(String.valueOf(PHOTO_ID.charAt(0))));
        assertThat(photoPath.getName(4).toString(), is(String.valueOf(PHOTO_ID.charAt(1))));
        assertThat(photoPath.getName(5).toString(), is(PHOTO_ID + "_" + PathScheme.SUFFIX_ORIGINAL_IMAGE + "." + PathScheme.EXTENSION));
    }

    @Test
    public void pathToThumbnailShouldYieldCorrectPath() {
        final Path photoPath = pathScheme.constructPathToThumbnail(OWNER_ID, ALBUM_ID, PHOTO_ID);
        assertThat(photoPath.getNameCount(), is(6));
        assertThat(photoPath.getName(0).toString(), is(PATH_TO_ARCHIVE.substring(1)));
        assertThat(photoPath.getName(1).toString(), is(OWNER_ID));
        assertThat(photoPath.getName(2).toString(), is(ALBUM_ID));
        assertThat(photoPath.getName(3).toString(), is(String.valueOf(PHOTO_ID.charAt(0))));
        assertThat(photoPath.getName(4).toString(), is(String.valueOf(PHOTO_ID.charAt(1))));
        assertThat(photoPath.getName(5).toString(), is(PHOTO_ID + "_" + PathScheme.SUFFIX_THUMBNAIL_IMAGE + "." + PathScheme.EXTENSION));
    }
}