/*********************************************************************
 CSCI 322/522  			  Assignment 7               		 FA2020

 Class Name: ItemC.java

 Developer: Matthew Gedge
 Due Date: 6 November 2020

 Purpose: This java class creates the ItemC object with a constructor and
 the get/set classes for each data type in the object.
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
