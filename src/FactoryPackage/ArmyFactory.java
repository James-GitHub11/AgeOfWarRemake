package FactoryPackage;

import TypesPackage.TurretType;
import TypesPackage.SoldierType;
import SoldierPackage.Soldier;
import StatePackage.AgeState;
import TurretPackage.Turret;

/**
 * Abstract ArmyFactory per UML:
 * - attributes: healthArmyHQ : int, armyType : AgeState
 * - abstract: spawnSoldier(SoldierType,...)
 * - concrete: createTurret(...)
 */
public abstract class ArmyFactory {
    protected int healthArmyHQ;
    protected AgeState armyType;

    public ArmyFactory(int healthArmyHQ, AgeState armyType) {
        this.healthArmyHQ = healthArmyHQ;
        this.armyType = armyType;
    }

    // Concrete factories implement this to return concrete Soldiers.
    public abstract Soldier spawnSoldier(TypesPackage.SoldierType type, double spawnPos, int direction);

    // Default turret creation (can be overridden by concrete factories if needed)
    public Turret createTurret(TypesPackage.TurretType type, double pos, int ownerDir) {
        switch (type) {
            case BASIC: return new TurretPackage.TurretType1(armyType, pos, ownerDir);
            case HEAVY: return new TurretPackage.TurretType2(armyType, pos, ownerDir);
            case MAGIC: return new TurretPackage.TurretType3(armyType, pos, ownerDir);
            default: throw new IllegalArgumentException("Unknown turret type: " + type);
        }
    }

    public int getHealthArmyHQ() { return healthArmyHQ; }
    public AgeState getArmyType() { return armyType; }
}
