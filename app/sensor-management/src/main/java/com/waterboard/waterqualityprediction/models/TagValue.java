package com.waterboard.waterqualityprediction.models;

public class TagValue {
    private int tagId;
    private float value;

    public TagValue(int tagId, float value) {
        this.tagId = tagId;
        this.value = value;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TagValue{tagId=" + tagId + ", value=" + value + "}";
    }
}
