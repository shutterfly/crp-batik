package com.shutterfly.crp.common;

import java.awt.Color;

/**
 * CRP: class added.
 * Java2d Color's extension for Shutterfly that supports both rgb and cmyk using Shutterfly's "hard-coded"
 * mappings.
 */
public class SflyColor extends Color {
	private static final long serialVersionUID = 3089903585555141146L;

	/**
	 * Used when caller already knows the mapping. See getSvgColorMappingIfExists below.
	 */
	public SflyColor(float[] rgbValues, float[] cmykValues, float alpha) {  
		super(new SflySingleColorCmykColorSpace(rgbValues, cmykValues), cmykValues, alpha);
	}
	
	/**
	 * Returns the rgb to cmyk mapping if it exists in the (SFLY) svg color map or null if not.
	 * Note that the use of the term "SvgColorMapping" means that the mapping comes in the svg
	 * file in the <crp:colorMapping> element.
	 */
	public static SflySvgColorMapping getSvgColorMappingIfExists(float[] rgbvalue) {
		String rgbHex = getRrbHexString(rgbvalue);
		final SflyColorMapFactory colorMapFactory = SflyBatikSingletons.getSFlyColorMapFactory();
		SflySvgColorMapping sflyColorMappingInfo = colorMapFactory.getRgbToCmykSvgColorMap().get(rgbHex);
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
