package org.bootstrap.utils;

import org.utils.log.EventLog;
import org.utils.log.Trace;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetUtils {
    public static EventLog multisetUnion(EventLog log1, EventLog log2) {
        log1.addAll(log2);
        return log1;
    }

    public static List<String> setUnion(List<String> log1, List<String> log2) {
        Set<String> set1 = new HashSet<>(log1);
        Set<String> set2 = new HashSet<>(log2);
        set1.addAll(set2);
        return new ArrayList<>(set1);
    }
}
