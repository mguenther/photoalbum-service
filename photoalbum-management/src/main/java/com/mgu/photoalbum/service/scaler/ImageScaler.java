package com.mgu.photoalbum.service.scaler;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface ImageScaler {

    BufferedImage scale(byte[] unscaledImage, Dimension targetDimension);

    BufferedImage scale(BufferedImage unscaledImage, Dimension targetDimension);
}
