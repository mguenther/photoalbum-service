package com.mgu.photoalbum.service.scaler;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ScalrImageScaler implements  ImageScaler {

    @Override
    public BufferedImage scale(final byte[] unscaledImage, final Dimension targetDimension) {
        BufferedImage unscaledBufferedImage = null;
        try {
            final InputStream in = new ByteArrayInputStream(unscaledImage);
            unscaledBufferedImage = ImageIO.read(in);
        } catch (IOException e) {
            throw new UnableToScaleImageException(e);
        }
        if (unscaledBufferedImage == null) {
            throw new UnableToScaleImageException();
        }
        return scale(unscaledBufferedImage, targetDimension);
    }

    @Override
    public BufferedImage scale(final BufferedImage unscaledImage, final Dimension targetDimension) {
        return Scalr.resize(unscaledImage, targetDimension.width);
    }
}