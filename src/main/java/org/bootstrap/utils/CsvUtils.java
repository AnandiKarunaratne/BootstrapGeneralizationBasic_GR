package org.bootstrap.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class CsvUtils {

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
