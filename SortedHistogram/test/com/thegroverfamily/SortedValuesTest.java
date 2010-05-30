package com.thegroverfamily;

import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class SortedValuesTest {

    private SortedValues sortedValues;

    private static final int MAXIMUM_POSSIBLE_VALUE = 55;

    @Before
    public void setUp() throws Exception {
        StringReader inputReader = new StringReader("6\r\n54\r\n12\r\n22\r\n1\r\n6\r\n");
        sortedValues = new SortedValues(inputReader, MAXIMUM_POSSIBLE_VALUE);
    }

    @Test
    public void aBlankStringIsTheResultIfABlankReaderIsUsedAsInput() throws Exception {
        assertEquals("", new SortedValues(new StringReader(""), MAXIMUM_POSSIBLE_VALUE).toString());
    }

    @Test
    public void aSingleValueInputResultsInASingleValueOutputWithLineDelimiterAlwaysAppended() throws Exception {
        assertEquals("1\r\n", new SortedValues(new StringReader("1"), MAXIMUM_POSSIBLE_VALUE).toString());
    }

    @Test
    public void eachLineFromReaderPutIntoANewStringWithLinesInAscendingOrder() {
        String sortedValues = this.sortedValues.toString();

        assertTrue("'" + sortedValues + "' has no '1'", sortedValues.contains("1"));
        assertTrue("'" + sortedValues + "' doesn't start with '1\\r\\n'", sortedValues.startsWith("1\r\n"));
        assertTrue("'" + sortedValues + "' contains no '1\\r\\n6'", sortedValues.contains("1\r\n6"));
        assertTrue("'" + sortedValues + "' doesn't end with '22\\r\\n54\\r\\n'", sortedValues.endsWith("22\r\n54\r\n"));
    }

    @Test
    public void resultingStringHasDuplicateLinesForASingleDuplicatedValue() {
        String sortedValues = this.sortedValues.toString();
        assertTrue("'" + sortedValues + "' doesn't have two sixes", sortedValues.contains("6\r\n6"));
    }

    @Test(expected = NullPointerException.class)
    public void aNullPointerExceptionIsThrownIfTheInputReaderIsNull() throws Exception {
        new SortedValues(null, MAXIMUM_POSSIBLE_VALUE);
    }

    @Test(expected = NumberFormatException.class)
    public void aStringWithoutValuesResultsInAnAppropriateException() throws Exception {
        new SortedValues(new StringReader("foo\r\nbar"), MAXIMUM_POSSIBLE_VALUE);
    }

    @Test(expected = NumberFormatException.class)
    public void intermingledNumbersAndStringsAlsoThrowsAppropriateException() throws Exception {
        new SortedValues(new StringReader("two2\r\n1one\r\n"), MAXIMUM_POSSIBLE_VALUE);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void whenInputValuesExceedTheSpecifiedMaximumAnExceptionWillBeThrown() throws Exception {
        StringReader reader = new StringReader(Integer.toString(MAXIMUM_POSSIBLE_VALUE + 1));
        new SortedValues(reader, MAXIMUM_POSSIBLE_VALUE);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void negativeValuesWillResultInAnExceptionWillBeThrown() throws Exception {
        StringReader reader = new StringReader(Integer.toString(-1));
        new SortedValues(reader, MAXIMUM_POSSIBLE_VALUE);
    }

    @Test
    public void inputValuesOfZeroAreValidLikeAnyOtherNumber() throws Exception {
        assertEquals("0\r\n0\r\n", new SortedValues(new StringReader("0\r\n0\r\n"), MAXIMUM_POSSIBLE_VALUE).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroMaximumValuesAreIllegalArguments() throws Exception {
        new SortedValues(new StringReader("0"), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeMaximumValuesAreIllegalArguments() throws Exception {
        new SortedValues(new StringReader("0"), -1);
    }

}