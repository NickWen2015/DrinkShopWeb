package drinkshop.cp102.server.orders;

import java.util.List;

public interface OrderDao {
	int insert(Order order, List<OrderDetail> orderDetails);
	
	int delete(int id);

	/**
	 * 用orderId找Order資訊
	 * @author mrosstro
	 * */
	List<Order> findOrderByOrderId(int orderId);
	
	//撈訂單歷史紀錄 
	List<Order> findOrderHistoryByMemberId(int member_id);
	
	/**
	 * 取得未完成的訂單 並依 時間排序（早 > 晚）
	 * 
	 * @author linpeko
	 * */
	int update(int order_id, String order_status);
	
	/**
	 * 取得未完成的訂單 並依 時間排序（早 > 晚）
	 * 
	 * @author linpeko
	 * */
	List<Order> getAllOrder();
	
	/**
	 * 取的訂單詳細
	 * 
	 * @author linpeko
	 * @param order_id 訂單編號
	 * */
	List<OrderDetail> findOrderDetailByOrderId(int order_id);
	
//	List<Order> getAllOrder();

}
