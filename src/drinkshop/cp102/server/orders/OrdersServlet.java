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

@SuppressWarnings("serial")
@WebServlet("/OrdersServlet")
public class OrdersServlet extends HttpServlet  {
	private final static String CONTENT_TYPE = "text/html; charset=utf-8";
	OrderDao orderDao = null;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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

		if (action.equals("orderInsert")) {
			String OrderD = jsonObject.get("order").getAsString();
			Type OrderType = new TypeToken<Order>() {
			}.getType();
			Order Order = gson.fromJson(OrderD, OrderType);
			String orderDetailList = jsonObject.get("orderDetailList").getAsString();
			Type orderDetailListType = new TypeToken<List<OrderDetail>>() {
			}.getType();

			List<OrderDetail> orderDetails = gson.fromJson(orderDetailList, orderDetailListType);
			int orderId = orderDao.insert(Order, orderDetails);
			writeText(response, gson.toJson(String.valueOf(orderId)));

		} else if (action.equals("findOrderByOrderId")) {
			String foboiOrderId = jsonObject.get("orderId").getAsString();
			List<Order> foboiOrder = orderDao.findOrderByOrderId(Integer.valueOf(foboiOrderId));
			writeText(response, gson.toJson(foboiOrder));

		} else if (action.equals("getAllOrder")) {
			List<Order> order = orderDao.getAllOrder();
			writeText(response, gson.toJson(order));

		} else if (action.equals("getOrderDetailByOrderId")) {
			int OrderId = jsonObject.get("orderId").getAsInt();
			List<OrderDetail> order = orderDao.getOrderDetailByOrderId(OrderId);
			writeText(response, gson.toJson(order));
		} else if (action.equals("changeOrderStatusByOrderId")) {
			int OrderId = jsonObject.get("orderId").getAsInt();
			String orderStatus = jsonObject.get("orderStatus").getAsString();
			String order = orderDao.changeOrderStatusByOrderId(OrderId, orderStatus);
			writeText(response, gson.toJson(order));

		} else if (action.equals("findOrderHistoryByMemberId")) {
			int memberIdOrderHistory = jsonObject.get("member_id").getAsInt();
			List<Order> orders = orderDao.findOrderHistoryByMemberId(memberIdOrderHistory);
			writeText(response, gson.toJson(orders));

		} else if (action.equals("findOrderByMemberId")) {
			int memberIdOrder = jsonObject.get("member_id").getAsInt();
			List<Order> memberOrders = orderDao.findOrderByMemberId(memberIdOrder);
			writeText(response, gson.toJson(memberOrders));
		} else {
			writeText(response, "");
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (orderDao == null) {
			orderDao = new OrderDaoMySqlImpl();
		}

		List<Order> orders = orderDao.findOrderHistoryByMemberId(1);
		writeText(response, new Gson().toJson(orders));
	}

	private void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(outText);
		System.out.println("output: " + outText);
	}

}
