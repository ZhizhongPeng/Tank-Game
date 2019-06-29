package Members;

import util.ImageUtil;

public class Base extends Wall {
	/**
	 * Base constructor method
	 * 
	 * @param x
	 *              base x coordinate
	 * @param y
	 *              base y coordinate
	 */
	public Base(int x, int y) {
		super(x, y, ImageUtil.BASE_IMAGE_URL);// use father's constructor method
	}
}
