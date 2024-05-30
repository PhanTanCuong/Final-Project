package com.example.cinema.model;

import java.io.Serializable;

// Category class encapsulates the essential properties of a category within the application,
// providing a structured way to manage and interact with category data.
public class Category implements Serializable {
    private long id;
    private String name;
    private String image;

    public Category() {
    }

    public Category(long id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
