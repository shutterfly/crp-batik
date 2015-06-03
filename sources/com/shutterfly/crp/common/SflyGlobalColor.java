package com.shutterfly.crp.common;

/**
 * CRP: class added.
 * Represents Shutterfly color mapping as found in Global Content System.
 * This is used for vectors only.
 */
public class SflyGlobalColor {
	public SflyGlobalColor() {

	}

	public SflyGlobalColor(SflyGlobalColor color) {
		this.colorId = color.colorId;
		this.displayName = color.displayName;
		this.rgb = color.rgb;
		this.rgb_svg = color.rgb_svg;
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

	public String getRgb_svg() {
		return rgb_svg;
	}

	public void setRgb_svg(String rgb_svg) {
		this.rgb_svg = rgb_svg;
	}
	
	/**
	 * Returns CMYK as four float values, where value is 0.0 <= value <= 1.0.
	 */
	public float[] getCmyk() { 
		return new float[] { c/100f, m/100f, y/100f, k/100f };
	}

	String colorId;
	String displayName;
	int c;
	int m;
	int y;
	int k;
	String rgb;
	String state;
	String rgb_svg;
}