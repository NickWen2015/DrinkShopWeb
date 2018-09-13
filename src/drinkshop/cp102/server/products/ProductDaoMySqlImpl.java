package drinkshop.cp102.server.products;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import drinkshop.cp102.server.main.Common;

public class ProductDaoMySqlImpl implements ProductDao {

	public ProductDaoMySqlImpl() {
		super();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int insert(Product spot, byte[] image) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Product spot, byte[] image) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Product findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getImage(int id) {

		return null;
	}

	@Override
	public List<Product> getAllProduct() {
		String sql = "SELECT p.product_id, c.category_name, p.product_name, ps.product_price, s.size_name FROM category AS c " + 
				"LEFT JOIN product AS p " + 
				"ON c.category_id = p.category_id " + 
				"LEFT JOIN product_size AS ps " + 
				"ON p.product_id = ps.product_id " + 
				"LEFT JOIN size AS s " + 
				"ON ps.size_id = s.size_id " + 
				"ORDER BY c.category_id, p.product_id, s.size_id;";
		Connection connection = null;
		PreparedStatement ps = null;
		List<Product> productList = new ArrayList<Product>();
		try {
			connection = DriverManager.getConnection(Common.URL, Common.USER, Common.PASSWORD);
			ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			int count = 0;
			
			int id = 0;
			String category = null;
			String name = null;
			int mprice = 0;
			int lprice = 0;
			while (rs.next()) {
				if((count % 2) == 1) {
					lprice = rs.getInt(4);
					Product product = new Product(id, category, name, mprice, lprice);
					productList.add(product);
					count++;
				} else {
					id = rs.getInt(1);
					category = String.valueOf(rs.getString(2));
					name = rs.getString(3);
					mprice = rs.getInt(4);
					count++;
				}
			}
			return productList;
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
		return productList;
	}

	@Override
	public List<Product> getProductDetail(int productID) {
		String sql = "SELECT p.product_id, c.category_name, p.product_name, ps.product_price, s.size_name FROM category AS c " + 
				"LEFT JOIN product AS p " + 
				"ON c.category_id = p.category_id " + 
				"LEFT JOIN product_size AS ps " + 
				"ON p.product_id = ps.product_id " + 
				"LEFT JOIN size AS s " + 
				"ON ps.size_id = s.size_id " + 
				"WHERE p.product_id = " + productID + " " +
				"ORDER BY c.category_id, p.product_id, s.size_id;";
		Connection connection = null;
		PreparedStatement ps = null;
		List<Product> productList = new ArrayList<Product>();
		try {
			connection = DriverManager.getConnection(Common.URL, Common.USER, Common.PASSWORD);
			ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			int count = 0;
			
			int id = 0;
			String category = null;
			String name = null;
			int mprice = 0;
			int lprice = 0;
			while (rs.next()) {
				if((count % 2) == 1) {
					lprice = rs.getInt(4);
					Product product = new Product(id, category, name, mprice, lprice);
					productList.add(product);
					count++;
				} else {
					id = rs.getInt(1);
					category = String.valueOf(rs.getString(2));
					name = rs.getString(3);
					mprice = rs.getInt(4);
					count++;
				}
			}
			return productList;
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
		return productList;
	}

//	@Override
//	public int insert(Product product, byte[] image) {
//		int count = 0;
//		String sql = "INSERT INTO product"
//				+ "(name, phoneNo, address, latitude, longitude, image) "
//				+ "VALUES(?, ?, ?, ?, ?, ?);";
//		Connection connection = null;
//		PreparedStatement ps = null;
//		try {
//			connection = DriverManager.getConnection(Common.URL, Common.USER,
//					Common.PASSWORD);
//			ps = connection.prepareStatement(sql);
//			ps.setString(1, product.getName());
//			ps.setString(2, product.getPhoneNo());
//			ps.setString(3, product.getAddress());
//			ps.setDouble(4, product.getLatitude());
//			ps.setDouble(5, product.getLongitude());
//			ps.setBytes(6, image);
//			count = ps.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (ps != null) {
//					// When a Statement object is closed,
//					// its current ResultSet object is also closed
//					ps.close();
//				}
//				if (connection != null) {
//					connection.close();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		return count;
//	}
//
//	@Override
//	public int update(Product product, byte[] image) {
//		int count = 0;
//		String sql = "UPDATE product SET name = ?, phoneNo = ?, address = ?, latitude = ?, longitude = ?, image = ? WHERE id = ?;";
//		Connection connection = null;
//		PreparedStatement ps = null;
//		try {
//			connection = DriverManager.getConnection(Common.URL, Common.USER,
//					Common.PASSWORD);
//			ps = connection.prepareStatement(sql);
//			ps.setString(1, product.getName());
//			ps.setString(2, product.getPhoneNo());
//			ps.setString(3, product.getAddress());
//			ps.setDouble(4, product.getLatitude());
//			ps.setDouble(5, product.getLongitude());
//			ps.setBytes(6, image);
//			ps.setInt(7, product.getId());
//			count = ps.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (ps != null) {
//					// When a Statement object is closed,
//					// its current ResultSet object is also closed
//					ps.close();
//				}
//				if (connection != null) {
//					connection.close();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		return count;
//	}
//
//	@Override
//	public int delete(int id) {
//		int count = 0;
//		String sql = "DELETE FROM product WHERE id = ?;";
//		Connection connection = null;
//		PreparedStatement ps = null;
//		try {
//			connection = DriverManager.getConnection(Common.URL, Common.USER,
//					Common.PASSWORD);
//			ps = connection.prepareStatement(sql);
//			ps.setInt(1, id);
//			count = ps.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (ps != null) {
//					// When a Statement object is closed,
//					// its current ResultSet object is also closed
//					ps.close();
//				}
//				if (connection != null) {
//					connection.close();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		return count;
//	}
//
//	@Override
//	public byte[] getImage(int id) {
//		String sql = "SELECT image FROM product WHERE id = ?;";
//		Connection connection = null;
//		PreparedStatement ps = null;
//		byte[] image = null;
//		try {
//			connection = DriverManager.getConnection(Common.URL, Common.USER,
//					Common.PASSWORD);
//			ps = connection.prepareStatement(sql);
//			ps.setInt(1, id);
//			ResultSet rs = ps.executeQuery();
//			if (rs.next()) {
//				image = rs.getBytes(1);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (ps != null) {
//					// When a Statement object is closed,
//					// its current ResultSet object is also closed
//					ps.close();
//				}
//				if (connection != null) {
//					connection.close();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		return image;
//	}
//
//	@Override
//	public Product findById(int id) {
//		String sql = "SELECT name, phoneNo, address, latitude, longitude FROM product WHERE id = ?;";
//		Connection conn = null;
//		PreparedStatement ps = null;
//		Product product = null;
//		try {
//			conn = DriverManager.getConnection(Common.URL, Common.USER,
//					Common.PASSWORD);
//			ps = conn.prepareStatement(sql);
//			ps.setInt(1, id);
//			ResultSet rs = ps.executeQuery();
//			if (rs.next()) {
//				String name = rs.getString(1);
//				String phoneNo = rs.getString(2);
//				String address = rs.getString(3);
//				double latitude = rs.getDouble(4);
//				double longitude = rs.getDouble(5);
//				product = new Product(id, name, phoneNo, address, latitude, longitude);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (ps != null) {
//					ps.close();
//				}
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		return product;
//	}
//
//	@Override
//	public List<Product> getAll() {
//		String sql = "SELECT id, name, phoneNo, address, latitude, longitude "
//				+ "FROM product ORDER BY timestamp DESC;";
//		Connection connection = null;
//		PreparedStatement ps = null;
//		List<Product> productList = new ArrayList<Product>();
//		try {
//			connection = DriverManager.getConnection(Common.URL, Common.USER,
//					Common.PASSWORD);
//			ps = connection.prepareStatement(sql);
//			ResultSet rs = ps.executeQuery();
//			while (rs.next()) {
//				int id = rs.getInt(1);
//				String name = rs.getString(2);
//				String phoneNo = rs.getString(3);
//				String address = rs.getString(4);
//				double latitude = rs.getDouble(5);
//				double longitude = rs.getDouble(6);
//				Product product = new Product(id, name, phoneNo, address, latitude,
//						longitude);
//				productList.add(product);
//			}
//			return productList;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (ps != null) {
//					ps.close();
//				}
//				if (connection != null) {
//					connection.close();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		return productList;
//	}
}
