package com.fiorano.stf.test.core;

import com.fiorano.stf.util.StaxParser;

import javax.xml.stream.XMLStreamException;
import java.util.Hashtable;

/**
 * @author Sandeep M
 * @Date:
 */
public class TestArtifact {
    private String componentName;
    private String portName;
    private String messageType;
    private String content;
    private String messageFile;
    private String outputFile;
    public static final Object XML_MESSAGE = "xml";
    private String testId;
    private long timeout;

    public TestArtifact(String testId) {
        this.testId = testId;
    }

    public String getComponentName() {
        return componentName;
    }

    public String getPortName() {
        return portName;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getContent() {
        return content;
    }

    public String getMessageFile() {
        return messageFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public String getId() {
        return testId + "__" + componentName + "__" + portName;
    }

    public long getTimeout() {
        return timeout;
    }

    /*-------------------------------------------------[ Stax Parsing ]---------------------------------------------------*/
    public static final String ATTR_COMPONENT_NAME = "componentName";
    public static final String ATTR_PORT_NAME = "port";
    public static final String ATTR_TIMEOUT = "timeout";
    public static final String ATTR_MESSAGE_TYPE = "type";
    public static final String ATTR_MESSAGE_CONTENT = "content";
    public static final String ATTR_MESSAGE_FILE = "messageFile";
    public static final String ATTR_OUTPUT_FILE = "outputFile";

    public void populate(StaxParser cursor) throws XMLStreamException {
        Hashtable attributes = cursor.getAttributes();
        componentName = (String) attributes.get(ATTR_COMPONENT_NAME);
        portName = (String) attributes.get(ATTR_PORT_NAME);
        String time = (String) attributes.get(ATTR_TIMEOUT);
        if (time != null)
            timeout = Long.parseLong(time);
        else timeout = 180000l;
        cursor.nextElement();
        Hashtable messageAttributes = cursor.getAttributes();
        messageType = (String) messageAttributes.get(ATTR_MESSAGE_TYPE);
        content = (String) messageAttributes.get(ATTR_MESSAGE_CONTENT);
        messageFile = (String) messageAttributes.get(ATTR_MESSAGE_FILE);
        outputFile = (String) messageAttributes.get(ATTR_OUTPUT_FILE);
    }
}
