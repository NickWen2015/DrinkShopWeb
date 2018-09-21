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
import drinkshop.cp102.server.members.Member;
import drinkshop.cp102.server.orders.OrderDetail;


public class OrderDaoMySqlImpl implements OrderDao {

	public OrderDaoMySqlImpl() {
		super();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 新增訂單
	 * @author mrosstro
	 * */
	@Override
	public int insert(Order order, List<OrderDetail> orderDetails) {
		Connection conn = null;
		PreparedStatement psMaster = null;
		PreparedStatement psDetail = null;
		String sqlMaster = "INSERT INTO `order`(store_id, member_id, order_accept_time, order_finish_time, coupon_id, order_type) VALUES(?, ?, now(), now(), ?, ?);";
		String sqlDetail = "INSERT INTO order_detail(order_id, product_id, size_id, sugar_id, ice_id, product_quantity) VALUES(?, ?, ?, ?, ?, ?);";
		int orderId = 0;

		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER, Common.PASSWORD);
			// roll back the transaction while insertion failure
			conn.setAutoCommit(false);
			// insert a new order and return the auto-increment order id
			psMaster = conn.prepareStatement(sqlMaster, Statement.RETURN_GENERATED_KEYS);
			psMaster.setInt(1, order.getStore_id());
			psMaster.setInt(2, order.getMember_id());
			if (order.getCoupon_id() != 0 && order.getCoupon_id() > 0) {
				psMaster.setInt(3, order.getCoupon_id());
			} else {
				psMaster.setInt(3, 0);
			}
			psMaster.setString(4, order.getOrder_type());

			psMaster.executeUpdate();
			// get the generated order id
			ResultSet rs = psMaster.getGeneratedKeys();
			if (rs.next()) {
				orderId = rs.getInt(1);
			}
			for (OrderDetail orderDetail : orderDetails) {
				 psDetail = conn.prepareStatement(sqlDetail);
				 psDetail.setInt(1, orderId);
				 psDetail.setInt(2, orderDetail.getProduct_id());
				 psDetail.setInt(3, orderDetail.getSize_id());
				 psDetail.setInt(4, orderDetail.getSugar_id());
				 psDetail.setInt(5, orderDetail.getIce_id());
				 psDetail.setInt(6, orderDetail.getProduct_quantity());
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
	public int delete(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 用orderId找Order資訊
	 * @author mrosstro
	 * */
	@Override
	public List<Order> findOrderByOrderId(int orderId) {
		Connection conn = null;
		PreparedStatement ps = null;
		List<Order> orderList = new ArrayList<>();
		List<OrderDetail> orderDetailList = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER, Common.PASSWORD);
			String sql;
			sql = "select " + 
					"CONCAT(o.invoice_prefix, '-', o.invoice_no) As invoice, " + 
					"o.order_accept_time, " + 
					"o.order_type, " + 
					"st.store_name, " + 
					"st.store_telephone, " + 
					"st.store_mobile, " + 
					"cp.coupon_discount, " + 
					"p.product_name, " + 
					"i.ice_name, " + 
					"s.sugar_name, " + 
					"sz.size_name, " + 
					"od.product_quantity, " + 
					"ps.product_price " + 
					"from order_detail as od  left join `order` as o on od.order_id = o.order_id " + 
					"left join product as p on od.product_id = p.product_id " + 
					"left join ice as i on od.ice_id = i.ice_id " + 
					"left join sugar as s on od.sugar_id = s.sugar_id " + 
					"left join product_size as ps on od.product_id  = ps.product_id  and od.size_id = ps.size_id " + 
					"left join size as sz on od.size_id = sz.size_id " + 
					"left join store as st  on o.store_id = st.store_id " + 
					"left join coupon as cp on o.coupon_id = cp.coupon_id " + 
					"where o.order_id = ? order by o.order_accept_time DESC;";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, orderId);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int order_id = orderId; // 訂單編號
				String invoice = rs.getString(1);// 發票號碼
				String order_accept_time = Helper.getFmtDateTimeToStr(rs.getDate(2));// 訂單接收時間
				String order_type = rs.getString(3);// 訂單狀態
				String store_name = rs.getString(4);// 商店名稱
				String store_telephone = rs.getString(5);// 商店電話
				String store_mobile = rs.getString(6);// 商店手機
				float coupon_discount = rs.getBigDecimal(7) != null ? rs.getBigDecimal(7).floatValue() : 10;// 購買折扣
				
				String product_name = rs.getString(8);// 產品名稱
				String ice_name = rs.getString(9);// 冰塊
				String sugar_name = rs.getString(10);// 甜度
				String size_name = rs.getString(11);// size
				int product_quantity = rs.getInt(12);// 產品數量
				int product_price = rs.getBigDecimal(13) != null ? rs.getBigDecimal(13).intValueExact() : 1000;

				OrderDetail orderDetail = new OrderDetail();
				orderDetail.setProduct_name(product_name);
				orderDetail.setIce_name(ice_name);
				orderDetail.setSugar_name(sugar_name);
				orderDetail.setSize_name(size_name);
				orderDetail.setProduct_quantity(product_quantity);
				orderDetail.setProduct_price(product_price);
				orderDetailList.add(orderDetail);
				
				Order order = new Order();
				order.setOrder_id(order_id);
				order.setOrder_accept_time(order_accept_time);
				order.setOrder_type(order_type);
				order.setStore_name(store_name);
				order.setStore_telephone(store_telephone);
				order.setStore_mobile(store_mobile);
				order.setCoupon_discount(coupon_discount);
				order.setOrderDetailList(orderDetailList);
				int index = orderList.indexOf(order);// 會透過equals比較
				if (index == -1) {
					orderList.add(order);
				}
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
		return orderList;
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
	
	/**
	 * 當使用者給店家掃描QRCode後 執行
	 * 修改 order（訂單）order_status 狀態 0：未付款 1：已付款
	 * 
	 * @author linpeko
	 * @param order_id 訂單編號
	 * @param order_status 訂單狀態
	 * */
	@Override
	public int update(int order_id, String order_status) {
		String sql = "UPDATE `order` " + 
				"SET order_status = ? " + 
				"WHERE order_id = ?;";
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		int changeOK = 0;
		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER,
					Common.PASSWORD);
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement(sql);
			ps.setString(1, order_status);
			ps.setInt(2, order_id);
			
			changeOK = ps.executeUpdate();  //如果成功會傳回參數
			System.out.println("changeOK = " + changeOK);
			
			conn.commit();
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
		return changeOK;
	}

	/**
	 * 取得全部訂單資訊
	 * 
	 * @author linpeko
	 * */
    public List<Order> getAllOrder() {
	String sql = "SELECT " + 
			"o.order_id, " + 
			"CONCAT(o.invoice_prefix, '-', o.invoice_no) As invoice, " + 
			"m.member_name, " + 
			"o.order_accept_time, " + 
			"o.order_status " + 
			"FROM `order` AS o " + 
			"LEFT JOIN member AS m " + 
			"ON m.member_id = o.member_id " + 
			"WHERE o.order_status = 0 ORDER BY o.order_accept_time DESC;";

	Connection conn = null;
	PreparedStatement ps = null;
	
	List<Order> orders = new ArrayList<>();  //所有的訂單
	try {
		conn = DriverManager.getConnection(Common.URL, Common.USER,
				Common.PASSWORD);
		ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		if (rs.next()) {
			int order_id = rs.getInt(1);
			String invoice = rs.getString(2);
			String member_name = rs.getString(3);
			String order_accept_time = rs.getString(4);
			String order_status = rs.getString(5);
			
			Order order = new Order();
			order.setOrder_id(order_id);
			order.setInvoice(invoice);
			order.setMember_name(member_name);
			order.setOrder_status(order_status);
			
			orders.add(order);
		}
		return orders;
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
	return orders;
}

    /**
     * 取得訂單詳細
     * 
	 * @author linpeko
	 * */
	@Override
	public List<OrderDetail> findOrderDetailByOrderId(int order_id) {
		String sql = "SELECT " + 
				"p.product_name, " + 
				"i.ice_name, " + 
				"s.sugar_name, " + 
				"sz.size_name, " + 
				"ps.product_price, " + 
				"od.product_quantity " + 
				"FROM order_detail AS od " + 
				"LEFT JOIN `order` AS o " + 
				"ON od.order_id = o.order_id " + 
				"LEFT JOIN product AS p  " + 
				"ON od.product_id = p.product_id  " + 
				"LEFT JOIN ice AS i \n" + 
				"ON od.ice_id = i.ice_id " + 
				"LEFT JOIN sugar AS s " + 
				"ON od.sugar_id = s.sugar_id " + 
				"LEFT JOIN product_size AS ps " + 
				"ON od.product_id  = ps.product_id  AND od.size_id = ps.size_id " + 
				"LEFT JOIN size AS sz " + 
				"ON od.size_id = sz.size_id  " + 
				"LEFT JOIN store AS st  " + 
				"ON o.store_id = st.store_id " + 
				"LEFT JOIN coupon AS cp " + 
				"ON o.coupon_id = cp.coupon_id " + 
				"WHERE o.order_id = ? ORDER BY o.order_accept_time DESC;";
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		List<Order> orderList = new ArrayList<>();
		List<OrderDetail> orderDetailList = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(Common.URL, Common.USER, Common.PASSWORD);

			ps = conn.prepareStatement(sql);
			ps.setInt(1, order_id);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String product_name = rs.getString(2);// 產品名稱
				String ice_name = rs.getString(3);// 冰塊
				String sugar_name = rs.getString(4);// 甜度
				String size_name = rs.getString(5);// size
				int product_price = rs.getBigDecimal(6) != null ? rs.getBigDecimal(6).intValueExact() : 0;
				int product_quantity = rs.getInt(7);// 產品數量

				OrderDetail orderDetail = new OrderDetail();			
				orderDetail.setOrder_id(order_id);
				orderDetail.setProduct_name(product_name);
				orderDetail.setIce_name(ice_name);
				orderDetail.setSugar_name(sugar_name);
				orderDetail.setSize_name(size_name);
				orderDetail.setProduct_price(product_price);
				orderDetail.setProduct_quantity(product_quantity);
					
				orderDetailList.add(orderDetail);
			}
		return orderDetailList;
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
		return orderDetailList;
	}
}
