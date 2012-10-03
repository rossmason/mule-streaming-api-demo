
package com.mulesoft.demo.mule.websocket;

import com.mulesoft.demo.mule.AbstractDemo;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.websocket.DefaultWebSocketListener;
import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketUpgradeHandler;

public class WebsocketDemo extends AbstractDemo
{
    public static void main(final String[] args) throws Exception
    {
        new WebsocketDemo().run();
    }

    @Override
    protected String getConfigurationFile()
    {
        return "mule-websocket-config.xml";
    }

    @Override
    protected void runDemo() throws Exception
    {
        final AsyncHttpClient httpClient = new AsyncHttpClient();

        httpClient.prepareGet("ws://localhost:8080/websocket/events")
            .execute(
                new WebSocketUpgradeHandler.Builder().addWebSocketListener(new DefaultWebSocketListener()
                {
                    @Override
                    public void onOpen(final WebSocket websocket)
                    {
                        websocket.sendTextMessage("select * from SignupEvent");
                    }

                    @Override
                    public void onMessage(final String message)
                    {
                        System.out.println("  Received on websocket: " + message);
                    }
                }).build())
            .get();
    }
}
