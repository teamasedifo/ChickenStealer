import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.tab.Inventory;


public class WalkToChicken extends Node
{
	
	@Override
	public boolean activate()
	{
		return !Inventory.isFull() &&
		!Functions.isInArea(Variables.frontRoomArea) &&
		!Functions.isInArea(Variables.backRoomArea) &&
		!Functions.closeEnough(NPCs.getNearest(Variables.chickenManId), 10);
	}
	
	@Override
	public void execute()
	{
		System.out.println("walking to chicken");
		Walking.newTilePath(Variables.boxToChickenTiles).traverse();
		Task.sleep(1000, 1500);
	}
}
