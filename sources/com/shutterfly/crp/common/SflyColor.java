package com.shutterfly.crp.common;

import java.awt.Color;

/**
 * CRP: class added.
 * Java2d Color's extension that embed SflyColor information(Sport color name, cmyk values).
 * This will be used in place of 'java.awt.Color' for vectors in Batik-scope.
 * while the embedded information will be utilized right in Itext-scope.
 */
public class SflyColor extends Color {
	
	private static final long serialVersionUID = 3089903585555141146L;

	public SflyColor(float[] rgbValue, float alpha) {  
		// Be nicer not to instantiate two colorspaces here, but the base class doesn't support it with existing ctors.
		super(new SflySingleColorColorSpace(rgbValue), new SflySingleColorColorSpace(rgbValue).fromRGB(null), alpha);
	}
}
