package com.mgu.photoalbum.service.scaler;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Consumes an unscaled image and yields a scaled image that is within
 * a given target {@link Dimension}. Implementing classes should be aware
 * that image conversion should retain the color model of the source image.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public interface ImageScaler {

    /**
     * Scales the binary image data provided by the {@code byte[]} array with respect
     * to the given {@link Dimension}. If the given target dimension is larger than the
     * size of the unscaled image, this method will perform an upscaling rather than
     * a downscaling of the unscaled image.
     *
     * @param unscaledImage
     *      Unscaled image in form of a {@code byte[]} array
     * @param targetDimension
     *      This is the target dimension the unscaled image will be scaled to
     * @return
     *      {@link BufferedImage} representing the scaled image
     */
    BufferedImage scale(byte[] unscaledImage, Dimension targetDimension);

    /**
     * Scales the given {@link BufferedImage}  with respect to the given {@link Dimension}.
     * If the given target dimension is larger than the size of the unscaled image,
     * this method will perform an upscaling rather than a downscaling of the unscaled
     * image.
     *
     * @param unscaledImage
     *      Unscaled image in form of a {@code BufferedImage}
     * @param targetDimension
     *      This is the target dimension the unscaled image will be scaled to
     * @return
     *      {@link BufferedImage} representing the scaled image
     */
    BufferedImage scale(BufferedImage unscaledImage, Dimension targetDimension);
}
