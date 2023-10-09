package org.bootstrap.log;

import org.utils.files.xes.XESReader;

import java.util.*;

/**
 * Future Updates: Update this to read an XES and convert into a List<String>
 */
public class EventLog {

    private List<String> traceList = new ArrayList<>();

    public EventLog() {
        generateSampleTraces();
    }

    public EventLog(String xesFilePath) {
        this.traceList = new XESReader().readXES(xesFilePath);
    }

    private void generateSampleTraces() {
        for (int i = 0; i < 5; i++) { traceList.add("abbbcf"); }
        for (int i = 0; i < 20; i++) { traceList.add("abcf"); }
        for (int i = 0; i < 10; i++) { traceList.add("adeef"); }
        for (int i = 0; i < 10; i++) { traceList.add("adefabcfadef"); }
        for (int i = 0; i < 20; i++) { traceList.add("adef"); }
        traceList.add("addef");
    }

    public List<String> getTraceList() {
        return traceList;
    }

}
