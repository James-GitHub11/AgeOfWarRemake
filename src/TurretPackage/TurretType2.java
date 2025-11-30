package TurretPackage;

import ObserverPackage.ArmyManager;
import SoldierPackage.Soldier;
import StatePackage.AgeState;

/**
 * Heavy turret: high damage, slower firing (cooldown).
 */
public class TurretType2 implements Turret {
    private final AgeState age;
    private final double position;
    private final int ownerDir;
    private final int damage;
    private final double range = 7.0;
    private int cooldown = 0;

    public TurretType2(AgeState age, double position, int ownerDir) {
        this.age = age;
        this.position = position;
        this.ownerDir = ownerDir;
        this.damage = 35 * age.getAgeStatMulti();
    }

    @Override public int getDamage() { return damage; }
    @Override public double getPosition() { return position; }
    @Override public int getOwnerDirection() { return ownerDir; }
    @Override public String getTypeName() { return "HeavyTurret"; }
    @Override public AgeState getAge() { return age; }

    @Override
    public void update(ArmyManager enemyManager) {
        if (cooldown > 0) { cooldown--; return; }
        Soldier target = enemyManager.findNearestAlive(position, ownerDir * -1);
        if (target == null) return;
        double dist = Math.abs(target.getPosition() - position);
        if (dist <= range) {
            target.takeDamage(damage);
            cooldown = 3; // 3 ticks cooldown
        }
    }
}
