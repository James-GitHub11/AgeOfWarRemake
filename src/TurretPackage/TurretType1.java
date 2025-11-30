package TurretPackage;

import ObserverPackage.ArmyManager;
import SoldierPackage.Soldier;
import StatePackage.AgeState;

/**
 * Basic turret: moderate damage, moderate range.
 */
public class TurretType1 implements Turret {
    private final AgeState age;
    private final double position;
    private final int ownerDir;
    private final int damage;
    private final double range = 6.0;

    public TurretType1(AgeState age, double position, int ownerDir) {
        this.age = age;
        this.position = position;
        this.ownerDir = ownerDir;
        this.damage = 20 * age.getAgeStatMulti();
    }

    @Override public int getDamage() { return damage; }
    @Override public double getPosition() { return position; }
    @Override public int getOwnerDirection() { return ownerDir; }
    @Override public String getTypeName() { return "BasicTurret"; }
    @Override public AgeState getAge() { return age; }

    @Override
    public void update(ArmyManager enemyManager) {
        Soldier target = enemyManager.findNearestAlive(position, ownerDir * -1);
        if (target == null) return;
        double dist = Math.abs(target.getPosition() - position);
        if (dist <= range) target.takeDamage(damage);
    }
}
