package com.song.web.socket;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.song.chat.message.ChatMessage;
import com.song.chat.message.MessageType;
import com.song.chat.room.Room;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@ServerEndpoint(value = "/chat")
public class ChatEndpoint extends HttpServlet  {
	private Logger log = Logger.getLogger(ChatEndpoint.class.getSimpleName());
	private Room room = Room.getRoom();
        
           
private static final String USERNAME = "root";
private static final String PASSWORD = "";
private static final String Database_name ="jdbc:mysql://localhost/mcrypt";
         Connection conn = null;

	@OnOpen
	public void onOpen(final Session session, EndpointConfig config) {}

	@OnMessage
	public void onMessage(final Session session, final String messageJson,HttpServletRequest request, HttpServletResponse response) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		ChatMessage chatMessage = null;
		try {
			chatMessage = mapper.readValue(messageJson, ChatMessage.class);
		} catch (IOException e) {
			String message = "Badly formatted message";
			try {
				session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, message));
			} catch (IOException ex) { log.severe(ex.getMessage()); }
		} ;

		Map<String, Object> properties = session.getUserProperties();
		if (chatMessage.getMessageType() == MessageType.LOGIN) {
			String name = chatMessage.getMessage();
                        /**
                         * new code here
                         * 
                         */
                        Dologin(request, response,name,name);
                        
			properties.put("name", name);
			room.join(session);
			room.sendMessage(name + " - Joined the chat room");
		}
		else {
			String name = (String)properties.get("name");
			room.sendMessage(name + " - " + chatMessage.getMessage());
		}
	}
        
        // check the database whethe user is registerd
    public int Dologin(HttpServletRequest request, HttpServletResponse response ,String usname, String psword) throws IOException{
       PrintWriter mvc= response.getWriter();        
         try{
            
              Class.forName("com.mysql.jdbc.Driver");
         conn = DriverManager.getConnection(Database_name, USERNAME, PASSWORD);
             String sql="SELECT * FROM user WHERE email=? AND password=? ";
     //  Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
       PreparedStatement stmt = conn.prepareStatement(sql);
       stmt.setString(1, usname);
       stmt.setString(2, psword);
	ResultSet rs = stmt.executeQuery();          
        int counter=0;
          while (rs.next()) {
                            counter++;
		            //set_user_id(rs.getString("user_id"));
                          //  success
                            
                     //  buffer.append(rs.getInt("user_id"));
                      
			}
         // mvc.println(counter + get_user_id());
          if(counter<=0){
              
              
              return 0;
              
             /* 
              mvc.println("<a href='Client'>Login here </a>");
              String gf="<font color='red'>Sorry your credentials dont match any account please check you email and password or contact the admin to register</font><br/>.<br/> <div>"
                      + "<a href='Client' class='submit'>Login here </a> </div>";
              HttpSession goaway=request.getSession();
              goaway.setAttribute("nop", gf);
              // emergency_header(request, response);
            //mvc.println(gf); 
                     */
          } else{
              return 1;
              
          } 
         } catch (Exception e){
             
         }
         return 2;
    }

	@OnClose
	public void OnClose(Session session, CloseReason reason) {
		room.leave(session);
		room.sendMessage((String)session.getUserProperties().get("name") + " - Left the room");
	}

	@OnError
	public void onError(Session session, Throwable ex) { log.info("Error: " + ex.getMessage()); }
}
