package observer;

import util.SoldierType;
import main.Main;

/**
 * EnemyArmyManager concrete observer. Simple AI that listens to GameEvents.
 */
public class EnemyArmyManager extends ArmyManager {
    public EnemyArmyManager(factory.ArmyFactory factory, int startingHP, int armySize, int turretSlots) {
        super(factory, startingHP, armySize, turretSlots);
    }

    @Override
    public void update(String eventMessage) {
        if ("SPAWN_INFANTRY_ENEMY".equals(eventMessage)) {
            soldier.Soldier s = factory.spawnSoldier(SoldierType.INFANTRY, Main.BATTLEFIELD_LENGTH - 2.0, -1);
            s.setOwnerManagers(this, null);
            addSoldier(s);
        } else if ("PLAYER_ATTACK".equals(eventMessage)) {
            reduceHP(10);
        } else if (eventMessage.startsWith("SPAWN_")) {
            // spawn for enemy if message indicates
            if (eventMessage.contains("INFANTRY") && eventMessage.contains("ENEMY")) {
                soldier.Soldier s = factory.spawnSoldier(SoldierType.INFANTRY, Main.BATTLEFIELD_LENGTH - 2.0, -1);
                s.setOwnerManagers(this, null);
                addSoldier(s);
            }
        }
    }
}
