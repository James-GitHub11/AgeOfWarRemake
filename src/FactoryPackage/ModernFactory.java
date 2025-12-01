package FactoryPackage;

import SoldierPackage.InfantrySoldier;
import SoldierPackage.MountedSoldier;
import SoldierPackage.RangedSoldier;
import SoldierPackage.Soldier;
import StatePackage.ModernState;

/**
 * Concrete factory for Modern Age.
 */
public class ModernFactory extends ArmyFactory {
    public ModernFactory(int healthArmyHQ) {
        super(healthArmyHQ, new ModernState());
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
