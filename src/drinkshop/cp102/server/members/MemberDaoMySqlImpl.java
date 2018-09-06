package drinkshop.cp102.server.members;

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

public class MemberDaoMySqlImpl implements MemberDao {

	public MemberDaoMySqlImpl() {
		super();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int insert(Member member) {
		int count = 0;
		String sql = "INSERT INTO member"
				+ "(member_account, member_password, member_name, member_birthday, member_sex, member_mobile, member_email, member_address, member_status) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			ps = connection.prepareStatement(sql);
			ps.setString(1, member.getMember_account());
			ps.setString(2, member.getMember_password());
			ps.setString(3, member.getMember_name());
//			ps.setDate(4, (Date) Helper.getFmtStrToDate(member.getMember_birthday()));
			ps.setDate(4, (Date) Helper.validDate(null));
			ps.setString(5, member.getMember_sex());
			ps.setString(6, member.getMember_mobile());
			ps.setString(7, member.getMember_email());
			ps.setString(8, member.getMember_address());
			ps.setString(9, "1");//預設啟用
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
	public int update(Member member) {
		int count = 0;
		String sql = "UPDATE member SET member_password = ?, member_name = ?, member_birthday = ?, member_sex = ?, member_mobile = ?, member_email = ?, member_address = ? WHERE member_id = ?;";
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			ps = connection.prepareStatement(sql);
			ps.setString(1, member.getMember_password());
			ps.setString(2, member.getMember_name());
			//ps.setDate(3, (Date) Helper.getFmtStrToDate(member.getMember_birthday()));
			ps.setDate(3, (Date) Helper.validDate(null));
			ps.setString(4, member.getMember_sex());
			ps.setString(5, member.getMember_mobile());
			ps.setString(6, member.getMember_email());
			ps.setString(7, member.getMember_address());
			ps.setInt(7, member.getMember_id());
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
	public int delete(int member_id) {
		int count = 0;
		String sql = "DELETE FROM member WHERE member_id = ?;";
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			ps = connection.prepareStatement(sql);
			ps.setInt(1, member_id);
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
	public Member findById(int member_id) {
		String sql = "SELECT member_account, member_password, member_name, member_birthday, member_sex, member_mobile, member_email,member_address FROM member WHERE member_id = ?;";
		Connection conn = null;
		PreparedStatement ps = null;
		Member member = null;
		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, member_id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String member_account = rs.getString(1);
				String member_password = rs.getString(2);
				String member_name = rs.getString(3);
				String member_birthday = Helper.getFmtDateTimeToStr(rs.getDate(4));
				String member_sex = rs.getString(5);
				String member_mobile = rs.getString(6);
				String member_email = rs.getString(7);
				String member_address = rs.getString(8);
				String member_status = rs.getString(9);
				
				member = new Member(member_id, member_account, member_password, member_name, member_birthday, member_sex, member_mobile, member_email, member_address, member_status);
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
		return member;
	}

	@Override
	public List<Member> getAll() {
		String sql = "SELECT member_id, member_account, member_password, member_name, member_birthday, member_sex, member_mobile, member_email, member_address, member_status FROM member;";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			List<Member> memberList = new ArrayList<Member>();
			while (rs.next()) {
				int member_id = rs.getInt(1);
				String member_account = rs.getString(2);
				String member_password = rs.getString(3);
				String member_name = rs.getString(4);
				String member_birthday = Helper.getFmtDateTimeToStr(rs.getDate(5));
				String member_sex = rs.getString(6);
				String member_mobile = rs.getString(7);
				String member_email = rs.getString(8);
				String member_address = rs.getString(9);
				String member_status = rs.getString(10);
				Member member = new Member(member_id, member_account, member_password, member_name, member_birthday, member_sex, member_mobile, member_email, member_address, member_status);
				memberList.add(member);
			}
			return memberList;
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
