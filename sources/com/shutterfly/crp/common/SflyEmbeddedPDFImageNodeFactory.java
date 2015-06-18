package com.shutterfly.crp.common;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Element;

/** 
 * CRP: class added.
 * 
 * Produces a GraphicsNode for emitting an embedded PDF file (as in CLM text) to the output PDF.
 * (This is needed because fop depends on batik, so any coding against the fop transcoder
 * needs to be done in the CRP project to avoid the cyclical dependency.)
 */
public interface SflyEmbeddedPDFImageNodeFactory {
	/** 
	 * Creates a GraphicsNode for emitting an embedded PDF file (as in CLM text) to the output PDF.
	 */
	GraphicsNode createEmbeddedPDFImageNode(BridgeContext ctx, Element e, ParsedURL purl);
}
