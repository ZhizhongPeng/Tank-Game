package Members;

import java.io.File;
import java.io.FileNotFoundException;

import util.MapIO;

/**
 * Level
 * 
 *
 */
public class Level {
	private static int nextLevel = 1;// next level 
	private static int previsousLevel = 1;// previous level
	private static int count=0;// total levels
	static{
		readLevel();
	}
	/**
	 * read level
	 */
	private static void readLevel() {
		try {
			File f = new File(MapIO.DATA_PATH);// create a map contents
			if (!f.exists()) {// if file does not exist
				throw new FileNotFoundException("Map File is lost! ");
			}
			File fs[] = f.listFiles();// get all files in the contents
			count = fs.length;// file number equals total levels
			if (count == 0) {// no files in the contents
				throw new FileNotFoundException("Map File is lost! ");// throw exception
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * goto next level
	 * 
	 * @return level
	 */
	public static int nextLevel() {
		nextLevel++;// level+1
		previsousLevel = nextLevel;// give the current level's next level as the previous level of next level
		if (nextLevel > count) {// if level number > total levels 
			nextLevel = 1;// go back to the first level
			previsousLevel=count;
		}
		return nextLevel;// return next level number
	}

	/**
	 * Previous level
	 * 
	 * @return level number
	 */
	public static int previsousLevel() {
		return previsousLevel;// return previous level number
	}
	/**
	 * get total level number
	 * @return total level number
	 */
	public static int getCount() {
		readLevel();
		return count;
	}
}
