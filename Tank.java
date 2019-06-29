package Members;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import enumtype.Direction;
import enumtype.TankType;
import frame.GamePanel;
import Members.BotTank;
import Members.Bullet;
import Members.DisplayableImage;
import Members.Tank;
import Members.wall.GrassWall;
//import Members.Tool;
//import Members.Tank.AttackCD;
//import Members.wall.GrassWall;
//import Members.wall.IronWall;
//import Members.wall.Wall;
import util.AudioPlayer;
import util.AudioUtil;
import util.ImageUtil;



public class Tank extends DisplayableImage {
	GamePanel gamePanel;// game panel
	Direction direction;// move direction
	protected boolean alive = true;// tank status
	protected int speed = 3;// ÒÆ¶¯ËÙ¶È
	private boolean attackCoolDown = true;// attack cool down status
	private int attackCoolDownTime = 500;// attack cool down time, ms
	TankType type;// tank type
	private String upImage;// tank move up image
	private String downImage;// tank move down image
	private String rightImage;// tank move right image
	private String leftImage;// tank move left image
	
	private int life=1;//number of lives
	private int starNum=0;//star tool number, increase 1 means speeding up attack speed, up to three, hit through the iron wall
	
	
	/**
	 * tank constructor method
	 * 
	 * @param x
	 *              initial x coordinate
	 * @param y
	 *              initial y coordinate
	 * @param url
	 *              image url
	 * @param gamePanel
	 *              gamePanel
	 * @param type
	 *              tank type
	 */
	public Tank(int x, int y, String url, GamePanel gamePanel, TankType type) {
		super(x, y, url);
		this.gamePanel = gamePanel;
		this.type = type;
		direction = Direction.UP;// initial direction: up
		switch (type) {// tank type
		case PLAYER_1:// player 1
			upImage = ImageUtil.PLAYER1_UP_IMAGE_URL;
			downImage = ImageUtil.PLAYER1_DOWN_IMAGE_URL;
			rightImage = ImageUtil.PLAYER1_RIGHT_IMAGE_URL;
			leftImage = ImageUtil.PLAYER1_LEFT_IMAGE_URL;
			break;
		case PLAYER_2:// player 2
			upImage = ImageUtil.PLAYER2_UP_IMAGE_URL;
			downImage = ImageUtil.PLAYER2_DOWN_IMAGE_URL;
			rightImage = ImageUtil.PLAYER2_RIGHT_IMAGE_URL;
			leftImage = ImageUtil.PLAYER2_LEFT_IMAGE_URL;
			break;
		case BOTTANK:// bot tank
			upImage = ImageUtil.BOT_UP_IMAGE_URL;
			downImage = ImageUtil.BOT_DOWN_IMAGE_URL;
			rightImage = ImageUtil.BOT_RIGHT_IMAGE_URL;
			leftImage = ImageUtil.BOT_LEFT_IMAGE_URL;
			break;
		}
	}
	
	@Override
	public Rectangle getRect() {
		return new Rectangle(x, y, width-3, height-3);
	}
	/**
	 * move left
	 */
	public void leftWard() {
		if (direction != Direction.LEFT) {// if previous direction is not left
			setImage(leftImage);// change to left direction image
		}
		direction = Direction.LEFT;// direction becomes left
		if (!hitWall(x - speed, y) && !hitTank(x - speed, y)) {// if not hit wall or other tanks
			x -= speed;// move left
			moveToBorder();// border judge
		}
	}

	/**
	 * move right
	 */
	public void rightWard() {
		if (direction != Direction.RIGHT) {// if previous direction is not right
			setImage(rightImage);// change to right direction image
		}
		direction = Direction.RIGHT;// irection becomes right
		if (!hitWall(x + speed, y) && !hitTank(x + speed, y)) {// if not hit wall or other tanks
			x += speed;// move right
			moveToBorder();// border judge
		}
	}

	/**
	 * move up
	 */
	public void upWard() {
		if (direction != Direction.UP) {
			setImage(upImage);
		}
		direction = Direction.UP;
		if (!hitWall(x, y - speed) && !hitTank(x, y - speed)) {
			y -= speed;
			moveToBorder();
		}
	}

	/**
	 * move down 
	 */
	public void downWard() {
		if (direction != Direction.DOWN) {
			setImage(downImage);
		}
		direction = Direction.DOWN;
		if (!hitWall(x, y + speed) && !hitTank(x, y + speed)) {
			y += speed;
			moveToBorder();
		}
	}

