package Members;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * DisplayableImage Class
 *
 */
public abstract class DisplayableImage {
	/**
	 * image x coordinate
	 */
	public int x;
	/**
	 * image y coordinate
	 */
	public int y;
	/**
	 * image's width
	 */
	int width;
	/**
	 * image's height
	 */
	int height;
	/**
	 * image's object
	 */
	BufferedImage image;

	/**
	 * constructor method
	 * @param x  x coordinate
	 * @param y  y coordinate
	 * @param width  
	 * @param height 
	 */
	public DisplayableImage(int x, int y, int width, int height) {
		this.x = x;// x coordinate
		this.y = y;// y coordinate
		this.width = width;// width
		this.height = height;// height
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);// create the image
	}

	/**
	 * constructor method
	 * @param x  x coordinate
	 * @param y  y coordinate
	 * @param url  image url
	 */
	public DisplayableImage(int x, int y, String url) {
		this.x = x;
		this.y = y;
		try {
			image = ImageIO.read(new File(url));// retrieve the image from specific url
			this.width = image.getWidth();
			this.height = image.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DisplayableImage() {
		
	}
	
	/**
	 * retrieve image
	 * @return image
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * set image
	 * @param image   show image
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	/**
	 * set image
	 * @param image   show image
	 */
	public void setImage(String url) {
		try {
			this.image = ImageIO.read(new File(url));// retrieve the image from specific url
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * judge whether there is a hit
	 * @param v   image
	 * @return if two rectangle intersect，return true，else return false
	 */
	public boolean hit(DisplayableImage v) {
		return hit(v.getRect());// 执行重载方法
	}

	/**
	 * judge whether there is a hit
	 * @param r   rectangle object
	 * @return if two rectangle intersect，return true，else return false
	 */
	public boolean hit(Rectangle r) {
		if (r == null) {// if no rectangle 
			return false;// return false
		}
		return getRect().intersects(r);// return if two rectangle intersect，return true，else return false
	}

	/**
	 * return a rectangle object
	 */
	public Rectangle getRect() {
		//return a rectangle object with (width,height) in the the position (x,y)
		return new Rectangle(x, y, width, height);
	}

	/**
	 * return the width of image
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * set the width of image
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * return the height of image
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * set the height of image
	 */
	public void setHeight(int height) {
		this.height = height;
	}

}
