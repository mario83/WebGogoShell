
package it.manza.bean;

import javax.websocket.RemoteEndpoint.Basic;

import it.manza.util.WebSocketTelnetClient;

/**
 * @author mario anzà
 *
 */
public class GogoBean {

	public GogoBean(Basic remote, WebSocketTelnetClient webSocketTelnetClient) {

		this.remote = remote;
		this.webSocketTelnetClient = webSocketTelnetClient;
	}

	public Basic getRemote() {

		return remote;
	}

	public void setRemote(Basic remote) {

		this.remote = remote;
	}

	public WebSocketTelnetClient getWebSocketTelnetClient() {

		return webSocketTelnetClient;
	}

	public void setWebSocketTelnetClient(
		WebSocketTelnetClient webSocketTelnetClient) {

		this.webSocketTelnetClient = webSocketTelnetClient;
	}

	private Basic remote;
	private WebSocketTelnetClient webSocketTelnetClient;
}
