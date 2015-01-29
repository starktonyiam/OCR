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
public class ProfileDTO {

    String profileId;
    Long userId;
    String profileName, profileDetails;

    public String getProfileId() {
	return profileId;
    }

    public void setProfileId(String profileId) {
	this.profileId = profileId;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public String getProfileName() {
	return profileName;
    }

    public void setProfileName(String profileName) {
	this.profileName = profileName;
    }

    public String getProfileDetails() {
	return profileDetails;
    }

    public void setProfileDetails(String profileDetails) {
	this.profileDetails = profileDetails;
    }
}
