package StatePackage;

import SoldierPackage.Soldier;

/**
 * DeadState: no operations, once a soldier is dead they stay dead.
 */
public class Dead implements SoldierState {
    private static final Dead instance = new Dead();
    private Dead() {}
    public static Dead get() { return instance; }

    @Override public void enterState(Soldier s) { /* play death logic */ }
    @Override public void executeState(Soldier s) { /* nothing: dead */ }
    @Override public void exitState(Soldier s) { /* nothing */ }
    @Override public String getName() { return "Dead"; }
}

