package controller.review;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.Controller;
import model.dto.review.Review;
import model.service.Review.ReviewManager;

public class DeleteReviewController implements Controller{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
	
			String reviewIdStr = request.getParameter("reviewId");
			int reviewId = Integer.parseInt(reviewIdStr);
			ReviewManager manager = ReviewManager.getInstance();
		//////\
			manager.deleteReview(reviewId);
		   return "/diary/CalendarPage.jsp";
		   
	}

}