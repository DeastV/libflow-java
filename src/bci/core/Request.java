package bci.core;

import java.io.Serializable;

class Request implements Serializable {
    private final int _userId;
    private final int _workId;
    private final int _endOfRequest;

    Request(int userId, int workId, int endOfRequest){
        _userId = userId;
        _workId = workId;
        _endOfRequest = endOfRequest;
    }

    int getUserId() {
        return _userId;
    }

    int getWorkId() {
        return _workId;
    }

    int getEndOfRequest() {
        return _endOfRequest;
    }
    

}
