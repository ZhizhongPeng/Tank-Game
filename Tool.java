package Members;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Random;

import enumtype.ToolType;
import frame.GamePanel;
import util.ImageUtil;
/**
 * 
 * tool class
 *
 */
public class Tool extends DisplayableImage{
	
	private static String[] imgURL= {
			ImageUtil.ADD_TANK_URL,
			ImageUtil.BOMB_URL,
			ImageUtil.SPADE_URL,
			ImageUtil.TIMER_URL,
			ImageUtil.STAR_URL,
			ImageUtil.GUN_URL
	};
	
	private static Image [] toolImgs= {
			Toolkit.getDefaultToolkit().createImage(imgURL[0]),
			Toolkit.getDefaultToolkit().createImage(imgURL[1]),
			Toolkit.getDefaultToolkit().createImage(imgURL[2]),
			Toolkit.getDefaultToolkit().createImage(imgURL[3]),
			Toolkit.getDefaultToolkit().createImage(imgURL[4]),
			Toolkit.getDefaultToolkit().createImage(imgURL[5])
	};
	
	private int timer=0;
	private int aliveTime=4500;//tool alive time
	private  Random r=new Random();//produce tool randomly
	private static  int height=20,width=20;
	ToolType type;
	private boolean alive=true;//tool status̬
	
	/**
	 * tool instance
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return tool object
	 */
	public static Tool getToolInstance(int x,int y) {
		return new Tool(x,y);
	}
	private Tool(int x,int y) {
		super(x,y,width,height);
		type=ToolType.values()[r.nextInt(6)];
	}
	
	public void changeToolType() {
		type=ToolType.values()[r.nextInt(6)];
		x=r.nextInt(550);
		y=r.nextInt(500);
		this.alive=true;
	}
	
	/**
	 * draw tool
	 * @param g return graphics
	 */
	public void draw(Graphics g) {
		if(timer>aliveTime) {
			timer=0;
			setAlive(false);
			//changeToolType();
		}else {
			g.drawImage(toolImgs[type.ordinal()], x, y, null);
			timer+=GamePanel.FRESHTIME;
		}
	}
	/**
	 * set tool status̬
	 * @param return status̬
	 */
	public void setAlive(boolean alive) {
		this.alive=alive;
		timer=0;
	}
	/**
	 *̬
	 * @return status
	 */
	public boolean getAlive() {
		return this.alive;
	}
}
