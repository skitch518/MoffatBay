package userStuff;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/Reserve")
public class ReservationServerlet  extends HttpServlet {
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		
		//get all parameters from form
    	String email = request.getParameter("email");
	    String checkin = request.getParameter("checkin");
	    String checkout =  request.getParameter("checkout");
	    int guests = Integer.valueOf(request.getParameter("guests"));
	    String roomType =  request.getParameter("roomType");
	    int length =  Integer.valueOf(request.getParameter("length"));
	    
	    //set all values
	    ReservationManager res = new ReservationManager();
	    //customer id is found using customer's email
	    res.setCustomerID(email);
	    res.setCheckIn(checkin);
	    res.setCheckOut(checkout);
	    res.setLengthOfStay(length);
	    res.setNumberOfGuests(guests);
	    res.setSuiteID(roomType);
	    res.setPrice(roomType, length);
	    
	    //check if room is available and redirect
	    if(res.isAvailable()) {
	    	 res.submitReservation();
	    	 System.out.println("reservation submitted");
	 	    	
	 	    if ("confirmed".equals(res.getStatus())) {
	 	    	
	 	    	request.setAttribute("confirmed", "submitted");
	 	         request.getRequestDispatcher("reservationConfirmed.jsp").forward(request, response);
	             
	         }
	 	    //in the case that something unexpected happens
	 	    else {
	 	    	 request.setAttribute("confirmed", "failed");
	 	         request.getRequestDispatcher("reservationConfirmed.jsp").forward(request, response);
	 	      
	 	    }
	    	
	    }else {
	    	request.setAttribute("confirmed", "unavailableRoom");
	        request.getRequestDispatcher("reservationConfirmed.jsp").forward(request, response);
	      

	    }
	   
	    
	    res.close();
	    
	}

}