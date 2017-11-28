package com.fiorano.stf.misc;


import javax.jms.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Sandeep M
 * @Date:
 */
public class OutputPortMessageListener extends Thread implements MessageListener, ExceptionListener {

    File outputFile;
    private boolean messageReceived = false;
    public boolean allDone = false;
    private long timeout;
    private boolean outFileFlag = false;
    private boolean jobFinished = false;


    public OutputPortMessageListener(String outputId, String testOutputPath, long timeout) {
        new File(testOutputPath).mkdirs();
        this.timeout = timeout;
        outputFile = new File(testOutputPath, outputId);
        outputFile.delete();
//        try {
//            outputFile.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public OutputPortMessageListener(long timeout) {
        this.timeout = timeout;
    }

    public void onMessage(Message message) {
        //using this flag to make sure one/first msg written
        if (outFileFlag)
            return;
        FileOutputStream outStream = null;
        BufferedOutputStream bufferedStream = null;
        try {
            if (message instanceof TextMessage) {
                if (outputFile != null) {
                    outputFile.createNewFile();
                    outStream = new FileOutputStream(outputFile);
                    bufferedStream = new BufferedOutputStream(outStream);

                    bufferedStream.write(((TextMessage) message).getText().getBytes());
                    outFileFlag = true;
                }

                if (message.getBooleanProperty("CLOSE_EVENT"))
                    jobFinished = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            messageReceived = true;
            try {
                if (bufferedStream != null)
                    bufferedStream.close();

                if (outStream != null)
                    outStream.close();
            } catch (IOException e) {
                //ignore..
            }

        }

    }

    public boolean isJobFinished() {
        return jobFinished;
    }

    public boolean getMessageReceived() {
        return messageReceived;
    }

    public void onException(JMSException jmsException) {
        //todo implement this
    }

    public void run() {
        while (!(messageReceived || allDone)) {
            try {
                sleep(2000l);
            } catch (InterruptedException e) {
                //ignore
            }
        }

    }

    public long getTimeout() {
        return timeout;
    }
}
