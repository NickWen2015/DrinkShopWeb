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
import drinkshop.cp102.server.main.Helper;

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
	public int insert(int store_id, int member_id, int coupon_id, String order_type, List<Order> cart) {
		Connection conn = null;
		PreparedStatement psMaster = null;
		PreparedStatement psDetail = null;
		String sqlMaster = "INSERT INTO `order`(store_id, member_id, order_accept_time, order_finish_time, coupon_id, order_type) VALUES(?, ?, now(), now(), ?, ?);";
		String sqlDetail = "INSERT INTO order_detail(order_id, product_id, size_id, sugar_id, ice_id, product_quantity) VALUES(?, ?, ?, ?, ?, ?);";
		int orderId = -1;

		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER, Common.PASSWORD);
			// roll back the transaction while insertion failure
			conn.setAutoCommit(false);
			// insert a new order and return the auto-increment order id
			psMaster = conn.prepareStatement(sqlMaster, Statement.RETURN_GENERATED_KEYS);
			psMaster.setInt(1, store_id);
			psMaster.setInt(2, member_id);
			if (coupon_id != 0 && coupon_id > 0) {
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
				// int productId = shoppingCart.getProductID();
				// int size = shoppingCart.getSize();
				// int suger = shoppingCart.getSuger();
				// int temperature = shoppingCart.getTemperature();
				// int quantity = shoppingCart.getQuantity();

				// psDetail = conn.prepareStatement(sqlDetail);
				// psDetail.setInt(1, orderId);
				// psDetail.setInt(2, productId);
				// psDetail.setInt(3, size);
				// psDetail.setInt(4, suger);
				// psDetail.setInt(5, temperature);
				// psDetail.setInt(6, quantity);

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
	public Order findById(int member_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Order> findOrderHistoryByMemberId(int member_id) {
		Connection conn = null;
		PreparedStatement ps = null;
		List<Order> orderList = new ArrayList<>();
		List<OrderDetail> orderDetailList = new ArrayList<>();
		List<Order> orderNewList = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER, Common.PASSWORD);
			String sql;
			sql = "select o.order_id, "
					+ "CONCAT(o.invoice_prefix, '-', o.invoice_no) As invoice, o.order_accept_time, o.order_finish_time, o.order_type, "
					+ "st.store_name, st.store_telephone, st.store_mobile ,st.store_address, st.store_location_x, st.store_location_y, "
					+ "cp.coupon_discount, "
					+ "p.product_name, i.ice_name, s.sugar_name, sz.size_name, od.product_quantity, ps.product_price, "
					+ "o.store_id, o.delivery_id, o.coupon_id, od.order_detail_id, p.product_id, "
					+ "sz.size_id, s.sugar_id, i.ice_id, " + "o.invoice_prefix, o.invoice_no, o.order_status "
					+ "from order_detail as od  " + "left join `order` as o " + "on od.order_id = o.order_id "
					+ "left join product as p " + "on od.product_id = p.product_id  " + "left join ice as i "
					+ "on od.ice_id = i.ice_id " + "left join sugar as s " + "on od.sugar_id = s.sugar_id  "
					+ "left join product_size as ps "
					+ "on od.product_id  = ps.product_id  and od.size_id = ps.size_id " + "left join size as sz  "
					+ "on od.size_id = sz.size_id  " + "left join store as st  " + "on o.store_id = st.store_id "
					+ "left join coupon as cp " + "on o.coupon_id = cp.coupon_id "
					+ "where o.member_id = ? and o.order_status = 1 " + "order by o.order_accept_time DESC;";
			System.out.println(sql);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, member_id);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int order_id = rs.getInt(1);// 訂單編號
				String invoice = rs.getString(2);// 發票號碼
				String order_accept_time = Helper.getFmtdDateToStr(rs.getDate(3));// 訂單接收時間
				String order_finish_time = Helper.getFmtdDateToStr(rs.getDate(4));// 訂單完成時間
				String order_type = rs.getString(5);// 訂單狀態
				String store_name = rs.getString(6);// 商店名稱
				String store_telephone = rs.getString(7);// 商店電話
				String store_mobile = rs.getString(8);// 商店手機
				String store_address = rs.getString(9);// 商店住址
				String store_location_x = rs.getString(10);
				String store_location_y = rs.getString(11);
				float coupon_discount = rs.getBigDecimal(12) != null ? rs.getBigDecimal(12).floatValue() : 1;// 購買折扣
				String product_name = rs.getString(13);// 產品名稱
				String ice_name = rs.getString(14);// 冰塊
				String sugar_name = rs.getString(15);// 甜度
				String size_name = rs.getString(16);// size
				int product_quantity = rs.getInt(17);// 產品數量
				int product_price = rs.getBigDecimal(18) != null ? rs.getBigDecimal(18).intValueExact() : 1000;
				int store_id = rs.getInt(19);
				int delivery_id = rs.getInt(20);
				int coupon_id = rs.getInt(21);
				int order_detail_id = rs.getInt(22);
				int product_id = rs.getInt(23);
				int size_id = rs.getInt(24);
				int sugar_id = rs.getInt(25);
				int ice_id = rs.getInt(26);
				String invoice_prefix = rs.getString(27);
				String invoice_no = rs.getString(28);
				String order_status = rs.getString(29);

				OrderDetail orderDetail = new OrderDetail(order_detail_id, order_id, product_id, size_id, sugar_id,
						ice_id, product_quantity, product_name, ice_name, sugar_name, size_name, product_price);
				orderDetailList.add(orderDetail);

				Order order = new Order(order_id, invoice_prefix, invoice_no, store_id, member_id, order_accept_time,
						order_finish_time, order_type, delivery_id, coupon_id, order_status, invoice, store_name,
						store_telephone, store_mobile, store_address, store_location_x, store_location_y,
						coupon_discount, orderDetailList);

				int orderIndex = orderList.indexOf(order);// 會透過equals比較,order_id要不同的才加進去orderList
				if (orderIndex == -1) {
					orderList.add(order);
				}
			}
			List<OrderDetail> orderDetailNewList = null;
			//整理多筆要回傳的orderList內容
			for (int k = 0; k < orderList.size(); k++) {
				Order order = orderList.get(k);
				orderDetailNewList = new ArrayList<>();
				for (int i = 0; i < orderDetailList.size(); i++) {
					if (order.getOrder_id() == orderDetailList.get(i).getOrder_id()) {
						orderDetailNewList.add(orderDetailList.get(i));
					}
				}
				order.setOrderDetailList(orderDetailNewList);
				orderNewList.add(order);
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
		return orderNewList;
	}

}
