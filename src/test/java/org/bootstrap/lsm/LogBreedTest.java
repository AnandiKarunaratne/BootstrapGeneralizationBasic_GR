package org.bootstrap.lsm;

import org.junit.Test;
import org.utils.log.EventLog;
import org.utils.log.Trace;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class LogBreedTest {

    LogBreeding logBreeding = new LogBreeding();

    @Test
    public void testEstimatePopulationWithLogBreeding() {
        EventLog eventLog = new EventLog(new ArrayList<>(Arrays.asList(new Trace(Arrays.asList("a", "b", "c", "f")), new Trace(Arrays.asList("a", "c", "f", "a", "d", "e", "f")))));
        List<Trace> result = logBreeding.estimatePopulationWithLogBreeding(eventLog, 2, 2, 1);
    }

    @Test
    public void testBreedLogs() {
        List<Trace> l1 = new ArrayList<>(Arrays.asList(new Trace(Arrays.asList("a", "b", "c", "f")), new Trace(Arrays.asList("a", "b", "c", "f"))));
        List<Trace> l2 = new ArrayList<>(Arrays.asList(new Trace(Arrays.asList("a", "c", "f", "a", "d", "e", "f")), new Trace(Arrays.asList("a", "c", "f", "a", "d", "e", "f"))));
        int k = 2;
        double p = 1.0;

        List<Trace> result = logBreeding.breedLogs(l1, l2, k, p);
        List<Trace> expected = new ArrayList<>(Arrays.asList(new Trace(Arrays.asList("a", "b", "c", "f", "a", "d", "e", "f")),
                new Trace(Arrays.asList("a", "c", "f"))));

        assertEquals(expected, result);
    }

    @Test
    public void testBreedTraces() {
        Trace t1 = new Trace(Arrays.asList("a", "b", "b", "b", "c", "f"));
        Trace t2 = new Trace(Arrays.asList("a", "b", "b", "b", "c", "f"));
        int k = 2;
        LogBreeding.Site site = new LogBreeding.Site(2, 3);

        Trace result = logBreeding.breedTraces(t1, t2, k, site);
        Trace expectedTrace = new Trace(Arrays.asList("a", "b", "b", "c", "f"));

        assertEquals(result, expectedTrace);
    }

    @Test
    public void testFindBreedingSites() {
        Trace t1 = new Trace(Arrays.asList("a", "d", "e", "e", "f"));
        Trace t2 = new Trace(Arrays.asList("a", "d", "e", "f", "a", "b", "c", "f", "a", "d", "e", "f"));
        int k = 2;

        List<LogBreeding.Site> result = logBreeding.findBreedingSites(t1, t2, k);

        List<LogBreeding.Site> expectedSites = List.of(new LogBreeding.Site(1, 1), new LogBreeding.Site(1, 9),
                new LogBreeding.Site(2,2), new LogBreeding.Site(2, 10), new LogBreeding.Site(4, 3), new LogBreeding.Site(4, 11));

        assertEquals(expectedSites.size(), result.size());

        for (int i = 0; i < expectedSites.size(); i++) {
            LogBreeding.Site expected = expectedSites.get(i);
            LogBreeding.Site actual = result.get(i);

            assertEquals(expected.p1, actual.p1);
            assertEquals(expected.p2, actual.p2);
        }
    }

}