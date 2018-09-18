package drinkshop.cp102.server.orders;

import java.util.List;

public interface OrderDao {
	int insert(int store_id, int member_id, int coupon_id, String order_type, List<Order> cart);

	int update(Order spot, byte[] image);

	int delete(int id);

	Order findById(int id);

}
