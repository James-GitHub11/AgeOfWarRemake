package SoldierPackage;

import StatePackage.Idle;

/**
 * RangedSoldier: greater attack range (overrides attackNearest).
 */
public class RangedSoldier extends Soldier {

    public RangedSoldier(int ageMultiplier, double startPos, int direction) {
        super(80 * ageMultiplier, 12 * ageMultiplier, 0.9 * ageMultiplier, startPos, direction);
        setState(Idle.get());
    }

    @Override
    public boolean attackNearest() {
        if (enemyManager == null) return false;
        Soldier target = enemyManager.findNearestAlive(this.position, -direction);
        if (target == null) return false;
        double dist = Math.abs(target.position - this.position);
        if (dist <= 5.0) { // ranged reach
            target.takeDamage(this.damage);
            return true;
        }
        return false;
    }

    @Override
    public String getTypeName() { return "Ranged"; }
}

