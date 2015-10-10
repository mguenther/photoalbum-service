package com.mgu.photoalbum.adapter.fileio;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class PathAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PathAdapter.class);

    public byte[] readBytes(final Path path) {
        try {
            return convertToBytes(Files.newInputStream(path));
        } catch (IOException e) {
            throw new UnableToReadFromFilesystemException(path.toString(), e);
        }
    }

    /**
     * Reads in all bytes from the given {@link InputStream} and returns them as
     * byte array.
     *
     * @param in
     * 		Represents the {@link InputStream} from which to read
     * @throws IOException
     * 		In case reading from the given {@link InputStream} raises an
     *      {@link IOException}
     * @return
     * 		<code>byte[]</code> array containing all the bytes that were
     *      read from the given {@link InputStream}
     */
    private byte[] convertToBytes(final InputStream in) throws IOException {
        try {
            return IOUtils.toByteArray(in);
        } finally {
            try {
                in.close();
            } catch (IOException ignore) {
            }
        }
    }

    public String readString(final Path path) {
        try {
            return convertToString(Files.newInputStream(path));
        } catch (IOException e) {
            throw new UnableToReadFromFilesystemException(path.toString(), e);
        }
    }

    /**
     * Reads in the data from the given {@link InputStream} and returns it
     * as {@link String}.
     *
     * @param in
     * 		Represents the {@link InputStream} from which to read
     * @throws IOException
     * 		In case reading from the given {@link InputStream} raises an
     * 		{@link IOException}
     * @return
     * 		<code>String</code> which holds the data that was read from the
     *      given {@link InputStream}
     */
    private String convertToString(final InputStream in) throws IOException {
        try {
            return IOUtils.toString(in);
        } finally {
            try {
                in.close();
            } catch (IOException ignore) {
            }
        }
    }

    public boolean exists(final Path path) {
        return Files.exists(path);
    }

    public boolean delete(final File file) {
        return FileUtils.deleteQuietly(file);
    }

    public void deleteDirectory(final Path path) {
        try {
            FileUtils.deleteDirectory(path.toFile());
        } catch (IOException e) {
            throw new UnableToWriteToFilesystemException(path.toString(), e);
        }
    }

    public void createDirectory(final Path path) {
        try {
            FileUtils.forceMkdir(path.toFile());
        } catch (IOException e) {
            throw new UnableToWriteToFilesystemException(path.toString(), e);
        }
    }

    public void copy(final InputStream source, final Path destination) {
        try {
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UnableToCopyFileException(destination.toString(), e);
        }
    }

    public void copy(final Path source, final Path destination) {
        try {
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UnableToCopyFileException(source.toString(), destination.toString(), e);
        }
    }

    public void copyImage(final BufferedImage image, final Path destination) {
        final Path directory = destination.getParent();
        if (!exists(directory)) {
            createDirectory(directory);
            LOGGER.debug("Created directory '" + directory.toString() + "'.");
        }
        try {
            ImageIO.write(image, "jpg", destination.toFile());
            LOGGER.info("JPEG image has been written to '" + destination.toString() + "'.");
        } catch (IOException e) {
            throw new UnableToCopyFileException(destination.toString(), e);
        }
    }
}