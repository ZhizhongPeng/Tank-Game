package Members;

import java.awt.Rectangle;
import java.util.List;
import java.util.Random;

import enumtype.Direction;
import Members.BotTank;
import Members.Tank;
import util.ImageUtil;
import enumtype.TankType;
import frame.GamePanel;

public class BotTank extends Tank{

	private Random random = new Random();
	private Direction dir;// direction
	private int freshTime = GamePanel.FRESHTIME;// refresh time
	private int moveTimer = 0;// move timer

	private boolean pause=false;//pause status
	/**
	 * get the bot tank pause status
	 */
	public boolean isPause() {
		return pause;
	}
	/**
	 * set the bot tank pause status
	 */
	public void setPause(boolean pause) {
		this.pause = pause;
	}

	/**
	 * 
	 * bot tank constructor method
	 * 
	 * @param x
	 *             x coordinate
	 * @param y
	 *             y coordinate
	 * @param gamePanel
	 *             game panel
	 * @param type
	 *             tank type
	 */

	public BotTank(int x, int y, GamePanel gamePanel, TankType type) {
		super(x, y, ImageUtil.BOT_DOWN_IMAGE_URL, gamePanel, type);// 调用父类构造方法，使用默认机器人坦克图片
		dir = Direction.DOWN;// default direction is down
		setAttackCoolDownTime(1000);// set attack cool down time
		}

	/**
	 * move method
	 */
	public void go(){
		if(isAttackCoolDown()){// if cool time is passed, then attack
			attack();
		}
		if(moveTimer>random.nextInt(1000)+500){// if move timer is greater than 0.5~1.5s, get a random direction again
			dir=randomDirection();
			moveTimer=0;// clear the move timer
		}else{
			moveTimer+=freshTime;// move timer increases as fresh time increases
		}
		switch (dir) {// judge direction
		case UP:
			upWard();
			break;
		case DOWN:
			downWard();
			break;
		case LEFT:
			leftWard();
			break;
		case RIGHT:
			rightWard();
			break;
		}	
	}

	/**
	 * get random direction
	 * 
	 * @return
	 */
	private Direction randomDirection() {
		Direction [] dirs=Direction.values();// get all direction
		Direction oldDir=dir;// keep the previous direction
		Direction newDir=dirs[random.nextInt(4)];
		if(oldDir==newDir||newDir==Direction.UP) {// if the dir is the same as previous one or the dir is up, get a new random direction
			return dirs[random.nextInt(4)];
		}
		return newDir;
	}

	/**
	 * move to border
	 */
	protected void moveToBorder() {
		if (x < 0) {
			x = 0;
			dir = randomDirection();// get a new direction
		} else if (x > gamePanel.getWidth() - width) {
			x = gamePanel.getWidth() - width;
			dir = randomDirection();// get a new direction
		}
		if (y < 0) {
			y = 0;
			dir = randomDirection();
		} else if (y > gamePanel.getHeight() - height) {
			y = gamePanel.getHeight() - height;
			dir = randomDirection();
		}
	}

	/**
	 * hit tank
	 */
	@Override
	boolean hitTank(int x, int y) {
		Rectangle next = new Rectangle(x, y, width, height);// get the hit position
		List<Tank> tanks = gamePanel.getTanks();// get all the tank
		for (int i = 0, lengh = tanks.size(); i < lengh; i++) {// traverse all the tank
			Tank t = tanks.get(i);// get a tank
			if (!this.equals(t)) {// if not self
				if (t.isAlive() && t.hit(next)) {// if it is alive, then hit happens
					if (t instanceof BotTank) {// if it is bot tank
						dir = randomDirection();// get a new direction
					}
					return true;// hit happens
				}
			}
		}
		return false;// no hit
	}

	/**
	 * attack, override attack method
	 */
	@Override
	public void attack() {
		int rnum = random.nextInt(100);// random number between 0~99
		if (rnum < 4) { // %4 possibility attack happens
			super.attack();// use father's method
		}
	}
}


