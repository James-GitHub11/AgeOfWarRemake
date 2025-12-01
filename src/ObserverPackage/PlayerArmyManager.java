package ObserverPackage;

import TypesPackage.SoldierType;

/**
 * PlayerArmyManager concrete observer. Reacts to GameEvents (e.g., spawn requests).
 */
public class PlayerArmyManager extends ArmyManager {
    public PlayerArmyManager(FactoryPackage.ArmyFactory factory, int startingHP, int armySize, int turretSlots) {
        super(factory, startingHP, armySize, turretSlots);
    }

    @Override
    public void update(String eventMessage) {
        // Example messages: "SPAWN_INFANTRY_PLAYER", "ENEMY_ATTACK"
        if ("SPAWN_INFANTRY_PLAYER".equals(eventMessage)) {
            SoldierPackage.Soldier s = factory.spawnSoldier(SoldierType.INFANTRY, 2.0, +1);
            s.setOwnerManagers(this, null); // main.Main will set enemy later if needed
            addSoldier(s);
        } else if ("ENEMY_ATTACK".equals(eventMessage)) {
            reduceHP(8);
        } else if (eventMessage.startsWith("SPAWN_")) {
            // flexible spawn naming: SPAWN_INfantry_PLAYER etc.
            if (eventMessage.contains("INFANTRY")) {
                SoldierPackage.Soldier s = factory.spawnSoldier(SoldierType.INFANTRY, 2.0, +1);
                s.setOwnerManagers(this, null);
                addSoldier(s);
            } else if (eventMessage.contains("RANGED")) {
                SoldierPackage.Soldier s = factory.spawnSoldier(SoldierType.RANGED, 2.0, +1);
                s.setOwnerManagers(this, null);
                addSoldier(s);
            } else if (eventMessage.contains("MOUNTED")) {
                SoldierPackage.Soldier s = factory.spawnSoldier(SoldierType.MOUNTED, 2.0, +1);
                s.setOwnerManagers(this, null);
                addSoldier(s);
            }
        }
    }
}

