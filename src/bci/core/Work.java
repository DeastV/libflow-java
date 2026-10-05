package bci.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Work implements Serializable, NotifiableWork {
    private final int _workId;
    private String _title;
    private int _price;
    private int _numberOfCopies;
    private int _availableCopies;
    private Category _category;
    private List<UserInterest> _interestedUsers;

    Work(String title, int price, int numberOfCopies, Category category, int nextWorkId){
        _workId = nextWorkId;
        _title = title;
        _numberOfCopies = _availableCopies = numberOfCopies;
        _price = price;
        _category = category;
        _interestedUsers = new ArrayList<>();
    }

    public int getWorkId() {return _workId;}

    public String getTitle() {return _title;}

    int getPrice() {return _price;}

    int getNumberOfCopies() {return _numberOfCopies;}

    int getNumberOfAvailableCopies() {return _availableCopies;}

    void setNumberOfCopies(int numberOfCopies) { _numberOfCopies = numberOfCopies; }

    void setNumberOfAvailableCopies(int numberOfAvailableCopies) { _availableCopies = numberOfAvailableCopies; }

    String getCategoryString() {
        if(_category == Category.FICTION) return "Ficção";
        if(_category == Category.REFERENCE) return "Referência";
        return "Técnica e Científica";
    }

    @Override
    public String toString(){
        return String.format("%d - %d de %d - %s - %s - %d - %s - %s",
            _workId, _availableCopies, _numberOfCopies, getType(), _title, _price, getCategoryString(), getAdInfo());
    }

    @Override
    public void addObserver(User user, NotificationType type) {
        UserInterest ui = new UserInterest(user, type);
        if (!_interestedUsers.contains(ui)) {
            _interestedUsers.add(ui);
        }
    }

    @Override
    public void removeObserver(User user, NotificationType type) {
        _interestedUsers.removeIf(ui -> ui.getUser().equals(user) && ui.getNotificationType() == type);
    }

    @Override
    public List<UserInterest> getObservers() {
        return List.copyOf(_interestedUsers);
    }

    public void notifyObservers(NotificationType type, String message) {
        Notification notification = new Notification(type, message);
        for (UserInterest ui : _interestedUsers) {
            if (ui.getNotificationType() == type) {
                ui.getUser().receiveNotification(notification);
            }
        }
    }

    abstract String getType();

    abstract String getAdInfo();

    abstract List<Creator> listCreators();

}
