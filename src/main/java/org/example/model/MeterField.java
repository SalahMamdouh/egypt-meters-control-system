package org.example.model;

import gurux.dlms.enums.ObjectType;

public class MeterField {

    private final String id;
    private final String name;
    private final String description;
    private final ObjectType dataType;

    private String value;
    private boolean selected;

    public MeterField(String id, String name, String description, ObjectType dataType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dataType = dataType;

        this.value = "--";
        this.selected = false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ObjectType getDataType() {
        return dataType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}