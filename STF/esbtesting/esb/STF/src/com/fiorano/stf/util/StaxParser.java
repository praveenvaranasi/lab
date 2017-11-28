/**
 * Copyright (c) 1999-2017, Fiorano Software Inc. and affiliates.
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Fiorano Software ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * enclosed with this product or entered into with Fiorano.
 */
package com.fiorano.stf.util;

import javax.xml.stream.*;
import javax.xml.stream.util.StreamReaderDelegate;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Hashtable;
import java.util.Stack;


/**
 * Created by IntelliJ IDEA.
 * User: Ibrahim Shaik
 * Date: May 8, 2007
 * Time: 10:49:24 AM
 * <p/>
 * This class will provide the utility methods to parse the given XML using STAX implementation.
 * NOTE: This class is copy of FioranoStaxParser. This is written again to remove depnedency on ESB code as much as possible.
 */
public class StaxParser extends StreamReaderDelegate {
    // This is the internal parser instance, which will be move forward while parsing.
    private XMLStreamReader cursor = null;

    // Current marked element.
    private String curMarkedElement;

    // Holds all the markers.
    private Stack markers = null;

    //True, if parser reached end of the marker.
    private boolean halt = false;

    //True, if document is parsed completely
    private boolean documentParsed = false;

    private boolean consumeMarkedEvent = false;

    private final String FIORANO_EXCEPTION_DESC = "[Fiorano Marker Exception] ";
    private InputStream inputStream = null;
    private Reader reader = null;

    //STAX XML Input factory instance.
    private static XMLInputFactory staxInputFactory = null;

    //STAX XML Output factory instance.
    private static XMLOutputFactory staxOutputFactory = null;

