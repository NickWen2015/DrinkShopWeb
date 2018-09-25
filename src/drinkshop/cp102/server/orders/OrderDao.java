package drinkshop.cp102.server.orders;

import java.util.List;

public interface OrderDao {
	
	/**
	 * 新增訂單
	 * @author mrosstro
	 * */
	int insert(Order order, List<OrderDetail> orderDetails);
	
	int delete(int id);

	/**
	 * 用orderId找Order資訊
	 * @author mrosstro
	 * */
	List<Order> findOrderByOrderId(int orderId);
	
	//撈訂單歷史紀錄 
	List<Order> findOrderHistoryByMemberId(int member_id);
	
	//撈訂單狀況 
		List<Order> findOrderByMemberId(int member_id);
		
	/**
	 * 改變訂單狀態（ 1 = 完成 ; 2 = 未完成 ）
	 *
	 * @author linpeko
	 * @param orderId QRCode掃描的結果
	 * @param orderStatus 要改變的訂單裝態
	 * @return 是否改變成功（ null = 沒成功 ; "1" = 成功 ）
	 * */
	String changeOrderStatusByOrderId(int orderId, String orderStatus);
	
	/**
     * 取得訂單詳細
     * 
     * @author linpeko
  	 * */
	List<OrderDetail> getOrderDetailByOrderId(int order_id);
	
	/**
	 * 取得未完成的訂單 並依 時間排序（早 > 晚）
	 * 
	 * @author linpeko
	 * */
	List<Order> getAllOrder();
	
//	/**
//	 * 取得未完成的訂單 並依 時間排序（早 > 晚）
//	 * 
//	 * @author linpeko
//	 * */
//	int update(int order_id, String order_status);
//	

//	
//	/**
//	 * 取的訂單詳細
//	 * 
//	 * @author linpeko
//	 * @param order_id 訂單編號
//	 * */
//	List<OrderDetail> findOrderDetailByOrderId(int order_id);
	
//	List<Order> getAllOrder();

	

}
