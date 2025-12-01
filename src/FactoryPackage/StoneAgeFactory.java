package FactoryPackage;

import SoldierPackage.*;
import StatePackage.StoneAgeState;

/**
 * Concrete factory for Stone Age.
 */
public class StoneAgeFactory extends ArmyFactory {
    public StoneAgeFactory(int healthArmyHQ) {
        super(healthArmyHQ, new StoneAgeState());
    }

    @Override
    public Soldier spawnSoldier(TypesPackage.SoldierType type, double spawnPos, int direction) {
        int m = armyType.getAgeStatMulti();
        switch (type) {
            case INFANTRY: return new InfantrySoldier(m, spawnPos, direction);
            case RANGED:   return new RangedSoldier(m, spawnPos, direction);
            case MOUNTED:  return new MountedSoldier(m, spawnPos, direction);
            default: throw new IllegalArgumentException("Unknown SoldierType: " + type);
        }
    }
}
