package z1818828.cs.niu.assignment6_radiobuttons;

public class calorieInfo {
    //Define calorie constants
    private static final int BEEF = 204;
    private static final int TURKEY = 193;
    private static final int VEGGIE = 124;
    private static final int BACON = 132;
    private static final int CHEDDAR = 113;
    private static final int MOZZARELLA = 70;
    private static final int SAUCE = 135;

    //Variables on screen
    private static int pattyCalories;
    private static int cheeseCalories;
    private static int baconCalories;
    private static int specialSauceCalories;

    calorieInfo() {
        pattyCalories = BEEF;
        cheeseCalories = 0;
        baconCalories = 0;
        specialSauceCalories = 0;
    }

    //Calculate calories by adding them together
    public static String calculateCalories() {
        int calories = 0;

        calories = pattyCalories + cheeseCalories + baconCalories + specialSauceCalories;

        return String.valueOf(calories);
    }

    //set the respective values for each ingredient
    public void setPatty(int pattySelect) {
        switch(pattySelect) {
            case 1: //turkey
                pattyCalories = TURKEY;
                break;
            case 2: //veggie
                pattyCalories = VEGGIE;
                break;
            default: //beef patty
                pattyCalories = BEEF;
                break;
        }
    }

    public void setCheese(int cheeseSelect) {
        switch(cheeseSelect) {
            case 1: //cheddar
                cheeseCalories = CHEDDAR;
                break;
            case 2: //Mozz
                cheeseCalories = MOZZARELLA;
                break;
            default: //no cheese
                cheeseCalories = 0;
                break;
        }
    }

    public void setBacon(int baconSelect) {
        if(baconSelect == 0)
            baconCalories = 0;
        else
            baconCalories = BACON;
    }

    public void setSpecialSauce(int specialSauceSelect) {
        specialSauceCalories = (specialSauceSelect * SAUCE) / 10;
    }
}
