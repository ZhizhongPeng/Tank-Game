package frame;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
       
import enumtype.WallType;
import Members.Level;
import Members.Map;
import Members.Base;
import Members.wall.BrickWall;
import Members.wall.GrassWall;
import Members.wall.IronWall;
import Members.wall.RiverWall;
import Members.Wall;
import util.ImageUtil;
import util.MapIO;

/**
 * nap editing panel
 * 
 *
 */
public class MapEditorPanel extends JPanel implements MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//four kinds of wall images
	private Image[] wallImgs= {
		Toolkit.getDefaultToolkit().createImage(ImageUtil.BRICKWALL_IMAGE_URL),
		Toolkit.getDefaultToolkit().createImage(ImageUtil.GRASSWALL_IMAGE_URL),
		Toolkit.getDefaultToolkit().createImage(ImageUtil.IRONWALL_IMAGE_URL),
		Toolkit.getDefaultToolkit().createImage(ImageUtil.RIVERWALL_IMAGE_URL)
	};

	private WallType wallType;
	private Graphics gra;
	
	int count=Level.getCount();
	int level=1;
	public static List<Wall> walls=Map.getWalls();
	private Base base;
	private Mainframe frame;

	
	public  MapEditorPanel(final Mainframe frame) {
		this.frame=frame;
		this.addMouseListener(this);
		
		base = new Base(360, 520);
		//initial walls
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
		JButton create=new JButton("Create");
		create.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				count++;			
				Boolean b = MapIO.writeMap(count+"");
				if(b) {
					JOptionPane.showMessageDialog(null, "Create successfully");
				}
				repaint();
			}
		});
		JButton update=new JButton("Update");
		update.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {			
				Boolean b = MapIO.writeMap(level+"");
				if(b) {
					JOptionPane.showMessageDialog(null, "Update successfully");
				}
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
		this.add(levelReduce);
		this.add(levelPlus);
		this.add(create);
		this.add(update);
		this.add(back);
		
	}
	private void gotoLoginPanel() {
		frame.removeMouseListener(this);
		frame.setPanel(new LoginPanel(frame));
	}
	@Override
	public void paint(Graphics g) {
		super.setBackground(Color.BLACK);
		super.paint(g);
		gra=g;
		
		g.setColor(Color.ORANGE);
		
		g.drawString("Current Level£º"+level, 0, 12);
		g.drawString("Total Levels£º"+count, 0, 24);
		g.setColor(Color.CYAN);
		// paint the horizontal line
		for(int i=0;i<560;i+=40) {
			g.drawLine(0, i, 760, i);
		}
		// paint the vertical line
		for(int j=0;j<780;j+=40) {
			g.drawLine(j, 0, j, 600);
		}
		// paint the four kinds of walls in the right
		g.drawImage(wallImgs[0], 762, 0, this);
		g.drawImage(wallImgs[1], 762, 20, this);
		g.drawImage(wallImgs[2], 762, 40, this);
		g.drawImage(wallImgs[3], 762, 60, this);
		
		// draw a eraser
		g.setColor(Color.MAGENTA);
		g.drawRect(762, 80, 20, 19);
		
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
	/**
	 * init walls
	 */
	public void initWalls() {
		Map.getMap(level);// get the map of current level
		walls.add(base);// add base into the wall list
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Point clickedPoint=e.getPoint();
		if(clickedPoint.x>=762&&clickedPoint.y<=100) {
			if(clickedPoint.y>0&&clickedPoint.y<20) {
				wallType=WallType.BRICK;
				setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			}else if(clickedPoint.y>20&&clickedPoint.y<40) {
				wallType=WallType.GRASS;
				setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			}else if(clickedPoint.y>40&&clickedPoint.y<60) {
				wallType=WallType.IRON;
				setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			}else if(clickedPoint.y>60&&clickedPoint.y<80) {
				wallType=WallType.RIVER;
				setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			}else if(clickedPoint.y>80&&clickedPoint.y<100) {
				wallType=null;
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		}
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Point p=new Point();
		p=e.getPoint();//get the point where the mouse is released
		p=new Point((p.x-p.x%20),(p.y-p.y%20));//make the point coordinate become the multiple of 20£¬wall is 20*20
		Point base1=new Point(360,520);
		Point base2=new Point(380,520);
		Point base3=new Point(360,540);
		Point base4=new Point(380,540);
		if((p.x<760)&&!p.equals(base1)&&!p.equals(base2)&&!p.equals(base3)&&!p.equals(base4)) {
			//traverse the wall list, if there exists a wall in the released point, erase it
			for(int i=0;i<walls.size();i++) {
				Wall w=walls.get(i);
				if(w.x>=760||(w.x==p.x&&w.y==p.y&&!w.equals(base))) {
					walls.remove(w);
					repaint();
				}
			}

			if(wallType!=null) {//if wall type is not null, then add walls
				addWall(wallType, p);
			}
		}
	}
	private void addWall(WallType type,Point p) {
		switch(type) {
		case BRICK:
			BrickWall b=new BrickWall(p.x, p.y);
			for(int i=0;i<walls.size();i++) {
				Wall w=walls.get(i);
				if(w.equals(b)) {
					walls.remove(w);
				}
			}
			walls.add(b);
			break;
		case GRASS:
			GrassWall grass=new GrassWall(p.x, p.y);
			for(int i=0;i<walls.size();i++) {
				Wall w=walls.get(i);
				if(w.equals(grass)) {
					walls.remove(w);
				}
			}
			walls.add(grass);
			break;
		case IRON:
			IronWall iron=new IronWall(p.x, p.y);
			for(int i=0;i<walls.size();i++) {
				Wall w=walls.get(i);
				if(w.equals(iron)) {
					walls.remove(w);
				}
			}
			walls.add(iron);
			break;
		case RIVER:
			RiverWall river=new RiverWall(p.x, p.y);
			for(int i=0;i<walls.size();i++) {
				Wall w=walls.get(i);
				if(w.equals(river)) {
					walls.remove(w);
				}
			}
			walls.add(river);
			break;
		default:
			break;
		}
		repaint();
	}
		
}
