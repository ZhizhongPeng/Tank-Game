package Members;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import Members.DisplayableImage;
import Members.BotTank;
import Members.Tank;
import Members.wall.BrickWall;
import Members.wall.GrassWall;
import Members.wall.IronWall;
import Members.wall.RiverWall;
import Members.Wall;
import util.AudioPlayer;
import util.AudioUtil;
import util.AudioPlayer.AudioThread;
import Members.Bullet;
import enumtype.Direction;
import enumtype.TankType;
import frame.GamePanel;

public class Bullet extends DisplayableImage{
	Direction direction;
	static final int LENGTH = 8;// the length of bullet
	private GamePanel gamePanel;// game panel
	private int speed = 7;// moving speed
	private boolean alive = true;// whether bullet is alive
	Color color = Color.ORANGE;// bullet color: orange
	TankType tanktype;// tank type 
	private boolean isHitIronWall=false;
	/**
	 * 
	 * bullet constructor method
	 * 
	 * @param x
	 *              bullet's initial x coordinate
	 * @param y
	 *              bullet's initial y coordinate
	 * @param direction
	 *              bullet's direction
	 * @param gamePanel
	 *              game panel
	 * @param tanktype
	 *              tank type
	 */
	public Bullet(int x, int y, Direction direction, GamePanel gamePanel, TankType tanktype) {
		super(x, y, LENGTH, LENGTH);
		this.direction = direction;
		this.gamePanel = gamePanel;
		this.tanktype = tanktype;
		init();
	}

	/**
	 * 初始化组件
	 */
	private void init() {
		Graphics g = image.getGraphics();// get the paint method
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, LENGTH, LENGTH);// paint a rectangle
		g.setColor(color);// use bullet color
		g.fillOval(0, 0, LENGTH, LENGTH);// paint a circle
		g.drawOval(0, 0, LENGTH - 1, LENGTH - 1);// paint a frame for circle, length-1, in order to keep the bullet inside border
	}

	/**
	 * bullet move method
	 */
	public void move() {
		switch (direction) {
		case UP:
			upward();
			break;
		case DOWN:
			downward();
			break;
		case LEFT:
			leftward();
			break;
		case RIGHT:
			rightward();
			break;
		}
	}

	/**
	 * moving left
	 */
	private void leftward() {
		x -= speed;
		moveToBorder();// move to border, dispose the bullet
	}

	/**
	 * moving right
	 */
	private void rightward() {
		x += speed;
		moveToBorder();// move to border, dispose the bullet
	}

	/**
	 * moving up
	 */
	private void upward() {
		y -= speed;
		moveToBorder();// move to border, dispose the bullet
	}

	/**
	 * moving down
	 */
	private void downward() {
		y += speed;
		moveToBorder();// move to border, dispose the bullet
	}

	/**
	 * hit tank method
	 */
	public void hitTank() {
		List<Tank> tanks = gamePanel.getTanks();// get all tank
		for (int i = 0, lengh = tanks.size(); i < lengh; i++) {// traverse all the tank
			Tank t = tanks.get(i);// get tank object
			if (t.isAlive() && this.hit(t)) {// if tank is alive and bullet hits tank
				switch (tanktype) {// judge where the bullet are from
				case PLAYER_1:// if player 1's bullet
				case PLAYER_2:// if player 2's bullet
					if (t instanceof BotTank) {// if tank is bot tank
						this.dispose(); // dispose the bullet
						t.setAlive(false);// bot tank is dead
					} 
				else if (t instanceof Tank) {// if tank is player
						this.dispose(); // dispose the bullet
					}
					break;
				case BOTTANK:// if bot tank's bullet
					if (t instanceof BotTank) {// if tank is bot tank
						this.dispose(); // dispose the bullet
					} else if (t instanceof Tank) {// if tank is player
						this.dispose(); // dispose the bullet
						t.setAlive(false);// player is dead
					}
					break;
				default:
					this.dispose();
					t.setAlive(false);// tank is dead
				}
			}
		}
	}
	
	/**
	 * hit base method
	 */
	public void hitBase() {
		Base b = gamePanel.getBase();// get base
		if (this.hit(b)) {// if hit base
			this.dispose(); // dispose the bullet
			b.setAlive(false);// base is destroyed
		}
	}

	
	/**
	 * hit bullet
	 */
	public void hitBullet() {
		List<Bullet> bullets=gamePanel.getBullets();
		for(int i=0;i<bullets.size();i++) {
			Bullet b=bullets.get(i);
			if(this.alive) {
				if(this.hit(b)&&this.tanktype!=b.tanktype) {
					b.dispose();// dispose the bullet
					this.dispose();// dispose the bullet
				}
			}
		}
	}
	/**
	 * move to border and dispose the bullet
	 */
	private void moveToBorder() {
		if (x < 0 || x > gamePanel.getWidth() - getWidth() || y < 0 || y > gamePanel.getHeight() - getHeight()) {// if bullet is out of border
			this.dispose();// dispose the bullet
		}
	}

	/**
	 * hit wall
	 */
	public void hitWall() {
		List<Wall> walls = gamePanel.getWalls();// get all walls
		for (int i = 0, lengh = walls.size(); i < lengh; i++) {// traverse the wall
			Wall w = walls.get(i);// get wall object
			if (this.hit(w)) {// if hit wall 
				if (w instanceof BrickWall) {// if wall is brick
					new AudioPlayer(AudioUtil.HIT).new AudioThread().start();
					this.dispose();
					w.setAlive(false);// brick is destroyed
				}
				if (w instanceof IronWall) {// if wall is iron wall
					this.dispose();
					if(this.isHitIronWall) {
						w.setAlive(false);
					}
					new AudioPlayer(AudioUtil.HIT).new AudioThread().start();
				}
				if(w instanceof RiverWall) {// if wall is river
					if(this.isHitIronWall) { 
						this.dispose();
						w.setAlive(false);
					}
				}
				if(w instanceof GrassWall) {// if wall is grass
					if(this.isHitIronWall) {
						this.dispose();
						w.setAlive(false);
					}
				}
			}
		}
	}
	
	/**
	 * dispose bullet
	 */
	private synchronized void dispose() {
		this.alive = false;
	}

	/**
	 * get the status of bullet
	 * 
	 * @return
	 */
	public boolean isAlive() {
		return alive;
	}
	
	public void setIsHitIronWall(boolean b) {
		this.isHitIronWall=b;
	}
}