	/**
	 * hit wall
	 * 
	 * @param x
	 *              tank x coordinate
	 * @param y
	 *              tank y coordinate
	 * @return hit wall, return true
	 */
	private boolean hitWall(int x, int y) {
		Rectangle next = new Rectangle(x, y, width-3, height-3);// create the target area of tank movement
		List<Wall> walls = gamePanel.getWalls();// get all the walls
		for (int i = 0, lengh = walls.size(); i < lengh; i++) {// traverse all the walls
			Wall w = walls.get(i);// get wall object
			if (w instanceof GrassWall) {// if wall is grass or river
				continue;
			} else if (w.hit(next)) {// if wall is others
				return true;
			}
		}
		return false;
	}

	/**
	 * hit other tanks
	 * 
	 * @param x
	 *              tank x coordinate
	 * @param y
	 *              tank y coordinate
	 * @return hit other tanks, return true
	 */
	 boolean hitTank(int x, int y) {
		Rectangle next = new Rectangle(x, y, width, height);// create the target area of tank movement
		List<Tank> tanks = gamePanel.getTanks();// get all the tanks
		for (int i = 0, lengh = tanks.size(); i < lengh; i++) {// traverse all the tanks
			Tank t = tanks.get(i);// get tank object
			if (!this.equals(t)) {// if not self
				if (t.isAlive() && t.hit(next)) {// if the tank is alive and hit happens
					return true;
				}
			}
		}
		return false;
	}
	

	/**
	 * move to border, if out of the border, keep in the border
	 */
	protected void moveToBorder() {
		if (x < 0) {
			x = 0;
		} else if (x > gamePanel.getWidth() - width) {
			x = gamePanel.getWidth() - width;
		}
		if (y < 0) {
			y = 0;
		} else if (y > gamePanel.getHeight() - height) {
			y = gamePanel.getHeight() - height;
		}
	}

	/**
	 * get the head point of tank
	 * 
	 * @return head point
	 */
	private Point getHeadPoint() {
		Point p = new Point();// create point object
		switch (direction) {// judge direction
		case UP:
			p.x = x + width / 2;
			p.y = y;
			break;
		case DOWN:
			p.x = x + width / 2;
			p.y = y + height;
			break;
		case RIGHT:
			p.x = x + width;
			p.y = y + height / 2;
			break;
		case LEFT:
			p.x = x;
			p.y = y + height / 2;
			break;
		default:
			p = null;
		}
		return p;// return head point
	}

	/**
	 * attack
	 */
	public void attack() {
		if (attackCoolDown) {// if attack cool down status is true
			Point p = getHeadPoint();// get head point of tank
			Bullet b = new Bullet(p.x - Bullet.LENGTH / 2, p.y - Bullet.LENGTH / 2, direction, gamePanel, type);// new a bullet in head position
			gamePanel.addBullet(b);// add bullet in game panel
			AudioPlayer fire=new AudioPlayer(AudioUtil.FIRE);
			fire.new AudioThread().start();
			new AttackCD().start();// attack cool town begins
		}
	}

	/**
	 * tank alive status
	 * 
	 * @return true if tank is alive
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * set alive status
	 * 
	 * @param alive
	 *             
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	/**
	 * set moving speed
	 * 
	 * @param speed
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * attack cool down thread
	 */
	private class AttackCD extends Thread {
		public void run() {
			attackCoolDown = false;// set attack cool down status false
			try {
				Thread.sleep(attackCoolDownTime);// sleep 0.5s
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			attackCoolDown = true;// set attack cool down status true
		}
	}

	/**
	 * get the attack cool down status
	 * 
	 * @return status of attack cool down
	 */
	public boolean isAttackCoolDown() {
		return attackCoolDown;
	}

	/**
	 * set attack cool down time
	 * 
	 * @param attackCoolDownTime
	 *             
	 */
	public void setAttackCoolDownTime(int attackCoolDownTime) {
		this.attackCoolDownTime = attackCoolDownTime;
	}
	
	/**
	 * get the number of lives
	 * @return
	 */
	public synchronized final int getLife() {
		return this.life;
	}
	/**
	 * decrease tank life
	 */
	public final void setLife() {
		if(life>0) {
			life--;
		} else {
			return;
		}
	}
	/**
	 * get tank type
	 * @return
	 */
	public TankType getTankType() {
		return type;
	}
}
