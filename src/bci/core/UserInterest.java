package bci.core;

import java.io.Serializable;

public class UserInterest implements Serializable {

    private final User _user;
    private final NotificationType _notificationType;

    public UserInterest(User user, NotificationType notificationType) {
        _user = user;
        _notificationType = notificationType;
    }
    
    public User getUser() { return _user; }
    public NotificationType getNotificationType() { return _notificationType; }

}
