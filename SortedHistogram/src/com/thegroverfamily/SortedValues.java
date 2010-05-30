package com.thegroverfamily;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;


public class SortedValues {


    private final int[] lines;
    static final String CR_LF = "\r\n";
    private int maximumValue;

    public SortedValues(Reader reader, int maximumPossibleValue) throws IOException {
        if (maximumPossibleValue > 0)
            this.maximumValue = maximumPossibleValue + 1;
        else
            throw new IllegalArgumentException(this.getClass().getSimpleName() +
                    " constructor parameter (maximumPossibleValue) parameter must be 1 or greater");

        BufferedReader buffer = new BufferedReader(reader);
        lines = new int[maximumValue];
        String line;
        while (null != (line = buffer.readLine())) {
            lines[new Integer(line)]++;
        }
    }

    StringBuffer appendOccurrencesOfValue(int value, int occurrence) {
        StringBuffer valuePrintout = new StringBuffer();
        for (int displayedLines = 1; displayedLines <= occurrence; ++displayedLines) {
            valuePrintout.append(value);
            valuePrintout.append(CR_LF);
        }
        return valuePrintout;
    }

    @Override
    public String toString() {
        StringBuffer orderedString = new StringBuffer();
        for (int index = 0; index < maximumValue; index++) {
            orderedString.append(appendOccurrencesOfValue(index, lines[index]));
        }
        return orderedString.toString();
    }

}
