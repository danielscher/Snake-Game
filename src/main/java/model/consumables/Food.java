package model.consumables;


import java.util.List;

public abstract class Food {

    protected FoodType type;
    protected List<Effect> effects;

    public List<Effect> getEffects() {
        return effects;
    }

    public FoodType getType() {
        return type;
    }
}
