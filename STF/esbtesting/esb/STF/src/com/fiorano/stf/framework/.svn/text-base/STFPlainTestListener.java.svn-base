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

import junit.framework.*;

import java.io.*;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: aditya
 * Date: Aug 9, 2007
 * Time: 4:34:02 PM
 * To change this template use File | Settings | File Templates.
 */

public class STFPlainTestListener implements TestListener {

    int runs;
    int failures;
    int errors;

    Hashtable testStarts = new Hashtable();
    Hashtable failedTests = new Hashtable();

    OutputStream out;
    StringWriter str;
    PrintWriter wri;

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

    public STFPlainTestListener() {

        str = new StringWriter();
        wri = new PrintWriter(str);
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

    public boolean setFile(String fname) {
        try {
            out = new BufferedOutputStream(new FileOutputStream(fname));
            return true;
        }
        catch (Exception e) {
            return false;
        }

    }

    public void startTestSuite(TestSuite suite) {
        try {
            StringBuffer sb = new StringBuffer(System.getProperty("line.separator"));
            sb.append("TestSuite: " + suite.getName());
            sb.append(System.getProperty("line.separator"));

            out.write(sb.toString().getBytes());
            out.flush();
        }
        catch (IOException e) {

        }
    }

    public void addError(Test test, Throwable throwable) {
        formatError("\terror occured", test, throwable);
    }

    public void addFailure(Test test, Throwable t) {
        formatError("\tfailed", test, t);
    }

    public void addFailure(Test test, AssertionFailedError assertionFailedError) {
        formatError("\tfailed", test, (Throwable) assertionFailedError);
    }

    public void endTest(Test test) {
        wri.print("TestCase: " + ((TestCase) test).getName());
        Long start = (Long) testStarts.get(test);
        Long end = new Long(System.currentTimeMillis());
        double seconds = 0;
        if (start != null) {
            seconds = (end.longValue() - start.longValue()) / 1000.0;
        }

        wri.println(" took " + seconds + " sec");
    }

    public void startTest(Test test) {
        testStarts.put(test, new Long(System.currentTimeMillis()));
        failedTests.put(test, Boolean.FALSE);
    }

    private void formatError(String type, Test test, Throwable t) {

        if (test != null) {
            endTest(test);
            failedTests.put(test, Boolean.TRUE);
        }

        wri.println("TestCase: " + ((TestCase) test).getName());
        wri.println(type);
        wri.println(t.getMessage());
        wri.print(filterStackTrace(t));
        wri.print(System.getProperty("line.separator"));
        wri.print(System.getProperty("line.separator"));
        wri.print(System.getProperty("line.separator"));
    }

    public void endTestSuite() {

        StringBuffer sb = new StringBuffer(System.getProperty("line.separator"));
        sb.append("Tests run: ");
        sb.append(runs);
        sb.append(", failures: ");
        sb.append(failures);
        sb.append(", errors:");
        sb.append(errors);
        sb.append(System.getProperty("line.separator"));
        sb.append(System.getProperty("line.separator"));
        sb.append("------------------------------");
        sb.append(System.getProperty("line.separator"));
        sb.append(System.getProperty("line.separator"));

        try {
            out.write(sb.toString().getBytes());
            wri.close();
            out.write(str.toString().getBytes());
            out.flush();
            out.close();
        }
        catch (Exception e) {
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
