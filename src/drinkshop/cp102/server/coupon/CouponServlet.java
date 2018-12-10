package drinkshop.cp102.server.coupon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import drinkshop.cp102.server.main.LogHelper;

@SuppressWarnings("serial")
@WebServlet("/CouponServlet")
public class CouponServlet extends HttpServlet {
	private final static String CONTENT_TYPE = "text/html; charset=UTF-8";
	private CouponDao couponDao = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (couponDao == null) {
			couponDao = new CouponDaoMySqlImpl();
		}
		String useStatus = request.getParameter("status");
		List<Coupon> couponList = couponDao.getAllCoupons(useStatus);
		writeText(response, new Gson().toJson(couponList));
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		BufferedReader br = request.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		System.out.println("input: " + jsonIn);

		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		if (couponDao == null) {
			couponDao = new CouponDaoMySqlImpl();
		}
		String action = jsonObject.get("action").getAsString();
		
		if (action.equals("getAllCoupons")) {
			String useStatus = jsonObject.get("status").getAsString();
			List<Coupon> couponList = couponDao.getAllCoupons(useStatus);
			writeText(response, gson.toJson(couponList));
		}else if (action.equals("getAllCouponsByMemberId")) {
			int member_id = jsonObject.get("member_id").getAsInt();
			List<Coupon> couponList = couponDao.getAllCouponsByMemberId(member_id);
			writeText(response, gson.toJson(couponList));
		}else if (action.equals("insert") || action.equals("updateCouponStatus")) {
			String spotJson = jsonObject.get("coupon").getAsString();
			Coupon coupon = gson.fromJson(spotJson, Coupon.class);//轉成Coupon物件
			int count = 0;
			if (action.equals("insert")) {
				count = couponDao.insert(coupon);
			} else if (action.equals("updateCouponStatus")) {
				count = couponDao.updateCouponStatus(coupon.getCoupon_id(), coupon.getMember_id());
			}
			writeText(response, String.valueOf(count));
		}else if (action.equals("findCouponById")) {
			int coupon_id = jsonObject.get("coupon_id").getAsInt();
			Coupon coupon = couponDao.findCouponById(coupon_id);
			writeText(response, gson.toJson(coupon));
		} else if(action.equals("getCouponsByMemberId")) {
			String useStatus = jsonObject.get("status").getAsString();
			int member_id = jsonObject.get("member_id").getAsInt();
			List<Coupon> couponList = couponDao.getCouponsByMemberId(member_id, useStatus);
			writeText(response, gson.toJson(couponList));
		}
	}

	private void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(outText);
//		System.out.println("output: " + outText);
		LogHelper.showOutput(outText);
	}
}