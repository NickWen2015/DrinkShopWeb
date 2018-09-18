package drinkshop.cp102.server.orders;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import drinkshop.cp102.server.main.Common;

public class OrderDaoMySqlImpl implements OrderDao {

	public OrderDaoMySqlImpl() {
		super();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int update(Order spot, byte[] image) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Order findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(int store_id, int member_id, int coupon_id, String order_type, List<Order> cart) {
		Connection conn = null;
		PreparedStatement psMaster = null;
		PreparedStatement psDetail = null;
		String sqlMaster = "INSERT INTO `order`(store_id, member_id, order_accept_time, order_finish_time, coupon_id, order_type) VALUES(?, ?, now(), now(), ?, ?);";
		String sqlDetail = "INSERT INTO order_detail(order_id, product_id, size_id, sugar_id, ice_id, product_quantity) VALUES(?, ?, ?, ?, ?, ?);";
		int orderId = -1;

		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			// roll back the transaction while insertion failure
			conn.setAutoCommit(false);
			// insert a new order and return the auto-increment order id
			psMaster = conn.prepareStatement(sqlMaster,
					Statement.RETURN_GENERATED_KEYS);
			psMaster.setInt(1, store_id);
			psMaster.setInt(2, member_id);
			if(coupon_id != 0 && coupon_id > 0) {
				psMaster.setInt(5, coupon_id);
			}
			psMaster.setString(6, order_type);

			psMaster.executeUpdate();
			// get the generated order id
			ResultSet rs = psMaster.getGeneratedKeys();
			if (rs.next()) {
				orderId = rs.getInt(1);
			}
			for (Order shoppingCart : cart) {
				int productId = shoppingCart.getProductID();
				int size = shoppingCart.getSize();
				int suger = shoppingCart.getSuger();
				int temperature = shoppingCart.getTemperature();
				int quantity = shoppingCart.getQuantity();
				
				psDetail = conn.prepareStatement(sqlDetail);
				psDetail.setInt(1, orderId);
				psDetail.setInt(2, productId);
				psDetail.setInt(3, size);
				psDetail.setInt(4, suger);
				psDetail.setInt(5, temperature);
				psDetail.setInt(6, quantity);

				psDetail.executeUpdate();
			}
			// commit without error
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				// roll back while SQLException
				conn.rollback();
				orderId = -1;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				if (psMaster != null) {
					psMaster.close();
				}
				if (psDetail != null) {
					psDetail.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return orderId;
	}

}
