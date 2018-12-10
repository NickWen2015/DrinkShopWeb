package drinkshop.cp102.server.coupon;

import java.util.List;

public interface CouponDao {
	
	//由會員產生Coupon
	int insert(Coupon coupon);
	
	//消費後更新Coupon卷狀態(0未用 >> 1已用)
	int updateCouponStatus(int coupon_id, int member_id);
	
	//找特定Coupon
	Coupon findCouponById(int coupon_id);
	
	//回傳會員所有可以使用Coupon券
	List<Coupon> getAllCouponsByMemberId(int member_id);

	//回傳所有Coupon券(0未用,1已用)
    List<Coupon> getAllCoupons(String useStatus);
    
    // 回傳會員未使用的Coupon卷(0未用,1已用)
    List<Coupon> getCouponsByMemberId(int member_id, String useStatus);
}
