

import java.awt.Point;

import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.map.Path;

public class Variables
{
	public static Tile chickenHouse = new Tile(3017, 3206, 0);
	//public static Area frontRoomArea = new Area(new Tile(3017, 3202, 0), new Tile(3011, 3202, 0), new Tile(3012, 3211, 0), new Tile(3017, 3207, 0));
	public static Area frontRoomArea = new Area(new Tile(3015, 3210, 0), new Tile(3012, 3203, 0));
	public static Area backRoomArea = new Area(new Tile(3012, 3203, 0), new Tile(3008, 3203, 0), new Tile(3008, 3210, 0), new Tile(3012, 3210, 0));
	
	public static Tile depositBoxLoc = new Tile(3047, 3236, 0);
	public static Tile crateLocation = new Tile(3009, 3209, 0);
	
	public static Path currentPath = null;
	public static Tile[] boxToChickenTiles = {new Tile(3044, 3235, 0), new Tile(3033, 3236, 0), new Tile(3028, 3228, 0), new Tile(3028, 3213, 0), new Tile(3018, 3206, 0)};
	
	public static int crateId = 15032;
	public static int innerDoorId = 2069;
	public static int outerDoorClosedId = 40108;
	public static int depositBoxId = 36788;
	public static int chickenManId = 557;
	
	public static Point yesLocation = new Point(259, 457);
	
	public static int chickenCount = 0;
	public static long chickenPrice = 0;
	public static long profit = 0;
	
	public static Timer timer = new Timer(0);
	
	public static int turnScreenChicken = 0;
}
