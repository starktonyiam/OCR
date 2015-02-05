/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConnectionUtils;

import OCR.ImageDTO;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author I076631
 */
public class AWSS3Utils {

    public enum IMAGE_TYPE {

	FULL, THUMB
    };

    public static String saveImage(String bucketName, ImageDTO imageDTO) {
	InputStream imageIS = null;
	Properties awsCredentialsProperties = new Properties();

	try {
	    awsCredentialsProperties.load(AWSS3Utils.class.getClassLoader().getResourceAsStream("prefs.properties"));
	} catch (Exception ex) {
	    Logger.getLogger(AWSS3Utils.class.getName()).log(Level.SEVERE, null, ex);

	}

	String awsAccessKey = awsCredentialsProperties.getProperty("AWSACCESSKEY");
	String awsSecretKey = awsCredentialsProperties.getProperty("SECRETACCESSKEY");

	try {
//	    AccessControlList accessControlList = new AccessControlList();
//	    accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.FullControl);

	    BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);

	    AmazonS3 s3Client = new AmazonS3Client(awsCredentials);

	    ObjectMetadata objectMetadata = new ObjectMetadata();

	    imageIS = imageDTO.getImageBlob();
	    PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
		    imageDTO.getIID() + ".jpg",
		    imageIS,
		    objectMetadata);
	    s3Client.putObject(putObjectRequest);//.withAccessControlList(accessControlList));

	    imageIS = imageDTO.getThumbnailBlob();
	    putObjectRequest = new PutObjectRequest(bucketName,
		    imageDTO.getIID() + "_" + IMAGE_TYPE.THUMB.toString() + ".jpg",
		    imageIS,
		    objectMetadata);
	    s3Client.putObject(putObjectRequest);//.withAccessControlList(accessControlList));

	    return ("Image Saved");
	} catch (Exception ex) {
	    Logger.getLogger(AWSS3Utils.class.getName()).log(Level.SEVERE, null, ex);
	    return "Image Not Saved";
	} finally {
	    try {
		if (imageIS != null) {
		    imageIS.close();
		}
	    } catch (IOException ex) {
		Logger.getLogger(AWSS3Utils.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
    }

    public static ImageDTO loadImage(String bucketName, ImageDTO imageDTO, IMAGE_TYPE imageType) {
	Properties awsCredentialsProperties = new Properties();

	try {
	    awsCredentialsProperties.load(AWSS3Utils.class.getClassLoader().getResourceAsStream("prefs.properties"));
	} catch (Exception ex) {
	    Logger.getLogger(AWSS3Utils.class.getName()).log(Level.SEVERE, null, ex);
	}

	String awsAccessKey = awsCredentialsProperties.getProperty("AWSACCESSKEY");
	String awsSecretKey = awsCredentialsProperties.getProperty("SECRETACCESSKEY");

	try {
	    BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
	    AmazonS3 s3Client = new AmazonS3Client(awsCredentials);

	    GetObjectRequest request = null;

	    if (imageType.equals(IMAGE_TYPE.FULL)) {
		request = new GetObjectRequest(bucketName, imageDTO.getIID() + ".jpg");
	    } else if (imageType.equals(IMAGE_TYPE.THUMB)) {
		request = new GetObjectRequest(bucketName, imageDTO.getIID() + "_" + IMAGE_TYPE.THUMB.toString() + ".jpg");
	    }
	    S3Object object = s3Client.getObject(request);
	    S3ObjectInputStream objectContent = object.getObjectContent();
	     if (imageType.equals(IMAGE_TYPE.FULL)) {
		imageDTO.setImageBlob(objectContent);
	    } else if (imageType.equals(IMAGE_TYPE.THUMB)) {
		imageDTO.setThumbnailBlob(objectContent);
	    }
	    return (imageDTO);
	} catch (Exception ex) {
	    Logger.getLogger(AWSS3Utils.class.getName()).log(Level.SEVERE, null, ex);
	    return null;
	}
    }

}
