/*********************************************************************
 CSCI 322/522  			  Assignment 7               		 FA2020

 Class Name: MainActivity.java

 Developer: Matthew Gedge
 Due Date: 11 September 2020

 Purpose: This java class starts the activity and runs the UI.
 When the calculate button is pressed, the inputs are checked for
 validity and the discriminate and x values are calculated.
 When the clear button is pressed, inputs and outputs are emptied.
 *********************************************************************/

package z1818828.cs.niu.assignment7_intents;

public class ItemC {
    private String title;
    private String description;
    private int image;

    public ItemC(String title, String description, int image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImage() {
        return image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
