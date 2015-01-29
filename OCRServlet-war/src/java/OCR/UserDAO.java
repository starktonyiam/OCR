/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OCR;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author starktony
 */
public class UserDAO extends ConnectionUtils.ConnectionUtils {

//    String sqlConnUrl = "jdbc:sqlserver://localhost:60299;DatabaseName=OCR;user=ocrsystem;Password=Initial1";
//    Connection sqlConn = null;
//    PreparedStatement sqlStmt = null;
//    ResultSet sqlRs = null;
    public String saveUser(UserDTO userDTO) {
	try {
	    sqlConn = connectToDB();

	    String sqlQuery = "INSERT INTO [OCR].[dbo].[USER_MASTER] VALUES ("
		    + "(SELECT CASE WHEN MAX(UID) IS NULL "
		    + "THEN 1 ELSE (MAX(UID)+1) END AS UID "
		    + "FROM [OCR].[dbo].[USER_MASTER])"
		    + ",?,?,?,?,?,?,?,?)";
	    sqlStmt = sqlConn.prepareStatement(sqlQuery);

	    sqlStmt.setString(1, userDTO.getName());
	    sqlStmt.setString(2, userDTO.getEmail());
	    sqlStmt.setString(3, userDTO.getPhone());
	    sqlStmt.setLong(4, userDTO.getCreatorUID());
	    sqlStmt.setBoolean(5, userDTO.isFwdShare());
	    sqlStmt.setBoolean(6, userDTO.isClaimedAcc());
	    sqlStmt.setString(7, userDTO.getPwd());
	    sqlStmt.setString(8, userDTO.getResetPwd());

	    sqlStmt.executeUpdate();

	    return "User Saved";
	} catch (Exception e) {
	    e.printStackTrace();
	    return e.toString();
	} finally {
	    disconnectFromDB();
	}
    }

    public UserDTO loadUser(UserDTO userDTO) {
	try {
	    String sqlQuery = "SELECT * FROM [OCR].[dbo].[USER_MASTER] WHERE NAME = ? AND PWD = ?";

	    sqlConn = connectToDB();
	    sqlStmt = sqlConn.prepareStatement(sqlQuery);
	    sqlStmt.setString(1, userDTO.getName());
	    sqlStmt.setString(2, userDTO.getPwd());

	    sqlRs = sqlStmt.executeQuery();
	    while (sqlRs.next()) {
		userDTO.setUID(sqlRs.getLong(1));
		userDTO.setName(sqlRs.getString(2));
		userDTO.setEmail(sqlRs.getString(3));
		userDTO.setPhone(sqlRs.getString(4));
		userDTO.setPwd(sqlRs.getString(5));
	    }

	    return userDTO;
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	} finally {
	    disconnectFromDB();
	}
    }

    public String updateUser(UserDTO userDTO) {
	try {
	    sqlConn = connectToDB();

	    String sqlQuery = "UPDATE [OCR].[dbo].[USER_MASTER] SET NAME = ?,EMAIL = ?, PHONE = ?, PWD = ? "
		    + "WHERE UID = ?";
	    sqlStmt = sqlConn.prepareStatement(sqlQuery);

	    sqlStmt.setString(1, userDTO.getName());
	    sqlStmt.setString(2, userDTO.getEmail());
	    sqlStmt.setString(3, userDTO.getPhone());
	    sqlStmt.setString(4, userDTO.getPwd());
	    sqlStmt.setLong(5, userDTO.getUID());

	    sqlStmt.executeUpdate();

	    return "User Updated";
	} catch (Exception e) {
	    e.printStackTrace();
	    return e.toString();
	} finally {
	    disconnectFromDB();
	}
    }

    public String deleteUser(UserDTO userDTO) {
	try {
	    sqlConn = connectToDB();

	    String sqlQuery = "DELETE FROM [OCR].[dbo].[IMAGE_MASTER] WHERE UID IN "
		    + "(SELECT UID FROM [OCR].[dbo].[USER_MASTER] WHERE CREATORUID = ? AND CLAIMEDACC = 0)";
	    sqlStmt = sqlConn.prepareStatement(sqlQuery);
	    sqlStmt.setLong(1, userDTO.getUID());
	    sqlStmt.executeUpdate();

	    sqlQuery = "DELETE FROM [OCR].[dbo].[USER_SHARE_DETAILS] WHERE UID IN "
		    + "(SELECT UID FROM [OCR].[dbo].[USER_MASTER] WHERE CREATORUID = ? AND CLAIMEDACC = 0)";
	    sqlStmt = sqlConn.prepareStatement(sqlQuery);
	    sqlStmt.setLong(1, userDTO.getUID());
	    sqlStmt.executeUpdate();

	    sqlQuery = "DELETE FROM [OCR].[dbo].[USER_MASTER] WHERE (CREATORUID = ? AND CLAIMEDACC = 0) OR UID = ? ";
	    sqlStmt = sqlConn.prepareStatement(sqlQuery);
	    sqlStmt.setLong(1, userDTO.getUID());
	    sqlStmt.setLong(2, userDTO.getUID());
	    sqlStmt.executeUpdate();

	    return "User Deleted";
	} catch (Exception e) {
	    e.printStackTrace();
	    return e.toString();
	} finally {
	    disconnectFromDB();
	}
    }

    public String shareUser(UserDTO userDTO) {
	try {
	    sqlConn = connectToDB();

	    String sqlQuery = "INSERT INTO [OCR].[dbo].[USER_SHARE_DETAILS] VALUES ("
		    + "?,(SELECT UID FROM [OCR].[dbo].[USER_MASTER] WHERE NAME = ? OR EMAILID = ?))";
	    sqlStmt = sqlConn.prepareStatement(sqlQuery);
	    sqlStmt.setLong(1, userDTO.getUID());
	    sqlStmt.setString(2, userDTO.getName());
	    sqlStmt.setString(3, userDTO.getEmail());
	    sqlStmt.executeUpdate();

	    return "User Shared";
	} catch (Exception e) {
	    e.printStackTrace();
	    return e.toString();
	} finally {
	    disconnectFromDB();
	}
    }
}
