/************************************************************************
 *                                                                      *
 * CSCI 322/522  			  Assignment 5               		 FA2020 *
 *                                                            		    *
 * 	Class Name: calorieInfo.java										*
 * 																		*
 *  Developer: Matthew Gedge											*
 *   Due Date: 9 October 2020							    			*
 *   																	*
 *    Purpose: This java class contains the calories and the calculator *
 *    to return the calories based on the selected inputs               *
 *																		*
 * *********************************************************************/

package z1818828.cs.niu.assignment6_radiobuttons;

public class CalorieInfo {
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

    /******************************************************
     * Constructor sets calories based on default selection
     ******************************************************/
    CalorieInfo() {
        pattyCalories = BEEF;
        cheeseCalories = 0;
        baconCalories = 0;
        specialSauceCalories = 0;
    }

    /******************************************************
     * calculateCalories adds the private calories together
     *      and returns the total
     ******************************************************/
    public static String calculateCalories() {
        int calories = 0;

        calories = pattyCalories + cheeseCalories + baconCalories + specialSauceCalories;

        return String.valueOf(calories);
    }

    /******************************************************
     * setPatty sets the private ingredient calories
     ******************************************************/
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

    /******************************************************
     * setCheese sets the private ingredient calories
     ******************************************************/
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

    /******************************************************
     * setBacon sets the private ingredient calories
     ******************************************************/
    public void setBacon(int baconSelect) {
        if(baconSelect == 0)
            baconCalories = 0;
        else
            baconCalories = BACON;
    }

    /******************************************************
     * setSpecialSauce sets the private ingredient calories
     ******************************************************/
    public void setSpecialSauce(int specialSauceSelect) {
        specialSauceCalories = (specialSauceSelect * SAUCE) / 10;
    }
}