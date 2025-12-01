package FactoryPackage;

import SoldierPackage.InfantrySoldier;
import SoldierPackage.MountedSoldier;
import SoldierPackage.RangedSoldier;
import SoldierPackage.Soldier;
import StatePackage.FutureState;

/**
 * Concrete factory for Future Age.
 */
public class FutureFactory extends ArmyFactory {
    public FutureFactory(int healthArmyHQ) {
        super(healthArmyHQ, new FutureState());
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
