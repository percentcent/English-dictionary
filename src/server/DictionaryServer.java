package server;
import java.net.*;
import java.util.*;
import java.io.*;
import org.json.*;
import java.util.concurrent.*;
class Reply
{
	private int type;
	private String reply;
	private boolean errorSign;
	
	public Reply(int type,String reply,boolean sign)
	{
		this.type=type;
		this.reply=reply;
		this.errorSign=sign;
	}
	
	public String createJSON() throws JSONException
	{
		JSONObject json = new JSONObject();
		json.put("type", type);
		json.put("reply", reply);
		json.put("errorSign", errorSign);
		return json.toString();
	}
}


public class DictionaryServer implements Runnable {
	private static int port;
	//private static Map<String,Object> dictMap = new HashMap<String,Object>();
	private static JSONObject json;
	private Socket client=null;
	
	public DictionaryServer(Socket client)
	{
		this.client=client;
	}
	
	public void run() 
	{
		try {
		serverClient(client);
		}catch(IOException e)
		{
			e.printStackTrace();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) throws IOException
	{
		port = Integer.parseInt(args[0]);
		
		ServerSocket server = new ServerSocket(port);
		
		buildMap(args[1]);
		//ExecutorService fixedPool = Executors.newFixedThreadPool(10);
		final int THREADPOOLSIZE = 10;
		/*while(true)
		{
			Socket client = server.accept();
			//client.setSoTimeout(10000);
			// Start a new thread for a connection
			DictionaryServer ThreadClient = new DictionaryServer(client);
			Thread t = new Thread(ThreadClient);
			t.start();
		}*/
		for(int i=0;i<THREADPOOLSIZE;i++){   
            Thread thread = new Thread(){  
                public void run(){   
                    while(true){  
                        try {  
                            
                            Socket client = server.accept();  
                            DictionaryServer ThreadClient = new DictionaryServer(client);
                            ThreadClient.run();
                        } catch (IOException e) {  
                            e.printStackTrace();  
                        }  
                    }   
                }  
            };  
            
            thread.start();  
        }
		PrintStream ps=new PrintStream(new FileOutputStream("work.txt"));  
        System.setOut(ps); 
		
	}
	
	private static void buildMap(String file)
	{
		String filename = file;
		try {
		BufferedReader read = new BufferedReader(new FileReader(filename));
		String s =null;
		while((s = read.readLine())!=null)
		{
			try {
				json = new JSONObject(s);
				
				/*JSONObject json = new JSONObject(s);
				if(json != JSONObject.NULL) {
				     dictMap = toMap(json);
				    }*/
				
			}catch(JSONException e)
			{
				e.printStackTrace();
			}
		}	
		}catch(FileNotFoundException e)
		{
			System.out.println("please check the file path and start again.");
			
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static Map<String, Object> toMap(JSONObject object) throws JSONException {
	    Map<String, Object> map = new HashMap<String, Object>();

	    Iterator<String> keysItr = object.keys();
	    while(keysItr.hasNext()) {
	        String key = keysItr.next();
	        Object value = object.get(key);

	        map.put(key, value);
	    }
	    return map;
	}
	
	private static int getReqType(String request)throws JSONException
	{
		JSONObject type=null;
		Object result=null;
		try{
		    type = new JSONObject(request);
			}catch(NullPointerException e)
		{
			//e.printStackTrace();	
		}
		try{
			result = type.get("type");
			}catch(NullPointerException e)
		{
				//e.printStackTrace();	
			}
		int n=0;
		try{
			n=Integer.parseInt(result.toString());
			}catch(NullPointerException e)
		{
				//e.printStackTrace();	
			}
		return n;

	}
	
	private static String getMeaning(String request) throws JSONException
	{
		JSONObject meaning = new JSONObject(request);
		Object result = meaning.get("meaning");
		return result.toString();
	}
	
	private static String getWord(String request)throws JSONException
	{
		JSONObject word = new JSONObject(request);
		Object result = word.get("word");
		return result.toString();

	}
	
	private static synchronized String search(String word) throws JSONException
	{
		String outputDefi = null;
		//Object defi=dictMap.get(searchWord);
		Object defi=null;
		boolean found;
		try {
		defi=json.get(word);
		}catch(JSONException e)
		{
			//e.printStackTrace();
		}
		if(defi != null)
		{
			outputDefi = defi.toString();
			found=true;
		}
		else {
			outputDefi ="Sorry, the word you search can't be found in the dictionary.";
			found = false;
		}
			Reply reply =new Reply(1,outputDefi,found);
			//System.out.println(reply.createJSON());
			return reply.createJSON();	
	}
	
	private static synchronized String add(String word,String meaning) throws JSONException
	{
		boolean addSuccess;
		String result;
		Object defi=null;
		try {
			defi=json.get(word);
			}catch(JSONException e)
			{
				//e.printStackTrace();
			}
		if(defi!=null)
		{
			addSuccess = false;
			result ="Sorry, the word you want to add has already existed.";
			
		}
		else
		{
			json.put(word, meaning);
			result ="Successfully added.";
			addSuccess = true;
		}
		Reply reply =new Reply(2,result,addSuccess);
		System.out.println(reply.createJSON());
		return reply.createJSON();
	}
	
	private static synchronized String remove(String word) throws JSONException
	{
		boolean remove;
		String result;
		Object defi=null;
		try {
			defi=json.get(word);
			}catch(JSONException e)
			{
				//e.printStackTrace();
			}
		if(defi!=null)
		{
			json.remove(word);
			remove = true;
			result ="Successfully removed.";
		}
		else
		{
			result ="The word you want to remove doesn't exist.";
			remove = false;
		}
		Reply reply =new Reply(3,result,remove);
		System.out.println(reply.createJSON());
		return reply.createJSON();
	}
	
	public static void serverClient(Socket client) throws Exception 
	{
		try(Socket serverClient = client)
		{
			DataInputStream input = new DataInputStream(serverClient.getInputStream());
			DataOutputStream output = new DataOutputStream(serverClient.getOutputStream());
			boolean flag =true;
			while(flag) {
				int type;
				String request=null;
				try
				{
				request = input.readUTF();
				}catch(EOFException e)
				{
					
				}
				type = getReqType(request);
				switch(type) {
				case 1:
				{
					String searchWord = getWord(request);
					String os = search(searchWord);
					output.writeUTF(os);
					break;
				}
				case 2:
				{
					String addWord = getWord(request);
					String addMeaning = getMeaning(request);
					String os =add(addWord,addMeaning);
					output.writeUTF(os);
					break;
				}
				case 3:
				{
					String removeWord =getWord(request);
					String os=remove(removeWord);
					output.writeUTF(os);
					break;
				}
				
				
			}
		}
			input.close();
			output.close();
			client.close();
		}
		catch(UnknownHostException e)
		{
			e.printStackTrace();
		}
	}

}
