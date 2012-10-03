
package com.mulesoft.demo.mule.ajax;

import java.util.HashMap;
import java.util.Map;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.ClientTransport;
import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.client.HttpClient;

import com.mulesoft.demo.mule.AbstractDemo;

public class AjaxDemo extends AbstractDemo
{
    private static final String AJAX_CHANNEL = "/football/scores";

    public static void main(final String[] args) throws Exception
    {
        new AjaxDemo().run();
    }

    @Override
    protected String getConfigurationFile()
    {
        return "mule-ajax-config.xml";
    }

    @Override
    protected void runDemo() throws Exception
    {
        final HttpClient httpClient = new HttpClient();
        httpClient.start();

        final Map<String, Object> options = new HashMap<String, Object>();
        final ClientTransport transport = LongPollingTransport.create(options, httpClient);

        final BayeuxClient client = new BayeuxClient("http://localhost:8090/ajax/cometd", transport);

        client.handshake();
        final boolean handshaken = client.waitFor(1000, BayeuxClient.State.CONNECTED);

        if (!handshaken)
        {
            System.err.println("Handshake failed");
            System.exit(-1);
        }

        client.getChannel("/financial/news").subscribe(new ClientSessionChannel.MessageListener()
        {
            public void onMessage(final ClientSessionChannel channel, final Message message)
            {
                System.out.println("  Received on " + AJAX_CHANNEL + ": " + message);
            }
        });
    }
}
