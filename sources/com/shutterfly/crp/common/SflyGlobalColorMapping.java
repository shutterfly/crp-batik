package com.shutterfly.crp.common;

/**
 * CRP: class added.
 * XML model class for global colors map. 
 * This class allows layouts to use SFLY global colorIds to express colors. For example: 
 *     <svg:rect stroke="id_hot-pink" stroke-width="2" ... />
 * and provides both the rbg and cmyk values for a given colorId. The values are obtained from global content.
 * 
 * [Note: at this time (11/04/15) the only known use case for colorIds in layouts is Photobook Frames.]
 * 
 * See also SflySvgColorMapping which is used for between svg rgb values (stroke="#E80652")
 * to cmyk for pdf output.
 */
public class SflyGlobalColorMapping {
	public SflyGlobalColorMapping() {

	}

	public SflyGlobalColorMapping(SflyGlobalColorMapping color) {
		this.colorId = color.colorId;
		this.displayName = color.displayName;
		this.rgb = color.rgb;
		this.c = color.c;
		this.m = color.m;
		this.y = color.y;
		this.k = color.k;
	}

	public String getColorId() {
		return colorId;
	}

	public void setColorId(String colorId) {
		this.colorId = colorId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	public String getRgb() {
		return rgb;
	}

	public void setRgb(String rgb) {
		this.rgb = rgb;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	/**
	 * Returns RGB as three float values, where value is 0.0 <= value <= 1.0.
	 */
	public float[] getRgbAsFloatArray() { 
		return rgbHexStringToFloatArray(rgb);
	}
	
	/**
	 * Returns CMYK as four float values, where value is 0.0 <= value <= 1.0.
	 */
	public float[] getCmykAsFloatArray() { 
		return new float[] { c/100f, m/100f, y/100f, k/100f };
	}
	
	/**
	 * Converts case insensitive 6 digit hex string to a three float array where each 
	 * float is from 0..1.
	 */
	public static float[] rgbHexStringToFloatArray(String rgbHex) {
		float[] rgb = new float[] { 
				Integer.valueOf(rgbHex.substring(0,2),16)/255.0f,
				Integer.valueOf(rgbHex.substring(2,4),16)/255.0f,
				Integer.valueOf(rgbHex.substring(4,6),16)/255.0f};
		return rgb;
	}
	

	String colorId;
	String displayName;
	int c;
	int m;
	int y;
	int k;
	String rgb;
	String state;
}

// @@@ FLUSH
//public class SflyGlobalColorMapping {
//	
//	/**
//	 * The colorId string is expected in the form "id_hot-poink", the rgb string is expected in form "2E3192",
//	 * and cmyk as a float array: {100, 100, 0, 0}.
//	 * Hint: use static methed SflySvgColorMappingInfo.cmykStringToFloatArray() to convert 
//	 * from string "100 100 0 0" to the float array form.
//	 */
//	public SflyGlobalColorMapping(String colorId, String rgb, float[] cmyk) {
//		if (colorId == null || colorId.isEmpty() || rgb == null || rgb.isEmpty() || cmyk == null || cmyk.length != 4) {
//			throw new RuntimeException("Attempting to create SflyColorMappingInfo with invalid colorId, rgb or cmyk.");
//		}
//		this.colorId = colorId;
//		this.rgb = rgb;
//		this.cmyk = cmyk;
//	}
//	
//	/**
//	 * Returns the rgb as hex values in form "id_hot-pink".
//	 */
//	public String getColorId() {
//		return colorId;
//	}
//
//	/**
//	 * Returns the rgb as hex values in form "2E3192".
//	 */
//	public String getRgb() {
//		return rgb;
//	}
//	
//	/**
//	 * Returns the cmyk as four float values, where value is 0.0 <= value <= 1.0.
//	 */
//	public float[] getCmyk() {
//		return cmyk;
//	}
//
//	private String colorId;
//	private String rgb;
//	private float[] cmyk;
//}