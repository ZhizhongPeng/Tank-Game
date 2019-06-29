package frame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import Members.Level;
import Members.Map;
import Members.Base;
import Members.Wall;
/**
 * map preview panel
 * 
 *
 */
public class MapPreViewPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int level=1;
	private List<Wall> walls=Map.getWalls();//new ArrayList<>();
	private Base base;
	private Graphics gra;
	private int count=Level.getCount();
	private Mainframe frame;

	
	public MapPreViewPanel(final Mainframe frame) {
		this.frame=frame;

		base=new Base(360, 520);
		
		//init walls
		initWalls();
				
		JButton levelReduce=new JButton("Previous Level");
		levelReduce.addActionListener(new ActionListener() {
					
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				level--;
				if(level==0) {
					level=count;
				}
				initWalls();
				repaint();
			}
		});
		JButton levelPlus=new JButton("Next Level");
		levelPlus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				level++;
				if(level>count) {
					level=1;
				}
				initWalls();
				repaint();
			}
		});	
		JButton back=new JButton("Back");
		back.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				frame.requestFocus();
				gotoLoginPanel();
			}
		});
		this.add(back);
		this.add(levelReduce);
		this.add(levelPlus);
	}
	private void gotoLoginPanel() {
		frame.setPanel(new LoginPanel(frame));
	}
	@Override
	public void paint(Graphics g) {
		super.setBackground(Color.BLACK);
		super.paint(g);
		gra = g;
		
		g.setColor(Color.ORANGE);
		g.drawString("Current Level£º"+level, 0, 12);
		g.drawString("Total Levels£º"+count, 0, 24);


		
		//paint the walls
		paintWalls();
	}
	/**
	 * paint walls
	 */
	private void paintWalls() {
		for (int i = 0; i < walls.size(); i++) {// traverse all the wall 
			Wall w = walls.get(i);// get wall object
			if(w.x>=760) {
				w.setAlive(false);
				walls.remove(w);
			}
			if (w.isAlive()&&(w.x<760)) {// if wall is alive
				gra.drawImage(w.getImage(), w.x, w.y, this);// paint the wall
			} else {// if wall is not alive
				walls.remove(i);// remove the wall from list
				i--;// i-1, make i not become i+1
			}
		}
	}
	public void initWalls() {
		Map.getMap(level);// get the map in current level
		walls.add(base);// add base into the wall list
	}
	
}
