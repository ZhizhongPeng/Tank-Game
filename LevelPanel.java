package frame;

import javax.swing.*;

import enumtype.GameType;

import java.awt.*;

public class LevelPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int level;
	private Mainframe frame;
	private GameType gametype;
	private String levelStr;// level string
	private String ready = "";// ready hint 
	
	// constructor method
    public LevelPanel(int level, Mainframe frame, GameType gametype)
    {
    	  this.level = level;
    	  this.frame = frame;
    	  this.gametype = gametype;
    	  levelStr = "LELEL " + level;
    	  Thread t = new LevelPanelThread();
    	  t.start();
    }

    public void paint(Graphics g)
    {
    	super.paint(g);
    	g.setColor(Color.BLACK);// black background
		g.fillRect(0, 0, getWidth(), getHeight());// fill a black rectangle into the panel
		g.setFont(new Font("¿¬Ìå", Font.BOLD, 50));// set font
		g.setColor(Color.CYAN);
		g.drawString(levelStr, 335, 300);// paint level string
		g.setColor(Color.RED);
		g.drawString(ready, 330, 400);// paint ready hint
    }
    
    private class LevelPanelThread extends Thread {
		public void run() {
			for (int i = 0; i < 6; i++) {
				if (i % 2 == 0) {
					levelStr = "Level " + level;
				} else {
					levelStr = "";// blink
				}
				if (i == 4) {
					ready = "Ready Go!";
				}
				repaint();
				try {
					Thread.sleep(500);// sleep 0.5s
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//Go to the game panel
			System.gc();
			if(gametype == GameType.SINGLE_PLAYER)
			      frame.setPanel(new GamePanel(frame, level, GameType.SINGLE_PLAYER));// single player mode
			else 
				  frame.setPanel(new GamePanel(frame, level, GameType.DUAL_PLAYER));// dual player mode
		}
	}
}


