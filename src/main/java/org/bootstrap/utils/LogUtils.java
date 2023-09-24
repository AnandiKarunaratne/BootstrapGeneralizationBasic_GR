package org.bootstrap.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class LogUtils {
    public static void generateXES(List<String> traces) {
        String filePath = "src/main/java/org/bootstrap/resources/samplelogfile.xes";
        File file = new File(filePath);
        OutputStream outputStream = null;
        String start = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<log xes.version=\"1.0\" xes.features=\"nested-attributes\" openxes.version=\"1.0RC7\">\n" +
                "<extension name=\"Lifecycle\" prefix=\"lifecycle\" uri=\"http://www.xes-standard.org/lifecycle.xesext\"/>\n" +
                "<extension name=\"Organizational\" prefix=\"org\" uri=\"http://www.xes-standard.org/org.xesext\"/>\n" +
                "<extension name=\"Time\" prefix=\"time\" uri=\"http://www.xes-standard.org/time.xesext\"/>\n" +
                "<extension name=\"Concept\" prefix=\"concept\" uri=\"http://www.xes-standard.org/concept.xesext\"/>\n" +
                "<extension name=\"Semantic\" prefix=\"semantic\" uri=\"http://www.xes-standard.org/semantic.xesext\"/>\n" +
                "<global scope=\"trace\">\n" +
                "<string key=\"concept:name\" value=\"UNKNOWN\"/>\n" +
                "</global>\n" +
                "<global scope=\"event\">\n" +
                "<string key=\"concept:name\" value=\"UNKNOWN\"/>\n" +
                "</global>\n" +
                "<classifier name=\"Activity classifier\" keys=\"concept:name\"/>\n" +
                "<string key=\"concept:name\" value=\"log\"/>\n";
        int i = 1;

        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(start.getBytes());

            for (String trace : traces) {
                outputStream.write(("\t<trace>\n\t\t<string key=\"concept:name\" value=\""+ i++ +"\"/>\n").getBytes());
                for (int j = 0; j < trace.length(); j++) {
                    outputStream.write(("\t\t<event>\n\t\t\t<string key=\"concept:name\" value=\""+ trace.charAt(j) +"\"/>\n\t\t</event>\n").getBytes());
                }
                outputStream.write("\t</trace>\n".getBytes());
            }

            outputStream.write("</log>".getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
