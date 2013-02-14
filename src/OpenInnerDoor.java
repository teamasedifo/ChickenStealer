import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

public class OpenInnerDoor extends Node
{
	
	@Override
	public boolean activate()
	{
		if (Inventory.isFull() &&
				Functions.isInArea(Variables.backRoomArea))
		{
			return true;
		}
		if (!Inventory.isFull() &&
				Functions.canReach(NPCs.getNearest(Variables.chickenManId)))//Functions.isInArea(Variables.frontRoomArea))
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	public void execute()
	{
		final SceneObject innerdoor = SceneEntities.getNearest(Variables.innerDoorId);
		
		if (!Utilities.isOnScreen(innerdoor) || !innerdoor.isOnScreen())
		{
			Utilities.cameraTurnTo(innerdoor);
		}
		if (Utilities.waitFor(new Condition()
		{
			public boolean validate()
			{
				return Utilities.isOnScreen(innerdoor);
			}
		}, 2000))
		{
			if (Utilities.interact(innerdoor, false, "Open") &&
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
				Camera.setPitch(Camera.getPitch() + Random.nextInt(20, 30));
				Camera.setAngle(Camera.getYaw() + Random.nextInt(-7, 9));
			}
		}
	}
}
