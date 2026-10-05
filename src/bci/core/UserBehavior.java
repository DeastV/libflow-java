package bci.core;

import java.io.Serializable;
import java.io.Serial;
import java.util.List;

public interface UserBehavior{
    int getReqTime(int copiesNum);
    int getMaxReq();
    String toString();
    void checkBehavior(User u);
}

class Normal implements UserBehavior, Serializable {
    public static final Normal INSTANCE = new Normal();

    private Normal() {}

    @Override
    public int getReqTime(int copiesNum){
        if(copiesNum == 1) return 3;
        else if(copiesNum <= 5) return 8;
        else return 15;
    }

    @Override
    public int getMaxReq(){ //public??
        return 3;
    }

    @Override
    public String toString(){
        return "NORMAL";
    }

    public void checkBehavior(User u) {
        List<Boolean> history = u.getLastRequestsOnTime();
        int size = history.size();

        if (size >= 5 && history.subList(size-5, size).stream().allMatch(v -> v))
            u.setBehavior(Cumpridor.INSTANCE);
        else if (size >= 3 && history.subList(size-3, size).stream().noneMatch(v -> v))
            u.setBehavior(Faltoso.INSTANCE);
    }

    @Serial
    private Object readResolve() {
        return Normal.INSTANCE;
    }
}


class Faltoso implements UserBehavior, Serializable {
    public static final Faltoso INSTANCE = new Faltoso();

    private Faltoso() {}

    @Override
    public int getReqTime(int copiesNum){
        return 2;
    }

    @Override
    public int getMaxReq(){
        return 1;
    }

    @Override
    public String toString(){
        return "FALTOSO";
    }

    public void checkBehavior(User u) {
        List<Boolean> history = u.getLastRequestsOnTime();
        if (history.size() >= 3 && history.subList(history.size()-3, history.size()).stream().allMatch(v -> v))
            u.setBehavior(Normal.INSTANCE);
    }

    @Serial
    private Object readResolve() {
        return Faltoso.INSTANCE;
    }
}

class Cumpridor implements UserBehavior, Serializable {
    public static final Cumpridor INSTANCE = new Cumpridor();

    private Cumpridor() {}

    @Override
    public int getReqTime(int copiesNum){
        if(copiesNum == 1) return 8;
        else if(copiesNum <= 5) return 15;
        else return 30;
    }

    @Override
    public int getMaxReq(){
        return 5;
    }

    @Override
    public String toString(){
        return "CUMPRIDOR";
    }

    public void checkBehavior(User u) {
        List<Boolean> history = u.getLastRequestsOnTime();
        if (history.contains(false))
            u.setBehavior(Normal.INSTANCE);
    }

    @Serial
    private Object readResolve() {
        return Cumpridor.INSTANCE;
    }
}