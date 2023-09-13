package org.bootstrap.log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class EventLog {

    private final List<Event> eventLog = new ArrayList<>();
    private final List<String> traceList = new ArrayList<>();

    public EventLog() {}

    public EventLog(String csvFile) {
        preProcessCSV(csvFile);
        generateTraces();
    }

    /**
     * This method will read the CSV file containing the event log,
     * extract the data and format it into a list of Events (List<Event>)
     *
     * @param csvFile : CSV file containing the event log
     */
    private void preProcessCSV(String csvFile) {
        // read CSV
        String line;
        String delimiter = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                // split and add to event log
                String[] values = line.split(delimiter);
                // depends on where our caseId, activity, timestamp is defined
                eventLog.add(new Event(values[0], values[1], values[2]));
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method will generate traces using the event log
     */
    private void generateTraces() {
        Map<String, List<Event>> eventMap = new HashMap<>();
        // group similar caseIds
        for (Event event : eventLog) {
            String caseId = event.getCaseId();
            if (!eventMap.containsKey(caseId)) {
                eventMap.put(caseId, new ArrayList<>());
            }
            eventMap.get(caseId).add(event);
        }

        for (Map.Entry<String, List<Event>> entry : eventMap.entrySet()) {
            List<Event> traceEvents = entry.getValue();

            //sort using timestamps
            traceEvents.sort(Comparator.comparing(Event::getTimestamp));

            // extract trace string
            StringBuilder stringBuilder = new StringBuilder();
            for (Event traceEvent : traceEvents) {
                stringBuilder.append(traceEvent.getActivityName()).append(", ");
            }

            // Convert the StringBuilder to a final string.
            traceList.add(stringBuilder.toString());
        }
    }

    public List<String> getTraceList() {
        return traceList;
    }

    public List<String> getSampleTraceList() {
        List<String> sampleTraceList = new ArrayList<>();
        for (int i = 0; i < 5; i++) { sampleTraceList.add("abbbcf"); }
        for (int i = 0; i < 20; i++) { sampleTraceList.add("abcf"); }
        for (int i = 0; i < 10; i++) { sampleTraceList.add("adeef"); }
        for (int i = 0; i < 10; i++) { sampleTraceList.add("adefabcfadef"); }
        for (int i = 0; i < 20; i++) { sampleTraceList.add("adef"); }
        sampleTraceList.add("addef");

        return sampleTraceList;
    }

}
