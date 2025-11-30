package SoldierPackage;

import StatePackage.Idle;

/**
 * MountedSoldier: fast and durable cavalry unit.
 */
public class MountedSoldier extends Soldier {

    public MountedSoldier(int ageMultiplier, double startPos, int direction) {
        super(150 * ageMultiplier, 20 * ageMultiplier, 1.6 * ageMultiplier, startPos, direction);
        setState(Idle.get());
    }

    @Override
    public String getTypeName() { return "Mounted"; }
}

