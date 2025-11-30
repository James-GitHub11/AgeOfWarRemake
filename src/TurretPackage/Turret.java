package TurretPackage;

import ObserverPackage.ArmyManager;
import StatePackage.AgeState;

/**
 * Turret interface. Concrete turrets implement update(enemyManager) to attack.
 */
public interface Turret {
    int getDamage();
    double getPosition();
    int getOwnerDirection(); // +1 player, -1 enemy
    void update(ArmyManager enemyManager);
    String getTypeName();
    AgeState getAge();
}
