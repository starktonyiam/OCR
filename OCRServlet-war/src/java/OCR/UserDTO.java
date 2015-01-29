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
public class UserDTO {
    Long uID, creatorUID;
    String name, email, phone, pwd, resetPwd;
    boolean fwdShare, claimedAcc;

    public boolean isFwdShare() {
	return fwdShare;
    }

    public void setFwdShare(boolean fwdShare) {
	this.fwdShare = fwdShare;
    }

    public boolean isClaimedAcc() {
	return claimedAcc;
    }

    public void setClaimedAcc(boolean claimedAcc) {
	this.claimedAcc = claimedAcc;
    }

    public Long getCreatorUID() {
	return creatorUID;
    }

    public void setCreatorUID(Long creatorUID) {
	this.creatorUID = creatorUID;
    }
    
    public Long getUID() {
	return uID;
    }

    public void setUID(Long uID) {
	this.uID = uID;
    }

    public String getName() {
	return name;
    }

    public void setName(String userName) {
	this.name = userName;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String userEmail) {
	this.email = userEmail;
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone(String userPhone) {
	this.phone = userPhone;
    }

    public String getPwd() {
	return pwd;
    }

    public void setPwd(String userPwd) {
	this.pwd = userPwd;
    }

    public String getResetPwd() {
	return resetPwd;
    }

    public void setResetPwd(String userResetPwd) {
	this.resetPwd = userResetPwd;
    }
}
