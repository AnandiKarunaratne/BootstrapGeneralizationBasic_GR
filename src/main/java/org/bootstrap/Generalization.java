package org.bootstrap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Generalization {

    double calculateEntropyRecall(Map<String, List<String>> model, List<String> log) {
        return calculateEntropyOfLog(findModelLogIntersection(model, log))/calculateEntropyOfLog(log);
    }

    double calculateEntropyOfLog(List<String> log) {
        int maxTraceLength = 0;
        double limitSup = 0.0;
        for (String trace : log) {
            if (trace.length() > maxTraceLength) {
                maxTraceLength = trace.length();
            }
        }
        for (int n = 1; n <= maxTraceLength; n++) {
            int count = 0;
            for (String trace : log) {
                if (trace.length() == n) {
                    count++;
                }
            }
            if (count > 0) {
                double logRatio = Math.log(count) / n;
                if (n == 1) limitSup = logRatio; // initialize the limit superior value with the first value
                else {
                    if (logRatio > limitSup) {
                        limitSup = logRatio;
                    }
                }
            }
        }
        return limitSup;
    }

    double calculateRecall(Map<String, List<String>> model, List<String> log) {
        int matchingEvents = findModelLogIntersection(model, log).size();
        int totalEventsInLog = log.size();

        return (double) matchingEvents/totalEventsInLog;
    }

    private List<String> findModelLogIntersection(Map<String, List<String>> model, List<String> log) {
        List<String> intersection = new ArrayList<>();

        for (String trace : log) {
            trace = "i" + trace + "o";
            for (int i = 0; i < trace.length() - 1; i++) {
                String currentEvent = String.valueOf(trace.charAt(i));
                String nextEvent = String.valueOf(trace.charAt(i + 1));

                if (model.containsKey(currentEvent) && model.get(currentEvent).contains(nextEvent)) {
                    if (i == trace.length() - 2) {
                        // remove i and o from the trace
                        String sub = trace.substring(1, trace.length() - 1);
                        intersection.add(sub);
                    }
                }
                else {
                    break;
                }
            }
        }
        return intersection;
    }

}
