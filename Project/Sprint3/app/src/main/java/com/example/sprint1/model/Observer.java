package com.example.sprint1.model;

public interface Observer {
    void onUpdate(String eventType, Object data);
}