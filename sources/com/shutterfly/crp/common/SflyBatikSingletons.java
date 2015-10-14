package com.shutterfly.crp.common;

/** 
 * CRP: class added.
 * A home for singletons used by batik for CRP. 
 */
public class SflyBatikSingletons {

	private static SflyColorMapFactory colorMapFactory = null;
	private static SflyEmbeddedPDFImageNodeFactory embeddedPDFImageNodeFactory = null;
	
	/**
	 * Initializes the color map factory for producing rgb to cmyk mapping. 
	 * All but the intial invocation is a nop.
	 */
	public static void initSflyColorMapFactory(SflyColorMapFactory factory) {
		if (colorMapFactory == null) {
			colorMapFactory = factory;
		}
	}
	
	public static SflyColorMapFactory getSFlyColorMapFactory() {
		return colorMapFactory;
	}
	
	/**
	 * Initializes the factory that produces a GraphicsNode for emitting an embedded 
	 * PDF file (as in CLM text) to the output PDF.
	 * 
	 * All but the intial invocation is a nop.
	 */
	public static void initSflyEmbeddedPDFImageNodeFactory(SflyEmbeddedPDFImageNodeFactory factory) {
		if (embeddedPDFImageNodeFactory == null) {
			embeddedPDFImageNodeFactory = factory;
		}
	}
	
	public static SflyEmbeddedPDFImageNodeFactory getSflyEmbeddedPDFImageNodeFactory() {
		return embeddedPDFImageNodeFactory;
	}
}
