package client;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
class Request
{
	private int type;
	private String word;
	private String meaning;
	
	public Request(int type,String word,String other)
	{
		this.type=type;
		this.word=word;
		this.meaning=other;
	}
	
	public String createJSON() throws JSONException
	{
		JSONObject json = new JSONObject();
		json.put("type", type);
		json.put("word", word);
		json.put("meaning", meaning);
		
		return json.toString();
	}
}

public class DictionaryClient {
	//IP and port
	private String IP ;
	private int port;
	//private Socket socket;
	//private DataInputStream input;
	//private DataOutputStream output;
	
	public  DictionaryClient(String IP,int port)
	{
		this.IP=IP;
		this.port=port;
	}
	
	public static String searchInput(String s) throws JSONException
	{
		String wordSearch = s.toUpperCase();
		Request searchReq = new Request(1,wordSearch,"none");
		return searchReq.createJSON();
	}
	
	public static String searchOutput(String d) throws JSONException
	{
		JSONObject search = new JSONObject(d);
		Object result = search.get("reply");

		return result.toString();
		
	}
	
	public String search(String word) throws Exception
	{
		String defn=null;
		try(Socket socket = new Socket(IP,port);) {
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			String wordSearch = searchInput(word);
			output.writeUTF(wordSearch);
			output.flush();
			try {
				String d = input.readUTF();
				defn=searchOutput(d);
				
			}catch(EOFException e)
			{
			}
			
			input.close();
			output.close();
			socket.close();
		}
		catch(ConnectException e)
		{
			return "error";
		}
		catch(UnknownHostException e)
		{
			return "error1";
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		
				return defn;
	}
	
	public static String addInput(String s,String m) throws JSONException
	{
		
		String wordAdd = s.toUpperCase();
		String meaningAdd = m;
		Request addReq = new Request(2,wordAdd,meaningAdd);
		return addReq.createJSON();
	}
	
	public static String addOutput(String d) throws JSONException
	{
		JSONObject add = new JSONObject(d);
		Object result = add.get("reply");

		return result.toString();
	}
	
	public String add(String word,String meaning) throws Exception
	{
		String result=null;
		try(Socket socket = new Socket(IP,port);) {
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			String wordAdd = addInput(word,meaning);
			output.writeUTF(wordAdd);
			output.flush();
			try {
				String d = input.readUTF();
				result=addOutput(d);
				
			}catch(EOFException e)
			{	
			}
			input.close();
			output.close();
			socket.close();
		}
		catch(ConnectException e)
		{
			return "error";
		}
		catch(UnknownHostException e)
		{
			return "error1";
		}catch(IOException e)
		{
			e.printStackTrace();
		}
						return result;
	}
	
	public static String deleteInput(String s) throws JSONException
	{
		String wordRemove = s.toUpperCase();
		Request deleteReq = new Request(3,wordRemove,"none");
		return deleteReq.createJSON();
	}
	
	public static String deleteOutput(String d ) throws JSONException
	{
		JSONObject delete = new JSONObject(d);
		Object result = delete.get("reply");

		return result.toString();
	}
	
	public String remove(String s) throws Exception
	{
		String result=null;
		try(Socket socket = new Socket(IP,port);) {
			socket.setSoTimeout(10000);
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			String word = deleteInput(s);
			output.writeUTF(word);
			output.flush();
			try {
				String d = input.readUTF();
				result=searchOutput(d);
				
			}catch(EOFException e)
			{
				
			}
			input.close();
			output.close();
			socket.close();
		}
		catch(ConnectException e)
		{
			return "error";
		}
		catch(UnknownHostException e)
		{
			return "error1";
		}catch(IOException e)
		{
			e.printStackTrace();
		}
				return result;
	}
	
	public void clientStart () throws Exception {
		try(Socket s = new Socket(IP,port);) {
			
		}
		
		catch(UnknownHostException e)
		{
			e.printStackTrace();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	

     }
}
