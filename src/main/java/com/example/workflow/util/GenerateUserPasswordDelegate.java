package com.example.workflow.util;

import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;
import java.security.SecureRandom;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;


@Named("generateUserPassword")
public class GenerateUserPasswordDelegate implements JavaDelegate
{
    public void execute(final DelegateExecution execution) throws Exception {
        final UserDetails user = (UserDetails)execution.getVariable("userDetails");
        final boolean alreadyCreated = this.doesUserAlreadyExist(user.getEmail(), execution);
        if (alreadyCreated) {
            throw new BpmnError("AlreadyExists");
        }
        execution.setVariable("firstName", (Object)user.getFirstName());
        execution.setVariable("secondName", (Object)user.getSecondName());
        execution.setVariable("emailAddress", (Object)user.getEmail());
        String userName = user.getFirstName() + user.getSecondName();
        userName = this.normalizeUserName(userName);
        for (int appendedNumber = 1; this.userExists(userName, execution); userName += appendedNumber, ++appendedNumber) {}
        execution.setVariable("userName", (Object)userName);
        execution.setVariable("password", (Object)this.randomPassword());
    }

    private String normalizeUserName(String userName) {
        userName = userName.replaceAll("\\s+", "");
        userName = userName.replaceAll("\u00fc", "ue").replaceAll("\u00f6", "oe").replaceAll("\u00e4", "ae").replaceAll("\u00df", "ss");
        userName = userName.replaceAll("\u00dc(?=[a-z\u00e4\u00f6\u00fc\u00df ])", "Ue").replaceAll("\u00d6(?=[a-z\u00e4\u00f6\u00fc\u00df ])", "Oe").replaceAll("\u00c4(?=[a-z\u00e4\u00f6\u00fc\u00df ])", "Ae");
        userName = userName.replaceAll("\u00dc", "UE").replaceAll("\u00d6", "OE").replaceAll("\u00c4", "AE");
        userName = userName.replaceAll("[^A-Za-z0-9()\\[\\]]", "");
        userName = userName.replaceAll("\\p{P}", "");
        if (userName.isEmpty()) {
            userName = "temp";
        }
        return userName;
    }

    private boolean doesUserAlreadyExist(final String email, final DelegateExecution execution) {
        final User user = (User)execution.getProcessEngineServices().getIdentityService().createUserQuery().userEmail(email).singleResult();
        return user != null;
    }

    private String randomPassword() {
        final char[] possibleCharacters = new String("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#&*()-_;:.").toCharArray();
        return RandomStringUtils.
                random(8, 0, possibleCharacters.length - 1, false, false, possibleCharacters, (Random)new SecureRandom());
    }

    private boolean userExists(final String UserName, final DelegateExecution execution) {
        final User user = (User)execution.getProcessEngineServices().getIdentityService().createUserQuery().userId(UserName).singleResult();
        return user != null;
    }

}
