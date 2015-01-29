/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConnectionUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author I076631
 */
public class ConnectionUtils {

    public String sqlConnUrl = "jdbc:sqlserver://localhost:60299;DatabaseName=OCR;user=ocrsystem;Password=Initial1";
    public Connection sqlConn = null;
    public PreparedStatement sqlStmt = null;
    public ResultSet sqlRs = null;

    public Connection connectToDB() {
	try {
	    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	    return (DriverManager.getConnection(sqlConnUrl));
	} catch (Exception ex) {
	    Logger.getLogger(ConnectionUtils.class.getName()).log(Level.SEVERE, null, ex);
	}

	return null;
    }

    public void disconnectFromDB() {
	try {
	    if (sqlRs != null) {
		sqlRs.close();
	    }
	    if (sqlStmt != null) {
		sqlStmt.close();
	    }
	    if (sqlConn != null) {
		sqlConn.close();
	    }
	} catch (Exception ex) {
	    Logger.getLogger(ConnectionUtils.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

}
