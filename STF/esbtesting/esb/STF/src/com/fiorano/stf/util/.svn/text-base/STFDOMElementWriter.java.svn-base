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

import org.w3c.dom.*;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: aditya
 * Date: Aug 10, 2007
 * Time: 2:13:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class STFDOMElementWriter {

    private XmlNamespacePolicy namespacePolicy = XmlNamespacePolicy.IGNORE;
    private HashMap nsPrefixMap = new HashMap();
    private static final String NS = "ns";
    private int nextPrefix = 0;
    private HashMap nsURIByElement = new HashMap();
    private static String lSep = System.getProperty("line.separator");
    protected String[] knownEntities = {"gt", "amp", "lt", "apos", "quot"};

    public static class XmlNamespacePolicy {
        private boolean qualifyElements;
        private boolean qualifyAttributes;

        /**
         * Ignores namespaces for elements and attributes, the default.
         */
        public static final XmlNamespacePolicy IGNORE =
                new XmlNamespacePolicy(false, false);

        /**
         * Ignores namespaces for attributes.
         */
        public static final XmlNamespacePolicy ONLY_QUALIFY_ELEMENTS =
                new XmlNamespacePolicy(true, false);

        /**
         * Qualifies namespaces for elements and attributes.
         */
        public static final XmlNamespacePolicy QUALIFY_ALL =
                new XmlNamespacePolicy(true, true);

        /**
         * @param qualifyElements   whether to qualify elements
         * @param qualifyAttributes whether to qualify elements
         */
        public XmlNamespacePolicy(boolean qualifyElements,
                                  boolean qualifyAttributes) {
            this.qualifyElements = qualifyElements;
            this.qualifyAttributes = qualifyAttributes;
        }
    }

    public STFDOMElementWriter() {
    }

    public void write(Element element, Writer out, int indent,
                      String indentWith)
            throws IOException {

        // Write child elements and text
        NodeList children = element.getChildNodes();
        boolean hasChildren = (children.getLength() > 0);
        boolean hasChildElements = false;
        openElement(element, out, indent, indentWith, hasChildren);

        if (hasChildren) {
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);

                switch (child.getNodeType()) {

                    case Node.ELEMENT_NODE:
                        hasChildElements = true;
                        if (i == 0) {
                            out.write(lSep);
                        }
                        write((Element) child, out, indent + 1, indentWith);
                        break;

                    case Node.TEXT_NODE:
                        out.write(encode(child.getNodeValue()));
                        break;

                    case Node.COMMENT_NODE:
                        out.write("<!--");
                        out.write(encode(child.getNodeValue()));
                        out.write("-->");
                        break;

                    case Node.CDATA_SECTION_NODE:
                        out.write("<![CDATA[");
                        out.write(encodedata(((Text) child).getData()));
                        out.write("]]>");
                        break;

                    case Node.ENTITY_REFERENCE_NODE:
                        out.write('&');
                        out.write(child.getNodeName());
                        out.write(';');
                        break;

                    case Node.PROCESSING_INSTRUCTION_NODE:
                        out.write("<?");
                        out.write(child.getNodeName());
                        String data = child.getNodeValue();
                        if (data != null && data.length() > 0) {
                            out.write(' ');
                            out.write(data);
                        }
                        out.write("?>");
                        break;
                    default:
                        // Do nothing
                }
            }
            closeElement(element, out, indent, indentWith, hasChildElements);
        }
    }

    public void openElement(Element element, Writer out, int indent,
                            String indentWith, boolean hasChildren)
            throws IOException {
        // Write indent characters
        for (int i = 0; i < indent; i++) {
            out.write(indentWith);
        }

        // Write element
        out.write("<");
        if (namespacePolicy.qualifyElements) {
            String uri = getNamespaceURI(element);
            String prefix = (String) nsPrefixMap.get(uri);
            if (prefix == null) {
                if (nsPrefixMap.isEmpty()) {
                    // steal default namespace
                    prefix = "";
                } else {
                    prefix = NS + (nextPrefix++);
                }
                nsPrefixMap.put(uri, prefix);
                addNSDefinition(element, uri);
            }
            if (!"".equals(prefix)) {
                out.write(prefix);
                out.write(":");
            }
        }
        out.write(element.getTagName());

        // Write attributes
        NamedNodeMap attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Attr attr = (Attr) attrs.item(i);
            out.write(" ");
            if (namespacePolicy.qualifyAttributes) {
                String uri = getNamespaceURI(attr);
                String prefix = (String) nsPrefixMap.get(uri);
                if (prefix == null) {
                    prefix = NS + (nextPrefix++);
                    nsPrefixMap.put(uri, prefix);
                    addNSDefinition(element, uri);
                }
                out.write(prefix);
                out.write(":");
            }
            out.write(attr.getName());
            out.write("=\"");
            out.write(encode(attr.getValue()));
            out.write("\"");
        }

        // write namespace declarations
        ArrayList al = (ArrayList) nsURIByElement.get(element);
        if (al != null) {
            Iterator iter = al.iterator();
            while (iter.hasNext()) {
                String uri = (String) iter.next();
                String prefix = (String) nsPrefixMap.get(uri);
                out.write(" xmlns");
                if (!"".equals(prefix)) {
                    out.write(":");
                    out.write(prefix);
                }
                out.write("=\"");
                out.write(uri);
                out.write("\"");
            }
        }

        if (hasChildren) {
            out.write(">");
        } else {
            removeNSDefinitions(element);
            out.write(" />");
            out.write(lSep);
            out.flush();
        }
    }

    private static String getNamespaceURI(Node n) {
        String uri = n.getNamespaceURI();
        if (uri == null) {
            // FIXME: Is "No Namespace is Empty Namespace" really OK?
            uri = "";
        }
        return uri;
    }

    private void addNSDefinition(Element element, String uri) {
        ArrayList al = (ArrayList) nsURIByElement.get(element);
        if (al == null) {
            al = new ArrayList();
            nsURIByElement.put(element, al);
        }
        al.add(uri);
    }

    private void removeNSDefinitions(Element element) {
        ArrayList al = (ArrayList) nsURIByElement.get(element);
        if (al != null) {
            Iterator iter = al.iterator();
            while (iter.hasNext()) {
                nsPrefixMap.remove(iter.next());
            }
            nsURIByElement.remove(element);
        }
    }

    public String encode(String value) {
        StringBuffer sb = new StringBuffer();
        int len = value.length();
        for (int i = 0; i < len; i++) {
            char c = value.charAt(i);
            switch (c) {
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '\'':
                    sb.append("&apos;");
                    break;
                case '\"':
                    sb.append("&quot;");
                    break;
                case '&':
                    int nextSemi = value.indexOf(";", i);
                    if (nextSemi < 0
                            || !isReference(value.substring(i, nextSemi + 1))) {
                        sb.append("&amp;");
                    } else {
                        sb.append('&');
                    }
                    break;
                default:
                    if (isLegalCharacter(c)) {
                        sb.append(c);
                    }
                    break;
            }
        }
        return sb.substring(0);
    }

    public String encodedata(final String value) {
        StringBuffer sb = new StringBuffer();
        int len = value.length();
        for (int i = 0; i < len; ++i) {
            char c = value.charAt(i);
            if (isLegalCharacter(c)) {
                sb.append(c);
            }
        }

        String result = sb.substring(0);
        int cdEnd = result.indexOf("]]>");
        while (cdEnd != -1) {
            sb.setLength(cdEnd);
            sb.append("&#x5d;&#x5d;&gt;")
                    .append(result.substring(cdEnd + 3));
            result = sb.substring(0);
            cdEnd = result.indexOf("]]>");
        }

        return result;
    }

    public void closeElement(Element element, Writer out, int indent,
                             String indentWith, boolean hasChildren)
            throws IOException {
        // If we had child elements, we need to indent before we close
        // the element, otherwise we're on the same line and don't need
        // to indent
        if (hasChildren) {
            for (int i = 0; i < indent; i++) {
                out.write(indentWith);
            }
        }

        // Write element close
        out.write("</");
        if (namespacePolicy.qualifyElements) {
            String uri = getNamespaceURI(element);
            String prefix = (String) nsPrefixMap.get(uri);
            if (prefix != null && !"".equals(prefix)) {
                out.write(prefix);
                out.write(":");
            }
            removeNSDefinitions(element);
        }
        out.write(element.getTagName());
        out.write(">");
        out.write(lSep);
        out.flush();
    }

    public boolean isReference(String ent) {
        if (!(ent.charAt(0) == '&') || !ent.endsWith(";")) {
            return false;
        }

        if (ent.charAt(1) == '#') {
            if (ent.charAt(2) == 'x') {
                try {
                    Integer.parseInt(ent.substring(3, ent.length() - 1), 16);
                    return true;
                } catch (NumberFormatException nfe) {
                    return false;
                }
            } else {
                try {
                    Integer.parseInt(ent.substring(2, ent.length() - 1));
                    return true;
                } catch (NumberFormatException nfe) {
                    return false;
                }
            }
        }

        String name = ent.substring(1, ent.length() - 1);
        for (int i = 0; i < knownEntities.length; i++) {
            if (name.equals(knownEntities[i])) {
                return true;
            }
        }
        return false;
    }

    public boolean isLegalCharacter(char c) {
        if (c == 0x9 || c == 0xA || c == 0xD) {
            return true;
        } else if (c < 0x20) {
            return false;
        } else if (c <= 0xD7FF) {
            return true;
        } else if (c < 0xE000) {
            return false;
        } else if (c <= 0xFFFD) {
            return true;
        }
        return false;
    }
}
