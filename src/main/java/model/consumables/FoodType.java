package model.consumables;

public enum FoodType {
    FRUIT (1) {

        @Override
        public void applyEffect(){return;}
    },
    CANDY (2) {
        @Override
        public void applyEffect(){return;}
    };

    private final int code;

    FoodType(int i) {
        this.code = i;
    }

    public int getCode() {
        return code;
    }

    // Static method to get an enum by its code
    public static FoodType getFoodByCode(int code) {
        for (FoodType myEnum : FoodType.values()) {
            if (myEnum.code == code) {
                return myEnum;
            }
        }
        throw new IllegalArgumentException("No enum constant with code: " + code);
    }

    public abstract void applyEffect();
}
