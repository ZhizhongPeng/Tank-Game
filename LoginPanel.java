package frame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import enumtype.GameType;



public class LoginPanel extends JPanel implements KeyListener{
	private Image background;// background image
	private Image tank;// tank image
	private int y1 = 270, y2 = 330, y3=390, y4=450;// 4 Y coordinates for tank image
	private int tankY = 270;// initial Y coordinate for tank image
	private Mainframe frame;
	// constructor method
	public LoginPanel(Mainframe frame)
	{
		this.frame = frame;
		this.frame.addKeyListener(this);
		try {
			background = ImageIO.read(new File("image/login_background.png"));// read background image
			tank = ImageIO.read(new File("image/tank/player1_right.png"));// read tank image
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//paint here
    public void paint(Graphics g)
    {
    	super.paint(g);
    	g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    	Font font = new Font("ºÚÌå", Font.BOLD, 35);// create font
		g.setFont(font);// use font
		g.setColor(Color.BLACK);
		g.drawString("Single Player Mode", 300, 300);// paint the 1st line sentence
		g.drawString("Dual Player Mode", 300, 360);// paint the 2nd line sentence
		g.drawString("Preview Map", 300, 420);// paint the 3rd line sentence
		g.drawString("User-Defined Map", 300, 480);// paint the 4th line sentence
		
		g.drawImage(tank, 260, tankY, this);// paint the tank image
    }
    
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int code = e.getKeyCode();
		switch (code) {
		case KeyEvent.VK_UP:// if press ¡°¡ü¡±
			if(tankY == y1) {
				tankY = y4;
			} else if(tankY == y4) {
				tankY = y3;
			} else if(tankY == y3){
				tankY = y2;
			} else if(tankY == y2){
				tankY=y1;
			}
			repaint();// after pressing, need repaint
			break;
		case KeyEvent.VK_DOWN:// if press ¡°¡ý¡±
			if (tankY == y4) {
				tankY = y1;
			} else if(tankY ==y1){
				tankY = y2;
			} else if(tankY == y2){
				tankY = y3;
			} else if(tankY == y3){
				tankY=y4;
			}
			repaint();// after pressing, need repaint
			break;
		case KeyEvent.VK_ENTER:// if press ¡°Enter¡±
			if (tankY == y1) {// if in the first line 
				frame.removeKeyListener(this);// frame remove keyListener
				frame.setPanel(new LevelPanel(1, frame, GameType.SINGLE_PLAYER));// go to the level panel-single player mode
				frame.setVisible(true);
			}
			if(tankY == y2){
				frame.removeKeyListener(this);// frame remove keyListener
				frame.add(new LevelPanel(1, frame, GameType.DUAL_PLAYER));// go to the level panel-dual player mode
				frame.setVisible(true);
			}
			if(tankY == y4){
				//type = null;
				frame.removeKeyListener(this);// frame remove keyListener
				frame.setPanel(new MapEditorPanel(frame));
			}
			if(tankY == y3)	{
				//type=null;
				frame.removeKeyListener(this);// frame remove keyListener
				frame.setPanel(new MapPreViewPanel(frame));
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
