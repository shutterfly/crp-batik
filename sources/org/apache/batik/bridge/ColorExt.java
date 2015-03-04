package org.apache.batik.bridge;

import java.awt.Color;
import java.awt.color.ColorSpace;

import com.shutterfly.crp.common.SflyColor;
/**
 * CRP: class added.
 * Java2d Color's extension that embed SflyColor information(Sport color name, cmyk values).
 * This will be used in place of 'java.awt.Color' for vectors in Batik-scope.
 * while the embedded information will be utilized right in Itext-scope.
 */
public class ColorExt extends Color {

	private static final long serialVersionUID = 3089903585555141146L;
	private final SflyColor sflyColor;

	public ColorExt(SflyColor sflyColor, int rgb) {
		super(rgb);
		this.sflyColor = sflyColor;
	}

	public ColorExt(SflyColor sflyColor, int rgba, boolean hasAlpha) {
		super(rgba, hasAlpha);
		this.sflyColor = sflyColor;
	}

	public ColorExt(SflyColor sflyColor, int r, int g, int b) {
		super(r, g, b);
		this.sflyColor = sflyColor;
	}

	public ColorExt(SflyColor sflyColor, float r, float g, float b) {
		super(r, g, b);
		this.sflyColor = sflyColor;
	}

	public ColorExt(SflyColor sflyColor, ColorSpace cspace, float[] components, float alpha) {
		super(cspace, components, alpha);
		this.sflyColor = sflyColor;
	}

	public ColorExt(SflyColor sflyColor, int r, int g, int b, int a) {
		super(r, g, b, a);
		this.sflyColor = sflyColor;
	}

	public ColorExt(SflyColor sflyColor, float r, float g, float b, float a) {
		super(r, g, b, a);
		this.sflyColor = sflyColor;
	}

	public SflyColor getSflyColor() {
		return sflyColor;
	}

}
