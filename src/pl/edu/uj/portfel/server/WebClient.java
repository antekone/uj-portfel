package pl.edu.uj.portfel.server;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.protocol.HttpContext;

import android.net.http.AndroidHttpClient;

public class WebClient {
	private AndroidHttpClient http;
	private HttpHost target;
	
	public WebClient(HttpHost _target) {
		target = _target;
	}
	
	public HttpResponse execute(HttpRequest req, HttpContext ctx) throws ClientProtocolException, IOException {
		http = AndroidHttpClient.newInstance("wportfel/1.0");
		HttpResponse resp = http.execute(target, req, ctx);
		return resp;
	}
	
	public HttpResponse execute(HttpRequest req) throws ClientProtocolException, IOException {
		http = AndroidHttpClient.newInstance("wportfel/1.0");
		return http.execute(target, req);
	}
	
	public void close() {
		if(http != null) {
			http.close();
			http = null;
		}
	}
}
