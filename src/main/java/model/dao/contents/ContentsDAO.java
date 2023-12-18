package model.dao.contents;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.dao.JDBCUtil;
import model.dto.contents.Contents;
import model.dto.contents.Contents.ContentType;
import model.dto.review.Review;

public class ContentsDAO {
	private JDBCUtil jdbcUtil = null;

	public ContentsDAO() {
		jdbcUtil = new JDBCUtil(); // JDBCUtil 객체 생성
	}

	public List<Contents> getContentList() {
		String sql = "SELECT c.contentId, c.contentImg, c.contentType, c.title, c.genre, c.publishDate FROM Contents c";

		jdbcUtil.setSqlAndParameters(sql, null);
		try {
			ResultSet rs = jdbcUtil.executeQuery();
			List<Contents> contentList = new ArrayList<Contents>();

			while (rs.next()) {
				Contents cont = new Contents();

				cont.setContentId(rs.getInt("contentId"));
				cont.setContentImg(rs.getString("contentImg"));
				cont.setContentType(ContentType.valueOf(rs.getString("contentType")));
				cont.setTitle(rs.getString("title"));
				cont.setGenre(rs.getString("genre"));
				cont.setPublishDate(rs.getDate("publishDate"));

				contentList.add(cont);
			}
			return contentList;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			jdbcUtil.close();
		}
		return null;
	}

	public List<Review> getReviewList(int contentId) {
	    String sql = "SELECT contentId, detail " +
	                 "FROM Review r WHERE contentId = ?";

	    Object[] param = new Object[] { contentId };

	    jdbcUtil.setSqlAndParameters(sql, param);

	    try {
	        ResultSet rs = jdbcUtil.executeQuery();
	        List<Review> reviewList = new ArrayList<Review>();

	        while (rs.next()) {
	            Review review = new Review();

	            review.setContentId(rs.getInt("contentId"));
	            review.setDetail(rs.getString("detail"));

	            reviewList.add(review);
	        }
	        return reviewList;

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        jdbcUtil.close();
	    }
	    return null;
	}

	public List<Contents> searchContentsByTitle(String title) {
		List<Contents> contentList = new ArrayList<>();

		try {
			String sql = "SELECT * FROM Contents WHERE title LIKE ?";
			String keyword = "%" + title + "%";

			jdbcUtil.setSqlAndParameters(sql, new Object[] { keyword });
			ResultSet rs = jdbcUtil.executeQuery();

			while (rs.next()) {
				Contents cont = new Contents();

				cont.setContentId(rs.getInt("contentId"));
				cont.setContentImg(rs.getString("contentImg"));
				cont.setReviews(null);
				cont.setContentType(ContentType.valueOf(rs.getString("contentType")));
				cont.setTitle(rs.getString("title"));
				cont.setGenre(rs.getString("genre"));
				cont.setPublishDate(rs.getDate("publishDate"));

				contentList.add(cont);
			}
		} catch (Exception ex) {
			jdbcUtil.rollback(); // 트랜잭션 rollback 실행
			ex.printStackTrace();
		} finally {
			jdbcUtil.commit(); // 트랜잭션 commit 실행
			jdbcUtil.close();
		}

		return contentList;
	}
	
	public Contents getContentsById(int contentId) {
		Contents contents = null;

		try {
			String sql = "SELECT * FROM Contents WHERE contentId = ?";

			jdbcUtil.setSqlAndParameters(sql, new Object[] { contentId });
			ResultSet rs = jdbcUtil.executeQuery();

			if (rs.next()) {
				contents = new Contents();

				contents.setContentId(rs.getInt("contentId"));
				contents.setContentImg(rs.getString("contentImg"));
				contents.setContentType(ContentType.valueOf(rs.getString("contentType")));
				contents.setTitle(rs.getString("title"));
				contents.setGenre(rs.getString("genre"));
				contents.setPublishDate(rs.getDate("publishDate"));
			}
		} catch (Exception ex) {
			jdbcUtil.rollback(); // 트랜잭션 rollback 실행
			ex.printStackTrace();
		} finally {
			jdbcUtil.commit(); // 트랜잭션 commit 실행
			jdbcUtil.close();
		}

		return contents;
	}

	public static void main(String[] args) throws SQLException {
		ContentsDAO dao = new ContentsDAO();
		System.out.println(dao.getReviewList(0));
	}
}
