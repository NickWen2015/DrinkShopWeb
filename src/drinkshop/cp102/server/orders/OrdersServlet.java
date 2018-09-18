package drinkshop.cp102.server.orders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import drinkshop.cp102.server.main.LogHelper;
import drinkshop.cp102.server.members.Member;
import drinkshop.cp102.server.members.MemberDaoMySqlImpl;
import drinkshop.cp102.server.products.Product;


@SuppressWarnings("serial")
@WebServlet("/OrdersServlet")
public class OrdersServlet extends HttpServlet {
	private final static String CONTENT_TYPE = "text/html; charset=utf-8";
	OrderDao orderDao = null;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		Gson gson = new Gson();
		BufferedReader br = request.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) { // 取得android端發送的資料
			jsonIn.append(line);
		}
		LogHelper.showInput(jsonIn);

		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		if (orderDao == null) {
			orderDao = new OrderDaoMySqlImpl();
		}
		String action = jsonObject.get("action").getAsString();
		/* --- 我是分隔線 --- */
		List<Product> products = null;
		switch (action) {
		case "orderInsert":
			int store_id = jsonObject.get("store_id").getAsInt();
			int member_id = jsonObject.get("member_id").getAsInt();
			int coupon_id = jsonObject.get("coupon_id").getAsInt();
			String order_type = jsonObject.get("order_type").getAsString();
			String shoppingCartList = jsonObject.get("shoppingCartList").getAsString();
			Type listType = new TypeToken<List<Order>>() {
			}.getType();
			List<Order> cart = gson.fromJson(shoppingCartList, listType);
			int orderId = orderDao.insert(store_id, member_id, coupon_id, order_type, cart);
			Order order = null;
			if (orderId != -1) {
				order = orderDao.findById(orderId);
			}
			writeText(response, gson.toJson(orderId));
			break;
			
			
		default:
			writeText(response, "");
			break;
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (orderDao == null) {
			orderDao = new OrderDaoMySqlImpl();
		}
		List<Order> orderList = orderDao.findOrderByMemberId(1);
		writeText(response, new Gson().toJson(orderList));
	}
	
	private void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(outText);
		System.out.println("output: " + outText);
	}

}
