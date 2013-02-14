

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Locatable;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.Player;

public class Functions
{	
	public static Player getPlayer()
	{
		return Players.getLocal();
	}
	
	private static Tile[] getSurrounding(Area a) //get the tiles that surrounds the area
	{
		ArrayList<Tile> ts = new ArrayList<Tile>();
		
		for (Tile t : a.getBoundingTiles())
			for (int x = -1 ; x <= 1 ; x++)
				for (int y = -1 ; y <= 1 ; y++)
					if (t.derive(x, y) != null && !a.contains(t.derive(x, y)) && !ts.contains(t.derive(x, y)))
						ts.add(t.derive(x, y));
		Tile[] tsArray = new Tile[ts.size()];
		for (int i = 0 ; i < ts.size(); i++)
		{
			tsArray[i] = ts.get(i);
		}
		return tsArray;
	}
	
	public static boolean isInArea(Area a)
	{
		return isInArea(getPlayer(), a);
	}
	
	public static boolean isInArea(Locatable l, Area a)
	{
		Area areaByBounds = new Area(getSurrounding(a));
		
		return areaByBounds.contains(l);
	}
	
	public static boolean canReach(Locatable a)
	{
		return a != null && a.getLocation().canReach();
	}
	
	public static boolean canReachAround(Locatable a)
	{
		for (Tile t : getSurrounding(new Area(a.getLocation())))
			if (canReach(t))
				return true;
		
		return false;
	}
	
	public static boolean closeEnough(Locatable locatable, int distance)
	{
		if (locatable == null)
		{
			return false;
		}
		
		return Calculations.distanceTo(locatable.getLocation()) <= distance;
	}
	
	public static int getPrice(final int id)
	{
		try
		{
			String price;
			final URL url = new URL("http://open.tip.it/json/ge_single_item?item=" + id);
			final URLConnection con = url.openConnection();
			final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while ((line = in.readLine()) != null)
			{
				if (line.contains("mark_price"))
				{
					price = line.substring(line.indexOf("mark_price") + 13, line.indexOf(",\"daily_gp") - 1);
					price = price.replace(",", "");
					return Integer.parseInt(price);
				}
			}
		}
		catch (final Exception ignored)
		{
			return -1;
		}
		return -1;
	}
	
	public static double perHour(double gained) //money or exp etc
	{
		return gained / ((double)Variables.timer.getElapsed()/1000.0/3600.0);
	}
	
	public static boolean randomChance(int percentage)
	{
		return Random.nextInt(0, 99) < percentage;
	}
}
