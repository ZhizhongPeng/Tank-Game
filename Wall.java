package Members;

import Members.DisplayableImage;

public abstract class Wall extends DisplayableImage {
	private boolean alive = true;// wall status

	/**
	 * wall constructor method
	 * 
	 * @param x
	 *              initial x coordinate
	 * @param y
	 *              initial y coordinate
	 * @param url
	 *              initial wall image
	 */
	public Wall(int x, int y, String url) {
		super(x, y, url);// use father's method
	}

	/**
	 * get wall status
	 * 
	 * @return true if wall is alive, otherwise false
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * set the wall is alive
	 * 
	 * @param alive
	 *             
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	/**
	 * judge whether two walls are the same, if the coordinates are the same, the walls are the same
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Wall) {// if the obj is wall or wall's subclass
			Wall w = (Wall) obj;// change to wall class
			if (w.x == x && w.y == y) {// if the coordinates are the same
				return true;// the walls are the same
			}
		}
		return super.equals(obj);// use father's method
	}
}
