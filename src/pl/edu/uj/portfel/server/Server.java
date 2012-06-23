package pl.edu.uj.portfel.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.edu.uj.portfel.db.AccountDao;
import pl.edu.uj.portfel.db.AttributeDao;
import pl.edu.uj.portfel.db.TransactionDao;
import android.util.Base64;
import android.util.Log;

public class Server {
	private WebClient remote;
	private String host = "wallet.thinkdifferent.pl";
	private HttpHost httpHost;
	
	public Server() {
		httpHost = new HttpHost(host);
		remote = new WebClient(httpHost);
	}
	
	
	public String getBody(HttpResponse resp) throws IOException {
		InputStream is = resp.getEntity().getContent();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		
		String line;
		StringBuffer buf = new StringBuffer();
		while((line = br.readLine()) != null) {
			buf.append(line);
		}
		
		return buf.toString();
	}
	
	public CreateUserReturnValues createUser(String email, String phone, String pass) {
		HttpPost req = new HttpPost("/users.json");
		
		String createdAtR = "", emailR = "", phoneR = "", updatedAtR = "", tokenR = "";
		int idR = 0;

		boolean malformed = true;

		try {
			StringEntity entity = new StringEntity(String.format(
					"user[email]=%s&user[phone]=%s&user[password]=%s&user[password_confirmation]=%s",
						email,
						phone,
						pass,
						pass
					), "UTF-8");
			
			entity.setContentType("application/x-www-form-urlencoded");
			req.setEntity(entity);
			
			try {
				HttpResponse resp = remote.execute(req);
				int status = resp.getStatusLine().getStatusCode();
				
				if(status == 201) {
					JSONObject obj = new JSONObject(getBody(resp));
					
					createdAtR = obj.getString("created_at");
					emailR = obj.getString("email");
					phoneR = obj.getString("phone");
					updatedAtR = obj.getString("updated_at");
					tokenR = obj.getString("token");
					idR = obj.getInt("id");
					
					malformed = false;
				} else if(status == 422) {
					String body = getBody(resp);
					malformed = false;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		} catch(UnsupportedEncodingException e) {
		} finally {
			remote.close();
		}
		
		
		return new CreateUserReturnValues(createdAtR, emailR, idR, phoneR, updatedAtR, tokenR, malformed);
	}
	
	public LoginUserReturnValues loginUser(String email, String pass) {
		String token = "";
		HttpPost req = new HttpPost("/login.json");

		boolean malformed = true;

		try {
			StringEntity entity = new StringEntity(String.format("email=%s&password=%s", email, pass), "UTF-8");
			entity.setContentType("application/x-www-form-urlencoded");
			req.setEntity(entity);
			
			try {
				HttpResponse resp = remote.execute(req);
				int status = resp.getStatusLine().getStatusCode();
				
				if(status == 200) {
					String body = getBody(resp);
					
					JSONObject json = new JSONObject(body);
					String result = json.getString("result");
	
					malformed = false;
					
					if(result.equals("1")) {
						token = json.getString("token");
					}
					
					Log.i("r", body);
				} else if(status == 401) {
					String body = getBody(resp);
					
					JSONObject json = new JSONObject(body);
					String result = json.getString("result");
					
					if(result.equals("0")) {
						malformed = false;
					}
				}
			} catch(IOException e) {
				e.printStackTrace();
			} catch(JSONException e) {
				e.printStackTrace();
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				remote.close();
			}
		} catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			remote.close();
		}
		
		LoginUserReturnValues ret = new LoginUserReturnValues(token, malformed);
		return ret;
	}
	
	public AccountList getAccountList(String token) {
		AccountList list = new AccountList();

		try {
			HttpGet req = new HttpGet("/accounts.json?token=" + token);
			HttpResponse resp = remote.execute(req);
			
			int status = resp.getStatusLine().getStatusCode();
			if(status == 200) {
				String body = getBody(resp);
				JSONArray arr = new JSONArray(body);
				
				for(int i = 0, len = arr.length(); i < len; i++) {
					JSONObject obj = arr.getJSONObject(i);
					
					AccountDao dao = AccountDao.newFromJSON(obj);
					list.getAccounts().add(dao);
				}
				
				list.setMalformed(false);
			} else {
				list.setMalformed(false);
			}
			
			remote.close();
		} catch(Exception e) {
			list.getAccounts().clear();
			e.printStackTrace();
		} finally {
			remote.close();
		}
		
		return list;
	}
	
	public AttributeList getAttributeList(String token, long localTransactionId, long remoteTransactionId) {
		AttributeList list = new AttributeList(true);
		
		try {
			HttpGet req = new HttpGet("/transactions/" + Long.toString(remoteTransactionId) + ".json?token=" + token);
			HttpResponse resp = remote.execute(req);
			
			int status = resp.getStatusLine().getStatusCode();
			if(status == 200) {
				String body = getBody(resp);
				
				JSONObject obj = new JSONObject(body);
				byte[] attrBlob = Base64.decode(obj.getString("description"), Base64.URL_SAFE | Base64.NO_WRAP);
				StringBuffer sb = new StringBuffer();
				
				for(byte b: attrBlob) {
					sb.append((char) b);
				}
				
				String attrBlobStr = sb.toString();
				
				JSONArray arr = new JSONArray(attrBlobStr);
				for(int i = 0; i < arr.length(); i++) {
					JSONObject jsobj = arr.getJSONObject(i);
					
					AttributeDao adao = new AttributeDao();
					if(jsobj.has("title"))
						adao.setTitle(jsobj.getString("title"));
					
					adao.setTid(localTransactionId);
					adao.setType(jsobj.getInt("type"));
					adao.setId(0);
					
					if(jsobj.has("aux1"))
						adao.setAux1(jsobj.getString("aux1"));
					
					if(jsobj.has("aux2"))
						adao.setAux2(jsobj.getString("aux2"));
					
					if(jsobj.has("aux3"))
						adao.setAux3(jsobj.getString("aux3"));
					
					list.getAttributeList().add(adao);
				}
			} else {
				list.setMalformed(false);
			}

			remote.close();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			remote.close();
		}
		
		return list;
	}
	
	public TransactionList getTransactionList(String token, long localAccId, long remoteAccId) {
		TransactionList list = new TransactionList(true);
		
		try {
			HttpGet req = new HttpGet("/transactions.json?token=" + token);
			HttpResponse resp = remote.execute(req);
			
			int status = resp.getStatusLine().getStatusCode();
			if(status == 200) {
				String body = getBody(resp);
				JSONArray arr = new JSONArray(body);
				
				for(int i = 0, len = arr.length(); i < len; i++) {
					JSONObject obj = arr.getJSONObject(i);
					
					long remoteAccountId = obj.getLong("account_id"); 
					
					if(remoteAccountId == remoteAccId) {
						TransactionDao dao = TransactionDao.newFromJSON(obj, localAccId);
						list.getTransactionList().add(dao);
					}
				}
				
			} else {
				list.setMalformed(false);
			}
			
			remote.close();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			remote.close();
		}
		
		return list;
	}

	public long createAccount(String token, String name, int type, double initialBalance) {
		String data = String.format("account[currency]=PLN&account[public]=true&account[balance]=%f&account[name]=%s", initialBalance, name);
		
		try {
			StringEntity entity = new StringEntity(data, "UTF-8");
			entity.setContentType("application/x-www-form-urlencoded");
			
			HttpPost req = new HttpPost("/accounts.json?token=" + token);
			req.setEntity(entity);
			
			HttpResponse resp = remote.execute(req);
			int status = resp.getStatusLine().getStatusCode();
			
			if(status == 201) {
				String body = getBody(resp);
				
				JSONObject obj = new JSONObject(body);
				return obj.getLong("id");
			} else if(status == 422) {
				// ...
			}
			
			remote.close();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			remote.close();
		}
		
		return -1;
	}

	public long createTransaction(String serverToken, TransactionDao tdao, long remoteAccountId, String attributeBlob) {
		String data = String.format("transaction[account_id]=%d&transaction[date]=2011-06-23&transaction[description]=%s&transaction[value]=%f&transaction[tag_names]=",
				remoteAccountId, 
				Base64.encodeToString(attributeBlob.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP), 
				((double) tdao.getAmount()) / 100.0f
		);
		
		try {
			StringEntity entity = new StringEntity(data, "UTF-8");
			entity.setContentType("application/x-www-form-urlencoded");
			
			HttpPost req = new HttpPost("/transactions.json?token=" + serverToken);
			req.setEntity(entity);
			
			HttpResponse resp = remote.execute(req);
			int status = resp.getStatusLine().getStatusCode();
			
			if(status == 201) {
				String body = getBody(resp);
				
				JSONObject obj = new JSONObject(body);
				return obj.getLong("id");
			} else if(status == 422) {

			}
			
			remote.close();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			remote.close();
		}
		
		return -1;
	}
}
