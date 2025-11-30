package MainPackage;

import FactoryPackage.*;
import ObserverPackage.*;
import TypesPackage.SoldierType;
import TypesPackage.TurretType;
import SoldierPackage.Soldier;

/**
 * MainPackage.Main game loop — our console playable prototype resembling Age of War.
 *
 * Mechanics:
 * - Two sides: player (left) and enemy (right).
 * - Each has ArmyManager storing Soldiers[] as UML requires.
 * - Factories spawn soldiers scaled by AgeState multipliers.
 * - Soldiers move each tick and attack when in range; turrets auto-fire.
 * - If a soldier reaches the enemy HQ, it deals its HP as damage and dies.
 * - Enemies / AI are spawned automatically, while player army requires input to spawn (1,2,3 for various soldier types; 4,5,6 for the various turrets)
 */
public class Main {
    public static final int BATTLEFIELD_LENGTH = 60;

    public static void main(String[] args) throws InterruptedException {
        // Create factories (both start in Stone Age)
        ArmyFactory playerFactory = new StoneAgeFactory(500);
        ArmyFactory enemyFactory  = new StoneAgeFactory(500);

        // Create managers (army size 10, 2 turret slots)
        PlayerArmyManager player = new PlayerArmyManager(playerFactory, playerFactory.getHealthArmyHQ(), 10, 2);
        EnemyArmyManager  enemy  = new EnemyArmyManager(enemyFactory, enemyFactory.getHealthArmyHQ(), 10, 2);

        // Place initial turrets
        player.placeTurret(playerFactory.createTurret(TurretType.BASIC, 4.0, +1), 0);
        enemy.placeTurret(enemyFactory.createTurret(TurretType.BASIC, BATTLEFIELD_LENGTH - 4.0, -1), 0);

        // Event subject and observers
        GameEvents events = new GameEvents();
        events.addObserver(player);
        events.addObserver(enemy);

        System.out.println("=== Age of War - Console Prototype ===");

        int tick = 0;
        int playerSpawnInterval = 6;
        int enemySpawnInterval = 7;

        // Run until base HP reaches 0 or tick limit
        while (player.getHP() > 0 && enemy.getHP() > 0 && tick < 2000) {
            tick++;

            // auto spawn infantry periodically
            if (tick % playerSpawnInterval == 0) {
                Soldier s = playerFactory.spawnSoldier(SoldierType.RANGED, 2.0, +1);
                s.setOwnerManagers(player, enemy);
                player.addSoldier(s);
            }
            if (tick % enemySpawnInterval == 0) {
                Soldier s2 = enemyFactory.spawnSoldier(SoldierType.INFANTRY, BATTLEFIELD_LENGTH - 2.0, -1);
                s2.setOwnerManagers(enemy, player);
                enemy.addSoldier(s2);
            }

            // update soldiers (movement and attack)
            player.updateAllSoldiers();
            enemy.updateAllSoldiers();

            // turrets auto-attack
            player.updateTurrets(enemy);
            enemy.updateTurrets(player);

            // check for soldiers at bases (collisions with HQ)
            for (Soldier s : enemy.spawnedPlayerSoldiers) {
                if (s != null && s.getHP() > 0 && s.getPosition() <= 1.0) {
                    player.reduceHP(s.getHP()); // soldier deals its HP as damage to base (simple)
                    s.takeDamage(9999); // kill soldier
                }
            }
            for (Soldier s : player.spawnedPlayerSoldiers) {
                if (s != null && s.getHP() > 0 && s.getPosition() >= BATTLEFIELD_LENGTH - 1.0) {
                    enemy.reduceHP(s.getHP());
                    s.takeDamage(9999);
                }
            }

            // render every 2 ticks
            if (tick % 2 == 0) renderConsole(player, playerFactory, enemy, enemyFactory, tick);

            Thread.sleep(200);
        }

        System.out.println("=== Game Over ===");
        System.out.println("Player's " + playerFactory.getArmyType() + " Base HP: " + player.getHP());
        System.out.println("Enemy Base HP:  " + enemy.getHP());
    }

    private static void renderConsole(PlayerArmyManager player, ArmyFactory playerFactory, EnemyArmyManager enemy, ArmyFactory enemyFactory, int tick) {
        System.out.println("\nTick: " + tick);
        System.out.printf("Player's %s Base HP: %d    Enemy %s Base HP: %d\n", playerFactory.getArmyType().getClass().getSimpleName(), player.getHP(), enemyFactory.getArmyType().getClass().getSimpleName(),enemy.getHP());

        char[] field = new char[BATTLEFIELD_LENGTH + 1];
        for (int i = 0; i < field.length; i++)
        {
            field[i] = '-';
        }

        // place turrets
        for (turret.Turret t : player.turrets) if (t != null) field[(int)Math.round(t.getPosition())] = 'T';
        for (turret.Turret t : enemy.turrets) if (t != null) field[(int)Math.round(t.getPosition())] = 't';

        // place soldiers
        for (soldier.Soldier s : player.spawnedPlayerSoldiers)
        {
            if (s != null && s.getHP() > 0)
            {
                if (s.getTypeName().equals("Infantry"))
                {
                    field[(int)Math.round(s.getPosition())] = 'I';
                }
                if (s.getTypeName().equals("Mounted"))
                {
                    field[(int)Math.round(s.getPosition())] = 'M';
                }
                if (s.getTypeName().equals("Ranged"))
                {
                    field[(int)Math.round(s.getPosition())] = 'R';
                }

                //field[(int)Math.round(s.getPosition())] = 'S';
            }

        }
        for (soldier.Soldier s : enemy.spawnedPlayerSoldiers) {
            if (s != null && s.getHP() > 0) field[(int)Math.round(s.getPosition())] = 'E';
        }

        System.out.println(new String(field));
    }
}