package com.example.workflow.util.listener;

import com.example.workflow.util.UserDetails;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named
public class AddUserToListListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        List<UserDetails> listOfCreatedUsers = (List<UserDetails>) execution.getVariable("listOfCreatedUsers");
        if(listOfCreatedUsers == null){
            listOfCreatedUsers = new ArrayList<UserDetails>();
        }
        UserDetails thisUser = (UserDetails) execution.getVariableLocal("userDetails");
        listOfCreatedUsers.add(thisUser);

        execution.setVariable("listOfCreatedUsers", listOfCreatedUsers);


    }
}
