package com.example.cinema.model;

// the Food class encapsulates the essential properties of a Room item within the application,
// providing a structured way to manage and interact with Room item data.
public class Room {
    private int id;
    private String title;
    public boolean isSelected;

    public Room() {
    }

    public Room(int id, String title, boolean isSelected) {
        this.id = id;
        this.title = title;
        this.isSelected = isSelected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
