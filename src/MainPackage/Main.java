package MainPackage;

import FactoryPackage.ArmyFactory;
import FactoryPackage.StoneAgeFactory;
import ObserverPackage.EnemyArmyManager;
import ObserverPackage.GameEvents;
import ObserverPackage.PlayerArmyManager;
import SoldierPackage.Soldier;
import TypesPackage.SoldierType;
import TypesPackage.TurretType;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

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

    //Volatile means we are allowed to access and change this variable from another thread
    volatile static Integer playerInput = null; // will be used in the separate thread to track player input and spawn soldiers in the main game loop
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





        // Thread that waits for player input without blocking the game loop (otherwise, the nextInt() will continue to wait for the enter button)
        new Thread(() ->
        {
            Scanner scanner = new Scanner(System.in);
            while (true)
            {
                try
                {
                    int val = scanner.nextInt();   // this blocks, but it’s OK in this thread
                    playerInput = val;        // stores the player input for main thread to use (so we can spawn soldiers)
                }
                catch (Exception e)
                {
                    scanner.nextLine(); // clear invalid input
                }
            }
        }).start(); //starts the thread that allows us to register the player's input


        // Run until base HP reaches 0 or tick limit
        while (player.getHP() > 0 && enemy.getHP() > 0 && tick < 2000) //player and enemy variable is referring to the actual manager / observer
        {
            tick++;
            long lastSpawnTime = 0;
            // auto spawn infantry periodically


            //enemy spawn system (automatic & randomized)
            if (tick % enemySpawnInterval == 0)
            {
                SoldierType randomType = null;
                int random = ThreadLocalRandom.current().nextInt(1,4); //gives us a random int between 1 and 4 (4 is the exclusive bounds --> so 1,2 or 3)
                switch(random)
                {
                    case 1:
                    {
                        randomType = SoldierType.INFANTRY;
                        break;
                    }
                    case 2:
                    {
                        randomType = SoldierType.RANGED;
                        break;
                    }
                    case 3:
                    {
                        randomType = SoldierType.MOUNTED;
                        break;
                    }
                    default:
                    {
                        randomType = null;
                    }
                }
                if (randomType != null)
                {
                    Soldier s2 = enemyFactory.spawnSoldier(randomType, BATTLEFIELD_LENGTH - 2.0, -1);
                    s2.setOwnerManagers(enemy, player);
                    enemy.addSoldier(s2);
                }

            }
            if (playerInput != null)
            {
                int selection = playerInput;
                playerInput = null;
                SoldierType selectedType = null;
                switch(selection)
                {
                    case 1:
                    {
                        selectedType = SoldierType.INFANTRY;
                        break;
                    }
                    case 2:
                    {
                        selectedType = SoldierType.RANGED;
                        break;
                    }
                    case 3:
                    {
                        selectedType = SoldierType.MOUNTED;
                        break;
                    }
                }
                //ensure that the player's selected type is not null and it has actually been selected before spawning it (if no input, the game loop continues to tick without spawning any player)
                if (selectedType != null)
                {
                    Soldier s = playerFactory.spawnSoldier(selectedType, 2.0, +1);
                    s.setOwnerManagers(player, enemy);
                    player.addSoldier(s);
                }
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
                    s.takeDamage(9999); // for now, our logic is set to kill off the soldier after he reaches the base (applying 9999 damage to ensure all types die)
                }
            }
            for (Soldier s : player.spawnedPlayerSoldiers) {
                if (s != null && s.getHP() > 0 && s.getPosition() >= BATTLEFIELD_LENGTH - 1.0) {
                    enemy.reduceHP(s.getHP());
                    s.takeDamage(9999);
                }
            }

            // render every 2 ticks
            if (tick % 2 == 0)
            {
                renderConsole(player, playerFactory, enemy, enemyFactory, tick);
            }

            Thread.sleep(400);
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
        for (TurretPackage.Turret t : player.turrets) if (t != null) field[(int)Math.round(t.getPosition())] = 'T';
        for (TurretPackage.Turret t : enemy.turrets) if (t != null) field[(int)Math.round(t.getPosition())] = 't';

        // place soldiers (NOTE: Players = capitalized letters ---> Enemies = lower-case)
        for (SoldierPackage.Soldier s : player.spawnedPlayerSoldiers)
        {
            if (s != null && s.getHP() > 0)
            {
                if (s.getTypeName().equals("Infantry"))
                {
                    field[(int)Math.round(s.getPosition())] = 'I'; //I for infantry, M for mounted and R for Ranged allows us to visually differentiate our soldiers in console.
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
        for (SoldierPackage.Soldier s : enemy.spawnedPlayerSoldiers) {
            if (s != null && s.getHP() > 0)
            {
                if (s.getTypeName().equals("Infantry"))
                {
                    field[(int)Math.round(s.getPosition())] = 'i';
                }
                if (s.getTypeName().equals("Mounted"))
                {
                    field[(int)Math.round(s.getPosition())] = 'r';
                }
                if (s.getTypeName().equals("Ranged"))
                {
                    field[(int)Math.round(s.getPosition())] = 'm';
                }
                //field[(int)Math.round(s.getPosition())] = 'E';
            }
        }

        System.out.println(new String(field));
    }
}