package bci.core;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class User implements Serializable, Notifiable{
    private String _name;
    private String _email;
    private final int _id;
    private boolean _isActive;
    private UserBehavior _behavior;
    private int _fine;
    private List<Request> _activeUserRequests;
    private List<Boolean> _lastRequestsOnTime;
    private List<Notification> _notifications;

    User(String name, String email, int nextUserId){
        _id = nextUserId;
        _isActive = true;
        _name = name;
        _email = email;
        _fine = 0;
        _behavior = Normal.INSTANCE;
        _activeUserRequests = new ArrayList<>();
        _lastRequestsOnTime = new ArrayList<>();
        _notifications = new ArrayList<>();
    }

    public int getUserId(){return _id;}

    public String getName(){return _name;}

    // ACTIVE

    Boolean isActive() {return _isActive;}

    void checkActive(int day){
        if(_fine == 0 && checkRequisitions(day)){
            _isActive = true;
            checkBehavior();
        }
    }

    // FINE

    void setFine(int quant){_fine = quant;}

    void payFine(int day){
        _fine = 0;
        checkActive(day);
    }

    int getUserFine(){return _fine;}

    // BEHAVIOR

    UserBehavior getBehavior() {return _behavior;}

    void setBehavior(UserBehavior newBehavior){_behavior = newBehavior;}

    void checkBehavior(){
        int size = _lastRequestsOnTime.size();
        if(size>5)
            _lastRequestsOnTime.subList(0,size-5).clear();
        //only the 5 last elements
        _behavior.checkBehavior(this);
    }

    // REQUEST

    int getActiveNumReq(){return _activeUserRequests.size();}

    List<Request> getUserRequests(){
        return List.copyOf(_activeUserRequests);
    }

    void addUserRequest(Request request){_activeUserRequests.add(request);}

    int removeUserRequest(int workId, int day){
        Iterator<Request> iterator = _activeUserRequests.iterator();
        while (iterator.hasNext()) {
            Request request = iterator.next();
            if (request.getWorkId() == workId) {
                iterator.remove();
                _lastRequestsOnTime.add(day <= request.getEndOfRequest());
                checkBehavior();
                return Math.max((day-request.getEndOfRequest()) * 5 + _fine, 0);
            }
        }
        return -1; //isso vai ser visto no app
    }

    List<Boolean> getLastRequestsOnTime(){
        return List.copyOf(_lastRequestsOnTime);
    }

    //checa se todas as requisicoes estao ativas
    boolean checkRequisitions(int day){
        for(Request request : _activeUserRequests)
            if(request.getEndOfRequest() < day) {
                _isActive = false;
                return false;
            }
        return true;
    }

    // NOTIFICATIONS

    public void receiveNotification(Notification n) {
        _notifications.add(n);
    }

    List<Notification> viewNotifications() {
        List<Notification> copy = List.copyOf(_notifications);
        _notifications.clear();
        return copy;
    }

    public String toString(){
        if(_isActive)
            return String.format("%d - %s - %s - %s - %s",
            _id, _name, _email, _behavior, _isActive? "ACTIVO":"SUSPENSO");
        return String.format("%d - %s - %s - %s - %s - %s %d",
        _id, _name, _email, _behavior, _isActive? "ACTIVO":"SUSPENSO" , "EUR", _fine);
    }

}
