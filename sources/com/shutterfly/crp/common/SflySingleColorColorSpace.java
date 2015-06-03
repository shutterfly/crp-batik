package com.shutterfly.crp.common;

import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.batik.bridge.SflyColor;
import org.apache.log4j.Logger;
import org.apache.xmlgraphics.java2d.color.DeviceCMYKColorSpace;

/**
 * CRP: class added.
 * 
 * The SflyColor derives from java.awt.Color which stores and returns the colorspace 
 * for the color. Typically this color space is capable of mapping several values two
 * and from cmyk. In shutterfly, we start in batik with the svg's initial rgb color and use
 * the global color's rgb_svgToCmykMap to determine the "hard-coded" cmyk value for the color.
 * If the rgb_svg value doesn't appear in mapping, we use the (Adobe default) ICC profiles
 * to determine the cmyk.
 * 
 * Thus when the SflyColor is instantiated, there's enough information to create a
 * single-color colorspace which just maps between the initial rgb color to/from
 * hard-coded or ICC color mapped cmyk values.
 * 
 * This class derives from org.apache.xmlgraphics.java2d.color.DeviceCMYKColorSpace 
 * because there is FOP code (PDFColor, PDFColorHandler) that uses "instanceof DeviceCMYKColorSpace"
 * to determine that the color is CMYK. It doesn't use any of the DeviceCMYKColorSpace code, 
 * but instead just uses the mapping descriped above. (Also, it does not implement 
 * org.apache.xmlgraphics.java2d.color.ColorSpaceOrigin.)
 */
public class SflySingleColorColorSpace extends DeviceCMYKColorSpace {
//	public class SflyCmykColorSpace extends ColorSpace { 
    private static final long serialVersionUID = 2925400926083528972L;
	private static Logger logger = Logger.getLogger(SflySingleColorColorSpace.class);

	private static String rgbProfile = "sRGB Color Space Profile.icm";
	private static String cmykProfile = "USWebCoatedSWOP.icc";
	private static Map<String, ICC_ColorSpace> colorSpaces = new HashMap<String, ICC_ColorSpace>();
	private static List<String> colorSpaceNames = Arrays.asList(new String[] { rgbProfile, cmykProfile });
	
	private static SFlyGlobalColorMapFactory sflyColorMapFactory;
	
	static {
		initProfiles();
	}
	
	private float[] initialRgbValues;  // 3 values, 0.0 <= value <= 1.0.
	private float[] cmykValues;  // 4 values, 0.0 <= value <= 1.0.
	
	private static void initProfiles() {
		if (colorSpaces.size() == 0) {
			for (String colorSpaceName : colorSpaceNames) {
				try {
					InputStream profileInputStream = SflyColor.class.getResourceAsStream("resources/"+colorSpaceName);
					ICC_ColorSpace colorSpace = new ICC_ColorSpace(ICC_Profile.getInstance(profileInputStream));
					colorSpaces.put(colorSpaceName, colorSpace);
				} catch (Exception ex) {
					logger.fatal("Could not initialize ICC profile needed for color conversion", ex);
					throw new RuntimeException("Could not initialize ICC profile needed for color conversion", ex);
				}
			}
		}
	}
    
	public static void initSflyColorMapFactory(SFlyGlobalColorMapFactory colorMapFactory) {
		sflyColorMapFactory = colorMapFactory;
	}
	
	
	public SflySingleColorColorSpace(float[] initialRgbValues) {
		if (initialRgbValues.length != 3) {
			throw new IllegalStateException("SflySingleColorColorSpace ctor expects 3 floats for rgb, Got: " + initialRgbValues.length);
		}
        this.initialRgbValues = initialRgbValues;
	}


    @Override
    public float[] toRGB(float[] colorvalue) {
    	// Ignoring input as per class description.
    	return this.initialRgbValues;
    }

    @Override
    public float[] fromRGB(float[] rgbvalue) {
    	// Ignoring input as per class description.
    	if (cmykValues == null) {
	    	// Use the lookup map first, then fall back to using the color profiles.
			SflyGlobalColor sflyGlobalColor = getSflyColorIfExists(initialRgbValues);
			if (sflyGlobalColor == null) {
				// Rgb color is not in mapping, convert using profiles.
				ICC_ColorSpace rgbColorSpace = colorSpaces.get(rgbProfile);
				ICC_ColorSpace cmykColorSpace = colorSpaces.get(cmykProfile);
				final float[] ciexyz = rgbColorSpace.toCIEXYZ(initialRgbValues);
				cmykValues = cmykColorSpace.fromCIEXYZ(ciexyz);
			} else {
				cmykValues = sflyGlobalColor.getCmyk();
			}
    	} 
        return cmykValues;
    }

    @Override
    public float[] toCIEXYZ(float[] colorvalue) {
        throw new UnsupportedOperationException("NYI");
    }

    @Override
    public float[] fromCIEXYZ(float[] colorvalue) {
        throw new UnsupportedOperationException("NYI");
    }
  
    private SflyGlobalColor getSflyColorIfExists(float[] rgbvalue) {
    	final int r = Math.round(rgbvalue[0] * 255f);
    	final int g = Math.round(rgbvalue[1] * 255f);
    	final int b = Math.round(rgbvalue[2] * 255f);
		String rgbHex = String.format("%02X%02X%02X", r, g, b);
		SflyGlobalColor sflyGlobalColor = sflyColorMapFactory.getRgb_svgToCmykMap().get(rgbHex);
		return sflyGlobalColor;
    }
}