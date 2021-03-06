package drinkshop.cp102.server.members;

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
@WebServlet("/MemberServlet")
public class MemberServlet extends HttpServlet {
	private final static String CONTENT_TYPE = "text/html; charset=UTF-8";
	private MemberDao memberDao = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (memberDao == null) {
			memberDao = new MemberDaoMySqlImpl();
		}
		List<Member> newsList = memberDao.getAll();
		writeText(response, new Gson().toJson(newsList));
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
		if (memberDao == null) {
			memberDao = new MemberDaoMySqlImpl();
		}
		String action = jsonObject.get("action").getAsString();

		if (action.equals("getAll")) {
			List<Member> newsList = memberDao.getAll();
			writeText(response, gson.toJson(newsList));
		}else if (action.equals("isUserValid")) {
			String member_account = jsonObject.get("name").getAsString();
			String member_password = jsonObject.get("password").getAsString();
			boolean isMemberValid = memberDao.isMemberValid(member_account, member_password);
			writeText(response, String.valueOf(isMemberValid));
		}else if (action.equals("findMemberByAccountAndPassword")) {
			String member_account = jsonObject.get("name").getAsString();
			String member_password = jsonObject.get("password").getAsString();
			Member member = memberDao.findMemberByAccountAndPassword(member_account, member_password);
			writeText(response, gson.toJson(member));
		}else if (action.equals("findById")) {
			String member_id = jsonObject.get("member_id").getAsString();
			Member member = memberDao.findById(Integer.parseInt(member_id));
			writeText(response, gson.toJson(member));
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