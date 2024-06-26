package com.mini.entity;

public class PropertyValue {
    private final String type;
    private final String name;
    private final Object value;

    private final boolean ifRef;
    public PropertyValue(String type,String name,Object value,boolean isRef)
    {
        this.type=type;
        this.name=name;
        this.value=value;
        this.ifRef=isRef;
    }

    public boolean isIfRef() {
        return ifRef;
    }

    //    public PropertyValue(String name, Object value) {
//        this.name = name;
//        this.value = value;
//    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
