package com.example.workflow.util;

import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.net.URL;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.camunda.bpm.engine.RuntimeService;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named("parseFileForUserInfo")
public class ParseFileForUserInfo implements JavaDelegate {

    final int USER_ID_POSITION = 0;
    final int FIRST_NAME_POSITION = 1;
    final int LAST_NAME_POSITION = 2;
    final int EMAIL_ADDRESS_POSITION = 3;
    final int CREATE_USER_FLAG_POSITION = 4;

    public void execute(final DelegateExecution execution) throws Exception {
        final RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
        final FileValue retrievedTypedFileValue = (FileValue)runtimeService.getVariableTyped(execution.getId(), "fileLocation");
        final List<UserDetails> userList = new ArrayList<UserDetails>();
        if (retrievedTypedFileValue == null) {
            execution.setVariable("userList", (Object)userList);
            return;
        }
        BufferedReader br = null;
        String line = "";
        final String cvsSplitBy = ",";
        int numberOfUsers = 0;
        try {
            br = new BufferedReader(new InputStreamReader(retrievedTypedFileValue.getValue(), StandardCharsets.UTF_8));
            br.readLine();
            while ((line = br.readLine()) != null) {
                final UserDetails newUser = new UserDetails();
                final String[] users = line.split(cvsSplitBy);
                    try {
                        newUser.setFirstName(users[FIRST_NAME_POSITION].replaceAll("\"", ""));
                        newUser.setSecondName(users[LAST_NAME_POSITION].replaceAll("\"", ""));
                        newUser.setEmail(users[EMAIL_ADDRESS_POSITION]);
                    }catch(Exception e){
                        System.out.println("USER NOT CREATED: There's missing data on this line: "+ line);
                        break;
                    }

                    System.out.println("User [First Name= " + newUser.getFirstName() + " , Second Name=" + newUser.getSecondName() + " , Email=" + newUser.getEmail() + "]");
                    userList.add(newUser);
                    ++numberOfUsers;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                }
                catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }
        try {
            br.close();
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
        System.out.println("Number of users created is: " + numberOfUsers);
        execution.setVariable("userList", (Object)userList);
    }

    private File getFileFromResources(final String fileName) {
        final ClassLoader classLoader = this.getClass().getClassLoader();
        final URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        }
        return new File(resource.getFile());
    }

}
