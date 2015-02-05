/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OCR;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.tess4j.*;

/**
 *
 * @author starktony
 */
@MultipartConfig
public class ImageServlet extends HttpServlet {

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

	try {
	    String returnStatus = "";
	    PrintWriter printWriter = null;
	    String postAction = request.getParameter("action").toUpperCase();
	    ImageDTO imageDTO = new ImageDTO();
	    ImageDAO imageDAO = new ImageDAO();
	    OutputStream outputStream;

	    switch (postAction) {
		case "SAVEIMAGE":
		    Properties thumbnailProperties = new Properties();

		    try {
			thumbnailProperties.load(this.getClass().getClassLoader().getResourceAsStream("prefs.properties"));
		    } catch (Exception ex) {
			Logger.getLogger(ImageServlet.class.getName()).log(Level.SEVERE, null, ex);

		    }

		    ImageIO.scanForPlugins();
		    Tesseract instance = Tesseract.getInstance();  // JNA Interface Mapping
		    instance.setDatapath("C:\\Users\\i076631\\Desktop\\OCR\\Tess4J-1.3-src\\Tess4J");

		    InputStream imageIS = request.getPart("image").getInputStream();
		    BufferedImage imageBI = ImageIO.read(imageIS);
		    ByteArrayOutputStream imageBAOS = new ByteArrayOutputStream();
		    ImageIO.write(imageBI, "jpg", imageBAOS);
		    
		    int thumbnailWidth = Integer.parseInt(thumbnailProperties.getProperty("WIDTH"));
		    int thumbnailHeight = Integer.parseInt(thumbnailProperties.getProperty("HEIGHT"));
		    ByteArrayOutputStream thumbnailBAOS = new ByteArrayOutputStream();
		    BufferedImage thumbnailBI = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_RGB);
		    thumbnailBI.createGraphics().drawImage(imageBI.getScaledInstance(thumbnailWidth, thumbnailHeight, BufferedImage.SCALE_SMOOTH), 0, 0, null);
		    ImageIO.write(thumbnailBI, "jpg", thumbnailBAOS);

		    BufferedImage imageBIGray = new BufferedImage(imageBI.getWidth(), imageBI.getHeight(),
			    BufferedImage.TYPE_BYTE_BINARY);
		    Graphics2D g = imageBIGray.createGraphics();
		    g.drawImage(imageBI, 0, 0, null);
		    
		    imageDTO.setIID(System.currentTimeMillis()
			    + "(" + request.getParameter("uid") + ")"
			    + "(" + request.getParameter("cid") + ")");
		    imageDTO.setUID(request.getParameter("uid"));
		    imageDTO.setCreatorID(request.getParameter("cid"));
		    imageDTO.setImageBlob(new ByteArrayInputStream(imageBAOS.toByteArray()));
		    imageDTO.setThumbnailBlob(new ByteArrayInputStream(thumbnailBAOS.toByteArray()));
		    imageDTO.setRawOCRClob(instance.doOCR(imageBIGray));

		    returnStatus = imageDAO.saveImage(imageDTO);
		    break;

		case "LOADTHUMBNAILSLIST":
		    ProfileDTO profileDTO = new ProfileDTO();

		    profileDTO.setProfileId(request.getParameter("profileid"));

		    List<ImageDTO> imageDTOList = imageDAO.loadThumbnailList(profileDTO);

		    for (ImageDTO imageDTOelement : imageDTOList) {
			returnStatus += imageDTOelement.getIID() + ";";
		    }
		    break;

		case "LOADTHUMBNAIL":
		    imageDTO.setIID(request.getParameter("iid"));
		    imageDTO = imageDAO.loadThumbnail(imageDTO);

		    if (imageDTO.getThumbnailBlob() == null) {
			returnStatus = "Thumbnail Not Found";
		    } else {
			response.setContentType("image/jpeg");
			outputStream = response.getOutputStream();
			ImageIO.write(ImageIO.read(imageDTO.getThumbnailBlob()), "jpg", outputStream);
			outputStream.close();
		    }
		    break;

		case "LOADIMAGE":
		    imageDTO.setIID(request.getParameter("iid"));
		    imageDTO = imageDAO.loadImage(imageDTO);

		    if (imageDTO.getImageBlob() == null) {
			returnStatus = "Image Not Found";
		    } else {
			response.setContentType("image/jpeg");
			outputStream = response.getOutputStream();
			ImageIO.write(ImageIO.read(imageDTO.getImageBlob()), "jpg", outputStream);
			outputStream.close();
		    }
		    break;

		case "DELETEIMAGE":
		    imageDTO.setIID(request.getParameter("iid"));
		    returnStatus = imageDAO.deleteImage(imageDTO);
		    break;

	    }

	    try {
		response.reset();
		response.setContentType("plain/text");
		printWriter = response.getWriter();
		printWriter.println(returnStatus);
	    } catch (Exception e) {
		printWriter.println("Error in ImageServlet Response");
	    }
	} catch (Exception ex) {
	    Logger.getLogger(ImageServlet.class.getName()).log(Level.SEVERE, null, ex);
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
