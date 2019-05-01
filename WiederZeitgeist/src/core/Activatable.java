package core;

import java.util.List;

/**
 *
 * @author Kosmic
 */
public interface Activatable {

    /**
     * Runs the activation script.
     */
    public void activate();

    /**
     * Runs the deactivation script.
     */
    public void deactivate();

    /**
     * Runs r in between activating and deactivating the Activatables.
     * @param r The runnable to run.
     * @param as The Activatables to activate and deactivate to run r.
     */
    public static void using(Runnable r, Activatable... as) {
        for (Activatable a : as) {
            a.activate();
        }
        r.run();
        for (Activatable a : as) {
            a.deactivate();
        }
    }

    /**
     * Runs r in between activating and deactivating the Activatables.
     * @param r The runnable to run.
     * @param as The Activatables to activate and deactivate to run r.
     */
    public static void using(Runnable r, List<Activatable> as) {
        as.forEach(Activatable::activate);
        r.run();
        as.forEach(Activatable::deactivate);
    }
}
