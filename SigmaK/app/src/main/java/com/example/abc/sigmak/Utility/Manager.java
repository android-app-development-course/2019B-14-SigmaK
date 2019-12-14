package com.example.abc.sigmak.Utility;

public class Manager {
    private static final Manager ourInstance = new Manager();

    public static Manager getInstance() {
        return ourInstance;
    }

    private Manager() {
    }
}
