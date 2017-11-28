package com.fiorano.stf.util;

import com.fiorano.stf.framework.STFException;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;


/**
 * Created by IntelliJ IDEA.
 * User: lokesh2
 * Date: Jul 12, 2010
 * Time: 10:53:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class ConfigsParser {

    //------------------ Elements------------------------------------------
    public static final String HA_KRPCPROVIDER = "HA_KRPCPROVIDER";
    public static final String RPL_HA_MANAGER = "RPL_HA_MANAGER";
    public static final String DEFAULT_OBJS_CONFIG = "DEFAULT_OBJS_CONFIG";
    public static final String SMTP_TRANSPORT_CONFIG = "SMTP_TRANSPORT_CONFIG";
    public static final String JMS_TRANSPORT_CONFIG = "JMS_TRANSPORT_CONFIG";
    public static final String EnterpriseServer = "EnterpriseServer";
    public static final String EventEmailNotifier = "EventEmailNotifier";
    public static final String JETTY_SERVICE_CONFIG = "JETTY_SERVICE_CONFIG";
    public static final String COMMON_CONFIG = "COMMON_CONFIG";
    public static final String AuditEventListener = "AuditEventListener";
    public static final String CONNECTION_MANAGER = "CONNECTION_MANAGER";
    public static final String DB = "DB";
    public static final String JMX_ENGINE = "JMX_ENGINE";
    public static final String DB_MANAGER = "DB_MANAGER";

    //---------------------Attributes--------------------------------------
    public static final String BackupHAIPAddress = "BackupHAIPAddress";
    public static final String BackupServerIp = "BackupServerIp";
    public static final String BasicAuthSupported = "BasicAuthSupported";
    public static final String PrimaryURL = "PrimaryURL";
    public static final String BackupURLs = "BackupURLs";
    public static final String LockFile = "LockFile";
    public static final String GatewayServerIPAddress = "GatewayServerIPAddress";
    public static final String ObjectName = "ObjectName";
    public static final String Password = "Password";
    public static final String DefSecurityCredentials = "DefSecurityCredentials";
    public static final String KeyPassword = "KeyPassword";
    public static final String KeystoreLocation = "KeystoreLocation";
    public static final String KeystorePassword = "KeystorePassword";
    public static final String TruststorePassword = "TruststorePassword";
    public static final String TrustStoreLocation = "TrustStoreLocation";
    public static final String RealmProperties = "RealmProperties";
    public static final String ConnectionRetryCount = "ConnectionRetryCount";
    public static final String ConnectionRetryInterval = "ConnectionRetryInterval";
    public static final String InitialContextFactory = "InitialContextFactory";
    public static final String ProviderClass = "ProviderClass";
    public static final String QueueConnectionFactory = "QueueConnectionFactory";
    public static final String ServerName = "ServerName";
    public static final String UseServerlessMode = "UseServerlessMode";
    public static final String UserName = "UserName";
    public static final String ConnectionFactory = "ConnectionFactory";
    public static final String SSLEnabled = "SSLEnabled";
    public static final String AuditListenerClassName = "AuditListenerClassName";
    public static final String DependencyHandlerName = "DependencyHandlerName";
    public static final String HandlerType = "HandlerType";
    public static final String Default = "Default";
    public static final String FMQServer = "FMQServer";
    public static final String UseForPeerToPeerCommunication = "UseForPeerToPeerCommunication";
    public static final String Port = "Port";
    public static final String Path = "Path";
    public static final String Protocol = "Protocol";


    public static Document createDOM(String fileName) throws STFException {
        DocumentBuilder builder = null;
        Document doc = null;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            File file = new File(fileName);
            doc = builder.parse(file);
        }
        catch (Exception e) {
            throw new STFException(e.getMessage(), e);
        }
        return doc;
    }

    public static void setAttribute(Document doc, String elementName, String attributeName, String attributeValue) {
        NodeList nodes = doc.getElementsByTagName(elementName);
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            element.setAttribute(attributeName, attributeValue);
        }
    }

    public static void createAttribute(Document doc, String elementName, String attributeName, String attributeValue) {
        NodeList nodes = doc.getElementsByTagName(elementName);
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            if (!element.hasAttribute(attributeName))
                element.setAttribute(attributeName, attributeValue);
        }
    }

    public static void changeAttributeVal(Document doc, String elementName, String attributeName, String attributeValue) {
        NodeList nodes = doc.getElementsByTagName(elementName);
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            if (element.hasAttribute(attributeName))
                element.setAttribute(attributeName, attributeValue);
        }
    }

    public static boolean isElementAttributeExists(Document doc, String elementName, String attributeName[], String attributeValue[]) {
        NodeList nodes = doc.getElementsByTagName(elementName);
        for (int i = 0, count = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            for (int j = 0; j < attributeName.length; j++) {
                if (element.getAttribute(attributeName[j]) == attributeValue[j])
                    count++;
            }
            if (count == attributeName.length)
                return true;
        }
        return false;
    }

    public static void removeElementNode(Document doc, String elementName) {
        NodeList nodes = doc.getElementsByTagName(elementName);
        for (int i = 0; i < nodes.getLength(); i++) {
            doc.removeChild(nodes.item(i));
        }
    }

    public static void createElementNode(Document doc, String elementName) {
        doc.createElement(elementName);
    }

    public static void createElementNode(Document doc, String parentElementName, String elementName, String[] attrNames, String[] attrVals) throws STFException {
        if (attrNames.length != attrVals.length)
            throw new STFException("Error! Different no of Attributes names and values.");
        Element element = doc.createElement(elementName);
        for (int i = 0; i < attrNames.length; i++) {
            element.setAttribute(attrNames[i], attrVals[i]);
        }
        NodeList nodes = doc.getElementsByTagName(parentElementName);
        for (int i = 0; i < nodes.getLength(); i++) {
            Element parent = (Element) nodes.item(i);
            parent.appendChild(element);
        }
    }

    public static void writeXmlFile(Document doc, String filename) throws STFException {
        try {
            /*
            // Prepare the DOM document for writing
            Source source = new DOMSource(doc);

            // Prepare the output file
            File file = new File(filename);
            Result result = new StreamResult(file);

            // Write the DOM document to the file
            Transformer xformer = null;
            try {
                xformer = TransformerFactory.newInstance().newTransformer();
                xformer.transform(source, result);
            } catch (Exception e)
            {e.printStackTrace() ;}
            finally {
                //using reset to close file resource else the file gets locked
                //<!--adding system property  "transfor.TransformerFactory" in esb/stf/build.xml, so that ConfigsParser Class can call the reset method in the factory specified.-->
                if (xformer != null)
                    xformer.reset();
            } */

            //issue http://stackoverflow.com/questions/1255427/java-method-works-in-1-5-but-not-1-6
            //so using xml serializer
            //Serialize DOM
            OutputFormat format    = new OutputFormat(doc);
            // as a String
            StringWriter stringOut = new StringWriter ();
            XMLSerializer serial   = new XMLSerializer (stringOut, format);
            serial.serialize(doc);
            String xmlString = stringOut.toString();

            OutputStream f0;
            byte buf[] = xmlString.getBytes();
            f0 = new FileOutputStream(filename);
            for (int i = 0; i < buf.length; i++) {
                f0.write(buf[i]);
            }
            f0.close();

        } catch (Exception e) {
            throw new STFException(e.getMessage());
        }
    }

}