    /**
     * This will returns the instance of the STAX XMLInputFactory and will initialze the properties to the factory.
     *
     * @return This will return the instance of STAX XmlInputFactory.
     */
    public static XMLInputFactory getStaxInputFactory() {

        if (staxInputFactory == null) {
            try {
                // These should be taken from either properties file or from command line arguments.
                System.setProperty("javax.xml.stream.XMLInputFactory", "com.ctc.wstx.stax.WstxInputFactory");
                staxInputFactory = XMLInputFactory.newInstance();
                staxInputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.TRUE);
                staxInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
                staxInputFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return staxInputFactory;
    }


    /**
     * This will returns the instance of the STAX XMLOutputFactory and will initialze the properties to the factory.
     *
     * @return This will return the instance of STAX XMLOutputFactory.
     */
    public static XMLOutputFactory getStaxOutputFactory() {

        if (staxOutputFactory == null) {
            try {
                // These should be taken from either properties file or from command line arguments.
                System.setProperty("javax.xml.stream.XMLOutputFactory", "com.ctc.wstx.stax.WstxOutputFactory");
                staxOutputFactory = XMLOutputFactory.newInstance();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return staxOutputFactory;
    }


    /**
     * This will create the STAX parser for the given Input Stream.
     *
     * @param is Input stream that will be parsed
     * @throws XMLStreamException
     */
    public StaxParser(InputStream is) throws XMLStreamException {
        this(StaxParser.getStaxInputFactory().createXMLStreamReader(is));
        this.inputStream = is;

    }

    /**
     * This will create the STAX parser for the given Input Stream.
     *
     * @param reader reader that will be parsed
     * @throws XMLStreamException
     */
    public StaxParser(Reader reader) throws XMLStreamException {
        this(StaxParser.getStaxInputFactory().createXMLStreamReader(reader));
        this.reader = reader;
    }

    /**
     * Initialize the parser with the specified cursor.
     *
     * @param cursor Reader on which parser wil parse data.
     */
    public StaxParser(XMLStreamReader cursor) {
        super(cursor);
        this.cursor = cursor;
        markers = new Stack();
    }

    /**
     * This will move the cursor to the specified element in the document.
     *
     * @param elementName Element to be marked.
     * @return true, if element can be marked or located else return false.
     * @throws XMLStreamException Throws excdeption if the given element desn't exist.
     */
    public boolean markCursor(String elementName) throws XMLStreamException {
        while (hasNext()) {
            if (cursor.getEventType() == XMLStreamConstants.START_DOCUMENT)
                next();

            boolean val = cursor.isStartElement();
            if (val && cursor.getLocalName().equalsIgnoreCase(elementName)) {
                //This will be removed from the stack when it reaches ist End Tag.
                curMarkedElement = elementName;
                //consumeMarkedEvent = true;
                markers.push(curMarkedElement);
                return true;
            } else
                next();
        }
        throw new XMLStreamException(FIORANO_EXCEPTION_DESC + "Element " + elementName + " doesn't exist in the XML tree.");
    }


    /**
     * This will reset the cursor and allow cursor to move furthur.
     */
    public void resetCursor() {
        //No markers.
        if (markers.empty())
            return;
        //Marker is valid
        if (markers.peek().equals(curMarkedElement)) {
            markers.pop();
            if (!markers.empty())
                curMarkedElement = (String) markers.peek();
            else
                curMarkedElement = null;
        }
        //Already reached the mark. So let parser to continue furthur.
        halt = false;
    }

    /**
     * This will cheeck if the cursor reached end of docuement.
     *
     * @return true, if document is parsed. Else return false.
     */
    public boolean allElementsParsed() {
        return documentParsed;
    }

    /**
     * Will check if there are any more events that can be parsed.
     *
     * @return true, if cursor can be moved forward.
     *         false, if the cursor is already at end of marked tag or document.
     * @throws XMLStreamException
     */
    public boolean hasNext() throws XMLStreamException {
        // Already reached END_OF_MARKER, so it will return false.
        if (halt)
            return false;

        // Will return false, only when the cursor is already at END_OF_DOCUMENT.
        return super.hasNext();
    }

    /**
     * This will move the cursor to next event.
     *
     * @return Event type.
     * @throws XMLStreamException
     */
    public int next() throws XMLStreamException {
        // Cannot iterate till the cursor is reset.
        if (halt)
            throw new XMLStreamException(FIORANO_EXCEPTION_DESC + "Cursor reached end of the marked element.");

        // ONETIME CHECK: This will consume the event, to compensate the event used by calling markParser
        if (consumeMarkedEvent) {
            consumeMarkedEvent = false;
            return cursor.getEventType();
        }

        // This will throw XMLStreamException if the cursor already at the END_OF_DOCUMENT.
        int eventType = super.next();

        if (eventType == XMLStreamConstants.END_ELEMENT) {
            // Check if it reaches end of marked Element
            if (super.getLocalName().equalsIgnoreCase(curMarkedElement))
                halt = true;
        }

        if (eventType == XMLStreamConstants.END_DOCUMENT) {
            halt = true;
            documentParsed = true;
        }

        return eventType;
    }

    /**
     * This will move cursor to next tag (To start or end element)
     *
     * @return Returns event type (Start/End element)
     * @throws XMLStreamException Throws exception, if cursor reaches either marked element or end of document.
     */
    public int nextTag() throws XMLStreamException {
        // Will retrun false if cursor is at end of the marked element or document.
        if (hasNext()) {
            int eventType = next();
            while (!halt && ((eventType == XMLStreamConstants.CHARACTERS && isWhiteSpace()) // skip whitespace
                    || (eventType == XMLStreamConstants.CDATA && isWhiteSpace())
                    // skip whitespace
                    || eventType == XMLStreamConstants.SPACE
                    || eventType == XMLStreamConstants.PROCESSING_INSTRUCTION
                    || eventType == XMLStreamConstants.COMMENT)
                    ) {
                eventType = next();
            }
            return eventType;

        } else
            throw new XMLStreamException(FIORANO_EXCEPTION_DESC + "Cursor reached either end of marked element or document.");
    }

    /**
     * This will move the cursor to the start of next element.
     *
     * @return true, if the cursor moved to start or next element. false, if the cursor is already halted (reached end of marked element) or reached end of document.
     * @throws XMLStreamException Will throw exception if this is invoked when cursor already at end of document or marked element.
     */
    public boolean nextElement() throws XMLStreamException {
        if (!hasNext()) {
            return false;
        }

        int eventType = nextTag();
        while ((eventType != XMLStreamConstants.START_ELEMENT) && !halt) {
            if (hasNext())
                eventType = nextTag();
        }
        return !halt;
    }

    /**
     * This will move cursor to the specified element in the XML tree.
     *
     * @param elementName Next cursor location.
     * @return true, if cursor moved to specified element. false, if cursor cannot locate given element or reaced end of marked or end of document.
     */
    public boolean getNextElement(String elementName) {
        try {
            markCursor(elementName);
            return nextElement();
        }
        catch (XMLStreamException xse) {
            xse.printStackTrace();
            return false;
        }
    }


    /**
     * This will return text of the element.
     *
     * @return Text of element, else null.
     */
    public String getText() {


        try {
            if (hasNext()) {
                //Move cursor to next ... Consume excpetion if any.
                StringBuffer sb = new StringBuffer();
                int event = next();
                while (cursor.getEventType() == XMLStreamConstants.CHARACTERS || cursor.getEventType() == XMLStreamConstants.SPACE) {

                    sb.append(super.getText());
                    event = next();

                }
                return sb.toString();

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This will return the CData assciated with the current element.
     *
     * @return Return CData of the current element, or null.
     */
    private String getCDataValue() {
        try {
            if (hasNext()) {
                //Move cursor to next ... Consume excpetion if any.
                int event = next();
                if (event == XMLStreamConstants.CDATA) {
                    StringBuffer sb = new StringBuffer();
                    do {
                        sb.append(super.getText());
                        next();
                    } while (cursor.getEventType() == XMLStreamConstants.CDATA);
                    return sb.toString();
                } else if (event == XMLStreamConstants.CHARACTERS) {
                    return getCDataValue();
                } else
                    return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCData() throws XMLStreamException {
        StringBuffer cdata = new StringBuffer();

        int eventType;
        do {
            cdata.append(getCDataValue());
            eventType = peak();
        } while (eventType == XMLStreamConstants.CDATA);
        String returnVal = cdata.toString();
        return returnVal.trim().equalsIgnoreCase("null") ? null : returnVal;
    }

    private int peak() throws XMLStreamException {
        if (!hasNext())
            return -1;
        int eventType = next();
        consumeMarkedEvent = true;
        return eventType;

    }

    /**
     * This will return the attributes of the current element.
     * NOTE: This method MUST be called before any other data related methods on this element.
     *
     * @return Hashtabale containing attributes or null.
     * @throws XMLStreamException Exception
     */
    public Hashtable getAttributes() throws XMLStreamException {
        if ((cursor.getEventType() == XMLStreamConstants.START_ELEMENT) || (cursor.getEventType() == XMLStreamConstants.ATTRIBUTE)) {
            if (getAttributeCount() > 0) {
                int attributeCount = cursor.getAttributeCount();
                Hashtable attrs = new Hashtable(attributeCount);
                for (int i = 0; i < attributeCount; i++) {
                    attrs.put(cursor.getAttributeLocalName(i), cursor.getAttributeValue(i));
                }
                return attrs;
            } else
                return null;

        } else
            throw new XMLStreamException("[Fiorano STAX Exception] Current state is not among the states START_ELEMENT , ATTRIBUTEvalid for getAttributes.");
    }


    /**
     * This will close all the open connections.
     *
     * @throws XMLStreamException Exception if any
     */
    public void disposeParser() throws XMLStreamException {
        markers = null;
        curMarkedElement = null;
        cursor.close();
        cursor = null;
        try {
            if (inputStream != null)
                inputStream.close();
            if (reader != null)
                reader.close();
        } catch (IOException e) {
            //nothing to do here.
        }
    }

    /**
     * This will print the event name for the given event type.
     *
     * @param eventType Event Type.
     */
    public void printEventType(int eventType) {
        printEventType(null, eventType);
    }


    /**
     * This will print the event name alongwith the description provided for the specified event.
     *
     * @param data      Any description to be prnted alongwith the event name
     * @param eventType Event Type
     */
    public void printEventType(String data, int eventType) {
        if (data != null) {
            System.out.print(data);
        }
        switch (eventType) {
            case XMLStreamConstants.START_ELEMENT:
                System.out.println("START_ELEMENT: " + XMLStreamConstants.START_ELEMENT);
                return;
            case XMLStreamConstants.END_ELEMENT:
                System.out.println("END_ELEMENT: " + XMLStreamConstants.END_ELEMENT);
                return;
            case XMLStreamConstants.PROCESSING_INSTRUCTION:
                System.out.println("PROCESSING_INSTRUCTION: " + XMLStreamConstants.PROCESSING_INSTRUCTION);
                return;
            case XMLStreamConstants.CHARACTERS:
                System.out.println("CHARACTERS: " + XMLStreamConstants.CHARACTERS);
                return;
            case XMLStreamConstants.COMMENT:
                System.out.println("COMMENT: " + XMLStreamConstants.COMMENT);
                return;
            case XMLStreamConstants.START_DOCUMENT:
                System.out.println("START_DOCUMENT: " + XMLStreamConstants.START_DOCUMENT);
                return;
            case XMLStreamConstants.END_DOCUMENT:
                System.out.println("END_DOCUMENT: " + XMLStreamConstants.END_DOCUMENT);
                return;
            case XMLStreamConstants.ENTITY_REFERENCE:
                System.out.println("ENTITY_REFERENCE: " + XMLStreamConstants.ENTITY_REFERENCE);
                return;
            case XMLStreamConstants.ATTRIBUTE:
                System.out.println("ATTRIBUTE: " + XMLStreamConstants.ATTRIBUTE);
                return;
            case XMLStreamConstants.DTD:
                System.out.println("DTD: " + XMLStreamConstants.DTD);
                return;
            case XMLStreamConstants.CDATA:
                System.out.println("CDATA: " + XMLStreamConstants.CDATA);
                return;
            case XMLStreamConstants.SPACE:
                System.out.println("SPACE: " + XMLStreamConstants.SPACE);
                return;
        }
        System.out.println("UNKNOWN_EVENT_TYPE , " + eventType);
    }

    /**
     * This will parse the complete document.
     *
     * @param fis FileInputStream of the XML.
     * @throws Exception
     */
    public static void parseDocument(FileInputStream fis, String elementName) throws Exception {
        StaxParser parser = new StaxParser(fis);
        //Parse Document
        if (elementName != null) {
            if (parser.markCursor(elementName)) {
                while (parser.nextElement()) {
                    parser.printEventType(parser.getEventType());

                }
            }
        }
        parser.disposeParser();
        parser = null;
    }

    /**
     * This class represent the Attribute (Name-Value pair)
     */
    class ElementAttribute {
        String name, value;

        public ElementAttribute(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }


    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: java FioranoStaxParser <XML file>");
            return;
        }
        //FioranoStaxParser.parseDocument( new FileInputStream (args[0]), args[1]);
        StaxParser parser = new StaxParser(new FileInputStream(args[0]));
        if (parser.markCursor("b")) {
            System.out.println("Element Name: B: " + parser.getLocalName());
            while (parser.nextElement()) {
                System.out.println("Element Name: C " + parser.getLocalName());
                if (parser.getLocalName().equalsIgnoreCase("c")) {
                    parser.markCursor("c");
                    while (parser.nextElement()) {
                        System.out.println("Element Name: D " + parser.getLocalName());
                        if (parser.getLocalName().equalsIgnoreCase("d")) {
                            parser.markCursor("d");
                            while (parser.nextElement()) {
                                System.out.println("Element Name: E " + parser.getLocalName());
                            }
                            parser.resetCursor();
                        }
                    }
                    parser.resetCursor();
                }
            }
            parser.resetCursor();
        }
    }

    public String getLocalName() {
        String localName = super.getLocalName();
        return localName == null ? "" : localName;
    }


}
