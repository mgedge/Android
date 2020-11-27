/************************************************************************
 *                                                                      *
 * CSCI 322/522  			  Assignment 8               		 FA2020 *
 *                                                            		    *
 * 	Class Name: Note.java							            		*
 * 																		*
 *  Developer: Matthew Gedge											*
 *   Due Date: 4 December 2020							    			*
 *   																	*
 *    Purpose: This java class is the note object. It contains the      *
 *    the constructors and gets/sets for its data types                 *
 *																		*
 * *********************************************************************/
package edu.csi.niu.z1818828.assignment8_sqlite;

public class Note {
    private String id;
    private String title;
    private String note;


    //this variable is used to determine if it is selected in the recycler view
    private boolean selected = false;
    private boolean checked = false;

    public Note(String id, String title, String note, Boolean checked) {
        this.id = id;
        this.title = title;
        this.note = note;
        this.checked = checked;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
