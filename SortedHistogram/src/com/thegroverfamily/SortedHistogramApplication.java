package com.thegroverfamily;

import java.io.FileReader;
import java.io.FileWriter;


public class SortedHistogramApplication {

    private static final int YOU_AINT_GONNA_NEED_VALUES_GREATER_THAN_THIS = 100;

    public static void main(String[] arguments) throws Throwable {
        if (arguments.length > 0)
            System.out.println("USAGE:  (no command-line arguments expected)");

        FileReader reader = new FileReader("input.txt");

        FileWriter ascendingWriter = new FileWriter("ascending.txt");
        ascendingWriter.write(new SortedValues(reader, YOU_AINT_GONNA_NEED_VALUES_GREATER_THAN_THIS).toString());
        ascendingWriter.close();

        reader = new FileReader("input.txt");

        FileWriter histogramWriter = new FileWriter("histogram.txt");
        histogramWriter.write(new SortedHistogram(reader, YOU_AINT_GONNA_NEED_VALUES_GREATER_THAN_THIS).toString());
        histogramWriter.close();

        reader.close();
    }
}
