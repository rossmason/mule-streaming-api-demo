
package com.mulesoft.demo.mule.websocket;

import java.util.Map;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.callback.SourceCallback;
import org.mule.api.processor.MessageProcessor;
import org.mule.module.esper.EsperModule;
import org.mule.module.xml.transformer.DomDocumentToXml;
import org.mule.transport.nio.http.HttpConnector;
import org.mule.util.NumberUtils;

public class EsperWebSocketUpdateListener implements MessageProcessor
{
    private HttpConnector httpConnector;

    private EsperModule esperModule;

    private DomDocumentToXml domToXmlTransformer;

    public MuleEvent process(final MuleEvent muleEvent) throws MuleException
    {
        final int channelId = NumberUtils.toInt(muleEvent.getMessage().getInboundProperty("nio.channel.id"));
        final String statement = muleEvent.getMessageAsString();

        esperModule.listen(statement, new SourceCallback()
        {
            public Object process(final Object payload) throws Exception
            {
                final Object xmlEvent = domToXmlTransformer.transform(payload);
                final MuleMessage muleMessage = new DefaultMuleMessage(xmlEvent,
                    httpConnector.getMuleContext());
                httpConnector.writeToWebSocket(muleMessage, "events", channelId);
                return null;
            }

            public Object process() throws Exception
            {
                throw new UnsupportedOperationException();
            }

            public Object process(final Object payload, final Map<String, Object> properties)
                throws Exception
            {
                throw new UnsupportedOperationException();
            }
        });

        return muleEvent;
    }

    public void setHttpConnector(final HttpConnector httpConnector)
    {
        this.httpConnector = httpConnector;
    }

    public void setEsperModule(final EsperModule esperModule)
    {
        this.esperModule = esperModule;
    }

    public void setDomToXmlTransformer(final DomDocumentToXml domToXmlTransformer)
    {
        this.domToXmlTransformer = domToXmlTransformer;
    }
}
