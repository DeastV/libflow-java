package bci.core;

import java.io.*;
import java.util.*;
import bci.core.exception.*;

public class Library implements Serializable {
    private Map<Integer, Work> _worksById;
    private Map<Integer, User> _usersById;
    private Map<String, Creator> _creatorsByName;

    private int _currentDay;
    private int _nextWorkId;
    private int _nextUserId;

    private RuleChecker _ruleChecker;

    /** Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202501101348L;

    Library() {
        _worksById = new LinkedHashMap<>();
        _usersById = new LinkedHashMap<>();
        _creatorsByName = new LinkedHashMap<>();
        _nextUserId = _nextWorkId = _currentDay = 1;
        _ruleChecker = new RuleChecker();
    }

    // ---------- FILES ----------

    void importFile(String filename) throws UnrecognizedEntryException, IOException {
        MyParser parser = new MyParser(this);
        parser.parseFile(filename);
    }

    // ---------- GETTERS ----------
    boolean validUser(int userId){
        if(_usersById.get(userId) == null) return false;
        else return true;
    }

    boolean validWork(int workId){
        if(_worksById.get(workId) == null) return false;
        else return true;
    }

    List<Work> getListOfWorks() {
        return List.copyOf(_worksById.values());
    }

    List<Creator> getListOfCreators() {
        return List.copyOf(_creatorsByName.values());
    }

    List<User> getListOfUsers() {
        return List.copyOf(_usersById.values());
    }


    // ---------- WORK INTERNAL ----------

    private void removeWorkInternal(Work work) {
        if (work == null) return;

        _worksById.remove(work.getWorkId());
        List<Creator> creators = getListOfCreators();
        for(Creator creator : creators)
            creator.removeWork(work.getWorkId());
        removeCreators(creators);
    }

    // ---------- WORKS ----------

    String listWork(int workId) throws NoSuchWorkExceptionCore {
        Work work = _worksById.get(workId);
        if (work != null) return work.toString();
        throw new NoSuchWorkExceptionCore(workId);
    }

    List<Work> listWorks(){
        return new ArrayList<>(_worksById.values());
    }

    List<Work> listWorksByCreators(String name) throws NoSuchCreatorExceptionCore {
        Creator creator = _creatorsByName.get(name);
        if (creator == null) throw new NoSuchCreatorExceptionCore(name);

        List<Work> works = new ArrayList<>(creator.getWorkList());
        if (works.isEmpty()) throw new NoSuchCreatorExceptionCore(name);

        return works;
    }

    void alterInvWork(int quantityChange, int workId) throws NotEnoughInventoryExceptionCore, NoSuchWorkExceptionCore {
        Work work = _worksById.get(workId);
        if (work == null) throw new NoSuchWorkExceptionCore(workId);

        int available = work.getNumberOfAvailableCopies();
        int total = work.getNumberOfCopies();

        if (quantityChange < 0){
            int remove = -quantityChange;
            if (available < remove) throw new NotEnoughInventoryExceptionCore(work.getTitle());
            work.setNumberOfCopies(total - remove);
            work.setNumberOfAvailableCopies(available - remove);
            if(work.getNumberOfCopies() == 0) removeWorkInternal(work);
        } else {
            work.setNumberOfCopies(total + quantityChange);
            work.setNumberOfAvailableCopies(available + quantityChange);
        }
        work.notifyObservers(NotificationType.DISPONIBILIDADE, work.toString());
    }


    String performSearch(String search) {
        if (search == null) return "";
        String searchLower = search.trim().toLowerCase();
        if (searchLower.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        boolean found = false;

        for (Work w : _worksById.values()) {
            if (w == null) continue;
            String workStr = w.toString();
            if (workStr == null) continue;
            if (workStr.toLowerCase().contains(searchLower)) {
                sb.append(workStr).append("\n");
                found = true;
            }
        }
        return found ? sb.toString() : "";
    }

    // ---------- REGISTER WORKS / CREATORS ----------

    void registerDvd(String igac, Creator creator, String title, int price, int numberOfCopies, Category type) {
        int id = _nextWorkId++;
        Dvd newDvd = new Dvd(igac, creator, title, price, numberOfCopies, type, id);

        Creator stored = _creatorsByName.get(creator.getName());
        if (stored == null) {
            _creatorsByName.put(creator.getName(), creator);
            stored = creator;
        }

        stored.addWork(newDvd);
        _worksById.put(newDvd.getWorkId(), newDvd);
    }

    void registerBook(String isbn, int price, String title, int numberOfCopies, List<Creator> creators, Category type) {
        int id = _nextWorkId++;
        Book newBook = new Book(isbn, price, title, numberOfCopies, creators, type, id);

        for (Creator c : creators) {
            Creator stored = _creatorsByName.get(c.getName());
            if (stored == null) {
                _creatorsByName.put(c.getName(), c);
                stored = c;
            }
            stored.addWork(newBook);
        }

        _worksById.put(newBook.getWorkId(), newBook);
    }

    Creator registerCreator(String name) {
        Creator existing = _creatorsByName.get(name);
        if (existing != null) return existing;

        Creator newCreator = new Creator(name);
        _creatorsByName.put(name, newCreator);
        return newCreator;
    }

    // ---------- TIME ----------
    int getCurrentDay() {
        return _currentDay;
    }

    void advanceDays(int day) {
        _currentDay += day;
        for(User u : _usersById.values()) //checar requisicoes expiradas
            u.checkRequisitions(getCurrentDay());
    }

    // ---------- USERS ----------

    int registerUser(String name, String email) {
        int id = _nextUserId++;
        User u = new User(name, email, id);
        _usersById.put(id, u);
        return id;
    }

    User listUser(int userId) throws NoSuchUserExceptionCore {
        User u = _usersById.get(userId);
        if (u != null) return u;
        throw new NoSuchUserExceptionCore(userId);
    }

    void subscribeObserver(int userId, int workId, NotificationType type){
        _worksById.get(workId).addObserver(_usersById.get(userId), type);
    }

    List<User> listUsers() {
        return new ArrayList<>(_usersById.values());
    }

    void payFine(int userId) throws UserIsActiveExceptionCore{
        if(!_usersById.get(userId).isActive()){
            _usersById.get(userId).payFine(getCurrentDay());
        }
        else{
            throw new UserIsActiveExceptionCore(userId);
        }
    }

    void setFine(int userId, int quant){
        _usersById.get(userId).setFine(quant);
    }

  //------------ CREATORS -------------

  void removeCreators(List<Creator> creators){
    for(Creator creator : creators)
        if(creator.getWorkList().isEmpty())
            _creatorsByName.remove(creator.getName());
  }

  //------------ REQUISITIONS/BEHAVIOR  -------------

  int requestWork(int userId, int workId) throws CouldNotRequestException, NotEnoughInventoryExceptionCore{
    User user = _usersById.get(userId);
    Work work = _worksById.get(workId);

    _ruleChecker.checkRules(work, user);
    user.checkBehavior();
    Request request = new Request(userId, workId, getCurrentDay()+ user.getBehavior().getReqTime(work.getNumberOfCopies())); //somando o dia de hoje
    user.addUserRequest(request);
    work.setNumberOfAvailableCopies(work.getNumberOfAvailableCopies()-1); //reduzindo o numero de copias disponiveis
    work.removeObserver(user, NotificationType.DISPONIBILIDADE);
    work.notifyObservers(NotificationType.REQUISIÇÂO, work.toString());
    return request.getEndOfRequest();
  }

  int returnWork(int userId, int workId){
    Work work = _worksById.get(workId);
    int res = _usersById.get(userId).removeUserRequest(workId, getCurrentDay());
    if(res>=0) work.setNumberOfAvailableCopies(work.getNumberOfAvailableCopies()+1);
    if(work.getNumberOfAvailableCopies() == 1){
        work.notifyObservers(NotificationType.DISPONIBILIDADE, work.toString());
    }
    return res;
  }

  //------------ NOTIFICATIONS  -------------

    List<Notification> showUserNotifications(int userId) throws NoSuchUserExceptionCore {
        User user = _usersById.get(userId);
        if (user == null) {
            throw new NoSuchUserExceptionCore(userId);
        }
        return user.viewNotifications();
    }

}
