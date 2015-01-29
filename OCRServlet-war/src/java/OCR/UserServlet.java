/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OCR;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author starktony
 */
public class UserServlet extends HttpServlet {

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
	UserDTO userDTO = new UserDTO();
	UserDAO userDAO = new UserDAO();

	OutputStream outputStream;

	String postAction = request.getParameter("action").toUpperCase();

	switch (postAction) {
	    case "SAVEUSER":
		userDTO.setEmail(request.getParameter("email"));
		userDTO.setName(request.getParameter("name"));
		userDTO.setPhone(request.getParameter("phone"));
		userDTO.setCreatorUID(Long.parseLong(request.getParameter("cuid")));
		userDTO.setFwdShare(Boolean.valueOf((request.getParameter("fwdshare"))));

		if (userDTO.getCreatorUID() == 0) {
		    userDTO.setClaimedAcc(true);
		} else {
		    userDTO.setClaimedAcc(false);
		}
		userDTO.setPwd(request.getParameter("pwd"));

		returnStatus = userDAO.saveUser(userDTO);
		break;

	    case "LOADUSER":
		userDTO.setName(request.getParameter("name"));
		userDTO.setPwd(request.getParameter("pwd"));
		userDTO = userDAO.loadUser(userDTO);

		if (userDTO.getUID() == null) {
		    returnStatus = "User Not Found";
		} else {
		    returnStatus = userDTO.getUID() + ";" + userDTO.getName() + ";" + userDTO.getEmail()
			    + ";" + userDTO.getPhone();
		}
		break;

	    case "UPDATEUSER":
		userDTO.setUID(Long.parseLong(request.getParameter("uid")));
		userDTO.setName(request.getParameter("name"));
		userDTO.setEmail(request.getParameter("email"));
		userDTO.setPhone(request.getParameter("phone"));
		userDTO.setPwd(request.getParameter("pwd"));

		returnStatus = userDAO.updateUser(userDTO);
		break;

	    case "DELETEUSER":
		userDTO.setUID(Long.parseLong(request.getParameter("uid")));

		returnStatus = userDAO.deleteUser(userDTO);
		break;
	    
	    case "SHAREUSER":
		userDTO.setUID(Long.parseLong(request.getParameter("uid")));
		userDTO.setName(request.getParameter("name"));
		userDTO.setEmail(request.getParameter("email"));

		returnStatus = userDAO.shareUser(userDTO);
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
