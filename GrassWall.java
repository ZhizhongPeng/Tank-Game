package Members.wall;

import Members.Wall;
import util.ImageUtil;

/**
 * 草地
 * 
 * @author 子墨
 *
 */
public class GrassWall extends Wall {
	/**
	 * 草地构造方法
	 * 
	 * @param x
	 *              初始化横坐标
	 * @param y
	 *              初始化纵坐标
	 */
	public GrassWall(int x, int y) {
		super(x, y, ImageUtil.GRASSWALL_IMAGE_URL);// 调用父类构造方法，使用默认草地图片
	}
}
