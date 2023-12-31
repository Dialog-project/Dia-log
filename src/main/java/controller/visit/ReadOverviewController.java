package controller.visit;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import controller.Controller;
import controller.diary.DiaryController;
import model.dto.review.ReviewDiary;
import model.dto.review.ReviewTypeNum;
import model.dto.visit.VisitNum;
import model.service.review.ReviewManager;
import model.service.visit.VisitManager;

public class ReadOverviewController implements Controller {
	private static final Logger logger = LoggerFactory.getLogger(ReadOverviewController.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");

		ReviewManager manager = ReviewManager.getInstance();
		VisitManager visitMan = VisitManager.getInstance();

		if (userId == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return null;
		}

		// 미디어별 통계
		List<ReviewTypeNum> reviewTypeNumList = manager.getReviewByType(userId);

		ObjectMapper objectMapper = new ObjectMapper();
		String jsonResult = objectMapper.writeValueAsString(reviewTypeNumList);

		request.setAttribute("reviewTypeNumJsonResult", jsonResult);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate currentDate = LocalDate.now();
		String currentDateString = currentDate.format(formatter);

		LocalDate oneMonthAgo = currentDate.minusMonths(1);
		LocalDate currentDatePlusOne = currentDate.plusDays(1);
		String currentDatePlusOneString = currentDatePlusOne.format(formatter);
		String oneMonthAgoString = oneMonthAgo.format(formatter);

		List<VisitNum> visitNum = visitMan.getVisitNum(userId, oneMonthAgoString, currentDatePlusOneString);

		ObjectMapper objectMapper2 = new ObjectMapper();
		String jsonVisitNumList = objectMapper2.writeValueAsString(visitNum);
		logger.info("visitNum" + jsonVisitNumList);

		// 장르별 통계
		List<ReviewTypeNum> reviewGenreNumList = manager.getReviewByGenreNum(userId);
		ObjectMapper objectMapper3 = new ObjectMapper();
		String reviewGenreNumListJson = objectMapper3.writeValueAsString(reviewGenreNumList);

		request.setAttribute("jsonVisitNumList", jsonVisitNumList);
		request.setAttribute("startDateForVisit", oneMonthAgoString);
		request.setAttribute("endDateForVisit", currentDateString);
		request.setAttribute("genreNumList", reviewGenreNumListJson);

		return "/diary/overview.jsp";
	}

}
