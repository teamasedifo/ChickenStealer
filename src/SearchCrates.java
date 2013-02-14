import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.WidgetChild;


public class SearchCrates extends Node
{
	@Override
	public boolean activate()
	{
		if (!Inventory.isFull() &&
				Functions.canReachAround(Variables.crateLocation) &&
				Functions.isInArea(Variables.backRoomArea))
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	public void execute()
	{
		final SceneObject crate = SceneEntities.getAt(Variables.crateLocation);
		
		if (crate != null && crate.validate())
		{
			if (!Utilities.isOnScreen(crate))
			{
				Utilities.cameraTurnTo(crate);
			}
			if (Utilities.waitFor(new Condition()
			{
				public boolean validate()
				{
					return Utilities.isOnScreen(crate);
				}
			}, 2000))
			{
				if (Functions.randomChance(38))
				{
					Camera.setPitch(Random.nextInt(39, 75));
				}
				if (Utilities.interact(crate, false, "Search") &&
						Mouse.move((int)Variables.yesLocation.getX()+Random.nextInt(-40, 40),
								(int)Variables.yesLocation.getY()+Random.nextInt(-4, 4)) && 
								Utilities.waitFor(new Condition()
								{
									public boolean validate() //wait until this returns true OR timeout completes
									{
										WidgetChild yes = Widgets.get(1188, 11);
										return yes != null && yes.validate() && yes.visible();
									}
								}, 6000))
				{
					WidgetChild yes = Widgets.get(1188, 11);
					yes.click(true);
				}
			}
		}
	}
}
