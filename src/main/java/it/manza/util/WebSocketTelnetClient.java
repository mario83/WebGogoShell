
package it.manza.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import javax.websocket.RemoteEndpoint.Basic;

import org.apache.commons.net.telnet.TelnetClient;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringPool;

/**
 * @author mario anzà
 *
 */
public class WebSocketTelnetClient implements Runnable {

	public WebSocketTelnetClient(Basic remote) {

		this.remote = remote;

		String[] gogoShellAddress =
			PropsUtil.get(PropsKeys.MODULE_FRAMEWORK_PROPERTIES + "osgi.console")
				.split(StringPool.COLON);

		tc = new TelnetClient();
		
		try {
			tc.connect(gogoShellAddress[0], Integer.valueOf(gogoShellAddress[1]));
			out = new PrintStream(tc.getOutputStream());
		}
		catch (Exception e) {
			_log.error(e);
		}
	}

	/***
	 * Reader thread. Reads lines from the TelnetClient and echoes them on the
	 * screen.
	 ***/
	@Override
	public void run() {

		InputStream instr = tc.getInputStream();

		try {
			byte[] buff = new byte[8192]; //according to default org.apache.tomcat.websocket.textBufferSize
			int ret_read = 0;

			do {
				ret_read = instr.read(buff);
				if (ret_read > 0) {
					String s = new String(buff, 0, ret_read);
//					remote.sendText(s.replaceAll("\\n", "<br>"));
					remote.sendText(s);
				}
			}
			while (ret_read >= 0);
		}
		catch (IOException e) {
			_log.error(e);
		}

		try {
			tc.disconnect();
		}
		catch (IOException e) {
			_log.error(e);
		}
	}

	public void sendCommand(String command) {

		out.print(command);
		out.flush();
	}

	public void disconnect() {

		try {
			tc.disconnect();
		}
		catch (IOException e) {
			_log.error(e);
		}
	}

	private TelnetClient tc;
	private Basic remote;
	private PrintStream out;

	private static final Log _log =
		LogFactoryUtil.getLog(WebSocketTelnetClient.class);
}
