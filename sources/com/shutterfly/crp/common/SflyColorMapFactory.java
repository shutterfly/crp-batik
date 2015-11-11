package com.shutterfly.crp.common;

import java.util.concurrent.ConcurrentMap;

/** 
 * CRP: class added.
 * Produces the color mapping (Map<String, SflyColorMappingInfo>) for SFLY colors.
 */
public interface SflyColorMapFactory {
	/** 
	 * Returns the SVG color mapping used to convert from rgb values to cmyk for
	 * PDF output. The key is the rgb value in the form "FFFFFF".
	 * Note that the use of the term "SvgColorMap" means that the mappings come in the svg
	 * file in the <crp:colorMapping> element.
	 */
	ConcurrentMap<String, SflySvgColorMapping> getRgbToCmykSvgColorMap();
	
	/** 
	 * Returns the global color mapping used to convert from SFLY colorId to 
	 * rgb for raster output and cmyk for PDF output. The key is the colorId in 
	 * the form "id_hot-pink".
	 */
	ConcurrentMap<String, SflyGlobalColorMapping> getColorIdToGlobalColorMap();

}
