package be.florens.eggtab.util;

/**
 * A simple class to work with HSV colors
 * Algorithms from: https://www.cs.rit.edu/~ncs/color/t_convert.html#RGB%20to%20HSV%20&%20HSV%20to%20RGB
 */
@SuppressWarnings("unused")
public class Color {
	private float hue;
	private float saturation;
	private float value;


	public Color(float hue, float saturation, float value) {
		this.hue = hue;
		this.saturation = saturation;
		this.value = value;
	}

	public static Color decode(String str) throws NumberFormatException {
		int i = Integer.decode(str);
		return fromRGB(i);
	}

	public int toRGB() {
		int r, g, b;
		r = g = b = 0;  // Will be returned if all else somehow fails

		if (saturation == 0) {
			// Grey
			r = g = b = Math.round(value * 255);
		} else {
			// Turn hue back to degrees
			float hue = (this.hue - (float)Math.floor(this.hue)) * 6;

			float f = hue - (float)Math.floor(hue);
			float p = value * (1.0f - saturation);
			float q = value * (1.0f - saturation * f);
			float t = value * (1.0f - (saturation * (1-f)));

			switch ((int) hue) {
				case 0:
					r = Math.round(value * 255);
					g = Math.round(t * 255);
					b = Math.round(p * 255);
					break;
				case 1:
					r = Math.round(q * 255);
					g = Math.round(value * 255);
					b = Math.round(p * 255);
					break;
				case 2:
					r = Math.round(p * 255);
					g = Math.round(value * 255);
					b = Math.round(t * 255);
					break;
				case 3:
					r = Math.round(p * 255);
					g = Math.round(q * 255);
					b = Math.round(value * 255);
					break;
				case 4:
					r = Math.round(t * 255);
					g = Math.round(p * 255);
					b = Math.round(value * 255);
					break;
				case 5:
					r = Math.round(value * 255);
					g = Math.round(p * 255);
					b = Math.round(q * 255);
			}
		}
		return 0xff000000 | (r << 16) | (g << 8) | (b);
	}

	public static Color fromRGB(int r, int g, int b) {
		float hue, saturation, value;  // Local

		int min = Math.min(r, Math.min(g, b)); // Biggest value
		int max = Math.max(r, Math.max(g, b)); // Smallest value
		float delta = max-min;

		value = max/255f; // Value is the highest value
		saturation = max != 0 ? delta/max : 0; // Calculate saturation

		if (saturation == 0) {
			hue = 0;
		} else {
			// Calculate the hue
			float redAmount = (max - r)/delta;
			float greenPart = (max - g)/delta;
			float bluePart = (max - b)/delta;

			if (r == max) {
				hue = bluePart - greenPart;
			} else if (g == max) {
				hue = 2 + redAmount - bluePart;
			} else {
				hue = 4 + greenPart - redAmount;
			}

			// Rescale hue down to between 0-1, add a full cycle if negative
			hue /= 6;
			if (hue < 0) {
				hue += 1;
			}
		}

		return new Color(hue, saturation, value);
	}

	public static Color fromRGB(int rgb) {
		return fromRGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
	}

	/**
	 * Cycle the hue
	 * @param step amount to increment/decrement
	 */
	public void cycleHue(float step) {
		hue += step;
	}
}
