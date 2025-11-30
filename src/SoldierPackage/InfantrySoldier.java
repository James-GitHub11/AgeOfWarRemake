package SoldierPackage;

import StatePackage.Idle;
/**
 * InfantrySoldier: concrete soldier type per UML.
 * ageMultiplier is applied by the factory (via AgeState.getStatMultiplier()).
 */
public class InfantrySoldier extends Soldier {

    public InfantrySoldier(int ageMultiplier, double startPos, int direction) {
        super(100 * ageMultiplier, 15 * ageMultiplier, 0.8 * ageMultiplier, startPos, direction);
        setState(Idle.get());
    }

    @Override
    public String getTypeName() { return "Infantry"; }
}

