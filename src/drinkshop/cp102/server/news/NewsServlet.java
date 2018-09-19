package drinkshop.cp102.server.news;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import drinkshop.cp102.server.main.LogHelper;
import drinkshop.cp102.server.main.ImageUtil;



/**
 * Servlet implementation class NewsServlet
 */
@SuppressWarnings("serial")
@WebServlet("/NewsServlet")
public class NewsServlet extends HttpServlet {
	private final static String CONTENT_TYPE = "text/html; charset=UTF-8";
	private NewsDao newsDao = null;
       
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (newsDao == null) {
			newsDao = new NewsDaoMySqlImpl();
		}
		List<News> myNews = newsDao.getAllNews();
		writeText(response, new Gson().toJson(myNews));
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
		LogHelper.showInput(jsonIn);
		
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		if (newsDao == null) {
			newsDao = new NewsDaoMySqlImpl();
		}
		
		String action = jsonObject.get("action").getAsString();

		if(action.equals("getNews_activity_id")) {
			List<News> mynews = newsDao.getNews_activity_id();
			writeText(response, gson.toJson(mynews)); 
		} else if(action.equals("getNewsImage")) {
			int activity_id = jsonObject.get("activity_id").getAsInt();
			int imageSize = jsonObject.get("imageSize").getAsInt();
			OutputStream os = response.getOutputStream();
			byte[] image = newsDao.getImage(activity_id);
			if (image != null) {  
				image = ImageUtil.shrink(image, imageSize); //縮圖
				response.setContentType("image/jpeg"); 
				response.setContentLength(image.length); //圖的長度
			}
			os.write(image); //輸出
		} else if (action.equals("getAllNews")) {   //拿到資料轉成JSON Object
			List<News> mynews = newsDao.getAllNews();
			writeText(response, gson.toJson(mynews)); 
		} else if (action.equals("getImage")) {
			OutputStream os = response.getOutputStream();
			int id = jsonObject.get("id").getAsInt();
			int imageSize = jsonObject.get("imageSize").getAsInt();
			byte[] image = newsDao.getImage(id); 
			if (image != null) {  
				image = ImageUtil.shrink(image, imageSize); //縮圖
				response.setContentType("image/jpeg"); 
				response.setContentLength(image.length); //圖的長度
			}
			os.write(image); //輸出
		} else if (action.equals("newsInsert") || action.equals("newsUpdate")) {  
			String newsJson = jsonObject.get("news").getAsString();  //JSON字串裡面有JSON字串
			News news = gson.fromJson(newsJson, News.class);  //解析成news物件
			String imageBase64 = jsonObject.get("imageBase64").getAsString();
			System.out.println("imageBase64 = " + imageBase64);
			byte[] image = Base64.getMimeDecoder().decode(imageBase64);
			;
			int count = 0;
			if (action.equals("newsInsert")) {  
				count = newsDao.insert(news, image);
			} else if (action.equals("newsUpdate")) {
				count = newsDao.update(news, image);
			}
			writeText(response, String.valueOf(count));
		}  else if (action.equals("newsDelete")) {
			String newsJson = jsonObject.get("news").getAsString();
			News news = gson.fromJson(newsJson, News.class);
			int count = newsDao.delete(news.getActivity_id()); 
			writeText(response, String.valueOf(count));
		} else if (action.equals("findById")) {
			int id = jsonObject.get("id").getAsInt();
			News news = newsDao.findById(id);
			writeText(response, gson.toJson(news));
		} else {
			writeText(response, "");
		}
	}
	
	
	
private void writeText(HttpServletResponse response, String outText) throws IOException {
	response.setContentType(CONTENT_TYPE); //純文字
	PrintWriter out = response.getWriter();
	out.print(outText);
	LogHelper.showOutput(outText);
	}

}
