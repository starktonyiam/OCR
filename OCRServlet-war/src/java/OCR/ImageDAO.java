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
public class ImageDAO extends ConnectionUtils.ConnectionUtils {
    
    public String saveImage(ImageDTO imageDTO) {
	try {
	    sqlConn = connectToDB();
	    
	    String sqlQuery = "SELECT NAME FROM [OCR].[dbo].[MEDICINE_MASTER]";
	    sqlStmt = sqlConn.prepareStatement(sqlQuery);
	    sqlRs = sqlStmt.executeQuery();
	    String medicineName;
	    String rawOCRClob = imageDTO.getRawOCRClob().toLowerCase();
	    String procOCRClob="";
	    
	    while (sqlRs.next()) {
		medicineName = sqlRs.getString(1).toLowerCase();
		if (rawOCRClob.contains(medicineName)) {
		    procOCRClob += medicineName + ";";
		}
	    }
	    
	    sqlQuery = "INSERT INTO [OCR].[dbo].[IMAGE_MASTER] VALUES (?,?,?,?,?,?,?)";
	    sqlStmt = sqlConn.prepareStatement(sqlQuery);
	    sqlStmt.setString(1, imageDTO.getIID());
	    sqlStmt.setString(2, imageDTO.getUID());
	    sqlStmt.setString(3, imageDTO.getCreatorID());
	    sqlStmt.setBytes(4, imageDTO.getImageBlob());
	    sqlStmt.setBytes(5, imageDTO.getThumbnailBlob());
	    sqlStmt.setString(6, rawOCRClob);
	    sqlStmt.setString(7, procOCRClob);
	    
	    sqlStmt.executeUpdate();
	    
	    return "Image Saved";
	} catch (Exception e) {
	    e.printStackTrace();
	    return e.toString();
	} finally {
	    disconnectFromDB();
	}
    }
    
    public ImageDTO loadThumbnail(ImageDTO imageDTO) {
	try {
	    String sqlQuery = "SELECT IMAGETHUMBBLOB FROM [OCR].[dbo].[IMAGE_MASTER] WHERE IMAGEID = ?";
	    
	    sqlConn = connectToDB();
	    sqlStmt = sqlConn.prepareStatement(sqlQuery);
	    sqlStmt.setString(1, imageDTO.getIID());
	    
	    ResultSet rs = sqlStmt.executeQuery();
	    while (rs.next()) {
		imageDTO.setThumbnailBlob(rs.getBytes(1));
	    }
	    
	    return imageDTO;
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	} finally {
	    disconnectFromDB();
	}
    }
    
    public List<ImageDTO> loadThumbnailList(ProfileDTO profileDTO) {
	try {
	    List<ImageDTO> imageDTOList = new ArrayList<>();
	    ImageDTO imageDTO;
	    
	    String sqlQuery = "SELECT IMAGEID FROM [OCR].[dbo].[IMAGE_MASTER] WHERE PROFILEID = ?";
	    
	    sqlConn = connectToDB();
	    sqlStmt = sqlConn.prepareStatement(sqlQuery);
	    sqlStmt.setString(1, profileDTO.getProfileId());
	    
	    ResultSet rs = sqlStmt.executeQuery();
	    while (rs.next()) {
		imageDTO = new ImageDTO();
		imageDTO.setIID(rs.getString(1));
		imageDTOList.add(imageDTO);
	    }
	    
	    return imageDTOList;
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	} finally {
	    disconnectFromDB();
	}
    }
    
    public ImageDTO loadImage(ImageDTO imageDTO) {
	try {
	    String sqlQuery = "SELECT IMAGEBLOB FROM [OCR].[dbo].[IMAGE_MASTER] WHERE IMAGEID = ?";
	    
	    sqlConn = connectToDB();
	    sqlStmt = sqlConn.prepareStatement(sqlQuery);
	    sqlStmt.setString(1, imageDTO.getIID());
	    
	    ResultSet rs = sqlStmt.executeQuery();
	    while (rs.next()) {
		imageDTO.setImageBlob(rs.getBytes(1));
	    }
	    
	    return imageDTO;
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	} finally {
	    disconnectFromDB();
	}
    }
    
    public String deleteImage(ImageDTO imageDTO) {
	try {
	    String sqlQuery = "DELETE FROM [OCR].[dbo].[IMAGE_MASTER] WHERE IMAGEID = ?";
	    
	    sqlConn = connectToDB();
	    sqlStmt = sqlConn.prepareStatement(sqlQuery);
	    sqlStmt.setString(1, imageDTO.getIID());
	    
	    sqlStmt.executeUpdate();
	    
	    return "Image Deleted";
	} catch (Exception e) {
	    e.printStackTrace();
	    return e.toString();
	} finally {
	    disconnectFromDB();
	}
    }
    
}
