package org.bootstrap.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class CsvUtils {
    public static List<String> prepareCsvRow(int log, int logSize, String noiseType, double noiseLevel, int sampleSizeCo, int g, double[] result, double duration) {
        List<String> csvRow = new ArrayList<>();
        csvRow.add(String.valueOf(log));
        csvRow.add(String.valueOf(logSize));
        csvRow.add(noiseType);
        csvRow.add(String.valueOf(noiseLevel));
        csvRow.add(String.valueOf(sampleSizeCo));
        csvRow.add(String.valueOf(g));
        for (double value : result) csvRow.add(String.valueOf(value));
        csvRow.add(String.valueOf(duration));
        return csvRow;
    }

    public static void writeToCsv(List<String> rowCsv, String filePath) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            StringJoiner joiner = new StringJoiner(",");
            for (String str : rowCsv) joiner.add(str);
            writer.append(joiner.toString());
            writer.append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
