package frame;

import javax.swing.*;


import Members.Bullet;
import Members.Level;
import Members.Tank;
import enumtype.GameType;
import enumtype.TankType;
import Members.Bomb;
import Members.BotTank;
import Members.Bullet;
//import Members.Tool;
import Members.Base;
import Members.Wall;
import Members.Map;
import util.ImageUtil;
import util.MapIO;
import util.AudioPlayer;
import util.AudioUtil;
import util.AudioPlayer.AudioThread;

import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class GamePanel extends JPanel implements KeyListener{
	public static final int FRESHTIME = 20;
	private BufferedImage image;// displayable image in game panel
	private Graphics g;// painting object
	private Mainframe frame;// main frame
	private GameType gameType;
	private Tank player1, player2;// player
	private boolean y_key, s_key, w_key, a_key, d_key, up_key, down_key, left_key, right_key, num1_key;// key listener
	int level=1;// level
	private List<Bullet> bullets;
	private volatile List<Tank> allTanks;// tank list
	private List<Tank> botTanks;// bot tank list
	private static final int botCount = 30;// maximum bot tank number
	private int botReadyCount = botCount;// imcoming bot tank number
	private int botSurplusCount = botCount;// the remaining bot tank number
	private int botMaxInMap = 6;// maximum bot tank number in the map
	private int botX[] = { 10, 367, 754 };// three different position bot tank might appear in the map in the beginning
	private List<Tank> playerTanks;// player number
	private volatile boolean finish = false;// game over status
	private Base base;
	private List<Wall> walls;
	private List<Bomb> BombImage;
	private Random r = new Random();// random direction
	private int createBotTimer = 0;// timer for create bot tanks
	private Tank survivor;// the remaining player number
	private List<AudioClip> audios=AudioUtil.getAudios();// background music list
	//private Tool tool=Tool.getToolInstance(r.nextInt(500), r.nextInt(500));
	//private int toolTimer=0;
	private int pauseTimer=0;// pause time
	private int count=Level.getCount();
	public GamePanel(Mainframe frame, int level, GameType gameType) {
		this.frame = frame;
		frame.setSize(775, 600);
		this.level = level;
		this.gameType = gameType;
		setBackground(Color.BLACK);// background color: black
		init();
		Thread t = new FreshThead();// fresh thread, refresh the game panel
		t.start();// start the thread
		new AudioPlayer(AudioUtil.START).new AudioThread().start();// start the music thread
		frame.addKeyListener(this);// add key listner
	}
	/**
	 * init method
	 */
	private void init() {
		bullets = new ArrayList<>();
		allTanks = new ArrayList<>();
		walls = new ArrayList<>();
		BombImage = new ArrayList<>();
		
		image = new BufferedImage(794, 572, BufferedImage.TYPE_INT_BGR);
		g = image.getGraphics();

		playerTanks = new Vector<>();
		player1 = new Tank(278, 537, ImageUtil.PLAYER1_UP_IMAGE_URL, this, TankType.PLAYER_1);// create player 1
		if (gameType == GameType.DUAL_PLAYER) {//  single player mode
			player2 = new Tank(448, 537, ImageUtil.PLAYER2_UP_IMAGE_URL, this, TankType.PLAYER_2);// create player 2
			playerTanks.add(player2);
		}
		playerTanks.add(player1);

		botTanks = new ArrayList<>();
		botTanks.add(new BotTank(botX[0], 1, this, TankType.BOTTANK));
		botTanks.add(new BotTank(botX[1], 1, this, TankType.BOTTANK));
		botTanks.add(new BotTank(botX[2], 1, this, TankType.BOTTANK));
		botReadyCount -= 3;// imcoming bot tank number-3
		allTanks.addAll(playerTanks);
		allTanks.addAll(botTanks);
		base = new Base(360, 520);// create base
		initWalls();// create the walls
	}
	
	private void gotoLoginPanel() {
		frame.setPanel(new LoginPanel(frame));
	}
	/**
	 * init walls
	 */
	@SuppressWarnings("static-access")
	public void initWalls() {
		Map map = Map.getMap(level);// get the map in the current level
		walls.addAll(map.getWalls());
		walls.add(base);// add base in the wall list
	}
	
	/**
	 * paint
	 */
	public void paint(Graphics g) {
		super.paint(g);
		paintTankActoin();// paint the players
		createBotTank();// paint the bot tanks
		paintImage();// paint images
		g.drawImage(image, 0, 0, this);
		System.gc();
	}
	
	/**
	 * paint image
	 */
	private void paintImage() {
		g.setColor(Color.BLACK);// background color
		g.fillRect(0, 0, image.getWidth(), image.getHeight());// fill a black rectangle
		panitBomb();// paint bomb
		paintBotCount();// paint the remaining bot tank numbers
		panitBotTanks();// paint bot tanks in the map
		paintPlayerTanks();// paint the player in the map
		allTanks.addAll(playerTanks);
		allTanks.addAll(botTanks);
		panitWalls();// paint the walls
		panitBullets();// paint the bullet
		
		if (botSurplusCount == 0) {// the remaining bot tanks number
			stopThread();
			paintBotCount();// paint the bot tanks
			g.setFont(new Font("楷体", Font.BOLD, 50));// set font
			g.setColor(Color.green);
			g.drawString("Success! ", 250, 400);
			gotoNextLevel();
		}

		if (gameType == GameType.SINGLE_PLAYER) {//single player mode
			if (!player1.isAlive()&&player1.getLife()==0) {// if player 1 is not alive
				stopThread();// stop thread begins
				BombImage.add(new Bomb(player1.x, player1.y));// create bomb image
				panitBomb();// paint bomb
				paintGameOver();// paint game over
				gotoPrevisousLevel();// back to the current level
			}
		} else if(gameType == GameType.DUAL_PLAYER){// dual player mode
			if (player1.isAlive() && !player2.isAlive() && player2.getLife()==0) {// survivor is player 1
				survivor = player1;
			} else if (!player1.isAlive() && player1.getLife()==0 && player2.isAlive()) {
				survivor = player2;// survivor is player 1
			} else if (!(player1.isAlive() || player2.isAlive())) {// all players are not alive
				stopThread();
				BombImage.add(new Bomb(survivor.x, survivor.y));
				panitBomb();
				paintGameOver();
				gotoPrevisousLevel();
			}
		}

		if (!base.isAlive()) {// base is destroyed
			stopThread();// stop thread begins
			paintGameOver();// paint game over
			base.setImage(ImageUtil.BREAK_BASE_IMAGE_URL);// create destroyed base image
			gotoPrevisousLevel();// back to current level
		}
		g.drawImage(base.getImage(), base.x, base.y, this);// paint
	}
	
	/**
	 * paint walls
	 */
	private void panitWalls() {
		for (int i = 0; i < walls.size(); i++) {
			Wall w = walls.get(i);
			if (w.isAlive()) {
				g.drawImage(w.getImage(), w.x, w.y, this);
			} else {
				walls.remove(i);
				i--;
			}
		}
	}
	
	/**
	 * paint bullets
	 */
	private void panitBullets() {
		for (int i = 0; i < bullets.size(); i++) {
			Bullet b = bullets.get(i);// get a bullet
			if (b.isAlive()) {
				b.move();
				b.hitBase();
				b.hitWall();
				b.hitTank();
				b.hitBullet();
				g.drawImage(b.getImage(), b.x, b.y, this);
			} else {
				bullets.remove(i);
				i--;
			}
		}
	}
	/**
	 * paint Player Tanks
	 */
	private void paintPlayerTanks() {
		for (int i = 0; i < playerTanks.size(); i++) {
			Tank t = playerTanks.get(i);// get a tank
			if (t.isAlive()) {
				//t.hitTool();
				//t.addStar();
				g.drawImage(t.getImage(), t.x,t.y, this);// draw tanks
			} else {
				//TankType type=t.getTankType();
				int life=t.getLife();
				playerTanks.remove(i);// remove the tank from list
				BombImage.add(new Bomb(t.x, t.y));// create bomb
				AudioClip blast=audios.get(2);
				blast.play();
				t.setLife();
				if(t.getLife()>0) {
					if(t.getTankType()==TankType.PLAYER_1) {
						player1 = new Tank(278, 537, ImageUtil.PLAYER1_UP_IMAGE_URL, this, TankType.PLAYER_1);
						playerTanks.add(player1);
					}
					if(t.getTankType()==TankType.PLAYER_2) {
							player2 = new Tank(448, 537, ImageUtil.PLAYER2_UP_IMAGE_URL, this, TankType.PLAYER_2);
							playerTanks.add(player2);
					}
				}
				
				
			}
		}
	}
	/**
	 * paint bot tanks
	 * 
	 */
	private void panitBotTanks() {
		for (int i = 0; i < botTanks.size(); i++) {
			BotTank t = (BotTank) botTanks.get(i);// get a bot tank
			if (t.isAlive()) {
				if(!t.isPause()) {
					t.go();// bot tank mvoes	
				} 
				if(t.isPause()) {
					if(pauseTimer>2500) {// pause 2.5s
						t.setPause(false);
						pauseTimer=0;// reset pause time
					}
					pauseTimer+=FRESHTIME;
				}
				g.drawImage(t.getImage(), t.x, t.y, this);// draw bot tanks
			} else {
				botTanks.remove(i);
				i--;
				BombImage.add(new Bomb(t.x, t.y));
				decreaseBot();//bot tanks number-1
			}
		}
	}
	/**
	 * paint the remaining bot tank number
	 */
	private void paintBotCount() {
		g.setFont(new Font("TimesRoman", Font.BOLD, 15));// set font
		g.setColor(Color.ORANGE);
		g.drawString("The Remaining Enemies:" + botSurplusCount, 305, 15);// paint the string
	}
	/**
	 * paint the bomb
	 */
	private void panitBomb() {
		for (int i = 0; i < BombImage.size(); i++) {
			Bomb Bomb = BombImage.get(i);// get a bomb
			if (Bomb.isAlive()) {
				AudioClip blast=audios.get(2);
				blast.play();
				Bomb.show(g);
			} else {
				BombImage.remove(i);
				i--;
			}
		}
	}
	/**
	 * paint game over
	 */
	private void paintGameOver() {
		g.setFont(new Font("楷体", Font.BOLD, 50));
		g.setColor(Color.RED);
		g.drawString("Game Over !", 250, 400);
		new AudioPlayer(AudioUtil.GAMEOVER).new AudioThread().start();// add game over music
	}
	
	/**
	 * go to next level
	 */
	private void gotoNextLevel() {
		Thread jump = new JumpPageThead(Level.nextLevel());
		jump.start();// start jump thread
	}

	/**
	 * go to previous level
	 */
	private void gotoPrevisousLevel() {
		Thread jump = new JumpPageThead(Level.previsousLevel());
		jump.start();
	}
	
	/**
	 * game over thread
	 */
	private synchronized void stopThread() {
		frame.removeKeyListener(this);// remove key listener
		finish = true;
	}
	/**
	 * fresh thread 
	 */
	private class FreshThead extends Thread {
		public void run() {
			while (!finish) {// game is not over
				repaint();// ִrepaint 
				System.gc();// collect the trash
				try {
					Thread.sleep(FRESHTIME);// ָsleep
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * create bot tanks
	 */
	
	private void createBotTank() {
		int index = r.nextInt(3);
		createBotTimer += FRESHTIME;// when game panel is fresh, 
		if (botTanks.size() < botMaxInMap && botReadyCount > 0 && createBotTimer >= 1500) {
			
			Rectangle bornRect = new Rectangle(botX[index], 1, 35, 35);// create a rectangle for new born bot tanks
			for (int i = 0, lengh = allTanks.size(); i < lengh; i++) {// 
				Tank t = allTanks.get(i);
				if (t.isAlive() && t.hit(bornRect)) {// if tank is alive and hit new born tank, then don't produce new bot tanks
					return;
				}
			}
			botTanks.add(new BotTank(botX[index], 1, GamePanel.this, TankType.BOTTANK));// create bot tanks
			new AudioPlayer(AudioUtil.ADD).new AudioThread().start();
			botReadyCount--;// the incoming bot tank number-1
			createBotTimer = 0;// reset create bot tank timer
		}
	}
	/**
	 * get all the tanks
	 * 
	 * @return tank list
	 */
	public List<Tank> getTanks() {
		return allTanks;
	}
	/**
	 * get bullet list
	 * @return
	 */
	public List<Bullet> getBullets(){
		return bullets;
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * key pressed
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_Y:
			y_key = true;
			break;
		case KeyEvent.VK_W:
			w_key = true;
			a_key = false;
			s_key = false;
			d_key = false;
			break;          
		case KeyEvent.VK_A:
			w_key = false;
			a_key = true;
			s_key = false;
			d_key = false;
			break;          
		case KeyEvent.VK_S:
			w_key = false;
			a_key = false;
			s_key = true;
			d_key = false;
			break;
		case KeyEvent.VK_D:
			w_key = false;//
			a_key = false;//
			s_key = false;//
			d_key = true;// 
			break;
		case KeyEvent.VK_HOME:
		case KeyEvent.VK_NUMPAD1:
			num1_key = true;
			break;
		case KeyEvent.VK_UP:
			up_key = true;
			down_key = false;
			right_key = false;
			left_key = false;
			break;
		case KeyEvent.VK_DOWN:
			up_key = false;
			down_key = true;
			right_key = false;
			left_key = false;
			break;
		case KeyEvent.VK_LEFT:
			up_key = false;
			down_key = false;
			right_key = false;
			left_key = true;
			break;
		case KeyEvent.VK_RIGHT:
			up_key = false;
			down_key = false;
			right_key = true;
			left_key = false;
			break;
		}
	}



	/**
	 * tank movement
	 */
	private void paintTankActoin() {
		if (y_key) {
			player1.attack();// attack
		}
		if (w_key) {
			player1.upWard();// move up
		}
		if (d_key) {
			player1.rightWard();// move right
		}
		if (a_key) {
			player1.leftWard();// move left
		}
		if (s_key) {
			player1.downWard();// move down
		}
		if (gameType == GameType.DUAL_PLAYER) {
			if (num1_key) {
				player2.attack();// attack
			}
			if (up_key) {
				player2.upWard();
			}
			if (right_key) {
				player2.rightWard();
			}
			if (left_key) {
				player2.leftWard();
			}
			if (down_key) {
				player2.downWard();
			}
		}
	}

	/**
	 * key released
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_Y:
			y_key = false;
			break;
		case KeyEvent.VK_W:
			w_key = false;
			break;
		case KeyEvent.VK_A:
			a_key = false;
			break;
		case KeyEvent.VK_S:
			s_key = false;
			break;
		case KeyEvent.VK_D:
			d_key = false;
			break;
		case KeyEvent.VK_HOME:
		case KeyEvent.VK_NUMPAD1:
			num1_key = false;
			break;
		case KeyEvent.VK_UP:
			up_key = false;
			break;
		case KeyEvent.VK_DOWN:
			down_key = false;
			break;
		case KeyEvent.VK_LEFT:
			left_key = false;
			break;
		case KeyEvent.VK_RIGHT:
			right_key = false;
			break;
		}
	}
	
	/**
	 * jump level thread
	 */
	private class JumpPageThead extends Thread {
		int level;// level number

		/**
		 * jump to specific level
		 * 
		 * @param level
		 *            
		 */
		public JumpPageThead(int level) {
			this.level = level;
		}

		/**
		 * run method
		 */
		public void run() {
			try {
				Thread.sleep(1000);// sleep 1s
				frame.setPanel(new LevelPanel(level, frame, gameType));// go to the level panel
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * get walls
	 * 
	 * @return wall list
	 */
	public List<Wall> getWalls() {
		return walls;
	}
	/**
	 * get base
	 * 
	 * @return base
	 */
	public Base getBase() {
		return base;
	}
	/**
	 * decrease bot tanks number
	 */
	public void decreaseBot() {
		botSurplusCount--;// bot tank number-1
	}

	/**
	 * add bullet in game panel
	 * 
	 * @param b
	 *              ��ӵ��ӵ�
	 */
	public void addBullet(Bullet b) {
		bullets.add(b);
	}
}
