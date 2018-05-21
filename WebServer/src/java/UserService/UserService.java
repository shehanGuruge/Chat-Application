/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author shehan vishwajith galappaththi guruge 2015297 
 * W1628058
 */
@WebService(serviceName = "UserService")
public class UserService {
private Connection conn = null;
     private String sqlQueryString = "";
    private static boolean isConnectionEstablished = false;
    private ResultSet resultSet;
   

    /**
     * This method add the users to the database by taking the user entered values
     * @param nickName
     * @param loginID
     * @param password
     * @param confirmationPassword
     */
    
    
    @WebMethod(operationName = "addUser")
    public void addUser(@WebParam(name = "nickName") String nickName, @WebParam(name = "logInID") String loginID,
            @WebParam(name = "password") String password, @WebParam(name = "confirmationPassword") String confirmationPassword) {

        try {
            isConnectionEstablished = establishConnection();

            if (isConnectionEstablished) {
                Statement statement = (Statement) conn.createStatement();
                sqlQueryString = "INSERT INTO userdetails VALUES('" + loginID + "','" + nickName + "','" + password + "','" + confirmationPassword + "');";
                statement.executeUpdate(sqlQueryString);
            } else {
                System.err.println("Connection not established");
            }
            System.out.println("done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method checks whether the connection is established or  not
     * @return 
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
      * This method verifies whether a user with a same login id exists or not
      * @param logInID type = String
      * @return boolean
      */
    @WebMethod(operationName = "checkUserWithSameLoginId")
    public boolean checkUserWithSameLoginId(@WebParam(name = "logInID") String logInID) {
        String id = "";
        boolean isUserExist  = false;
        try {
            isConnectionEstablished = establishConnection();
/**
 * If the establishConnection method true then a sql query is written to get the login id from the database table with the relevant id
 * if the result set is not null then it will check the id and verifies whether a user exist or not.
 */
            if (isConnectionEstablished) {
                Statement statement = (Statement) conn.createStatement();
                sqlQueryString = "SELECT userdetails.logInId FROM userdetails WHERE userdetails.logInId = '" + logInID + "'";
                resultSet = statement.executeQuery(sqlQueryString);

                while (resultSet.next()) {
                    id = resultSet.getString("LogInId");
                    if (id.equalsIgnoreCase(logInID)) {
                        isUserExist = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
        }
        return isUserExist;
    }

    /**
     * This method checks whether a user exists or not
     * @param logInID type = String
     * @param password type = String
     * @return  boolean
     */
    @WebMethod(operationName = "checkUserExistOrNot")
    public boolean checkUserExistOrNot(@WebParam(name = "logInID") String logInID, @WebParam(name = "password") String password) {
        boolean isUserExist = false;
        String id = "";
        String passwrd = "";
        try {
            isConnectionEstablished = establishConnection();

            /**
             * if establishConnection method returns true then a query is written to take the id and the password from the database 
             * inorder to check whether the password and the login id matches with the one's in the database
             */
            if (isConnectionEstablished) {
                System.out.println(logInID+" "+password);
                Statement stat = (Statement) conn.createStatement();
                sqlQueryString = "SELECT userdetails.logInId,userdetails.userPassword from userdetails WHERE userdetails.logInId = '"+logInID+"'AND userdetails.userPassword = '"+password+"'";
                resultSet = stat.executeQuery(sqlQueryString);

                while (resultSet.next()) {
                    id = resultSet.getString("logInId");
                    passwrd = resultSet.getString("userPassword");
                    System.out.println("id: " + id);
                    if (id.equals(logInID) && password.equals(passwrd)) {
                        isUserExist = true;// if the password and the login id is equal then there is a user in the database with that relevant details
                        System.out.println(isUserExist);
//                        break;
                    }
                }
            }
        } catch (Exception e) {
        }
        return isUserExist;
    }
}
