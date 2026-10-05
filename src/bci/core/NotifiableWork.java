package bci.core;
import java.util.List;

public interface NotifiableWork {
    void addObserver(User user, NotificationType type);
    void removeObserver(User user, NotificationType type);
    List<UserInterest> getObservers();
}