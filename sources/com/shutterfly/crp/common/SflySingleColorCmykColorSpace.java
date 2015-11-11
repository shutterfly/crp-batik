package com.shutterfly.crp.common;

import org.apache.xmlgraphics.java2d.color.DeviceCMYKColorSpace;

/**
 * CRP: class added.
 * 
 * The SflyColor derives from java.awt.Color which stores and returns the colorspace 
 * for the color. Typically this color space is capable of mapping several values to
 * and from cmyk. In Shutterfly, however, whenever we can obtain a cmyk value for a given
 * numeric (or colorId) rgb value, then we use this class to represent that single-color mapping. 
 * We can think of this as a "hard-coded" mapping, because both rgb and cmyk values are 
 * provided to the constructor, and thus ICC color profiles are not used to perform the mapping.
 * We do not use ICC color profiles for mapping because we have thus far been unable 
 * to achieve acceptable results obtaining (close enough to) the cmyk values specified
 * in Adobe Illustrator.
 * 
 * Note: This class derives from org.apache.xmlgraphics.java2d.color.DeviceCMYKColorSpace 
 * because there is FOP code (PDFColor, PDFColorHandler) that uses "instanceof DeviceCMYKColorSpace"
 * to determine that the color is CMYK. It doesn't use any of the DeviceCMYKColorSpace code, 
 * but instead just uses the mapping descriped above. (Also, it does not implement 
 * org.apache.xmlgraphics.java2d.color.ColorSpaceOrigin.)
 */
public class SflySingleColorCmykColorSpace extends DeviceCMYKColorSpace {
    private static final long serialVersionUID = 2925400926083528972L;
	
	private float[] rgbValues;  // 3 values, 0.0 <= value <= 1.0.
	private float[] cmykValues;  // 4 values, 0.0 <= value <= 1.0.
	
	/**
	 * Creates a single-color "hard-coded" mapping from rgb to cmyk.
	 */
	public SflySingleColorCmykColorSpace(float[] rgbValues, float[] cmykValues) {
		if (rgbValues.length != 3) {
			throw new IllegalStateException("SflySingleColorColorSpace ctor expects 3 floats for rgb, Got: " + rgbValues.length);
		}
		if (cmykValues.length != 4) {
			throw new IllegalStateException("SflySingleColorColorSpace ctor expects 4 floats for cmyk, Got: " + cmykValues.length);
		}
        this.rgbValues = rgbValues;
        this.cmykValues = cmykValues;
	}
	
    @Override
    public float[] toRGB(float[] colorvalue) {
    	// Ignoring input argument because this mapping is single-color and "hard-coded".
    	return this.rgbValues;
    }

    @Override
    public float[] fromRGB(float[] rgbvalue) {
    	// Ignoring input argument because this mapping is single-color and "hard-coded".
        return this.cmykValues;
    }

    @Override
    public float[] toCIEXYZ(float[] colorvalue) {
        throw new UnsupportedOperationException("NYI");
    }

    @Override
    public float[] fromCIEXYZ(float[] colorvalue) {
        throw new UnsupportedOperationException("NYI");
    }
  
    
// 	(11/04/15: Keep this around for a while in case we decide to start using color profiles for mapping.)
//	private static String rgbProfile = "sRGB Color Space Profile.icm";
//	private static String cmykProfile = "USWebCoatedSWOP.icc";
//	private static Map<String, ICC_ColorSpace> colorSpaces = new HashMap<String, ICC_ColorSpace>();
//	private static List<String> colorSpaceNames = Arrays.asList(new String[] { rgbProfile, cmykProfile });
//		
//	static {
//		initProfiles();
//	}
//	
//	private static void initProfiles() {
//		if (colorSpaces.size() == 0) {
//			for (String colorSpaceName : colorSpaceNames) {
//				try {
//					InputStream profileInputStream = SflySingleColorCmykColorSpace.class.getResourceAsStream("resources/"+colorSpaceName);
//					ICC_ColorSpace colorSpace = new ICC_ColorSpace(ICC_Profile.getInstance(profileInputStream));
//					colorSpaces.put(colorSpaceName, colorSpace);
//				} catch (Exception ex) {
//					logger.fatal("Could not initialize ICC profile needed for color conversion", ex);
//					throw new RuntimeException("Could not initialize ICC profile needed for color conversion", ex);
//				}
//			}
//		}
//	}
//
}