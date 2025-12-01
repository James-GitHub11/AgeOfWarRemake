package ObserverPackage;

import SoldierPackage.Soldier;

/**
 * Abstract ArmyManager per UML:
 * - Soldiers[] spawnedPlayerSoldiers
 * - hp : int
 * - factory : ArmyFactory
 *
 * Note: uses arrays (not lists) to match UML exactly.
 */
public abstract class ArmyManager implements Observer {
    public Soldier[] spawnedPlayerSoldiers; // UML: Soldiers[] spawnedPlayerSoldiers
    protected int hp;
    protected FactoryPackage.ArmyFactory factory;
    public TurretPackage.Turret[] turrets;

    public ArmyManager(FactoryPackage.ArmyFactory factory, int startingHP, int armySize, int turretSlots) {
        this.factory = factory;
        this.hp = startingHP;
        this.spawnedPlayerSoldiers = new Soldier[armySize];
        this.turrets = new TurretPackage.Turret[turretSlots];
    }

    // Place soldier in first available slot
    public boolean addSoldier(Soldier s) {
        for (int i = 0; i < spawnedPlayerSoldiers.length; i++) {
            if (spawnedPlayerSoldiers[i] == null || spawnedPlayerSoldiers[i].getHP() <= 0) {
                spawnedPlayerSoldiers[i] = s;
                return true;
            }
        }
        return false;
    }

    // Find nearest alive soldier to a given position. directionHint unused in this simple impl.
    public Soldier findNearestAlive(double position, int directionHint) {
        Soldier best = null;
        double bestDist = Double.MAX_VALUE;
        for (Soldier s : spawnedPlayerSoldiers) {
            if (s == null) continue;
            if (s.getHP() <= 0) continue;
            double d = Math.abs(s.getPosition() - position);
            if (d < bestDist) { bestDist = d; best = s; }
        }
        return best;
    }

    public void placeTurret(TurretPackage.Turret t, int slot) {
        if (slot >= 0 && slot < turrets.length) turrets[slot] = t;
    }

    // Tick: update all soldiers (movement and attack). Remove dead soldiers as nulls.
    public void updateAllSoldiers() {
        for (int i = 0; i < spawnedPlayerSoldiers.length; i++) {
            Soldier s = spawnedPlayerSoldiers[i];
            if (s != null) {
                s.update();
                if (s.getHP() <= 0) spawnedPlayerSoldiers[i] = null;
            }
        }
    }

    // Tick: update turrets (target enemy manager)
    public void updateTurrets(ArmyManager enemyManager) {
        for (TurretPackage.Turret t : turrets) {
            if (t != null) t.update(enemyManager);
        }
    }

    public int getHP() { return hp; }
    public void reduceHP(int d) { hp -= d; if (hp < 0) hp = 0; }

    @Override public abstract void update(String eventMessage);
}
