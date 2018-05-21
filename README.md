# Chat-Application
A basic chat application using web client and web server. This repository contains the code only for the web server. For the web client code visit to the web client repository

All the user inputs are stored in a basic mysql database. The sql scripts are in this repository itself or can be copied from here,



CREATE TABLE userdetails (
 logInId varchar(250) NOT NULL,
 nickName varchar(200) NOT NULL,
 userPassword varchar(150) NOT NULL,
 confirmationPassword varchar(150) NOT NULL,
 PRIMARY KEY (logInId)
);


CREATE TABLE threads (
 threadId int(255) NOT NULL AUTO_INCREMENT PRIMARY KEY,
 threadTitle varchar(1000) NOT NULL,
 lastEditedDate date DEFAULT NULL,
 lastEditedTime time NOT NULL,
 createdBy varchar(250) NOT NULL
) ;


CREATE TABLE messages (
 message varchar(1500) NOT NULL,
 lastEditedDate date DEFAULT NULL,
 lastEditedTime time NOT NULL,
 messagedBy varchar(250) NOT NULL,
 threadID int(255) NOT NULL,
 CONSTRAINT fk_messagedBy FOREIGN KEY (messagedBy) REFERENCES userdetails (logInId),
 CONSTRAINT fk_threadId FOREIGN KEY (threadID) REFERENCES threads (threadId)
) ;


CREATE DATABASE chatapp;


