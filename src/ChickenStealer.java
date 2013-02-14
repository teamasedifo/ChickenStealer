import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.powerbot.core.event.events.MessageEvent;
import org.powerbot.core.event.listeners.MessageListener;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Job;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;

@Manifest(authors = { "asedifo" }, description = "Gives you so many raw chickens that you will need several money pouches eventually.", name = "Chicken Stealer", version = 1.0)

public class ChickenStealer extends ActiveScript implements MessageListener, PaintListener
{
	private final List<Node> jobsCollection = Collections.synchronizedList(new ArrayList<Node>());
	private Tree jobContainer = null;
	
	public synchronized final void provide(final Node... jobs)
	{
		for (final Node job : jobs)
		{
			if(!jobsCollection.contains(job))
			{
				jobsCollection.add(job);
			}
		}
		jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection.size()]));
	}
	
	public synchronized final void revoke(final Node... jobs)
	{
		for (final Node job : jobs)
		{
			if(jobsCollection.contains(job))
			{
				jobsCollection.remove(job);
			}
		}
		jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection.size()]));
	}
	
	public final void submit(final Job... jobs)
	{
		for (final Job job : jobs)
		{
			getContainer().submit(job);
		}
	}
	
	@Override
	public void onRepaint(Graphics g1)
	{
		Graphics2D g = (Graphics2D)g1;
		
		g.setColor(new Color(0x00, 0x00, 0x00, 127));
		g.fillRect(340, 60, 150, 50);
		
		g.setColor(new Color(0x12, 0x40, 0xAB, 200));
		g.drawString("Run time: " + Variables.timer.toElapsedString(), 342, 74);
		g.drawString("Chickens: " + Variables.chickenCount, 342, 86);
		double d = Functions.perHour(Variables.profit);
		g.drawString("Profit: " + ((double)Variables.profit > 50000 ? String.format("%.2fk", (double)Variables.profit/1000.0) : String.format("%.0f", (double)Variables.profit)) + "(" + (d > 8500 ? String.format("%.2fk", d/1000.0) : String.format("%.1f", d)) + "/hr)", 342, 98);
	}
	
	@Override
	public void messageReceived(MessageEvent m)
	{
		if (m.getMessage().equals("You take a raw chicken."))
		{
			Variables.chickenCount++;
			Variables.profit += Variables.chickenPrice;
		}
	}
	
	@Override
	public void onStart()
	{
		System.out.println("Welcome to Team asedifo's ChickenStealer!");
		
		Variables.chickenPrice = Functions.getPrice(2138); //raw chicken
		Variables.profit = Variables.chickenPrice * Variables.chickenCount;
		
		provide(new SearchCrates(), new OpenInnerDoor(), new OpenOuterDoor(), new Banker(), new WalkToChicken());
	}
	
	@Override
	public void onStop()
	{
		System.out.println("Thanks a lot for using Team asedifo's ChickenStealer!");
		System.out.println("Profit: " + Variables.profit);
		System.out.println("Chickens: " + Variables.chickenCount);
	}
	
	@Override
	public int loop()
	{
		try
		{
			if (Game.getClientState() != Game.INDEX_MAP_LOADED)
			{
				return 1000;
			}
			if (Players.getLocal() == null)
				return 10000;
			
			if (Game.isLoggedIn())
			{
				if (jobContainer != null)
				{
					final Node job = jobContainer.state();
					if (job != null)
					{
						jobContainer.set(job);
						getContainer().submit(job);
						job.join();
					}
				}
			}
			
			if (Inventory.getCount() >= Variables.turnScreenChicken)
			{
				Camera.setPitch(Random.nextInt(45, 86));
				Camera.setAngle(Random.nextInt(181, 236));
				Variables.turnScreenChicken = 99;
			}
			
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return Random.nextInt(250, 500);
	}
	
}
