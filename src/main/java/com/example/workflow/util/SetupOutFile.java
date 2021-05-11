package com.example.workflow.util;

import org.apache.commons.io.FileUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.FileValue;

import javax.inject.Named;
import java.io.File;
import java.util.List;

@Named
public class SetupOutFile implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {

        //File outPutFile = (File) execution.getVariable("CamundaUserNamesAndPasswords");
        File outPutFile;
       // if (outPutFile == null) {
        String defaultBaseDir = System.getProperty("java.io.tmpdir");
        String filePath = defaultBaseDir + "/Cam" + execution.getProcessInstance().getId() + "/CamundaUserNamesAndPasswords.csv";
        outPutFile = new File(filePath);

       // }

        List<UserDetails> listOfUsers;
        listOfUsers = (List<UserDetails>) execution.getVariable("listOfCreatedUsers");

        for (UserDetails user: listOfUsers) {

//            String firstName = (String) execution.getVariable("firstName");
//            String secondName = (String) execution.getVariable("secondName");
//            String emailAddress = (String) execution.getVariable("emailAddress");
//            String userName = (String) execution.getVariable("userName");
//            String password = (String) execution.getVariable("password");
            String firstName = user.getFirstName();
            String secondName = user.getSecondName();
            String emailAddress = user.getEmail();
            String userName = user.getCamundaUserName();
            String password = user.getCamundaPassword();

            String content = firstName + "," + secondName + "," + emailAddress + "," + userName + "," + password + "\n";
            FileUtils.writeStringToFile(outPutFile, content, "ISO-8859-1", true);
        }

        //String fileOutPut = (String) delegateExecution.getVariable("gitHubOutputString");

        FileValue typedFileValue = Variables
                .fileValue("CamundaUserNamesAndPasswords.csv")
                .file(new File(filePath))
                .mimeType("text/plain")
                .encoding("UTF-8")
                .create();
        execution.setVariable("CamundaUserNamesAndPasswords",typedFileValue);


        //delegateExecution.setVariable("filePath", filePath);

       // runtimeService.setVariable(execution.getId(), "fileVariable", typedFileValue);

    }
}
