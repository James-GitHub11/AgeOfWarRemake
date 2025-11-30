package TurretPackage;

import ObserverPackage.ArmyManager;
import SoldierPackage.Soldier;
import StatePackage.AgeState;

/**
 * Magic turret: medium damage and slow effect.
 */
public class TurretType3 implements Turret {
    private final AgeState age;
    private final double position;
    private final int ownerDir;
    private final int damage;
    private final double range = 5.5;

    public TurretType3(AgeState age, double position, int ownerDir) {
        this.age = age;
        this.position = position;
        this.ownerDir = ownerDir;
        this.damage = 25 * age.getStatMultiplier();
    }

    @Override public int getDamage() { return damage; }
    @Override public double getPosition() { return position; }
    @Override public int getOwnerDirection() { return ownerDir; }
    @Override public String getTypeName() { return "MagicTurret"; }
    @Override public AgeState getAge() { return age; }

    @Override
    public void update(ArmyManager enemyManager) {
        Soldier target = enemyManager.findNearestAlive(position, ownerDir * -1);
        if (target == null) return;
        double dist = Math.abs(target.getPosition() - position);
        if (dist <= range) {
            target.takeDamage(damage);
            // apply a simple slow (reduces speed slightly)
            target.speed = Math.max(0.2, target.speed * 0.8);
        }
    }
}
