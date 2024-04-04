package org.bootstrap.log;

import org.jbpt.petri.NetSystem;
import org.jbpt.petri.Transition;
import org.utils.log.Trace;

import java.util.Collection;

public class EventLogOps {

    public EventLog sampleRandomLog(NetSystem sys, int n) {
        if (sys==null || n<0) return null;

        EventLog sample = new EventLog();
        for (int i=0; i<n; i++) {
            sys.loadNaturalMarking();
            Trace trace = new Trace();

            while (!sys.getEnabledTransitions().isEmpty()) {
                Transition t = random(sys.getEnabledTransitions());
                if (t.isObservable()) trace.add(t.getLabel());
                sys.fire(t);
            }
            sample.add(trace);
        }
        return sample;
    }

    private <T> T random(Collection<T> coll) {
        int num = (int) (Math.random() * coll.size());
        for(T t: coll) if (--num < 0) return t;
        throw new AssertionError();
    }

}
