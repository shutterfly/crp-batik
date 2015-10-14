package com.shutterfly.crp.common;

import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.xmlgraphics.java2d.color.DeviceCMYKColorSpace;

/**
 * CRP: class added.
 * 
 * The SflyColor derives from java.awt.Color which stores and returns the colorspace 
 * for the color. Typically this color space is capable of mapping several values two
 * and from cmyk. In shutterfly, we start in batik with the svg's initial rgb color and use
 * the SVG's <crp:colorMapping> (rgb_svgToCmykMap) to determine the "hard-coded" cmyk value for the color.
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
    private static final long serialVersionUID = 2925400926083528972L;
	private static Logger logger = Logger.getLogger(SflySingleColorColorSpace.class);

	private static String rgbProfile = "sRGB Color Space Profile.icm";
	private static String cmykProfile = "USWebCoatedSWOP.icc";
	private static Map<String, ICC_ColorSpace> colorSpaces = new HashMap<String, ICC_ColorSpace>();
	private static List<String> colorSpaceNames = Arrays.asList(new String[] { rgbProfile, cmykProfile });
		
	static {
		initProfiles();
	}
	
	private float[] initialRgbValues;  // 3 values, 0.0 <= value <= 1.0.
	private float[] cmykValues;  // 4 values, 0.0 <= value <= 1.0.
	
	private static void initProfiles() {
		if (colorSpaces.size() == 0) {
			for (String colorSpaceName : colorSpaceNames) {
				try {
					InputStream profileInputStream = SflySingleColorColorSpace.class.getResourceAsStream("resources/"+colorSpaceName);
					ICC_ColorSpace colorSpace = new ICC_ColorSpace(ICC_Profile.getInstance(profileInputStream));
					colorSpaces.put(colorSpaceName, colorSpace);
				} catch (Exception ex) {
					logger.fatal("Could not initialize ICC profile needed for color conversion", ex);
					throw new RuntimeException("Could not initialize ICC profile needed for color conversion", ex);
				}
			}
		}
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
    	// Ignoring input argument as per class description.
    	if (cmykValues == null) {
	    	// Use the lookup map first, then fall back to using the color profiles.
			SflyColorMappingInfo sflyColorMappingInfo = getSflyColorIfExists(initialRgbValues);
			if (sflyColorMappingInfo == null) {
				
				// TODO TODO: So CRP wants to allow SVG from any source and that SVG may not have a color mapping.
				// We really don't want the ERROR log below in this case, so we should probably give it
				// only when the mapping exists in the SVG (when SflyBatikSingletons.getSFlyColorMapFactory() != null)
				// (but the given rgb value cannot be found.)
				// In the cases where the mapping doesn't exist at all in the SVG, do we want to
				// (a) use the GLOBAL color mapping and THEN fall back to colorspace mapping, 
				// or (b) not emit CMYK at all? Approach (a) is complex, because now we need to manage global color
				// synchronization (and fall back on unreliable color space mapping.)
				// We can implement (b) simply by turning off the CMYK output to PDF.
				// (Well, we'd need to figure out how to do that.) If we choose (b) then get rid of all
				// color space mapping code in this file.
				// TODO TODO.
				
				// This log message is very important. It flags any colors that we don't have in the mapping.
				// These colors may be converted to incorrect CMYK values! 
				String message = "IMPORTANT: cannot find rbg value in mapping: " + getRrbHexString(initialRgbValues) +
						" (This color may be mapped to an incorrect CMYK value) Add this color to the mapping!!!";
				logger.error(message);
								
				// Rgb color is not in mapping, convert using profiles.
				ICC_ColorSpace rgbColorSpace = colorSpaces.get(rgbProfile);
				ICC_ColorSpace cmykColorSpace = colorSpaces.get(cmykProfile);
				final float[] ciexyz = rgbColorSpace.toCIEXYZ(initialRgbValues);
				cmykValues = cmykColorSpace.fromCIEXYZ(ciexyz);
			} else {
				cmykValues = sflyColorMappingInfo.getCmyk();
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
  
    private SflyColorMappingInfo getSflyColorIfExists(float[] rgbvalue) {
    	String rgbHex = getRrbHexString(rgbvalue);
		final SflyColorMapFactory colorMapFactory = SflyBatikSingletons.getSFlyColorMapFactory();
		
		// CRP allows SVG from any source and that SVG may not have the color mapping info. Return null in this case.
		if (colorMapFactory == null) {
			return null;
		}
		SflyColorMappingInfo sflyColorMappingInfo = colorMapFactory.getRgbToCmykMap().get(rgbHex);
		return sflyColorMappingInfo;
    }
    
    private static String getRrbHexString(float[] rgbvalue) {
    	final int r = Math.round(rgbvalue[0] * 255f);
    	final int g = Math.round(rgbvalue[1] * 255f);
    	final int b = Math.round(rgbvalue[2] * 255f);
		final String rgbHex = String.format("%02X%02X%02X", r, g, b);
		return rgbHex;
    }
  
}