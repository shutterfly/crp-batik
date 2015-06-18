package com.shutterfly.crp.common;


/** 
 * CRP: class added.
 * A home for singletons used by batik for CRP. 
 */
public class SflyBatikSingletons {

	private static SFlyGlobalColorMapFactory globalColorMapFactory = null;
	private static SflyEmbeddedPDFImageNodeFactory embeddedPDFImageNodeFactory = null;
	
	/**
	 * Initializes the "global data" color map factory for producing svg_rgb to cmyk mapping. 
	 * All but the intial invocation is a nop, however the factory should produce a 
	 * "fresh" mapping when invoked. 
	 */
	public static void initSflyGlobalColorMapFactory(SFlyGlobalColorMapFactory factory) {
		if (globalColorMapFactory == null) {
			globalColorMapFactory = factory;
		}
	}
	
	public static SFlyGlobalColorMapFactory getSFlyGlobalColorMapFactory() {
		return globalColorMapFactory;
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
