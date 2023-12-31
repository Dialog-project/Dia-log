package controller;

import java.util.HashMap;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.board.BoardController;
import controller.board.BoardViewController;
import controller.board.BoardWriteController;
import controller.board.DeleteActionController;
import controller.board.EditBoardController;
import controller.board.ViewBoardController;
import controller.board.WriteActionController;
import controller.board.WriteUpdateActionController;
import controller.contents.ContentsHallOfFameController;
import controller.contents.ListContentsController;
import controller.contents.ListReviewsController;
import controller.contents.PickContentsController;
import controller.friend.FriendListController;
import controller.login.LoginController;
import controller.login.MyPageUpdateController;
import controller.login.MypageController;
import controller.login.RegisterController;
import controller.login.RegisterPageController;
import controller.diary.DiaryController;
import controller.review.ReadReviewForDateController;
import controller.review.UpdateReviewController;
import controller.visit.ReadOverviewController;
import controller.visit.ReadVisitorController;
import controller.review.DeleteReviewController;

import controller.review.FilterReviewController;
import controller.contents.SearchContentsController;

public class RequestMapping {
	private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

	// 각 요청 uri에 대한 controller 객체를 저장할 HashMap 생성
	private Map<String, Controller> mappings = new HashMap<String, Controller>();

	public void initMapping() {
		// 각 uri에 대응되는 controller 객체를 생성 및 저장
		mappings.put("/", new ForwardController("index.jsp"));

		// 메인 컨텐츠 관련 request URI 추가
		mappings.put("/contents", new ForwardController("/contents/Contents.jsp"));
		mappings.put("/contents/list", new ListContentsController());
		mappings.put("/contents/search", new SearchContentsController());
		mappings.put("/contents/pick", new PickContentsController());
		mappings.put("/contents/reviewList", new ListReviewsController());
		mappings.put("/contents/hallOfFame", new ContentsHallOfFameController());

		// 다이어리 관련 request URI 추가
		mappings.put("/diary", new DiaryController());
		mappings.put("/diary/reviewList", new ReadReviewForDateController());
		mappings.put("/diary/filter", new FilterReviewController());
		mappings.put("/diary/filter/genre", new FilterReviewController());
		mappings.put("/review/delete", new DeleteReviewController());
		mappings.put("/review/update", new UpdateReviewController());

		// 통계 관련 request URI 추가
		mappings.put("/readOverview", new ReadOverviewController());
		mappings.put("/readVisitor", new ReadVisitorController());

		// 친구 관련 request URI 추가
		mappings.put("/friend", new FriendListController());
		mappings.put("/friend/request", new FriendListController());
		mappings.put("/friend/request/send", new FriendListController());
		mappings.put("/friend/request/receive", new FriendListController());
		mappings.put("/friend/search", new FriendListController());
		mappings.put("/friend/list/follower", new FriendListController());
		mappings.put("/friend/list/followee", new FriendListController());
		mappings.put("/friend/list/recommend", new FriendListController());
		mappings.put("/friend/delete/follower", new FriendListController());
		mappings.put("/friend/delete/followee", new FriendListController());

		// 유저 관련 request URI 추가
		mappings.put("/login", new LoginController()); // 여기에 추가
		mappings.put("/login/registerPage", new RegisterPageController()); // 여기에 추가
		mappings.put("/login/register", new RegisterController()); // 여기에 추가
		mappings.put("/mypage", new MypageController()); // 여기에 추가
		mappings.put("/mypage/update", new MyPageUpdateController()); // 여기에 추가

		// 게시판 관련 request URI 추가
		mappings.put("/board", new BoardController());
		mappings.put("/board/list", new BoardViewController());
		mappings.put("/board/delete", new DeleteActionController());
		mappings.put("/board/write", new BoardWriteController());
		mappings.put("/board/write/redirect", new BoardWriteController());
		mappings.put("/board/writeAction", new WriteActionController());
		mappings.put("/board/writeupdate", new EditBoardController());
		mappings.put("/board/writeupdateAction", new WriteUpdateActionController());
		mappings.put("/board/view", new ViewBoardController());
		
		logger.info("Initialized Request Mapping!");

	}

	public Controller findController(String uri) {
		// 주어진 uri에 대응되는 controller 객체를 찾아 반환
		return mappings.get(uri);
	}
}