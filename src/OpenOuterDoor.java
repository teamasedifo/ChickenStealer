import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;


public class OpenOuterDoor extends Node
{
	@Override
	public boolean activate()
	{
		if (Inventory.isFull() &&
				Functions.canReach(NPCs.getNearest(Variables.chickenManId)) && //Functions.isInArea(Variables.frontRoomArea) &&
				!Functions.canReach(Variables.depositBoxLoc))
		{
			return true;
		}
		
		if (!Inventory.isFull() &&
				!Functions.canReach(NPCs.getNearest(Variables.chickenManId)) &&
				Functions.closeEnough(NPCs.getNearest(Variables.chickenManId), 8))
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	public void execute()
	{
		final SceneObject outerDoor = SceneEntities.getNearest(Variables.outerDoorClosedId)/*new Filter<SceneObject>()
				{
			@Override
			public boolean accept(SceneObject s)
			{
				System.out.println("s.getId() == Variables.outerDoorClosedId + " + (s.getId() == Variables.outerDoorClosedId));
				System.out.println("Functions.isInArea thing + " + (Functions.isInArea(s, new Area(new Tile(3016, 3210, 0), new Tile(3008, 3202, 0)))));
				return s.getId() == Variables.outerDoorClosedId && Functions.isInArea(s, new Area(new Tile(3017, 3206, 0), new Tile(3016, 3206, 0)));
			}
				})*/;
		if (outerDoor != null)
		{
			if (!Utilities.isOnScreen(outerDoor))
			{
				Utilities.cameraTurnTo(outerDoor);
				if (!outerDoor.isOnScreen())
				{
					Camera.turnTo(outerDoor);
				}
			}
			if (Utilities.waitFor(new Condition()
			{
				public boolean validate()
				{
					return Utilities.isOnScreen(outerDoor);
				}
			}, 2000))
			{
				if (Utilities.interact(outerDoor, false, "Open") &&
						Utilities.waitFor(new Condition()
						{
							public boolean validate()
							{
								return Functions.getPlayer().getLocation() == new Tile(3012, 3204, 0) ||
								Functions.getPlayer().getLocation() == new Tile(3011, 3204, 0);
							}
						}, 1000))
				{
					Task.sleep(500);
				}
			}
		}
	}
}
