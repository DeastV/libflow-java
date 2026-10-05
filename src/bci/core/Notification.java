package bci.core;

import java.io.Serializable;

public class Notification implements Serializable {
    private final String _message;
    private final NotificationType _type;


    Notification(NotificationType type, String message) {
        _type = type;
        _message = message;
    }

    @Override
    public String toString() {
        return _type + ": " + _message;
    }
}

