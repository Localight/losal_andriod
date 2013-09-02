package co.localism.losal.async;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.JSONTokener;

import co.localism.losal.activities.MainActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class InstagramRequests extends AsyncTask<String, String, String> {

	private final String tag = "InstagramRequests";
	private String access_token = "";
	
	@Override
	protected String doInBackground(String... params) {

		Log.d(tag, "InstagramRequests called");

		if (params[0].equalsIgnoreCase("like")) {
			String request = params[0];
			String img_id = params[1];
			String user_id= params[3];
			access_token = params[2];

			return (executeRequest(request, img_id, user_id));

		} else if (params[0].equalsIgnoreCase("unlike")) {
			String request = params[0];

		}
		return null;
	}

	private String executeRequest(String request, String img_id, String user_id) {
		try {
			Log.d(tag, "request: "+request);
//			img_id = "529079352584594709_422129888";
//			String likeURL = "access_token="+access_token+"https://api.instagram.com/v1/media/"+img_id+"/likes";			
//			if()
			String likeURL = "https://api.instagram.com/v1/media/"+img_id+"/likes?"+"access_token="+access_token;			

			
			String deleteURL = "https://api.instagram.com/v1/media/"+img_id+"/likes"+"?access_token="+access_token;

			InputStream is = null;

			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(likeURL);
			HttpPost httpPost = new HttpPost(likeURL);

			
			HttpResponse response = client.execute(httpPost);
			HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.d(tag, "success");
            String resp = streamToString(is);
            Log.d(tag, resp);
            JSONObject jsonObject = (JSONObject) new JSONTokener(resp)
			.nextValue();
            try{
            	jsonObject.getJSONObject("meta").get("error_type");
            	return "fail";
            }catch(Exception e){
            	
            }
            new PushData().execute("like",img_id, user_id);//log the like in our database
			return "success";
		} catch (Exception e) {
			Log.d(tag, e.toString());

			return "fail";
		}
	}
	
	
	public static String streamToString(InputStream p_is) {
		try {
			BufferedReader m_br;
			StringBuffer m_outString = new StringBuffer();
			m_br = new BufferedReader(new InputStreamReader(p_is));
			String m_read = m_br.readLine();
			while (m_read != null) {
				m_outString.append(m_read);
				m_read = m_br.readLine();
			}
			return m_outString.toString();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Instagram", "streamToString error: " + e.toString());
			return "";
		}
	}


}
