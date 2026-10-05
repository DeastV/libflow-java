package bci.core;

import bci.core.exception.*;
import java.io.Serializable;
import java.util.*;

abstract class Rule implements Serializable {
    private final int _ruleId;

    protected Rule(int ruleId){_ruleId = ruleId;}

    protected int getId(){return _ruleId;}

    abstract void check(Work work, User user)
        throws CouldNotRequestException, NotEnoughInventoryExceptionCore;

}

class CheckRequestTwice extends Rule {
    CheckRequestTwice(int id){
        super(id);
    }

    void check(Work work, User user) throws CouldNotRequestException{
        List<Request> userRequests = user.getUserRequests();
        if(userRequests.isEmpty())
            return;
        for(Request request : userRequests){
            if(request.getWorkId() == work.getWorkId())
                throw new CouldNotRequestException(1);
        }
    }
}

class CheckActiveUser extends Rule{
    CheckActiveUser(int id){
        super(id);
    }

    void check(Work work, User user) throws CouldNotRequestException{
        if(!user.isActive())
            throw new CouldNotRequestException(2);
    }
}

class CheckInventory extends Rule{
    CheckInventory(int id){
        super(id);
    }

    void check(Work work, User user) throws NotEnoughInventoryExceptionCore{ //Para as notificacoes
        if(work.getNumberOfAvailableCopies() < 1)
            throw new NotEnoughInventoryExceptionCore();
    }
}

class CheckNumberRequisitions extends Rule{
    CheckNumberRequisitions(int id){
        super(id);
    }

    void check(Work work, User user) throws CouldNotRequestException{
        if(user.getActiveNumReq()+1 > user.getBehavior().getMaxReq()) //mais 1 porque é com a nova requisicao
            throw new CouldNotRequestException(4);
    }
}

class CheckCategory extends Rule{
    CheckCategory(int id){
        super(id);
    }

    void check(Work work, User user) throws CouldNotRequestException{
        if(work.getCategoryString().equals("Referência"))
            throw new CouldNotRequestException(5);
    }
}


class CheckPrice extends Rule{
    CheckPrice(int id){
        super(id);
    }

    void check(Work work, User user) throws CouldNotRequestException, NotEnoughInventoryExceptionCore{
        if (user.getBehavior() == Cumpridor.INSTANCE) return; // fixed

        if(work.getPrice()>25) throw new CouldNotRequestException(6);
    }
}

