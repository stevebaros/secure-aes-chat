/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.song.web.socket;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class Client extends HttpServlet {
     private String email = null;
    private String password = null;  
     private String fname=null;
    private String lname=null;
    private String glaobaId=null;
    private String equipId=null;
    private String policyname=null;
    private double policyfee=0;
    private String csscode=null;
    private String Exp=null;
    private String glob;
    private String Eqtype=null;
    private String inuser=null;
    private String message;
   
private static final String USERNAME = "root";
private static final String PASSWORD = "";
private static final String Database_name ="jdbc:mysql://localhost/mcrypt";
         Connection conn = null;
         
          // send/receive max 256 bytes per attempt
    private static final int MAXBYTE = 256;

    // this method contains a a login for,
    protected void processlogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter(); 
        try {          
            emergency_header(request, response);
            
           
            /**
             * 
             * login
             */
            out.println("<h3>Please login from here</h3><br/> <form method=\"POST\" action=\"Client?reg=login\">"
                    + "<input type=\"email\" id=\"userid\" class='inputbox' name=\"urmail\" placeholder=\"Enter email\" required />\n" +
"<input type=\"password\" id=\"pwd\" name=\"password\" class='inputbox'  placeholder=\"password\" required />\n" +
"<input type=\"submit\" value=\"login\" class='submit'  /> </form>");
            out.println("<h1>We offer premium security</h1> <br/>");
            footer(request, response);
                        
        } finally {
            out.close();
        }   
        
        
    }
    
    

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
           PrintWriter out=response.getWriter();
           
         response.setContentType("text/html;charset=UTF-8");
        header(request, response);
      try{
           HttpSession thanks=request.getSession();
            setUserType(thanks.getAttribute("usertype").toString());
           String checker=request.getParameter("reg");
           Class.forName("com.mysql.jdbc.Driver");
         conn = DriverManager.getConnection(Database_name, USERNAME, PASSWORD);
          if(getUserType().equals("normal") || getUserType().equals("admin") ){
       if(checker.equals("Takeit")){
           licenceAgreement(request, response);
       } 
      
      if(checker.equals("details")){
          my_insure_covers(request, response);
      }
      
      if(checker.equals("out")){
          logout(request, response);
      }
      } else{
            response.sendRedirect("Client");  
          } 
      }
      
      catch(Exception fre){      // catching exception for session and the rest    
           processlogin(request, response); // this is the login form function
          out.println(fre+"Your not logged in");
          sendMsg(response);
           footer(request, response);
       }
      
         footer(request, response);
        
    }

    public void sendMsg(HttpServletResponse response) throws IOException{
         //delph
         PrintWriter mvc=response.getWriter();
            
            Scanner scan = new Scanner(System.in);

        int selection = 0;

        while(selection != 3) {

            mvc.println("Welcome.");
            mvc.println("Please select operation.");
            mvc.println("1. Send File (to all users will connect)");
            mvc.println("2. Receive File");
            mvc.println("3. Exit");
            mvc.print("> ");

            /*selection = scan.nextInt();

            switch (selection) {
                case 1:
                    mvc.println("Enter port for listening.");

                    // get port from user
                    int port = scan.nextInt();

                    mvc.println("Enter file path (C:/file.txt) for sending to all clients: ");

                    // get file path from user
                    String filePath = scan.next();

                    // create server socket for listening
                    ServerSocket serverSocket = new ServerSocket(port);

                    // create logger for thread
                    Logger threadLogger = Logger.getLogger("serverLogger");

                    // read selected file into string
                    String fileContent = FileManager.getInstance().readFile(filePath);
                    if(!fileContent.equalsIgnoreCase("")) {
                        // get selected file name
                        String fileName = FileManager.getInstance().getFileName(filePath);

                        mvc.println("Server mode initiated. Serving clients on port " + port);

                        while(true) {
                            Socket clientSocket = serverSocket.accept();

                            // Create new thread to handle new client
                            Thread thread = new Thread(new ServerProtocol(clientSocket, threadLogger, fileName, fileContent));
                            thread.start();
                            threadLogger.info("Created and started new thread " + thread.getName() + " for client.");
                        }
                    } else {
                        System.err.println("File not found.");
                    }

                    break;

                case 2:
                    // get server from user
                    mvc.println("Enter server for receiving.");
                    String server = scan.next();

                    // get port from user
                    mvc.println("Enter port for listening.");
                    int servPort = scan.nextInt();

                    Socket clientSocket = new Socket(server, servPort);

                    DataInputStream fromServer = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream toServer = new DataOutputStream(clientSocket.getOutputStream());

                    // client receives p(prime number) and g (prime number's generator) from server
                    // client selects a secret number (b)
                    // client calculates B=g^b(modp)
                    // client receives A from the server
                    // client sends B to server.
                    // client calculates s=A^b(modp)
                    // client now has the secret key

                    //---implementation
                    // receive prime number from server
                    BigInteger p = new BigInteger(fromServer.readUTF());
                    // receive prime number generator from server
                    BigInteger g = new BigInteger(fromServer.readUTF());
                    // receive A from server
                    BigInteger A = new BigInteger(fromServer.readUTF());

                    // generate secret b
                    Random randomGenerator = new Random();
                    BigInteger b = new BigInteger(1024, randomGenerator); // secret key b (private) (on client)

                    // calculate public B
                    BigInteger B = g.modPow(b, p); // calculated public client key (B=g^b(modp))

                    // send B to server
                    toServer.writeUTF(B.toString());

                    // calculate secret key
                    BigInteger decryptionKeyClient = A.modPow(b, p);

                    mvc.println("Calculated key: " + decryptionKeyClient);

                    // generate AES key
                    Key key = generateKey(decryptionKeyClient.toByteArray());

                    // continue below...
                    mvc.println("Waiting for file or text.");

                    try {
                        // read filename from server
                        String fName = fromServer.readUTF();

                        // read encrypted file contents from server
                        String encryptedFile = "";
                        String line;
                        while (!(line = fromServer.readUTF()).equalsIgnoreCase("")) {

                            encryptedFile += line;

                            if (line.isEmpty()) {
                                break;
                            }
                        }

                        // decrypt downloaded file
                        String decryptedFile = FileManager.getInstance().decryptFile(encryptedFile, key);

                        // write to file
                        FileManager.getInstance().writeFile(fName, decryptedFile);

                        // inform user
                        mvc.println("File download complete. Saved in ./" + fName + "\n");

                    } catch (Exception e) {
                        System.err.println("Error while creating/reading server socket: " + e);
                    }

                    break;
                case 3:
                    mvc.println("Bye bye.");
                    break;
                default:
                    mvc.println("Select 1, 2 or 3.");
                    break;*/
            }

        }

            
            //delph
        
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
           PrintWriter out=response.getWriter();
          // String checker=request.getParameter("reg");
        
         response.setContentType("text/html;charset=UTF-8");
        header(request, response);
        
        try{
             String checker=request.getParameter("reg");
        Class.forName("com.mysql.jdbc.Driver");
         conn = DriverManager.getConnection(Database_name, USERNAME, PASSWORD);
         
       if(checker.equals("login")){
           Dologin(request, response);
       }
        } catch (ClassNotFoundException ex) { 
            emergency_header(request, response);
           out.println(ex);
         } catch (SQLException ex) {  
             emergency_header(request, response);
               out.println(ex);
         }  catch(NullPointerException c){
             emergency_header(request, response);
             out.println("sorry please login properly");
         }
        
         footer(request, response);
        
    }

   // check the database whethe user is registerd
    public void Dologin(HttpServletRequest request, HttpServletResponse response) throws IOException{
       PrintWriter mvc= response.getWriter();        
         try{
             setPassword(request.getParameter("password"));
             set_mail(request.getParameter("urmail"));
             String sql="SELECT * FROM user WHERE email=? AND password=? ";
     //  Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
       PreparedStatement stmt = conn.prepareStatement(sql);
       stmt.setString(1, get_email());
       stmt.setString(2, getPassword());
	ResultSet rs = stmt.executeQuery();          
        int counter=0;
          while (rs.next()) {
                            counter++;
		            set_user_id(rs.getString("user_id"));
                            set_mail(rs.getString("email"));
                            setfname(rs.getString("FirstName"));
                            setLname(rs.getString("lastname"));
                            setUserType(rs.getString("user_type"));
                     //  buffer.append(rs.getInt("user_id"));
                      
			}
         // mvc.println(counter + get_user_id());
          if(counter<=0){
              mvc.println("<a href='Client'>Login here </a>");
              String gf="<font color='red'>Sorry your credentials dont match any account please check you email and password or contact the admin to register</font><br/>.<br/> <div>"
                      + "<a href='Client' class='submit'>Login here </a> </div>";
              HttpSession goaway=request.getSession();
              goaway.setAttribute("nop", gf);
               emergency_header(request, response);
            mvc.println(gf);   
          } else{             // String succ="welcome ";
              // mvc.println("ohh yes \n");
               // mvc.print("num "+ get_user_id());
              
              
               //delph
            
            Scanner scan = new Scanner(System.in);

        int selection = 0;

        while(selection != 3) {

            mvc.println("Welcome.");
            mvc.println("Please select operation.");
            mvc.println("1. Send File (to all users will connect)");
            mvc.println("2. Receive File");
            mvc.println("3. Exit");
            mvc.print("> ");

            selection = scan.nextInt();

            switch (selection) {
                case 1:
                    mvc.println("Enter port for listening.");

                    // get port from user
                    int port = scan.nextInt();

                    mvc.println("Enter file path (C:/file.txt) for sending to all clients: ");

                    // get file path from user
                    String filePath = scan.next();

                    // create server socket for listening
                    ServerSocket serverSocket = new ServerSocket(port);

                    // create logger for thread
                    Logger threadLogger = Logger.getLogger("serverLogger");

                    // read selected file into string
                    String fileContent = FileManager.getInstance().readFile(filePath);
                    if(!fileContent.equalsIgnoreCase("")) {
                        // get selected file name
                        String fileName = FileManager.getInstance().getFileName(filePath);

                        mvc.println("Server mode initiated. Serving clients on port " + port);

                        while(true) {
                            Socket clientSocket = serverSocket.accept();

                            // Create new thread to handle new client
                            Thread thread = new Thread(new ServerProtocol(clientSocket, threadLogger, fileName, fileContent));
                            thread.start();
                            threadLogger.info("Created and started new thread " + thread.getName() + " for client.");
                        }
                    } else {
                        System.err.println("File not found.");
                    }

                    break;

                case 2:
                    // get server from user
                    mvc.println("Enter server for receiving.");
                    String server = scan.next();

                    // get port from user
                    mvc.println("Enter port for listening.");
                    int servPort = scan.nextInt();

                    Socket clientSocket = new Socket(server, servPort);

                    DataInputStream fromServer = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream toServer = new DataOutputStream(clientSocket.getOutputStream());

                    // client receives p(prime number) and g (prime number's generator) from server
                    // client selects a secret number (b)
                    // client calculates B=g^b(modp)
                    // client receives A from the server
                    // client sends B to server.
                    // client calculates s=A^b(modp)
                    // client now has the secret key

                    //---implementation
                    // receive prime number from server
                    BigInteger p = new BigInteger(fromServer.readUTF());
                    // receive prime number generator from server
                    BigInteger g = new BigInteger(fromServer.readUTF());
                    // receive A from server
                    BigInteger A = new BigInteger(fromServer.readUTF());

                    // generate secret b
                    Random randomGenerator = new Random();
                    BigInteger b = new BigInteger(1024, randomGenerator); // secret key b (private) (on client)

                    // calculate public B
                    BigInteger B = g.modPow(b, p); // calculated public client key (B=g^b(modp))

                    // send B to server
                    toServer.writeUTF(B.toString());

                    // calculate secret key
                    BigInteger decryptionKeyClient = A.modPow(b, p);

                    mvc.println("Calculated key: " + decryptionKeyClient);

                    // generate AES key
                    Key key = generateKey(decryptionKeyClient.toByteArray());

                    // continue below...
                    mvc.println("Waiting for file or text.");

                    try {
                        // read filename from server
                        String fName = fromServer.readUTF();

                        // read encrypted file contents from server
                        String encryptedFile = "";
                        String line;
                        while (!(line = fromServer.readUTF()).equalsIgnoreCase("")) {

                            encryptedFile += line;

                            if (line.isEmpty()) {
                                break;
                            }
                        }

                        // decrypt downloaded file
                        String decryptedFile = FileManager.getInstance().decryptFile(encryptedFile, key);

                        // write to file
                        FileManager.getInstance().writeFile(fName, decryptedFile);

                        // inform user
                        mvc.println("File download complete. Saved in ./" + fName + "\n");

                    } catch (Exception e) {
                        System.err.println("Error while creating/reading server socket: " + e);
                    }

                    break;
                case 3:
                    mvc.println("Bye bye.");
                    break;
                default:
                    mvc.println("Select 1, 2 or 3.");
                    break;
            }

        }

            
            //delph
              
              
               /* HttpSession goin=request.getSession();
		goin.setAttribute("theid", get_user_id());
		goin.setAttribute("themail", get_email());
                goin.setAttribute("fname", getfname());
                goin.setAttribute("lname", getLname());
                goin.setAttribute("user_type",getUserType());
                if(getUserType().equals("normal")){
                response.sendRedirect("Client?reg=details&uid="+get_user_id()+"&lname="+getLname());
                } else if(getUserType().equals("admin")){
                response.sendRedirect("Admin?reg=addnew");
                }*/ 
          }
        } catch(SQLException hj){
           // mvc.println("ohh no \n" +hj);
        }
    }
    public void licenceAgreement(HttpServletRequest request, HttpServletResponse response) throws IOException{
       PrintWriter mvc=response.getWriter();
       HttpSession noway=request.getSession();
       if (noway.getAttribute("usertype").toString().equals("normal")){
       String mt[]={"January","February","March","April","May","June","July","August","September","October","November","December" };
       mvc.print("<h1>License Agreement ..!!</h1>");
       mvc.println("<div>Do you agree wiht our licence of insuring  your <font color=red> "+request.getParameter("prodname")+ " </font> At <font color=blue>"+request.getParameter("policyfee")+"</font>Anually<br/>"
               + ""
               + "select the start date of the insurance policy"
               + "</div>");
       mvc.println("<form method='post' action='Client?reg=complete_transaction'><br/>\n" + 
      "<select name='month' class='inputbox'>"
               );
         for (String mt1 : mt) {
             mvc.print( "<option>"+mt1);
             mvc.print("</option>");
         }               
    
               mvc.print( "</select>\n"+
               
       " <select name='year' class='inputbox'>"
             );
    for(int x=2015;x<2200;x++){
    mvc.print( "<option>"+x);
     mvc.print("</option>");
    }
     mvc.print("</select>"+       
"<input type=\"hidden\" name=\"equip_id\" value='"+request.getParameter("prodid") +"'>\n" +
"<input type=\"hidden\" name=\"user_id\" value='"+request.getParameter("hashcode")  +"'>\n" +
               
               "<input type='submit' class='submitt' value='I Agree'/>"+
"</form> ");
     mvc.println("<div><a href='Admin?reg=showall&flg=premium'><input  type='submit' class='submit' value='Decline' onclick='Admin?reg=showall&flg=premium'/></a></div>");
 
    } else {
           mvc.println("Please you are not allowed to perform such a transaction it is illegal");
       }
    }
    public void my_insure_covers(HttpServletRequest request, HttpServletResponse response) throws IOException{
      PrintWriter mvc= response.getWriter();        
         try{
             setPassword(request.getParameter("password"));
             set_mail(request.getParameter("urmail"));
             set_user_id(request.getParameter("uid"));
             setLname(request.getParameter("lname"));            
             
             
             
           
        } catch(Exception hj){
            mvc.println("Nothing \n" +hj);
            hj.printStackTrace();
        }  
    }
    
     // generates usable SecretKey from given value. In default, user cannot create keys.
    private static Key generateKey(byte[] sharedKey)
    {
        // AES supports 128 bit keys. So, just take first 16 bits of DH generated key.
        byte[] byteKey = new byte[16];
        for(int i = 0; i < 16; i++) {
            byteKey[i] = sharedKey[i];
        }

        // convert given key to AES format
        try {
            Key key = new SecretKeySpec(byteKey, "AES");

            return key;
        } catch(Exception e) {
            System.err.println("Error while generating key: " + e);
        }

        return null;
    }
    
  // log out  
