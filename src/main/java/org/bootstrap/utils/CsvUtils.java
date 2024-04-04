package org.bootstrap.utils;

import java.io.BufferedReader;
import java.io.FileReader;
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

    public static String[] readCSV(String csvFile) {
        List<String> entriesList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                entriesList.add(line); // Add each line to the list
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] entries = new String[entriesList.size()];
        entries = entriesList.toArray(entries);

        return entries;
    }
}
