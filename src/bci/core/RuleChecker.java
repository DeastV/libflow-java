package bci.core;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import bci.core.exception.*;


class RuleChecker implements Serializable {
    private List<Rule> rules;

    RuleChecker(){
        rules = new ArrayList<>();
        rules.add(new CheckRequestTwice(1));
        rules.add(new CheckActiveUser(2));
        rules.add(new CheckInventory(3));
        rules.add(new CheckNumberRequisitions(4));
        rules.add(new CheckCategory(5));
        rules.add(new CheckPrice(6));
    }

    void addRule(Rule rule){
        for(Rule r : rules)
            if(r.getId() == rule.getId())
                return; 
        rules.add(rule);
        rules.sort((a,b) -> Integer.compare(a.getId(), b.getId()));
    }

    void checkRules(Work work, User user) throws CouldNotRequestException, NotEnoughInventoryExceptionCore{
        for(Rule rule : rules)
            rule.check(work, user);
    }
}
