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
package com.fiorano.stf.framework;

import com.fiorano.stf.consts.XMLConstants;
import com.fiorano.stf.util.STFDOMElementWriter;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.ApplicationParser;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestListener;
import junit.framework.TestSuite;
import org.apache.tools.ant.util.TeeOutputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: aditya
 * Date: Aug 10, 2007
 * Time: 2:10:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class STFXMLTestListener implements TestListener, XMLConstants {
    private static final PrintStream SYSTEM_ERR_STREAM = System.err;

    private int runs, failures, errors;

    private static final String UNKNOWN = "unknown";

    private Document doc;

    private Element rootElement;

    private Hashtable testElements = new Hashtable();

    private Hashtable failedTests = new Hashtable();

    private Hashtable testStarts = new Hashtable();

    private OutputStream out;

    private ByteArrayOutputStream errStream;
    private PrintStream sysError;

    private String serversStatus = "";

    private static final String[] DEFAULT_TRACE_FILTERS = new String[]{
            "junit.framework.TestCase",
            "junit.framework.TestResult",
            "junit.framework.TestSuite",
            "junit.framework.Assert.", // don't filter AssertionFailure
            "junit.swingui.TestRunner",
            "junit.awtui.TestRunner",
            "junit.textui.TestRunner",
            "java.lang.reflect.Method.invoke(",
            "sun.reflect.",
            "org.apache.tools.ant.",
            // JUnit 4 support:
            "org.junit.",
            "junit.framework.JUnit4TestAdapter",
            // See wrapListener for reason:
            "Caused by: java.lang.AssertionError",
            " more",
    };

    private static DocumentBuilder getDocumentBuilder() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (Exception exc) {
            throw new ExceptionInInitializerError(exc);
        }
    }

    public STFXMLTestListener() {
        errStream = new ByteArrayOutputStream();
        sysError = new PrintStream(new TeeOutputStream(errStream, SYSTEM_ERR_STREAM));
        System.setErr(sysError);
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public void setFailures(int failures) {
        this.failures = failures;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    public boolean setFile(String name, String serversStatus, String extension) {
        try {
            this.serversStatus = serversStatus;
            File file = new File(name + serversStatus + extension);
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            out = new BufferedOutputStream(new FileOutputStream(file));
            return true;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void startTestSuite(TestSuite suite) {
        doc = getDocumentBuilder().newDocument();
        rootElement = doc.createElement(TESTSUITE);
        String name = suite.getName() + serversStatus;
        rootElement.setAttribute(ATTR_NAME, name == null ? UNKNOWN : name);
        rootElement.setAttribute(HOSTNAME, getHostname());
    }

    public void endTestSuite() throws RuntimeException {
        rootElement.setAttribute(ATTR_TESTS, "" + runs);
        rootElement.setAttribute(ATTR_FAILURES, "" + failures);
        rootElement.setAttribute(ATTR_ERRORS, "" + errors);

//        sysError.close();
        sysError = null;
        Element nested = doc.createElement(SYSTEM_ERR);
        rootElement.appendChild(nested);
        nested.appendChild(doc.createCDATASection(errStream.toString()));

        if (out != null) {
            Writer wri = null;
            try {
                wri = new BufferedWriter(new OutputStreamWriter(out, "UTF8"));
                wri.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                (new STFDOMElementWriter()).write(rootElement, wri, 0, "  ");
                wri.flush();
                wri.close();
            }
            catch (IOException e) {

            }
        }

    }

    public void startTest(junit.framework.Test test) {
        if (test instanceof TestCase)
            System.out.println("Starting test  ********* " + ((TestCase) test).getName());
        else
            System.out.println("Starting test  ********* " + test);
        testStarts.put(test, new Long(System.currentTimeMillis()));
    }

    public void endTest(junit.framework.Test test) {
        if (test instanceof TestCase)
            System.out.println("Ended test     ********* " + ((TestCase) test).getName());
        else
            System.out.println("Ended test     ********* " + test);
        if (!testStarts.containsKey(test)) {
            startTest(test);
        }

        Element currentTest = null;

        if (!failedTests.containsKey(test)) {
            currentTest = doc.createElement(TESTCASE);
            String className = getLastPart(test.getClass().getName());
            String testName = ((TestCase) test).getName();
            currentTest.setAttribute(ATTR_NAME, testName == null ? UNKNOWN : testName);
            currentTest.setAttribute(ATTR_CLASSNAME, className);
            rootElement.appendChild(currentTest);
            testElements.put(test, currentTest);
        } else {
            currentTest = (Element) testElements.get(test);
        }

        Long start = (Long) testStarts.get(test);
        currentTest.setAttribute(ATTR_TIME, "" + ((System.currentTimeMillis() - start.longValue()) / 1000.0));
    }

    private String getLastPart(String full) {
        try {
            int lastDotIndex = full.lastIndexOf('.');
            String str = full.substring(lastDotIndex + 1);
            return str;
        }
        catch (Exception e) {
            return full;
        }
    }

    public void addError(junit.framework.Test test, java.lang.Throwable throwable) {
        if (test instanceof TestCase)
            System.err.println("Error in test ********* " + ((TestCase) test).getName());
        else
            System.err.println("Error in test ********* " + test);
        throwable.printStackTrace();
        formatError(ERROR, test, throwable);
    }

    public void addFailure(junit.framework.Test test, junit.framework.AssertionFailedError assertionFailedError) {
        if (test instanceof TestCase)
            System.err.println("Failure in test ********* " + ((TestCase) test).getName());
        else
            System.err.println("Failure in test ********* " + test);
        assertionFailedError.printStackTrace();
        formatError(FAILURE, test, (Throwable) assertionFailedError);
    }


    void formatError(String type, Test test, Throwable t) {
        if (test != null) {
            endTest(test);
            failedTests.put(test, test);
        }

        Element nested = doc.createElement(type);
        Element currentTest = null;
        if (test != null) {
            currentTest = (Element) testElements.get(test);
        } else {
            currentTest = rootElement;
        }

        currentTest.appendChild(nested);
        String message = t.getMessage();
        if (message != null && message.length() > 0) {
            nested.setAttribute(ATTR_MESSAGE, t.getMessage());
        }
        nested.setAttribute(ATTR_TYPE, t.getClass().getName());

        String strace = filterStackTrace(t);
        Text trace = doc.createTextNode(strace);
        nested.appendChild(trace);
    }

    private String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "localhost";
        }
    }

    private static String filterStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.flush();
        pw.close();

        String trace = sw.toString();

        sw = new StringWriter();
        pw = new PrintWriter(sw);
        StringReader sr = new StringReader(trace);
        BufferedReader br = new BufferedReader(sr);

        String line;
        try {
            while ((line = br.readLine()) != null) {
                if (!filterLine(line))
                    pw.println(line);
            }
        }
        catch (Exception e) {
            return trace;
        }

        return sw.toString();
    }

    private static boolean filterLine(String line) {
        for (int i = 0; i < DEFAULT_TRACE_FILTERS.length; i++) {
            if (line.indexOf(DEFAULT_TRACE_FILTERS[i]) != -1) {
                return true;
            }
        }
        return false;
    }

}
