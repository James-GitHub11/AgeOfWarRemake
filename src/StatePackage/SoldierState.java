package StatePackage;

import SoldierPackage.Soldier;
/**
 * SoldierState interface (State pattern for soldier behavior).
 * Each soldier holds a SoldierState and delegates per-tick logic to it.
 */
public interface SoldierState {
    void enterState(Soldier s);
    void executeState(Soldier s);
    void exitState(Soldier s);
    String getName();
}
