package drinkshop.cp102.server.news;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import drinkshop.cp102.server.main.Common;


public class NewsDaoMySqlImpl implements NewsDao {

		public NewsDaoMySqlImpl() {
			super();
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		@Override
		public int insert(News news, byte[] image) {
			int count = 0;  
			String sql = "INSERT INTO activity "  
					+ "(activity_name, activity_date_start, activity_date_end, activity_pic1) "
					+ "VALUES (?, ?, ?, ?);";
			Connection connection = null;
			PreparedStatement ps = null;
			try {
				connection = DriverManager.getConnection(Common.URL, Common.USER,
						Common.PASSWORD);
				ps = connection.prepareStatement(sql);
				ps.setString(1, news.getActivity_name());
				ps.setDate(2, Date.valueOf(news.getActivity_date_start()));
				ps.setDate(3, Date.valueOf(news.getActivity_date_end()));
				ps.setBytes(4, image);
				count = ps.executeUpdate(); //如果新增成功可能會得到1
				
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
		public int update(News news, byte[] image) {
			int count = 0;
			String sql = "UPDATE activity SET activity_name = ?, activity_date_start = ?, activity_date_end = ?,  activity_pic1 = ? WHERE activity_id = ?;";
			Connection connection = null;
			PreparedStatement ps = null;
			try {
				connection = DriverManager.getConnection(Common.URL, Common.USER,
						Common.PASSWORD);
				ps = connection.prepareStatement(sql);
				ps.setString(1, news.getActivity_name());     
				ps.setString(2, news.getActivity_date_start());
				ps.setString(3, news.getActivity_date_end());
				ps.setBytes(4, image);
				ps.setInt(5, news.getActivity_id());
				count = ps.executeUpdate(); //成功異動比數
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
		public int delete(int id) {
			int count = 0;
			String sql = "DELETE FROM activity WHERE activity_id = ?;";
			Connection connection = null;
			PreparedStatement ps = null;
			try {
				connection = DriverManager.getConnection(Common.URL, Common.USER,
						Common.PASSWORD);
				ps = connection.prepareStatement(sql);
				ps.setInt(1, id);
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
		public byte[] getImage(int id) {
			String sql = "SELECT activity_pic1 FROM activity WHERE activity_id = ?;";
			Connection connection = null;
			PreparedStatement ps = null;
			byte[] image = null;
			try {
				connection = DriverManager.getConnection(Common.URL, Common.USER,
						Common.PASSWORD);
				ps = connection.prepareStatement(sql);
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					image = rs.getBytes(1);
				}
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
			return image;
		}
		
		@Override
		public List<News> getNews_activity_id() {
			String sql = "SELECT activity_id FROM activity;";
			Connection connection = null;
			PreparedStatement ps = null;
			List<News> activity_id = new ArrayList<>();
			try {
				connection = DriverManager.getConnection(Common.URL, Common.USER,
						Common.PASSWORD);
				ps = connection.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					int id = rs.getInt(1);
					System.out.println("id = " + id);
					News news = new News(id);
					activity_id.add(news);
				}
				return activity_id;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (ps != null) {
						ps.close();
					}
					if (connection != null) {
						connection.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return activity_id;
		}
		
		@Override
		public News findById(int id) {
			String sql = "SELECT activity_name, activity_date_start, activity_date_end FROM activity WHERE activity_id = ?;";
			Connection conn = null;
			PreparedStatement ps = null;
			News news = null;
			try {
				conn = DriverManager.getConnection(Common.URL, Common.USER,
						Common.PASSWORD);
				ps = conn.prepareStatement(sql);
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					String activityName = rs.getString(1);
					String activityDateStart = rs.getString(2);
					String activityDateEnd = rs.getString(3);
					news = new News(id, activityName, activityDateStart, activityDateEnd);
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
			return news;
		}
		
		@Override
		public List<News> getAllNews() {
			String sql = "SELECT activity_id, activity_name, activity_date_start, activity_date_end FROM activity;";
			Connection connection = null;
			PreparedStatement ps = null;
			List<News> newsList = new ArrayList<News>();
			try {
				connection = DriverManager.getConnection(Common.URL, Common.USER,
						Common.PASSWORD);
				ps = connection.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					int id = rs.getInt(1);
					String activityName = rs.getString(2);
					String activityDateStart = rs.getString(3);
					String activityDateEnd = rs.getString(4);
					
					News news = new News(id, activityName, activityDateStart, activityDateEnd);  //把文字回傳回去,圖後補
							
					newsList.add(news);
				}
				return newsList;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (ps != null) {
						ps.close();
					}
					if (connection != null) {
						connection.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return newsList;
		}

		
		
}
		
