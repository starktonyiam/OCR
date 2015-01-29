/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OCR;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author starktony
 */
public class ProfileServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	String returnStatus = "";
	PrintWriter printWriter = null;
	ProfileDTO profileDTO = new ProfileDTO();
	ProfileDAO profileDAO = new ProfileDAO();

	OutputStream outputStream;

	String postAction = request.getParameter("action").toUpperCase();

	switch (postAction) {
	    case "SAVEPROFILE":
		profileDTO.setProfileId(System.currentTimeMillis() + "("
			+ request.getParameter("userid") + ")");
		profileDTO.setUserId(Long.parseLong(request.getParameter("userid")));
		profileDTO.setProfileName(request.getParameter("profilename"));
		profileDTO.setProfileDetails(request.getParameter("profiledetails"));

		returnStatus = profileDAO.saveProfile(profileDTO);
		break;

	    case "LOADPROFILE":
		profileDTO.setProfileId(request.getParameter("profileid"));
		profileDTO = profileDAO.loadProfile(profileDTO);

		if (profileDTO.getProfileName() == null) {
		    returnStatus = "Profile Not Found";
		} else {
		    returnStatus = profileDTO.getProfileId()
			    + ";" + profileDTO.getUserId()
			    + ";" + profileDTO.getProfileName()
			    + ";" + profileDTO.getProfileDetails();
		}
		break;

	    case "UPDATEPROFILE":
		profileDTO.setProfileId(request.getParameter("profileid"));
		profileDTO.setProfileName(request.getParameter("profilename"));
		profileDTO.setProfileDetails(request.getParameter("profiledetails"));

		returnStatus = profileDAO.updateProfile(profileDTO);
		break;

	    case "DELETEPROFILE":
		profileDTO.setProfileId(request.getParameter("profileid"));

		returnStatus = profileDAO.deleteProfile(profileDTO);
		break;
		
	    case "LOADPROFILESLIST":
		profileDTO.setUserId(Long.parseLong(request.getParameter("userid")));
		List<ProfileDTO> profileDTOList = new ArrayList<>();
		profileDTOList = profileDAO.loadProfilesList(profileDTO);
		for (ProfileDTO profileDTOelement : profileDTOList ){
		returnStatus += profileDTOelement.getProfileId() + ";";
		}
		break;
	}

	try {
	    response.reset();
	    response.setContentType("plain/text");
	    printWriter = response.getWriter();
	    printWriter.println(returnStatus);
	} catch (Exception e) {
	    printWriter.println("Error in UserServlet Response");
	}
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
	return "Short description";
    }// </editor-fold>

}
