package pl.edu.uj.portfel;

import pl.edu.uj.portfel.server.Server;
import android.content.Context;

public abstract class WPAsyncTask implements Runnable {
	private Context self;
	private Server server;
	
	public WPAsyncTask(Context _self, Server _server) {
		self = _self;
		server = _server;
	}
	
	public abstract void onFinished();
	
	@Override
	public abstract void run();
}