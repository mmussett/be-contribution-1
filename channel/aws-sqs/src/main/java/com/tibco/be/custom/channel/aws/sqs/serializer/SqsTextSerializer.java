/*
* Copyright © 2020. TIBCO Software Inc.
* This file is subject to the license terms contained
* in the license file that is distributed with this file.
*/

package com.tibco.be.custom.channel.aws.sqs.serializer;

import com.amazonaws.services.sqs.model.Message;
import com.tibco.be.custom.channel.BaseEventSerializer;
import com.tibco.be.custom.channel.Event;
import com.tibco.be.custom.channel.EventWithId;
import com.tibco.be.custom.channel.ExtendedDefaultEventImpl;
import com.tibco.be.custom.channel.framework.CustomEvent;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.Logger;

import java.util.Map;
import java.util.Properties;


public class SqsTextSerializer extends BaseEventSerializer {

    Logger logger;

    public SqsTextSerializer() {}

    /**
     * Initialize the SQS Message serializer
     *
     * @param s -
     * @param properties - The channel properties
     * @param logger - Logger handle
     */
    public void initUserEventSerializer(String s, Properties properties, Logger logger) {
        this.logger = logger;
    }

    /**
     * Deserialize the SQS Message to a BusinessEvent Event
     *
     * @param message - The SQS message
     * @param properties - The channel properties
     * @return BusinessEvents event
     */
    public Event deserializeUserEvent(Object message, Map<String, Object> properties) {

        logger.log(Level.DEBUG,"De-serializing SQS Message");

        Event event = new CustomEvent();
        if(message instanceof Message) {

            Message m = (Message) message;
            String payload = m.getBody();
            logger.log(Level.DEBUG,payload);
            String extId = m.getMessageId();

            event.setPayload((payload != null) ? payload.getBytes() : new byte[0]);
            event.setExtId(extId);

        } else {
            return null;
        }
        return event;

    }

    /**
     * Serialize the BusinessEvent Event to a SQS Message
     *
     * @param event - The event to serialise
     * @param properties - The channel properties
     * @return SQS message
     */
    public Object serializeUserEvent(EventWithId event, Map<String, Object> properties)  {

        logger.log(Level.DEBUG,"Serializing SQS Message");

        Message message = new Message();
        message.setMessageId(event.getExtId());
        message.setBody(((ExtendedDefaultEventImpl) event).getUnderlyingSimpleEvent().getPayloadAsString());
        return message;
    }

}
