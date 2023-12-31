package model.dao.visit;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import model.dao.JDBCUtil;
import model.dto.review.Review;
import model.dto.visit.Visit;
import model.dto.visit.VisitNum;

public class VisitDAO {
	private JDBCUtil jdbcUtil = null;

	public VisitDAO() {
		jdbcUtil = new JDBCUtil();

	}

	public boolean createVisitor(Visit visit) {

		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO Visit (ownerId, visitorId, visitDate) VALUES (?, ?, ?)");

		java.util.Date currentDate = Calendar.getInstance().getTime();
		java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());
		Object[] param = new Object[] { visit.getOwnerId(), visit.getVisitorId(), sqlDate };

		jdbcUtil.setSqlAndParameters(query.toString(), param);

		try {
			ResultSet rs = jdbcUtil.executeQuery();
			if (rs.next()) {
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

	public List<VisitNum> getVisitNum(int ownerId, String startDate, String endDate) {

		StringBuilder query = new StringBuilder();

		query.append("SELECT TO_CHAR(visitDate, 'YYYY/MM/DD') AS visitDate, ");
		query.append("COUNT(visitDate) AS visitCount ");
		query.append("FROM Visit ");
		query.append("WHERE ownerId = ? AND visitDate BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD') ");
		query.append("GROUP BY TO_CHAR(visitDate, 'YYYY/MM/DD')");

		Object[] param = new Object[] { ownerId, startDate, endDate };

		jdbcUtil.setSqlAndParameters(query.toString(), param);

		try {
			List<VisitNum> visitNumList = new ArrayList<>();

			ResultSet rs = jdbcUtil.executeQuery();
			String visitDateStr;
			while (rs.next()) {
				VisitNum visit = new VisitNum();

				visit.setNum(rs.getInt("visitCount"));
				visitDateStr = rs.getString("visitDate");

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
				java.util.Date parsedDate = dateFormat.parse(visitDateStr);
				java.sql.Date visitDate = new java.sql.Date(parsedDate.getTime());

				visit.setVisitDate(visitDate);

				visitNumList.add(visit);
			}

			return visitNumList;
		} catch (Exception ex) {
			jdbcUtil.rollback();
			ex.printStackTrace();
		} finally {
			jdbcUtil.commit();
			jdbcUtil.close();
		}
		return null;

	}
}