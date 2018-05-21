/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServiceChat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author shehan vishwajith galappaththi guruge 2015297 
 * W1628058
 */
@WebService(serviceName = "ServiceChat")
public class ServiceChat {

    private Connection conn = null;
    private String sqlQueryString = "";
    private static boolean isConnectionEstablished = false;
    private ResultSet resultSet;
    private boolean isExist = false;
//    private ArrayList<String> threads = new ArrayList<>();

    /**
     * This method is used for establishing connection
     */
    @WebMethod(operationName = "establishConnection")
    public boolean establishConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/chatapp", "root", "");
            return true;
        } catch (Exception e) {
            System.err.println("No drivers found in your system.");
            return false;
        }
    }

    /**
     * This method add threads to the database
     * @param threadTitle type = String
     * @param lastEditedDate type = String
     * @param lastEditedTime type = String
     * @param createdBy type = String
     */
    @WebMethod(operationName = "addThreads")
    public void addThreads(@WebParam(name = "threadTitle") String threadTitle, @WebParam(name = "lastEditedDate") String lastEditedDate,
            @WebParam(name = "lastEditedTime") String lastEditedTime, @WebParam(name = "createdBy") String createdBy) {

       
        try {
            isConnectionEstablished = establishConnection();

            if (isConnectionEstablished) {
                Statement statement = (Statement) conn.createStatement();

                System.out.println(lastEditedTime);
                sqlQueryString = "INSERT INTO threads(threadTitle,lastEditedDate,lastEditedTime,createdBy) VALUES('" + threadTitle + "','" + lastEditedDate + "','" + lastEditedTime + "','" + createdBy + "')";
                statement.executeUpdate(sqlQueryString);

            }
        } catch (Exception e) {

        }
    }
    
    /**
     * This method is responsible for retrieving threads from  the database
     * @return ArrayList<String>
     */

    @WebMethod(operationName = "retrieveThreads")
    public ArrayList<String> retrieveThreads() {
        ArrayList<String> threads = new ArrayList<>();
        try {
            System.out.println("retrievinmg");
            isConnectionEstablished = establishConnection();
            Statement statement = (Statement) conn.createStatement();
            sqlQueryString = "SELECT * FROM threads";
            resultSet = statement.executeQuery(sqlQueryString);

            while (resultSet.next()) {

                String lastEditedTime = "";
                String lastEditedDate = "";
                String lastEditedBy = "";

                String sqlQuery_lastEditedDateTime = "SELECT messages.lastEditedTime,messages.lastEditedDate,messages.messagedBy FROM messages WHERE threadID = " + resultSet.getInt("threadId");
                Statement statement_2 = (Statement) conn.createStatement();
                ResultSet resultSetForMessages = statement_2.executeQuery(sqlQuery_lastEditedDateTime);

                while (resultSetForMessages.next()) {
                    lastEditedDate = resultSetForMessages.getDate("lastEditedDate").toString();
                    lastEditedTime = resultSetForMessages.getTime("lastEditedTime").toString();
                    lastEditedBy = resultSetForMessages.getString("messagedBy");
                    System.out.println("thread Id: " + resultSet.getInt("threadId") + "time : " + lastEditedTime + " date: " + lastEditedDate);

                }
                
                if (lastEditedBy.equals("")) {
                    threads.add(resultSet.getInt("threadId") + "a1b2c3" + resultSet.getString("threadTitle") + "a1b2c3" + resultSet.getString("createdBy") + "a1b2c3"
                            + resultSet.getDate("lastEditedDate").toString() + "a1b2c3" + resultSet.getTime("lastEditedTime").toString());
                } else {
                    threads.add(resultSet.getInt("threadId") + "a1b2c3" + resultSet.getString("threadTitle") + "a1b2c3" + lastEditedBy + "a1b2c3"
                            + lastEditedDate + "a1b2c3" + lastEditedTime);

                }
            }
            System.out.println("retreived" + threads.size());
        } catch (Exception e) {

        }
        return threads;
    }

    
    /**
     * This method add messages for the relevant threads to the database
     * @param message type = String
     * @param lastEditedDate type = String
     * @param lastEditedTime type = String
     * @param messageBy  type = String
     * @param threadID type = String
     */
    
    @WebMethod(operationName = "addMessages")
    public void addMessages(@WebParam(name = "message") String message, @WebParam(name = "lastEditedDate") String lastEditedDate,
            @WebParam(name = "lastEditedTime") String lastEditedTime, @WebParam(name = "messageBy") String messageBy,
            @WebParam(name = "threadID") String threadID) {

        try {
            isConnectionEstablished = establishConnection();
            if (isConnectionEstablished) {
                Statement statement = (Statement) conn.createStatement();
                sqlQueryString = "INSERT INTO messages VALUES('" + message + "','" + lastEditedDate + "','"
                        + lastEditedTime + "',(SELECT userdetails.logInID FROM userdetails WHERE userdetails.logInID='" + messageBy
                        + "'),(SELECT threads.threadId FROM threads WHERE threads.threadId=" + Integer.parseInt(threadID) + "))";
                statement.executeUpdate(sqlQueryString);
                System.out.println("saving messages");
            }
        } catch (Exception e) {

        }
    }

    
    
    /**
     * This method retrieve the messages from the thread
     * @param threadID type = Integer
     * @return 
     */
    
    @WebMethod(operationName = "retriveMessages")
    public ArrayList<String> retriveMessages(int threadID) {

        ArrayList<String> messages = new ArrayList<>();

        try {
            isConnectionEstablished = establishConnection();
            if (isConnectionEstablished) {
                Statement statement = (Statement) conn.createStatement();
                sqlQueryString = "SELECT * FROM messages WHERE messages.threadID = " + threadID;
                System.out.println("messages retrieving");
                resultSet = statement.executeQuery(sqlQueryString);
                while (resultSet.next()) {
                    System.out.println("messages retrieving started");
                    messages.add(resultSet.getString("message") + "a1b2c3" + resultSet.getDate("lastEditedDate") + "a1b2c3"
                            + resultSet.getTime("lastEditedTime") + "a1b2c3" + resultSet.getString("messagedBy"));
                }
            }

        } catch (Exception e) {

        }
        return messages;
    }

    /**
     * This method returns whether the database table is updated or not
     * @param rowCount type = Integer
     * @param isThreadRowCount type = boolean
     * @param isMessageRowCount type = boolean
     * @param threadId type = Integer
     * @return boolean
     */
    
    
    @WebMethod(operationName = "getTableRowCount")
    public boolean getTableRowCount(@WebParam(name = "rowCount") int rowCount, @WebParam(name = "isThreadRowCount") boolean isThreadRowCount,
            @WebParam(name = "isMessageRowCount") boolean isMessageRowCount, @WebParam(name = "threadId") int threadId) {

        int updatedRowCount = 0;
        boolean isRowsUpdated = false;

        try {
            Statement statement = (Statement) conn.createStatement();
            if (isThreadRowCount) {
                sqlQueryString = "SELECT COUNT(*) FROM threads";
            } else if (isMessageRowCount) {
                sqlQueryString = "SELECT COUNT(*) FROM messages WHERE messages.threadID = " + threadId;
            }

            resultSet = statement.executeQuery(sqlQueryString);
            while (resultSet.next()) {
                updatedRowCount = resultSet.getInt("COUNT(*)");
            }

            if (updatedRowCount == rowCount) {
                isRowsUpdated = false;
            } else if (updatedRowCount > rowCount) {
                isRowsUpdated = true;
            }
//            }
        } catch (Exception e) {

        }
        return isRowsUpdated;
    }

    /**
     * This method returns the last update row of the database table 
     * @param isThread type = boolean
     * @param isMessage type = boolean
     * @param threadId type = Integer
     * @param date type = String
     * @return String
     */
    
    @WebMethod(operationName = "getLastUpdatedRow")
    public String getLastUpdatedRow(@WebParam(name = "isThread") boolean isThread, @WebParam(name = "isMessage") boolean isMessage,
            @WebParam(name = "threadId") int threadId, @WebParam(name = "date") String date) {

        String lastRowData = "";
        try {

            Statement statement = (Statement) conn.createStatement();
            //if thread is true then a sql query is written to get threads on descending order by limiting it to one
            //then its concated to a string variable and returned
            if (isThread) {
                System.out.println("THREAD");
                sqlQueryString = "SELECT * FROM threads ORDER BY threads.threadId DESC LIMIT 1";
                resultSet = statement.executeQuery(sqlQueryString);

                while (resultSet.next()) {
                    lastRowData = resultSet.getString("threadTitle") + "a1b2c3d4" + resultSet.getDate("lastEditedDate") + "a1b2c3d4"
                            + resultSet.getTime("lastEditedTime") + "a1b2c3d4" + resultSet.getString("createdBy");
                }
//if thread is true then a sql query is written to get threads on descending order by limiting it to one
            //then its concated to a string variable and returned
            } else if (isMessage) {
                String sqlQueryString1 = "SELECT * FROM messages WHERE messages.threadID = " + threadId + " AND lastEditedDate = '" + date + "' ORDER BY messages.lastEditedTime DESC LIMIT 1";
                resultSet = statement.executeQuery(sqlQueryString1);
                while (resultSet.next()) {
                    lastRowData = resultSet.getString("message") + "a1b2c3d4" + resultSet.getDate("lastEditedDate") + "a1b2c3d4"
                            + resultSet.getTime("lastEditedTime") + "a1b2c3d4" + resultSet.getString("messagedBy");

                }

            }
        } catch (Exception e) {

        }
        return lastRowData;
    }
    
    /**
     * This method returns the id of the newly added row
     * @return String
     */
    @WebMethod(operationName = "getIDofNewRow")
    public String getIDofNewRow(){
        String threadId = "";
         try {

            Statement statement = (Statement) conn.createStatement();
            sqlQueryString = "SELECT threads.threadId FROM threads ORDER BY threadId DESC LIMIT 1";
            resultSet = statement.executeQuery(sqlQueryString);
            
            while(resultSet.next()){
                threadId = Integer.toString(resultSet.getInt("threadId"));
                
            }
         }catch(Exception e){
             
         }
         return threadId;
    }
}
