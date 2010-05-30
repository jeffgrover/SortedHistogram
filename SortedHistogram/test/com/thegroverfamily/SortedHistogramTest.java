package com.thegroverfamily;

import org.junit.Test;

import java.io.StringReader;

import static junit.framework.Assert.*;

public class SortedHistogramTest {

    private SortedHistogram sortedHistogram;


    @Test
    public void getHistogramCreatesAStringWithSortedValuesAndOccurrencesHyphenSeparated() throws Exception {
        StringReader inputReader = new StringReader("6\r\n54\r\n12\r\n22\r\n1\r\n6\r\n");
        sortedHistogram = new SortedHistogram(inputReader, 54);
        assertEquals("1 - 1\r\n6 - 2\r\n12 - 1\r\n22 - 1\r\n54 - 1\r\n", sortedHistogram.toString());
    }

    @Test
    public void illegalArgumentExceptionMessageIsTailoredToThisClass() {
        try {
            new SortedHistogram(new StringReader(""), -1);
            fail("An IllegalArgumentException should have been thrown");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertTrue(e.getMessage().contains(SortedHistogram.class.getSimpleName()));
        }
    }

}
