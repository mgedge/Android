package z1818828.cs.niu.assignment6_radiobuttons;

public class caloreInfo {
    private int pattySelect;
    private int cheeseSelect;
    private int baconSelect;
    private int specialSauceSelect;

    private static int pattyCalories;
    private static int cheeseCalories;
    private static int baconCalories;
    private static int specialSauceCalories;

    caloreInfo() {
        pattySelect = 0;
        cheeseSelect = 0;
        baconSelect = 0;
        specialSauceSelect = 0;
    }

    //Pass the selections into the object. i.e beef(0), cheddar(1), bacon(0), special(10)
    caloreInfo(int pattySelect, int cheeseSelect, int baconSelect, int specialSauceSelect) {
        this.pattySelect = pattySelect;
        this.cheeseSelect = cheeseSelect;
        this.baconSelect = baconSelect;
        this.specialSauceSelect = specialSauceSelect;

        setPatty(pattySelect);
        setCheese(cheeseSelect);
        setBacon(baconSelect);
        setSpecialSauce(specialSauceSelect);
    }

    public static String calculateCalories() {
        int calories = 0;

        calories += pattyCalories += cheeseCalories += baconCalories += specialSauceCalories;

        return String.valueOf(calories);
    }

    //return the respective values for each ingredient
    private int getPatty(int pattySelect) {
        return pattyCalories;
    }

    private int getCheese(int cheeseSelect) {
        return cheeseCalories;
    }

    private int getBacon(int baconSelect) {
        return baconCalories;
    }

    private int getSpecialSauce(int specialSauceSelect) {
        return specialSauceCalories;
    }

    //set the respective values for each ingredient
    public void setPatty(int pattySelect) {
        switch(pattySelect) {
            case 1: //turkey
                pattyCalories = 193;
                break;
            case 2: //veggie
                pattyCalories = 124;
                break;
            default: //beef patty
                pattyCalories = 204;
                break;
        }
    }

    public void setCheese(int cheeseSelect) {
        switch(cheeseSelect) {
            case 1: //cheddar
                cheeseCalories = 113;
                break;
            case 2: //Mozz
                cheeseCalories = 78;
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
            baconCalories = 132;
    }

    public void setSpecialSauce(int specialSauceSelect) {
        specialSauceCalories = specialSauceSelect;
    }
}
