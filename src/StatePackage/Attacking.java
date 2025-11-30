package StatePackage;

import SoldierPackage.Soldier;

/**
 * AttackingState: the soldier attacks nearest enemy when in range.
 */
public class Attacking implements SoldierState {
    private static final Attacking instance = new Attacking();
    private Attacking() {}
    public static Attacking get() { return instance; }

    @Override public void enterState(Soldier s) { /* possible attack animation start */ }

    @Override
    public void executeState(Soldier s) {
        // Try to attack nearest; if nothing to attack, go back to idle.
        boolean attacked = s.attackNearest();
        if (!attacked) s.setState(Idle.get());
        if (s.getHP() <= 0) s.setState(Dead.get());
    }

    @Override public void exitState(Soldier s) { /* possible animation end */ }

    @Override public String getName() { return "Attacking"; }
}

