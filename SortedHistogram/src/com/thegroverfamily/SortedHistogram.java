package com.thegroverfamily;

import java.io.IOException;
import java.io.Reader;

public class SortedHistogram extends SortedValues {
    private static final String HYPHEN_SEPARATOR = " - ";

    public SortedHistogram(Reader reader, int maximumValue) throws IOException {
        super(reader, maximumValue);
    }

    @Override
    protected StringBuffer appendOccurrencesOfValue(int value, int occurrence) {
        StringBuffer valuePrintout = new StringBuffer();
        if (occurrence > 0) {
            valuePrintout.append(value);
            valuePrintout.append(HYPHEN_SEPARATOR);
            valuePrintout.append(occurrence);
            valuePrintout.append(CR_LF);
        }
        return valuePrintout;
    }

}
