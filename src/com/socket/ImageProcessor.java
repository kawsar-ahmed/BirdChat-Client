package com.socket;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * 
 */

/**
 * @author Kawsar
 *
 */
public class ImageProcessor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
	
/*	public static BufferedImage getImageByCanonicalPath(String filePath) {
		String canonicalPath = ImageProcessor.class.getCanonicalName();
		if (filePath.)
		InputStream fileInputStream = ImageProcessor.class.getResourceAsStream(filePath);
		BufferedImage image = null;
		try {
			image = ImageIO.read(fileInputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    imageIcon = new ImageIcon(image);
	    
	}*/
	/**
	 * scale image
	 * 
	 * @param sbi BufferedImage to scale
	 * @param imageType type of image
	 * @param dWidth width of destination image
	 * @param dHeight height of destination image
	 * @param fWidth x-factor for transformation / scaling
	 * @param fHeight y-factor for transformation / scaling
	 * @return scaled image
	 */
	public static BufferedImage scale(BufferedImage sbi, int imageType, int dWidth, int dHeight, double fWidth, double fHeight) {
	    BufferedImage dbi = null;
	    if(sbi != null) {
	        dbi = new BufferedImage(dWidth, dHeight, imageType);
	        Graphics2D g = dbi.createGraphics();
	        AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
	        g.drawRenderedImage(sbi, at);
	    }
	    return dbi;
	}

	public static BufferedImage scaleBalanced(BufferedImage sbi, int imageType, int dWidth, int dHeight) {
		BufferedImage dbi = null;
		int height = sbi.getHeight();
		int width = sbi.getWidth();
		float factor = 0;
		if(dWidth > 0)
			factor = (float)dWidth/(float)width; 
		else 
			factor = (float)dHeight/(float)height;
		dWidth = (int)(width*factor);
		dHeight = (int)(height*factor);
		if(sbi != null) {
			dbi = new BufferedImage(dWidth, dHeight, imageType);
			Graphics2D g = dbi.createGraphics();
			AffineTransform at = AffineTransform.getScaleInstance(factor, factor);
			g.drawRenderedImage(sbi, at);
		}
		return dbi;
	}
}
