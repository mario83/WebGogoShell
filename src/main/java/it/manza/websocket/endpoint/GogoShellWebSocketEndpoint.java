
package it.manza.websocket.endpoint;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import it.manza.bean.GogoBean;
import it.manza.util.WebSocketTelnetClient;

/**
 * @author mario anzà
 */
@Component(
	immediate = true, 
	property = {
		"org.osgi.http.websocket.endpoint.path=/o/websocket/gogoshell"
	}, 
	service = Endpoint.class
)
public class GogoShellWebSocketEndpoint extends Endpoint {

	@Override
	public void onOpen(final Session session, EndpointConfig endpointConfig) {

		_log.debug("onOpen " + session.getId());

		WebSocketTelnetClient webSocketTelnetClient =
			new WebSocketTelnetClient(session.getBasicRemote());

		Thread reader = new Thread(webSocketTelnetClient);
		reader.start();

		GogoBean gogoBean =
			new GogoBean(session.getBasicRemote(), webSocketTelnetClient);

		remoteEndpoints.put(session.getId(), gogoBean);

		session.addMessageHandler(new MessageHandler.Whole<String>() {

			@Override
			public void onMessage(String text) {

				_log.debug("onMessage " + text);
				GogoBean gogoBean2 = remoteEndpoints.get(session.getId());
				gogoBean2.getWebSocketTelnetClient().sendCommand(text);

			}
		});
	}

	@Override
	public void onClose(Session session, CloseReason closeReason) {

		_log.debug(
			"onClose " + session.getId() + " closeReason " +
				closeReason.getReasonPhrase());
		remoteEndpoints.get(
			session.getId()).getWebSocketTelnetClient().disconnect();
		remoteEndpoints.remove(session.getId());
	}

	private static ConcurrentMap<String, GogoBean> remoteEndpoints =
		new ConcurrentHashMap<String, GogoBean>();

	private static final Log _log =
		LogFactoryUtil.getLog(GogoShellWebSocketEndpoint.class);
}
