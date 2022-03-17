package com.example.imageConverter;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class CopyOfBufferedImage {

	
	public static BufferedImage getCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), null);
		}
}
