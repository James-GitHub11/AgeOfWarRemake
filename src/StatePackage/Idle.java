package StatePackage;

import SoldierPackage.Soldier;
/**
 * IdleState: soldier moves forward and checks for enemies to switch to Attacking.
 * Implemented as a singleton because states often don't need per-instance data.
 */
public class Idle implements SoldierState {
    private static final Idle instance = new Idle();
    private Idle() {}
    public static Idle get() { return instance; }

    @Override public void enterState(Soldier s) { /* nothing special */ }

    @Override
    public void executeState(Soldier s) {
        // Move forward each tick
        s.moveForward();
        // If enemy in range, switch to attacking
        if (s.findEnemyInRange()) s.setState(Attacking.get());
    }

    @Override public void exitState(Soldier s) { /* nothing special */ }

    @Override public String getName() { return "Idle"; }
}

