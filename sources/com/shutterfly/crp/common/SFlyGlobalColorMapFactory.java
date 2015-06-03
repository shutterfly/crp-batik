package com.shutterfly.crp.common;

import java.util.Map;

/** 
 * CRP: class added.
 * Produces the global color maps (Map<String, SflyColor>) for SFLY colors.
 */
public interface SFlyGlobalColorMapFactory {
	/** 
	 * Returns the SFLY global color mapping from rgb_svg to cmyk. The return value should be up-to-date after
	 * the mapping in SFLY global content changes.
	 */
	Map<String, SflyGlobalColor> getRgb_svgToCmykMap();
}
