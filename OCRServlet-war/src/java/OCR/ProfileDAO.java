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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author starktony
 */
public class ProfileDAO {

    String sqlConnUrl = "jdbc:sqlserver://localhost:60299;DatabaseName=OCR;user=ocrsystem;Password=Initial1";
    Connection sqlConn = null;
    PreparedStatement sqlStmt = null;
    ResultSet sqlRs = null;

    public String saveProfile(ProfileDTO profileDTO) {
	try {
	    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	    sqlConn = DriverManager.getConnection(sqlConnUrl);

	    String sqlQuery = "INSERT INTO [OCR].[dbo].[PROFILE_MASTER] VALUES (?,?,?,?)";
	    sqlStmt = sqlConn.prepareStatement(sqlQuery);
	    sqlStmt.setString(1, profileDTO.getProfileId());
	    sqlStmt.setLong(2, profileDTO.getUserId());
	    sqlStmt.setString(3, profileDTO.getProfileName());
	    sqlStmt.setString(4, profileDTO.getProfileDetails());

	    sqlStmt.executeUpdate();

	    return "Profile Saved";
	} catch (Exception e) {
	    e.printStackTrace();
	    return e.toString();
	} finally {
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
	    } catch (Exception e) {
		return e.toString();
	    }
	}
    }

    public ProfileDTO loadProfile(ProfileDTO profileDTO) {
	try {
	    String sqlQuery = "SELECT * FROM [OCR].[dbo].[PROFILE_MASTER] WHERE PROFILEID = ?";

	    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	    sqlConn = DriverManager.getConnection(sqlConnUrl);
	    sqlStmt = sqlConn.prepareStatement(sqlQuery);
	    sqlStmt.setString(1, profileDTO.getProfileId());

	    ResultSet rs = sqlStmt.executeQuery();
	    while (rs.next()) {
		profileDTO.setProfileId(rs.getString(1));
		profileDTO.setUserId(rs.getLong(2));
		profileDTO.setProfileName(rs.getString(3));
		profileDTO.setProfileDetails(rs.getString(4));
	    }

	    return profileDTO;
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	} finally {
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
	    } catch (Exception e) {
		return null;
	    }
	}
    }

    public String updateProfile(ProfileDTO profileDTO) {
	try {
	    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	    sqlConn = DriverManager.getConnection(sqlConnUrl);

	    String sqlQuery = "UPDATE [OCR].[dbo].[PROFILE_MASTER] SET PROFILENAME = ?, PROFILEDETAILS = ? "
		    + "WHERE PROFILEID = ?";
	    sqlStmt = sqlConn.prepareStatement(sqlQuery);
	    sqlStmt.setString(1, profileDTO.getProfileName());
	    sqlStmt.setString(2, profileDTO.getProfileDetails());
	    sqlStmt.setString(3, profileDTO.getProfileId());

	    sqlStmt.executeUpdate();

	    return "Profile Updated";
	} catch (Exception e) {
	    e.printStackTrace();
	    return e.toString();
	} finally {
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
	    } catch (Exception e) {
		return e.toString();
	    }
	}
    }

    public String deleteProfile(ProfileDTO profileDTO) {
	try {
	    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	    sqlConn = DriverManager.getConnection(sqlConnUrl);

	    String sqlQuery = "DELETE FROM [OCR].[dbo].[IMAGE_MASTER] WHERE PROFILEID = ?";
	    sqlStmt = sqlConn.prepareStatement(sqlQuery);
	    sqlStmt.setString(1, profileDTO.getProfileId());
	    sqlStmt.executeUpdate();

	    sqlQuery = "DELETE FROM [OCR].[dbo].[PROFILE_MASTER] WHERE PROFILEID = ?";
	    sqlStmt = sqlConn.prepareStatement(sqlQuery);
	    sqlStmt.setString(1, profileDTO.getProfileId());
	    sqlStmt.executeUpdate();

	    return "Profile Deleted";
	} catch (Exception e) {
	    e.printStackTrace();
	    return e.toString();
	} finally {
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
	    } catch (Exception e) {
		return e.toString();
	    }
	}
    }

    public List<ProfileDTO> loadProfilesList(ProfileDTO profileDTO) {
	try {
	    List<ProfileDTO> profileDTOList = new ArrayList<>();
	    String sqlQuery = "SELECT PROFILEID FROM [OCR].[dbo].[PROFILE_MASTER] WHERE USERID = ?";

	    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	    sqlConn = DriverManager.getConnection(sqlConnUrl);
	    sqlStmt = sqlConn.prepareStatement(sqlQuery);
	    sqlStmt.setLong(1, profileDTO.getUserId());

	    ResultSet rs = sqlStmt.executeQuery();
	    while (rs.next()) {
		profileDTO = new ProfileDTO();
		profileDTO.setProfileId(rs.getString(1));
		profileDTOList.add(profileDTO);
	    }

	    return profileDTOList;
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	} finally {
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
	    } catch (Exception e) {
		return null;
	    }
	}
    }
}
