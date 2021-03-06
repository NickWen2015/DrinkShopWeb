package drinkshop.cp102.server.products;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import drinkshop.cp102.server.main.ImageUtil;
import drinkshop.cp102.server.main.LogHelper;

@SuppressWarnings("serial")
@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {
	private final static String CONTENT_TYPE = "text/html; charset=utf-8";
	ProductDao productDao = null;

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
		if (productDao == null) {
			productDao = new ProductDaoMySqlImpl();
		}
		String action = jsonObject.get("action").getAsString();
		/* --- 我是分隔線 --- */
		List<Product> products = null;
		switch (action) {
		case "getAllProduct":
			 products = productDao.getAllProduct();
			writeText(response, gson.toJson(products));
			break;
			
		case "getProductDetail":
			int productID = jsonObject.get("productID").getAsInt();
			products = productDao.getProductDetail(productID);
			writeText(response, gson.toJson(products));
			break;
			
		default:
			writeText(response, "");
			break;
		}
	

//		if (action.equals("getAll")) {
//			List<Product> products = productDao.getAll();
//			writeText(response, gson.toJson(products));
//		} else if (action.equals("getImage")) {
//			OutputStream os = response.getOutputStream();
//			int id = jsonObject.get("id").getAsInt();
//			int imageSize = jsonObject.get("imageSize").getAsInt();
//			byte[] image = productDao.getImage(id);
//			if (image != null) {
//				image = ImageUtil.shrink(image, imageSize);//java縮圖
//				response.setContentType("image/jpeg");//輸出圖
//				response.setContentLength(image.length);//圖的長度
//			}
//			os.write(image);
//		} else if (action.equals("spotInsert") || action.equals("spotUpdate")) {
//			String spotJson = jsonObject.get("spot").getAsString();
//			Product spot = gson.fromJson(spotJson, Product.class);
//			String imageBase64 = jsonObject.get("imageBase64").getAsString();
//			byte[] image = Base64.getMimeDecoder().decode(imageBase64);
//			;
//			int count = 0;
//			if (action.equals("spotInsert")) {
//				count = productDao.insert(spot, image);
//			} else if (action.equals("spotUpdate")) {
//				count = productDao.update(spot, image);
//			}
//			writeText(response, String.valueOf(count));
//		} else if (action.equals("spotDelete")) {
//			String spotJson = jsonObject.get("spot").getAsString();
//			Product spot = gson.fromJson(spotJson, Product.class);
//			int count = productDao.delete(spot.getId());
//			writeText(response, String.valueOf(count));
//		} else if (action.equals("findById")) {
//			int id = jsonObject.get("id").getAsInt();
//			Product spot = productDao.findById(id);
//			writeText(response, gson.toJson(spot));
//		} else {
//			writeText(response, "");
//		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (productDao == null) {
			productDao = new ProductDaoMySqlImpl();
		}
		List<Product> products = productDao.getAllProduct();
		writeText(response, new Gson().toJson(products));
	}

	private void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(outText);
		System.out.println("output: " + outText);
	}

}
