package drinkshop.cp102.server.products;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import drinkshop.cp102.server.main.Common;
import drinkshop.cp102.server.orders.Order;
import drinkshop.cp102.server.orders.OrderDetail;

public class ProductDaoMySqlImpl implements ProductDao {

	public ProductDaoMySqlImpl() {
		super();
		try { // org.mariadb.jdbc.Driver
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 新增類別
	 * */
	@Override
	public int insertCategory(String categoryName) {
		int categoryId = 0;
		String sql = "INSERT INTO category"
				+ "(category_name) "
				+ "VALUES(?);";
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, categoryName);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			while (rs.next()) {
				categoryId = rs.getInt(1);
			}
			return categoryId;
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
		return categoryId;
	}

	/**
	 * 取得所有類別
	 * */
	@Override
	public List<Category> getAllCategory() {
		
		String sql = "SELECT " + 
				"category_id, " + 
				"category_name " + 
				"FROM category;";

		Connection conn = null;
		PreparedStatement ps = null;
		
		List<Category> categories = new ArrayList<>();  //所有的訂單
		
		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				int category_id = rs.getInt(1);
				String category_name = rs.getString(2);

				Category category = new Category(category_id, category_name);
				categories.add(category);
			}
			
			return categories;
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
		return categories;
	}

	/**
	 * 新增商品
	 * */
	@Override
	public int productInsert(Product product, byte[] image) {
		int count = 0;
		int product_id = 0;
		String sqlProduct = "INSERT INTO product (category_id, product_name, product_pic1) VALUES (?, ?, ?);";
		String sqlProduct_size = "INSERT INTO product_size (size_id, product_id, product_price) VALUES (? ,? , ?);";
		
		Connection conn = null;
		PreparedStatement psProduct = null;
		PreparedStatement psProduct_size = null;
		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			conn.setAutoCommit(false);
			psProduct = conn.prepareStatement(sqlProduct, Statement.RETURN_GENERATED_KEYS);
			psProduct.setInt(1, product.getCategoryId());
			psProduct.setString(2, product.getName());
			psProduct.setBytes(3, image);
			psProduct.executeUpdate();
			ResultSet rs = psProduct.getGeneratedKeys();
			
			while (rs.next()) {
				product_id = rs.getInt(1);
				System.out.println("product_id = " + rs.getInt(1));
			}
			
			
			
			for(int size = 1; size <= 2 ; size++) {
				psProduct_size = conn.prepareStatement(sqlProduct_size);
				psProduct_size.setInt(1, size);
				psProduct_size.setInt(2, product_id);
				if(size == 1) {
					psProduct_size.setInt(3, product.getMPrice());	
				} else if(size == 2) {
					psProduct_size.setInt(3, product.getLPrice());
				}
				psProduct_size.executeUpdate();
			}
			count = 1;
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (psProduct_size != null) {
					// When a Statement object is closed,
					// its current ResultSet object is also closed
					psProduct_size.close();
				}
				if (psProduct != null) {
					// When a Statement object is closed,
					// its current ResultSet object is also closed
					psProduct.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	/**
	 * 更新商品
	 * */
	@Override
	public int productUpdate(Product product, byte[] image) {
		int changeOK = 0;
		
		String sqlProduct = "UPDATE product " + 
				"SET category_id = ?, product_name = ?, product_pic1 = ? " + 
				"WHERE product_id = ?;";
		
		String sqlProduct_size = "UPDATE product_size " + 
				"SET product_price = ? " + 
				"WHERE size_id = ? AND product_id = ?;";
		
		Connection conn = null;
		PreparedStatement psProduct = null;
		PreparedStatement psProduct_size = null;
				
		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			conn.setAutoCommit(false);
			
			psProduct = conn.prepareStatement(sqlProduct);
			psProduct.setInt(1, product.getCategoryId());
			psProduct.setString(2, product.getName());
			psProduct.setBytes(3, image);
			psProduct.setInt(4, product.getId());
			
			psProduct.executeUpdate();  //如果成功會傳回參數
			
			for(int size = 1; size <= 2 ; size++) {
				psProduct_size = conn.prepareStatement(sqlProduct_size);
				
				if(size == 1) {
					psProduct_size.setInt(1, product.getMPrice());
					psProduct_size.setInt(2, size);	
				} else if(size == 2) {
					psProduct_size.setInt(1, product.getLPrice());
					psProduct_size.setInt(2, size);
				}
				psProduct_size.setInt(3, product.getId());
				psProduct_size.executeUpdate();  //如果成功會傳回參數
			}
			
			changeOK = 1;
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (psProduct_size != null) {
					psProduct_size.close();
				}
				if (psProduct != null) {
					psProduct.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return changeOK;
	}
	
	/**
	 * 更新商品版次
	 * 2018/11/10
	 * @author Nick
	 * @return 1(success) or 0(failure)
	 * */
	@Override
	public int productsVersionsUpdate() {
		int changeOK = 0;
		
		String sqlProduct = "UPDATE productsVersions " + 
				"SET versions = versions + 1 " + 
				"WHERE productsVersions_id = 1;";
		
		Connection conn = null;
		PreparedStatement psProductsVersions = null;
				
		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			conn.setAutoCommit(false);
			
			psProductsVersions = conn.prepareStatement(sqlProduct);
			
			psProductsVersions.executeUpdate();  
			
			changeOK = 1;
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				
				if (psProductsVersions != null) {
					psProductsVersions.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return changeOK;
	}

	/**
	 * 刪除商品
	 * */
	@Override
	public int productDelete(int product_id) {
		int count = 0;
		
		String sqlProduct = "DELETE FROM product " + 
				"WHERE product_id = ?;";
		
		
		String sqlProduct_size = "DELETE FROM product_size " + 
				"WHERE product_id = ?;";
		
		Connection conn = null;
		PreparedStatement psProduct = null;
		PreparedStatement psProduct_size = null;
				
		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			conn.setAutoCommit(false);
			
			psProduct_size = conn.prepareStatement(sqlProduct_size);
			psProduct_size.setInt(1, product_id);
			psProduct_size.executeUpdate();
			
			psProduct = conn.prepareStatement(sqlProduct);
			psProduct.setInt(1, product_id);
			psProduct.executeUpdate();

			count = 1;
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (psProduct_size != null) {
					psProduct_size.close();
				}
				if (psProduct != null) {
					psProduct.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	@Override
	public Product findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 取的商品圖片
	 * */
	@Override
	public byte[] getProductImage(int productID) {
		String sql = "SELECT product_pic1 FROM product WHERE product_id = ?;";
		Connection connection = null;
		PreparedStatement ps = null;
		byte[] image = null;
		try {
			connection = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			ps = connection.prepareStatement(sql);
			ps.setInt(1, productID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
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

	/**
	 * 取的全部商品
	 * 
	 * @author mrosstro
	 * @return 傳回所有的 商品id, 類別名稱, 商品名稱, M尺寸的商品價格 ,L尺寸的商品價格
	 * */
	@Override
	public List<Product> getAllProduct() {
		String sql = "SELECT p.product_id, c.category_name, p.product_name, ps.product_price, s.size_name FROM product AS p " + 
				"LEFT JOIN category AS c " + 
				"ON p.category_id = c.category_id " + 
				"LEFT JOIN product_size AS ps " + 
				"ON p.product_id = ps.product_id " + 
				"LEFT JOIN size AS s " + 
				"ON ps.size_id = s.size_id " + 
				"ORDER BY c.category_id, p.product_id, s.size_id;";
		System.out.println("sql:"+sql);
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

	/**
	 * 取得單一商品詳細
	 * 
	 * @author mrosstro
	 * @param productID 商品id
	 * @return 商品id, 類別名稱, 商品名稱, M尺寸的商品價格 ,L尺寸的商品價格
	 * */
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
	
	public int getProductsVersions(int version) {
		int nowVersion = version;
		String sql = "SELECT " + 
				"versions " + 
				"FROM productsVersions " +
				"WHERE productsVersions_id = 1";

		Connection connection = null;
		PreparedStatement ps = null;

		try {
			connection = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				nowVersion = rs.getInt(1);
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
		return nowVersion;
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
