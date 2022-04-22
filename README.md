# Camunda 7 User and Tenant Creator

![](https://img.shields.io/badge/Educational%20Tooling-Project%20for%20getting%20started%20with%20Camunda%20for%20educators-%239F2B68)
This is intended to be a quick and easy tool which would let you generate an instance of Camunda giving multiple independent users the abilibty to share the same instance without interfearing with eachother. 

## Features in a nutshell

### The Use Case
Setup an instance where users have their own Camunda workspace. Where they can deploy, start and monitor their own procses without affecting other users. While giving an administrator an overview of everything going on.

### The Setup Process
- This Contains a process which a CVS file of users as input
- For each user it generates a user, tenant and authorizations
- Turns on security for the REST API

## How to Run it
Use maven to build the project and you can start it as you would any spring boot project. If you start it localy you can log into it form ``localhost:8080`` and login with the admin user which is defined in the `application.yaml` and the default is:
user ``TheDoctor``
password ``DoctorWho``

Go to tasklist where you can start a process instance. It will ask you to upload a file containing users. the file should be a csv file. The following is an example of the format:

```json
StudentID,FristName,SecondName,EmailAddress,SendMail
100,Niall,Deehan,niall.deehan@camunda.com,true
200,Pond,Doctor,pond.doctor@email.com,true
300,reb,brown,reb@email.com,true
400,Cool,Person,doom@email.com,false
```

For each entery a Camunda User will be created and a password generated. 

You can find those usernames and passwords by seaching cockpit

@TODO

* Add export of usernames and passwords to a file.
* Add email sending feature.