public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException{
     HttpSession yes=request.getSession();
          yes.invalidate();
          response.sendRedirect("Client");
}
  // here we are  using java beans  for easy data acces and memory management
    
    public String getfname(){
        return fname;
    }
    public void setfname(String firstname){
        this.fname=firstname;
    }    
     public String getLname(){
        return lname;
    }
    public void setLname(String lastname){
        this.lname=lastname;
    }
     public String getPassword() { 
        return password; 
    }

    public void setPassword(String password) { 
        this.password = password; 
    }
    
    public String get_email(){
        return email;
    }
    public void set_mail(String real_mail){
        this.email=real_mail;
    }
    public void set_user_id(String uid){
    this.glaobaId=uid;    
    }
    public String get_user_id(){
    return glaobaId;
    }
      public void setUserType(String utype){
        this.inuser=utype; }
    public String getUserType(){
     return inuser;
    }  
         
    
   // this for styling the page 
 public void header(HttpServletRequest request, HttpServletResponse response) throws IOException{
        try{
        
        PrintWriter out = response.getWriter();
          HttpSession thanks=request.getSession();
         glob=thanks.getAttribute("theid").toString();
          String inemail= thanks.getAttribute("themail").toString();
          String fnme=thanks.getAttribute("fname").toString();
          String lnme=thanks.getAttribute("lname").toString();
          String utype=thanks.getAttribute("usertype").toString();
        response.setContentType("text/html;charset=UTF-8");
        out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Welcome </title>");            
            out.println("</head>");
            out.println("<body>\n" +
"        <div class=\"container\">\n" +
"        	<div class=\"header\">\n"
                    + "<style>"+ set_CssStyle()+ " </style>" +
                    
"        		\n" +
"        	</div>\n" +
"        	<div class=\"inner\">");
            set_user_id(glob);
            setLname(lnme);
            setfname(fnme);
            out.println("<h1>Key Exchange protocal </h1> <br/>");
            out.print("welcome  " + fnme + " logged in with " +inemail );
            out.println("<h2>");
        out.println(" <ul class=\"social\"> "  );
       // out.println("<li><a href='Admin?reg=login'>Login here</a> |</li>");
        if(getUserType().toString().equals("admin")){
        out.println("<li><a href='Admin?reg=regis'>Register here</a> |</li>");
        out.println("<li><a href='Admin?reg=addnew'>Add new Policy</a> |</li>");
        out.println("<li><a href='Admin?reg=showall&flg=premuim'>Available Policies</a> |</li>\n");
        out.println("<li><a href='Admin?reg=allusers'>All Clients</a></li> \n");
        out.println("<li><a href='Client?reg=out'>Logout</a> </li>");
        }
        if(getUserType().toString().equals("normal")){
        out.println("<li><a href='Admin?reg=showall&flg=premium'>Available Policies</a> |</li>\n");
        out.println("<li><a href='Client?reg=details&uid="+get_user_id()+ "&lname="+getLname() +"'>My policies</a></li> "
                + "<li><a href='Client?reg=out'>Logout</a>  </li>");}
        out.println("</ul></h2><br/> " ); 
        }
        
        catch(Exception rf){
           
        }

}
 
 
 public void send_and_recieve_chat(){
     
     
 }
