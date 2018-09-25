package drinkshop.cp102.server.order.online;

import java.io.*;
import java.util.*;

import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.Session;
import javax.websocket.OnOpen;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.OnClose;
import javax.websocket.CloseReason;

@ServerEndpoint("/AllChatServer/{userName}")
public class AllChatServer {//@ServerEndpoint 背後會繼承webserver socket並需要實作4個方法
	private static final Set<Session> connectedSessions = Collections.synchronizedSet(new HashSet<>());//Session for webscoket存連線資訊,可能會多人連,要考慮執行緒安全性synchronized
	//等第一個人加進HashSet另一個再加,如果有mutilthread就要考慮線程安全
	/*
	 * 如果想取得HttpSession與ServletContext必須實作
	 * ServerEndpointConfig.Configurator.modifyHandshake()，
	 * 參看https://stackoverflow.com/questions/21888425/accessing-servletcontext-and-
	 * httpsession-in-onmessage-of-a-jsr-356-serverendpo
	 */
	@OnOpen
	public void onOpen(@PathParam("userName") String userName, Session userSession) throws IOException {//@PathParam("userName") 取網址列userName的值並傳給方法的參數userName
		connectedSessions.add(userSession);//Session for webscoket存連線資訊 與當初的java web http Session不同
		String text = String.format("Session ID = %s, connected; userName = %s", userSession.getId(), userName);
		System.out.println(text);
	}

	@OnMessage
	public void onMessage(Session userSession, String message) {//onOpen,onClose為了onMessage準備,有人發訊息時onMessage被呼叫
		for (Session session : connectedSessions) {//mary發訊息上來
			if (session.isOpen()) {//每個檢查是否都在線上,比如john,包含mary自己都要檢查
				session.getAsyncRemote().sendText(message);//將訊息發給john,包含mary
			} else {
				connectedSessions.remove(userSession);//不在線上,隨時移除
			}
		}
		System.out.println("Message received: " + message);
	}

	@OnClose
	public void onClose(Session userSession, CloseReason reason) {//斷線,socket程式有一個斷線其他人會馬上知道onClose馬上被呼叫,斷線者的userSession要移除,除非要儲存訊息像line,連線加進來,斷線移除
		connectedSessions.remove(userSession);
		String text = String.format("session ID = %s, disconnected; close code = %d; reason phrase = %s",
				userSession.getId(), reason.getCloseCode().getCode(), reason.getReasonPhrase());
		System.out.println(text);
	}

	@OnError
	public void onError(Session userSession, Throwable e) {//onError 錯誤傳訊息,比較少用
		System.out.println("Error: " + e.toString());
	}

}
