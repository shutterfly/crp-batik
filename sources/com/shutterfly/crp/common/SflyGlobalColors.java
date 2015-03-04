package com.shutterfly.crp.common;

import java.util.List;
/**
 * CRP: class added.
 * Container for SflyColors.
 *
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

		List<SflyColor> color;

		public List<SflyColor> getColor() {
			return color;
		}

		public void setColor(List<SflyColor> color) {
			this.color = color;
		}

	}

}
