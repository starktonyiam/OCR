/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OCR;

/**
 *
 * @author starktony
 */
public class ImageDTO {
    String iID, uID, creatorID;
    byte[] imageBlob, thumbnailBlob;
    String rawOCRClob, procOCRClob;

    public String getCreatorID() {
	return creatorID;
    }

    public void setCreatorID(String creatorID) {
	this.creatorID = creatorID;
    }

    public String getProcOCRClob() {
	return procOCRClob;
    }

    public void setProcOCRClob(String procOCRClob) {
	this.procOCRClob = procOCRClob;
    }

    public String getIID() {
	return iID;
    }

    public void setIID(String iID) {
	this.iID = iID;
    }

    public String getUID() {
	return uID;
    }

    public void setUID(String uID) {
	this.uID = uID;
    }

    public byte[]  getImageBlob() {
	return imageBlob;
    }

    public void setImageBlob(byte[]  imageBlob) {
	this.imageBlob = imageBlob;
    }

    public byte[]  getThumbnailBlob() {
	return thumbnailBlob;
    }

    public void setThumbnailBlob(byte[]  thumbnailBlob) {
	this.thumbnailBlob = thumbnailBlob;
    }

    public String getRawOCRClob() {
	return rawOCRClob;
    }

    public void setRawOCRClob(String rawOCRClob) {
	this.rawOCRClob = rawOCRClob;
    }   
}
