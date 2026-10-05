package bci.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Creator implements Serializable{
    private final String _name;
    private List<Work> _workList;

    Creator(String name){
        _name = name;
        _workList = new ArrayList<>();
    }

    void addWork(Work work){
        _workList.add(work);
    }

    void removeWork(int workId){
        Iterator<Work> it = _workList.iterator();
        while(it.hasNext()){
            if(it.next().getWorkId() == workId){
                it.remove();
            }
        }
    }
    
    String getName() {return _name;}

    List<Work> getWorkList() {
        return List.copyOf(_workList);
    }
    
} 

