/**
 * 
 */
package networking;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONObject;

/**
 * @author 201575091_Mtileni_MF
 *
 */
public class NetworkClient {
	public NetworkClient() {
		
	}
	
	
	public  JSONObject SpeechToText(byte[] audiofile) throws Exception {
		
		//request URL
	    URL url = new URL("https://westus.stt.speech.microsoft.com/speech/recognition/conversation/cognitiveservices/v1?language=en-US");
	   
	    
	    //get audio file in byte[]
	    byte[] postDataBytes = audiofile;
	    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	    
	    //setup header properties
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Accept", "application/json;text/xml");
	    conn.setRequestProperty("Content-Type", "codec=audio/pcm; samplerate=16000");
	    conn.setRequestProperty("Ocp-Apim-Subscription-Key", "92abeae85af9472793ed0155b0e64bca");
	    conn.setRequestProperty("Host", "westus.stt.speech.microsoft.com");
	    conn.setRequestProperty("ransfer-Encoding", "chunked");
	    conn.setRequestProperty("Expect", "100-continue");
	    conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
	    conn.setDoOutput(true);
	    conn.getOutputStream().write(postDataBytes);
	    Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	    StringBuilder sb = new StringBuilder();
	    for (int c; (c = in.read()) >= 0;)
	        sb.append((char)c);
	    String response = sb.toString();
	    JSONObject myResponse = new JSONObject(response.toString());
	    
	    //return results in JsonObject
	    return myResponse;
	}
}