public void emergency_header(HttpServletRequest request, HttpServletResponse response) throws IOException{
    PrintWriter out =response.getWriter();
      out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Welcome to Amart</title>");            
            out.println("</head>");
            out.println("<body>\n" +
"        <div class=\"container\">\n" +
"        	<div class=\"header\">\n"
                    + "<style>"+ set_CssStyle()+ " </style>" +
                    
"        		\n" +
"        	</div>\n" +
"        	<div class=\"inner\">");
            
            out.println("<h1> Key exchange protocol </h1> <br/> "
                    + "<br/>"
                    );
           
}    
public String set_CssStyle(){
    String styles="html, body, .container{ height: 100%; margin: 0; padding: 0; }\n" +
"body{\n" +
"	min-width: 320px;\n" +
"	overflow: hidden;\n" +
"	background:#dbd8c7;\n" +
"}\n" +
"body{\n" +
"	font-family: 'Chivo', Helvetica, Arial, sans-serif;\n" +
"	font-size: 14px;\n" +
"	font-weight: 200;\n" +
"	letter-spacing: 2px;\n" +
"	line-height: 14px;\n" +
"	//color: #202020;\n" +
"}\n" +
"\n" +
"a{\n" +
"	color: #999;\n" +
"	text-decoration: none;\n" +
"	-webkit-transition: all 0.2s linear;\n" +
"	-moz-transition: all 0.2s linear;\n" +
"	-o-transition: all 0.2s linear;\n" +
"	transition: all 0.2s linear;\n" +
"}\n" +
"a:hover{\n" +
"	color: #e5512d;\n" +
"	background-color:#FFF;\n" +
"}\n" +
"\n" +
".clr {\n" +
"	clear:both;}\n" +
"\n" +
"img {\n" +
"	max-width:100%;\n" +
"	height:auto;}\n" +
"	\n" +
".header {\n" +
"	width:100%;\n" +
"	top:0;\n" +
"	height:50px;\n" +
"	padding:15px 0;\n" +
"	text-align:center;\n" +
"	background:url(../images/bg-01.jpg) repeat-x;}\n" +
"\n" +
".container{\n" +
"	max-width:100%;\n" +
"	margin:0 auto;\n" +
"	box-shadow: 0px -5px 0 0px #ee4e1d inset, 0px 5px 0 0px #ee4e1d inset;\n" +
"}\n" +
"\n" +
".inner {\n" +
"	max-width:1024px;\n" +
"	margin:0 auto;}\n" +
"\n" +
".left-block {\n" +
"	float:left;\n" +
"	max-width:47%;\n" +
"	text-align: right;\n" +
"	margin-top:20px;\n" +
"	padding:68px 10px 0 0;\n" +
"	}\n" +
"	\n" +
".left-block p {\n" +
"	font-family:'AmaticSCBold', Verdana, Geneva, sans-serif;\n" +
"	font-size: 53px;\n" +
"	font-weight: normal;\n" +
"	word-spacing:5px;\n" +
"	letter-spacing: 0px;\n" +
"	line-height: 55px;\n" +
"	color: #404040;\n" +
"	margin-top:22px;}\n" +
"	\n" +
".right-block {\n" +
"	float:left;\n" +
"	max-width:50%;\n" +
"	text-align: center;\n" +
"	margin-top:20px;\n" +
"	padding-left:10px;}\n" +
"	\n" +
".bottom {\n" +
"	width:100%;\n" +
"	padding:10px;\n" +
"	bottom:0;\n" +
"	border-top:1px dashed #CCC;\n" +
"	text-align:center;}\n" +
"\n" +
"ul.signup {\n" +
"	margin: 15px auto 15px -45px;}\n" +
"ul.signup li {\n" +
"	display:inline-block;}\n" +
"	\n" +
".inputbox {\n" +
"	background-color: #FAFAFA;\n" +
"    border: 1px solid #CCCCCC;\n" +
"    border-radius: 4px 4px 4px 4px;\n" +
"    box-shadow: 0 1px 1px #999999 inset;\n" +
"    color: #333333;\n" +
"    font-family: Arial,Helvetica,sans-serif;\n" +
"    font-size: 14px;\n" +
"    font-weight: normal;\n" +
"    height: 33px;\n" +
"    padding-left: 5px;\n" +
"    text-decoration: none;\n" +
"    width: 270px;\n" +
"	}\n" +
".submit {\n" +
"	background-color: #ee4e1d;\n" +
"    border: 0 solid #CCCCCC;\n" +
"    border-radius:3px;\n" +
"    -moz-box-shadow: 0 0 9px #666666 inset;\n" +
"	-webkit-box-shadow: 0 0 9px #666666 inset;\n" +
"	box-shadow: 0 0 9px #666666 inset;\n" +
"    color: #FFFFFF;\n" +
"    cursor: pointer;\n" +
"    font-family: Helvetica,Arial,\"Lucida Grande\",sans-serif;\n" +
"    font-size: 12px;\n" +
"    font-weight: bold;\n" +
"    height: 34px;\n" +
"    padding: 4px 8px;\n" +
"    text-align: center;\n" +
"    text-shadow: -1px -1px #666666;\n" +
"    text-transform: uppercase;\n" +
"	-webkit-transition: all 0.2s linear;\n" +
"	-moz-transition: all 0.2s linear;\n" +
"	-o-transition: all 0.2s linear;\n" +
"	transition: all 0.2s linear;}\n" +
            ".submitt {\n" +
"	background-color: #de3ee2;\n" +
"    border: 0 solid #CCCCCC;\n" +
"    border-radius:3px;\n" +
"    -moz-box-shadow: 0 0 9px #666666 inset;\n" +
"	-webkit-box-shadow: 0 0 9px #666666 inset;\n" +
"	box-shadow: 0 0 9px #666666 inset;\n" +
"    color: #FFeeec;\n" +
"    cursor: pointer;\n" +
"    font-family: Helvetica,Arial,\"Lucida Grande\",sans-serif;\n" +
"    font-size: 12px;\n" +
"    font-weight: bold;\n" +
"    height: 34px;\n" +
"    padding: 4px 8px;\n" +
"    text-align: center;\n" +
"    text-shadow: -1px -1px #666666;\n" +
"    text-transform: uppercase;\n" +
"	-webkit-transition: all 0.2s linear;\n" +
"	-moz-transition: all 0.2s linear;\n" +
"	-o-transition: all 0.2s linear;\n" +
"	transition: all 0.2s linear;}\n" +
".submit:hover {\n" +
"	box-shadow: 0 0 5px #666666 inset;}\n" +
"\n" +
"ul.social {\n" +
"	margin: 15px auto 15px -46px;}\n" +
"ul.social li {\n" +
"	display:inline-block;}\n" +
"	.social li a {\n" +
"		margin:5px;\n" +
"		opacity:0.8;\n" +
"		background:none;}\n" +
"		.social li a:hover {\n" +
"			opacity:1;}\n" +
"		\n" +
"		\n" +
"/*---- Media Queries to make design responsive ----*/\n" +
"    \n" +
"@media only screen and (max-width: 640px) and (min-width: 320px){\n" +
"	body, .container {\n" +
"		overflow-x:hidden;}\n" +
"	.left-block {\n" +
"	float:left;\n" +
"	max-width:100%;\n" +
"	text-align: center;\n" +
"	padding:0;\n" +
"	margin:0;\n" +
"	}\n" +
"	.left-block p {\n" +
"		margin:0 0 10px 0;}\n" +
"	.right-block {\n" +
"		max-width:100%;\n" +
"		float:left;\n" +
"		margin:0;\n" +
"		padding:0;\n" +
"	}\n" +
"	.bottom { padding:0px;}\n" +
"	.submit { margin-top:10px;}\n" +
"}\n" +
"\n" +
"@media only screen and (max-width: 319px) and (min-width: 240px){\n" +
"		body, .container {\n" +
"		overflow-x:hidden;}\n" +
"	.header {\n" +
"		height:40px;}\n" +
"	.header img {\n" +
"		max-width:80%;\n" +
"		height:auto;\n" +
"		margin-right:46px;}\n" +
"	.left-block {\n" +
"	float:left;\n" +
"	max-width:84%;\n" +
"	text-align: center;\n" +
"	padding:0;\n" +
"	margin:0;\n" +
"	}\n" +
"	.left-block p {\n" +
"		margin:0 0 10px 0;}\n" +
"	.right-block {\n" +
"		max-width:84%;\n" +
"		float:left;\n" +
"		margin:0;\n" +
"		padding:0;\n" +
"	}\n" +
"	.bottom { padding:0px;}\n" +
"	.submit { margin-top:10px;}\n" +
"} ";
    this.csscode=styles;
return csscode;
}     
public void footer(HttpServletRequest request, HttpServletResponse response) throws IOException{
     PrintWriter out = response.getWriter();
     
             out.println("            </div>\n" +
"        <div class=\"clr\">&nbsp;</div>\n" +
"          	<div class=\"bottom\">\n" +
"            	<div class=\"inner\">\n" +
"                \n" +
"                \n" +
"                \n" +
"            </div>\n" +
"            </div>\n" +
"		<div class=\"logo\">\n" +
"				\n" +
"			</div>\n" +
"			<p class=\"copyright\" align=\"center\">\n" +
"				<span>&copy; security Group</span>\n" +
"			</p>\n" +
"        </div>\n" +
"    </body>\n" +
"</html>");
}

}

