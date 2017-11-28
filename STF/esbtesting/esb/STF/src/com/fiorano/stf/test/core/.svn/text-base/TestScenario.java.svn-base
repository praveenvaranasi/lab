package com.fiorano.stf.test.core;

import com.fiorano.stf.util.StaxParser;
import com.fiorano.stf.util.StringHashtable;

import javax.xml.stream.XMLStreamException;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 * @author Sandeep M
 * @Date:
 */
public class TestScenario {
    private List inputs = new Vector();
    private StringHashtable expectedOutputs = new StringHashtable();
    private String testId;

    public List getInputs() {
        return inputs;
    }

    public StringHashtable getExpectedOutputs() {
        return expectedOutputs;
    }

    public String getTestId() {
        return testId;
    }

    /*-------------------------------------------------[ Stax Parsing ]---------------------------------------------------*/
    public static final String ELEM_INPUTS = "inputs";
    public static final String ELEM_EXPECTED_OUTPUTS = "expectedOutputs";
    public static final String ELEM_INPUT = "input";
    public static final String ELEM_OUTPUT = "output";
    public static final String ATTR_ID = "id";

    public void populate(StaxParser cursor) throws XMLStreamException {
        Hashtable attribites = cursor.getAttributes();
        testId = (String) attribites.get(ATTR_ID);

        while (cursor.nextElement()) {
            String localeName = cursor.getLocalName();
            if (localeName.equalsIgnoreCase(ELEM_INPUTS)) {
                cursor.markCursor(ELEM_INPUTS);
                while (cursor.nextElement()) {
                    if (cursor.getLocalName().equalsIgnoreCase(ELEM_INPUT)) {
                        TestArtifact input = new TestArtifact(testId);
                        input.populate(cursor);
                        inputs.add(input);
                    }
                }
                cursor.resetCursor();

            } else if (localeName.equalsIgnoreCase(ELEM_EXPECTED_OUTPUTS)) {
                cursor.markCursor(ELEM_EXPECTED_OUTPUTS);
                while (cursor.nextElement()) {
                    if (cursor.getLocalName().equalsIgnoreCase(ELEM_OUTPUT)) {
                        TestArtifact expectedOutput = new TestArtifact(testId);
                        expectedOutput.populate(cursor);
                        expectedOutputs.put(expectedOutput.getId(), expectedOutput);
                    }
                }
                cursor.resetCursor();

            }
        }
    }
}