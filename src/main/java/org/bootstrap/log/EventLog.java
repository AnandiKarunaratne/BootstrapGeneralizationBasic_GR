package org.bootstrap.log;

import org.utils.files.xes.XESReader;
import org.utils.log.Trace;

import java.util.*;

/**
 * Future Updates: Update this to read an XES and convert into a List<String>
 */
public class EventLog extends org.utils.log.EventLog {

    private List<Trace> traceList = new ArrayList<>();

    public EventLog() {
        generateSampleTraces();
    }

    public EventLog(String xesFilePath) {
        this.traceList = new XESReader().readXES(xesFilePath);
    }

    public EventLog(List<Trace> traces) {
        super(traces);
        this.traceList = traces;
    }

    private void generateSampleTraces() {
        for (int i = 0; i < 5; i++) traceList.add(new Trace(Arrays.asList("a", "b", "b", "b", "c", "f")));
        for (int i = 0; i < 20; i++) traceList.add(new Trace(Arrays.asList("a", "b", "c", "f")));
        for (int i = 0; i < 10; i++) traceList.add(new Trace(Arrays.asList("a", "d", "e", "e", "f")));
        for (int i = 0; i < 10; i++) traceList.add(new Trace(Arrays.asList("a", "d", "e", "f", "a", "b", "c", "f", "a", "d", "e", "f")));
        for (int i = 0; i < 20; i++) traceList.add(new Trace(Arrays.asList("a", "d", "e", "f")));
        traceList.add(new Trace(Arrays.asList("a", "d", "d", "e", "f")));
    }

    public List<Trace> getTraceList() {
        return traceList;
    }

}
