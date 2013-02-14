


import java.awt.Dimension;
import java.awt.Point;
import java.awt.Polygon;

import org.powerbot.core.bot.Bot;
import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.node.Menu;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Entity;
import org.powerbot.game.api.wrappers.Locatable;
import org.powerbot.game.api.wrappers.interactive.Character;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

/**
 * 
 * Thanks to harrynoob
 *
 */

public class Utilities {

	/**
	 * Wait for condition.
	 *
	 * @param condition the c
	 * @param timeout the timeout
	 * @return true, if successful
	 */
	public static boolean waitFor(Condition condition, final long timeout) {
		final Timer t = new Timer(timeout);
		while (t.isRunning() && !condition.validate()) {
			Task.sleep(50);
		}
		return condition.validate();
	}

	/**
	 * Checks if entity is on screen.
	 *
	 * @param e The entity
	 * @return true, if e is on screen
	 */
	public static boolean isOnScreen(Entity e) {
		ensureActionBar(true);
		WidgetChild actionbar = Widgets.get(640, 6);
		return e != null && e.isOnScreen()
				&& (actionbar == null || !(actionbar != null
						&& actionbar.isOnScreen() && actionbar
						.getBoundingRectangle().contains(e.getCentralPoint())));
	}

	/**
	 * Enhanced check if entity is on screen.
	 *
	 * @param e The entity
	 * @return true, if e is on screen
	 */
	public static boolean isOnScreenEnhanced(org.powerbot.game.api.wrappers.interactive.Character e) {
		WidgetChild ab = Widgets.get(640, 6);
		if(ab == null || !ab.isOnScreen() || e.getModel().getTriangles().length == 0) return e.isOnScreen();
		for(Polygon p : e.getModel().getTriangles()) {
			for(int i = 0; i < p.npoints; i++) {
				Point a = new Point(p.xpoints[i], p.ypoints[i]);
				if(ab.contains(a) || !e.isOnScreen()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Ensure inventory tab is open.
	 */
	public static void ensureInventoryTab() {
		if (!Tabs.INVENTORY.isOpen())
			Tabs.INVENTORY.open();
	}

	/**
	 * Ensure action bar state equals flag.
	 *
	 * @param flag the flag
	 */
	public static void ensureActionBar(boolean flag) {
		/*if (flag) {
			WidgetChild actionbar = Widgets.get(640, 6);
			if (actionbar != null && !actionbar.isOnScreen()) {
				WidgetChild abToggle = Widgets.get(640, 28);
				if (abToggle != null) {
					abToggle.interact("Expand");
				}
			}
		} else {
			WidgetChild actionbar = Widgets.get(640, 6);
			if (actionbar != null && actionbar.isOnScreen()) {
				WidgetChild abToggle = Widgets.get(640, 30);
				if (abToggle != null) {
					abToggle.interact("Minimise");
				}
			}
		} */
		WidgetChild actionbar = Widgets.get(640, 6);
		if(flag == actionbar.isOnScreen()) return;
		if(flag ? !actionbar.isOnScreen() : actionbar.isOnScreen()) {
			WidgetChild toggle = Widgets.get(640, flag ? 28 : 30);
			if(toggle != null && toggle.isOnScreen()) {
				toggle.interact(flag ? "Expand" : "Minimise");
			}
		}
	}

	/**
	 * Camera rotation method.
	 *
	 * @param loc A locatable entity
	 */
	public static void cameraTurnTo(final Locatable loc) {
		Thread t = new Thread() {
			public void run() {
				Camera.turnTo(loc);
				if(!isOnScreen((Entity) loc))
					Camera.setPitch(false);
			}
		};
		t.start();
	}

	public static void cameraTurnToTemp(final Locatable loc) {
		Camera.turnTo(loc);
		if(!isOnScreenEnhanced((Character) loc)) {
			Camera.setPitch(false);
		}
	}

	public static boolean interact(final Entity e, final boolean hop, final String s) {
		if (e.validate()) {
			if (hop) {
				Mouse.hop((int) e.getCentralPoint().getX(), (int) e
						.getCentralPoint().getY());
			} else {
				Mouse.move(e.getCentralPoint());
				if (e instanceof org.powerbot.game.api.wrappers.interactive.Character
						&& ((org.powerbot.game.api.wrappers.interactive.Character) e)
								.isMoving()) {
					Mouse.hop((int) e.getCentralPoint().getX(), (int) e
							.getCentralPoint().getY());
				}
			}
			Mouse.click(false);
			if(Menu.isOpen() && Menu.select(s)) {
				Dimension d = Bot.getInstance().getCanvas().getSize();
				return Mouse.move(Random.nextInt(10, d.width - 10), Random.nextInt(10, d.width - 10));
			}
		}
		return false;
	}

}
