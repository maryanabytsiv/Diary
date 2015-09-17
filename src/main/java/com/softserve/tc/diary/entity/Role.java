package com.softserve.tc.diary.entity;

public class Role {
    private String id;
    private String name;
    
    public Role() {
        // TODO Auto-generated constructor stub
    }
    
    public Role(String name) {
        super();
        this.name = name;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return "Role [id=" + id + ", name=" + name + "]";
    }
    
}
