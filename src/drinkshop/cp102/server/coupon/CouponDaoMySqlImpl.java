package drinkshop.cp102.server.coupon;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import drinkshop.cp102.server.main.Common;
import drinkshop.cp102.server.main.Helper;

public class CouponDaoMySqlImpl implements CouponDao {

	public CouponDaoMySqlImpl() {
		super();
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int insert(Coupon coupon) {
		int count = 0;
		String sql = "INSERT INTO coupon"
				+ "(member_id, coupon_no, coupon_discount, coupon_status, coupon_start, coupon_end) "
				+ "VALUES(?, ?, ?, ?, ?, ?);";
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			ps = connection.prepareStatement(sql);
			ps.setInt(1, coupon.getMember_id());
			ps.setString(2, coupon.getCoupon_no());
			ps.setBigDecimal(3,  new BigDecimal(coupon.getCoupon_discount()));
			ps.setString(4, "0");//預設優惠卷未使用
			ps.setDate(5, Date.valueOf(coupon.getCoupon_start()));
			ps.setDate(6, Date.valueOf(coupon.getCoupon_end()));
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					// When a Statement object is closed,
					// its current ResultSet object is also closed
					ps.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	@Override
	public int updateCouponStatus(int coupon_id, int member_id) {
		int count = 0;
		String sql = "UPDATE coupon SET coupon_status = ? WHERE coupon_id = ? and member_id = ?;";
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			ps = connection.prepareStatement(sql);
			ps.setString(1, "1");
			ps.setInt(2, coupon_id);
			ps.setInt(3, member_id);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					// When a Statement object is closed,
					// its current ResultSet object is also closed
					ps.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	@Override
	public Coupon findCouponById(int query_coupon_id) {
		String sql = "SELECT coupon_id, member_id, coupon_no, coupon_discount, coupon_status, coupon_start, coupon_end"
				+ " FROM coupon WHERE coupon_id = ?;";
		Connection conn = null;
		PreparedStatement ps = null;
		Coupon coupon = null;
		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, query_coupon_id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int coupon_id = rs.getInt(1);
				int member_id = rs.getInt(2);
				String coupon_no = rs.getString(3);
				float coupon_discount = rs.getBigDecimal(4) != null ? rs.getBigDecimal(4).floatValue() : 10;
				String coupon_status = rs.getString(5);
				String coupon_start = Helper.getFmtdDateToStr(rs.getDate(6));
				String coupon_end = Helper.getFmtdDateToStr(rs.getDate(7));
				
				coupon = new Coupon(coupon_id, member_id, coupon_no, coupon_end, coupon_status, coupon_start, coupon_discount);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return coupon;
	}
	
	
	@Override
	public List<Coupon> getAllCouponsByMemberId(int query_member_id) {
		String sql = "SELECT coupon_id, member_id, coupon_no, coupon_discount, coupon_status, coupon_start, coupon_end FROM coupon WHERE member_id = ? Order By coupon_end DESC;";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, query_member_id);
			ResultSet rs = ps.executeQuery();
			List<Coupon> couponList = new ArrayList<Coupon>();
			while (rs.next()) {
				int coupon_id = rs.getInt(1);
				int member_id = rs.getInt(2);
				String coupon_no = rs.getString(3);
				float coupon_discount = rs.getBigDecimal(4) != null ? rs.getBigDecimal(4).floatValue() : 10;
				String coupon_status = rs.getString(5);
				String coupon_start = Helper.getFmtdDateToStr(rs.getDate(6));
				String coupon_end = Helper.getFmtdDateToStr(rs.getDate(7));
				
				Coupon coupon = new Coupon(coupon_id, member_id, coupon_no, coupon_status, coupon_start, coupon_end, coupon_discount);
				couponList.add(coupon);
			}
			return couponList;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public List<Coupon> getAllCoupons(String useStatus) {
		String sql = "SELECT coupon_id, member_id, coupon_no, coupon_discount, coupon_status, coupon_start, coupon_end FROM coupon WHERE coupon_status = ? Order By coupon_end DESC;";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			ps = conn.prepareStatement(sql);
			ps.setString(1, useStatus);
			ResultSet rs = ps.executeQuery();
			List<Coupon> couponList = new ArrayList<Coupon>();
			while (rs.next()) {
				int coupon_id = rs.getInt(1);
				int member_id = rs.getInt(2);
				String coupon_no = rs.getString(3);
				float coupon_discount = rs.getBigDecimal(4) != null ? rs.getBigDecimal(4).floatValue() : 10;
				String coupon_status = rs.getString(5);
				String coupon_start = Helper.getFmtdDateToStr(rs.getDate(6));
				String coupon_end = Helper.getFmtdDateToStr(rs.getDate(7));
				
				Coupon coupon = new Coupon(coupon_id, member_id, coupon_no, coupon_status, coupon_start, coupon_end, coupon_discount);
				couponList.add(coupon);
			}
			return couponList;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
