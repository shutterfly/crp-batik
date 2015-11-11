package com.shutterfly.crp.common;

import java.util.List;

/**
 * XML model class: container for SflyGlobalColorMapping. 
 */
public class SflyGlobalColors {
	public SflyGlobalColors() {

	}

	GlobalContentColors colors;

	public GlobalContentColors getColors() {
		return colors;
	}

	public void setColors(GlobalContentColors colors) {
		this.colors = colors;
	}

	public static class GlobalContentColors {
		public GlobalContentColors() {
		}

		List<SflyGlobalColorMapping> colorMappings;

		public List<SflyGlobalColorMapping> getColor() { 
			return colorMappings;
		}

		public void setColor(List<SflyGlobalColorMapping> colorMappings) {
			this.colorMappings = colorMappings;
		}
	}
}