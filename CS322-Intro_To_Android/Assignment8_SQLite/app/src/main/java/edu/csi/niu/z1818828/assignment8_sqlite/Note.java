package edu.csi.niu.z1818828.assignment8_sqlite;

public class Note {
    private String id;
    private String title;
    private String note;
    private boolean selected = false;

    public Note() {
        id = null;
        title = null;
        note = null;
        selected = false;
    }

    public Note(String id, String title, String note) {
        this.id = id;
        this.title = title;
        this.note = note;
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
}
