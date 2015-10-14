package com.shutterfly.crp.common;

/**
 * CRP: class added.
 * Encapsulates a Shutterfly color mapping used to emit CMYK values to PDF.
 * This is used for vectors only. Note that the RGB value will not necessarily match
 * those in the SFLY Global Colors mapping:
 * -	SflyColorInfo rgb: 		The rgb value mapped from the original cmyk when converting AI to SVG.
 * -	Global color rgb:		This value is used for tweaking the UI experience so that browser
 * 								colors appear the same as the printed cmyk.
 * CRP doesn't use global content for mapping colors. Instead it uses <crp:colorMapping> from tne
 * SVG input.
 */
public class SflyColorMappingInfo {
	
	/**
	 * The rgb string is expected in form "2E3192" and cmyk as a float array: {100, 100, 0, 0}.
	 * Hint: use static methed cmykStringToFloatArray() to convert from string "100 100 0 0" to 
	 * the float array form.
	 */
	public SflyColorMappingInfo(String rgb, float[] cmyk) {
		if (rgb == null || rgb.isEmpty() || cmyk == null || cmyk.length != 4) {
			throw new RuntimeException("Attempting to create SflyColorMappingInfo with invalid rgb or cmyk.");
		}
		this.rgb = rgb;
		this.cmyk = cmyk;
	}
	
	/**
	 * Returns the rgb as hex values in form "2E3192".
	 */
	public String getRgb() {
		return rgb;
	}
	
	/**
	 * Returns the cmyk as four float values, where value is 0.0 <= value <= 1.0.
	 */
	public float[] getCmyk() {
		return cmyk;
	}

	/**
	 * Converts from string format "100 100 0 0" to float array {100, 100, 0, 0}
	 */
	public static float[] cmykStringToFloatArray(String cmykString) {
		try {
			if (cmykString == null) {
				return null;
			}
			String[] values = cmykString.split(" ");
			if (values.length != 4) {
				throw new RuntimeException("Must have 4 values");
			}
			float[] cmykFloats = new float[] { 
					Float.valueOf(values[0]), Float.valueOf(values[1]), Float.valueOf(values[2]), Float.valueOf(values[3])
			};
			return cmykFloats;
		} catch (Exception ex){
			throw new RuntimeException("Unable to convert cmyk string to float array: " + cmykString, ex);
		}
	}

	private String rgb;
	private float[] cmyk;
}