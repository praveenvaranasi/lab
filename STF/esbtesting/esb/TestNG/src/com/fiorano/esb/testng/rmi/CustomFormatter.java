package com.fiorano.esb.testng.rmi;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.Date;
import java.text.MessageFormat;
import java.io.StringWriter;
import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 * User: gopi
 * Date: Mar 17, 2010
 * Time: 10:58:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class CustomFormatter extends Formatter {

    private Date dat = new Date();
    private final static String format = "{0,date} {0,time}";
    private MessageFormat formatter;
    private static int linenumbercount = 1;
    private Object args[] = new Object[1];
    private String lineSeparator = (String) java.security.AccessController.doPrivileged(
            new sun.security.action.GetPropertyAction("line.separator"));

    public synchronized String format(LogRecord record) {
        StringBuffer sb = new StringBuffer();
        sb.append(linenumbercount + ".");
        linenumbercount++;
        sb.append(" ");
        dat.setTime(record.getMillis());
        args[0] = dat;
        StringBuffer text = new StringBuffer();
        if (formatter == null) {
            formatter = new MessageFormat(format);
        }
        formatter.format(args, text, null);
        sb.append(text);
        sb.append(" ");

        String message = formatMessage(record);
        sb.append(" ");
        sb.append(message);
        sb.append(lineSeparator);
        if (record.getThrown() != null) {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            } catch (Exception ex) {
            }
        }
        return sb.toString();
    }
}


