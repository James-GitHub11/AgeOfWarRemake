package SoldierPackage;

//import main.Main;
import StatePackage.Dead;
import StatePackage.Idle;
import StatePackage.SoldierState;

/**
 * Abstract Soldier class per your UML.
 * Contains HP, damage, speed, position, state, and helpers for movement & attack.
 */
public abstract class Soldier {
    protected int hp;
    protected int damage;
    public double speed;
    protected double position; // 0 .. BATTLEFIELD_LENGTH
    protected int direction;   // +1 moves right (player), -1 moves left (enemy)
    protected SoldierState state;

    // Managers that hold soldiers (owner and enemy) - set by manager when spawned
    protected observer.ArmyManager ownerManager;
    protected observer.ArmyManager enemyManager;

    protected Soldier(int baseHp, int baseDamage, double speed, double startPos, int direction) {
        this.hp = baseHp;
        this.damage = baseDamage;
        this.speed = speed;
        this.position = startPos;
        this.direction = direction;
        this.state = Idle.get();
    }

    // Called each game tick
    public void update() {
        if (state != null) state.executeState(this);
    }

    // Move forward based on speed and direction
    public void moveForward() {
        if (state == Dead.get()) return;
        position += speed * direction;
        // clamp to battlefield limits
        if (position < 0) position = 0;
        if (position > MainPackage.BATTLEFIELD_LENGTH) position = MainPackage.BATTLEFIELD_LENGTH;
    }

    // Finds nearest enemy in range and attacks if close enough.
    // Default melee reach; subclasses (Ranged) may override attackNearest.
    public boolean attackNearest() {
        if (enemyManager == null) return false;
        Soldier target = enemyManager.findNearestAlive(position, -direction);
        if (target == null) return false;
        double dist = Math.abs(target.position - this.position);
        if (dist <= 1.5) { // melee reach
            target.takeDamage(this.damage);
            return true;
        }
        return false;
    }

    public void takeDamage(int d) {
        hp -= d;
        if (hp <= 0) {
            hp = 0;
            setState(Dead.get());
        }
    }

    // Utility to check if any enemy is inside the soldier's perception/attack range
    public boolean findEnemyInRange() {
        if (enemyManager == null) return false;
        Soldier t = enemyManager.findNearestAlive(position, -direction);
        if (t == null) return false;
        double dist = Math.abs(t.position - this.position);
        // default perception: melee sees 1.5, ranged overrides
        return dist <= 1.5;
    }

    public int getHP() { return hp; }
    public double getPosition() { return position; }
    public String getStateName() { return state == null ? "null" : state.getName(); }

    public void setState(SoldierState newState) {
        if (this.state != null) this.state.exitState(this);
        this.state = newState;
        if (this.state != null) this.state.enter(this);
    }

    public void setOwnerManagers(observer.ArmyManager owner, observer.ArmyManager enemy) {
        this.ownerManager = owner;
        this.enemyManager = enemy;
    }

    // Subclasses must implement a type name
    public abstract String getTypeName();
}
