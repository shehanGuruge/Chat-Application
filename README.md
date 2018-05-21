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


INSERT INTO userdetails
VALUES('shehan','s2015297','W1628058','W1628058');

INSERT INTO userdetails
VALUES('John','jDiggles','jd789','jd789');


INSERT INTO threads(threads.threadTitle,threads.lastEditedDate,threads.lastEditedTime,threads.createdBy)
VALUES('CLIENT SERVER ARCHITECTURE CWK','2018-01-25','10:52:32','shehan guruge')

INSERT INTO threads(threads.threadTitle,threads.lastEditedDate,threads.lastEditedTime,threads.createdBy)
VALUES('Informatics Institute Of Technology','2018-03-02','19:02:52','john')

INSERT INTO messages
VALUES('Hello everyone I am Shehan','2018-02-25','7:02:03',(SELECT userdetails.logInId FROM userdetails WHERE userdetails.logInId = 'shehan'),(SELECT threads.threadId FROM threads WHERE threads.threadId = 1))


INSERT INTO messages
VALUES('Hello everyone I am John','2018-03-05','18:20:13',(SELECT userdetails.logInId FROM userdetails WHERE userdetails.logInId = 'John'),(SELECT threads.threadId FROM threads WHERE threads.threadId = 2))
