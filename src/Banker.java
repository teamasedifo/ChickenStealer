import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.DepositBox;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.node.SceneObject;


public class Banker extends Node
{
	
	@Override
	public boolean activate()
	{
		if (Inventory.isFull() && Functions.canReachAround(Variables.depositBoxLoc))
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	public void execute()
	{
		Walking.findPath(Variables.depositBoxLoc).traverse();
		
		if (Utilities.waitFor(new Condition()
		{
			
			@Override
			public boolean validate()
			{
				return SceneEntities.getNearest(Variables.depositBoxId) != null;
			}
			
		}, 1500))
		{
			final SceneObject depoBox = SceneEntities.getNearest(Variables.depositBoxId);
			
			if (depoBox != null && depoBox.validate())
			{
				if (!Utilities.isOnScreen(depoBox))
				{
					Utilities.cameraTurnTo(depoBox);
				}
				if (Utilities.waitFor(new Condition()
				{
					public boolean validate()
					{
						return Utilities.isOnScreen(depoBox);
					}
				}, 2000))
				{
					if (Utilities.interact(depoBox, false, "Deposit") && 
							Utilities.waitFor(new Condition()
							{
								public boolean validate() //wait until this returns true OR timeout completes
								{
									return DepositBox.isOpen() || DepositBox.open();
								}
							}, 3000))
					{
						if (Inventory.getCount() == 0 || !DepositBox.depositInventory())
						{
							while (!Utilities.waitFor(new Condition()
							{
								@Override
								public boolean validate()
								{
									return DepositBox.depositInventory() || Inventory.getCount() == 0;
								}
							}, 1000));
						}
						Task.sleep(750, 1250);
						DepositBox.close();
						
						Variables.turnScreenChicken = Random.nextInt(19, 26);
					}
				}
			}
		}
	}
}
