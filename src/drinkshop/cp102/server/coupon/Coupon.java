package drinkshop.cp102.server.coupon;

import java.io.Serializable;

public class Coupon implements Serializable {
	
	private int coupon_id;
	private int member_id;
	private String coupon_no;
	private String coupon_status;
	private String coupon_start;
	private String coupon_end;
	private float coupon_discount;
	
	public Coupon(int coupon_id, int member_id, String coupon_no, String coupon_status, String coupon_start,
			String coupon_end, float coupon_discount) {
		super();
		this.coupon_id = coupon_id;
		this.member_id = member_id;
		this.coupon_no = coupon_no;
		this.coupon_status = coupon_status;
		this.coupon_start = coupon_start;
		this.coupon_end = coupon_end;
		this.coupon_discount = coupon_discount;
	}
	
	
	public int getCoupon_id() {
		return coupon_id;
	}
	public void setCoupon_id(int coupon_id) {
		this.coupon_id = coupon_id;
	}
	public int getMember_id() {
		return member_id;
	}
	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}
	public String getCoupon_no() {
		return coupon_no;
	}
	public void setCoupon_no(String coupon_no) {
		this.coupon_no = coupon_no;
	}
	public String getCoupon_status() {
		return coupon_status;
	}
	public void setCoupon_status(String coupon_status) {
		this.coupon_status = coupon_status;
	}
	public String getCoupon_start() {
		return coupon_start;
	}
	public void setCoupon_start(String coupon_start) {
		this.coupon_start = coupon_start;
	}
	public String getCoupon_end() {
		return coupon_end;
	}
	public void setCoupon_end(String coupon_end) {
		this.coupon_end = coupon_end;
	}
	public float getCoupon_discount() {
		return coupon_discount;
	}
	public void setCoupon_discount(float coupon_discount) {
		this.coupon_discount = coupon_discount;
	}

	
}
