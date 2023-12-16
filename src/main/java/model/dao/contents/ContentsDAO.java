package model.dao.contents;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.dao.JDBCUtil;
import model.dto.contents.Contents;
import model.dto.contents.Contents.ContentType;
import model.dto.contents.Movie;

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
			SimpleDateFormat dtFormat = new SimpleDateFormat("yy/mm/dd");

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

	public boolean pickContent(int userId, int contentId) {
		LocalDateTime currentDateTime = LocalDateTime.now();
		LocalDate currentDate = LocalDate.now();
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO Review (createdAt, updatedAt, watchedAt, contentId, writerId) VALUES (?, ?, ?,?, ?)");

		Object[] param = new Object[] { currentDateTime, currentDateTime, currentDate, contentId, userId };

		jdbcUtil.setSqlAndParameters(query.toString(), param); // JDBCUtil 에 insert문과 매개 변수 설정

		try {
			int affectedRows = jdbcUtil.executeUpdate(); // executeUpdate 사용
			if (affectedRows > 0) {
				return true;
			}
		} catch (Exception ex) {
			jdbcUtil.rollback(); // 트랜잭션 rollback 실행
			ex.printStackTrace();
		} finally {
			jdbcUtil.commit(); // 트랜잭션 commit 실행
			jdbcUtil.close();
		}
		return false;
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
//		ContentsDAO dao = new ContentsDAO();
//		System.out.println(dao.getContentList());
	}
}
