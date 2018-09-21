package drinkshop.cp102.server.orders;

import java.util.List;

public interface OrderDao {
	int insert(Order order, List<OrderDetail> orderDetails);

	int update(Order spot, byte[] image);

	int delete(int id);

	/**
	 * 用orderId找Order資訊
	 * @author mrosstro
	 * */
	List<Order> findOrderByOrderId(int orderId);
	
	//撈訂單歷史紀錄 
	List<Order> findOrderHistoryByMemberId(int member_id);
}
