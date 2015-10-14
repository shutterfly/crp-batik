package com.shutterfly.crp.common;

import java.util.Map;

/** 
 * CRP: class added.
 * Produces the color mapping (Map<String, SflyColorMappingInfo>) for SFLY colors.
 */
public interface SflyColorMapFactory {
	/** 
	 * Returns the SFLY color mapping used to convert from rgb to cmyk. The key
	 * is the rgb value in the form "FFFFFF".
	 */
	Map<String, SflyColorMappingInfo> getRgbToCmykMap();
}
