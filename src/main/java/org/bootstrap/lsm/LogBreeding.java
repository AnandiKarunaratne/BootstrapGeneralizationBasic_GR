package org.bootstrap.lsm;

import java.util.*;

import org.bootstrap.utils.SetUtils;
import org.utils.log.EventLog;
import org.utils.log.Trace;

public class LogBreeding {

    public List<Trace> estimatePopulationWithLogBreeding(EventLog log, int g, int k, double p) {
        List<Trace>[] G = new ArrayList[g + 1];
        G[0] = new ArrayList<>(log);

        // Generate log generations
        for (int i = 1; i <= g; i++) {
            G[i] = breedLogs(log, G[i - 1], k, p);
        }

        List<Trace> aggregatedLog = new ArrayList<>();
        for (List<Trace> bredLog : G) {
            aggregatedLog = SetUtils.multisetUnion(new EventLog(aggregatedLog), new EventLog(bredLog));
        }
        return aggregatedLog;
    }

    /**
     * This method will choose traces from L1 and L2 and breed traces with a probability of p checking for k-overlaps
     *
     * @param L1 Log1, a multiset of traces
     * @param L2 Log2, a multiset of traces
     * @param k length of common subtrace
     * @param p breeding probability
     * @return List of traces which is a result of breeding L1 and L2
     */
    protected List<Trace> breedLogs(List<Trace> L1, List<Trace> L2, int k, double p) {
        List<Trace> bredLog = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < L1.size() / 2; i++) {
            Trace t1 = L1.get(rand.nextInt(L1.size()));
            Trace t2 = L2.get(rand.nextInt(L2.size()));

            List<Site> sites = findBreedingSites(t1, t2, k);

            if (rand.nextDouble() < p && !sites.isEmpty()) {
                int randomIndex = rand.nextInt(sites.size());
                Site site = sites.get(randomIndex);

                Trace bredTrace1 = breedTraces(t1, t2, k, site);
                Trace bredTrace2 = breedTraces(t2, t1, k, new Site(site.p2, site.p1));

                bredLog.add(bredTrace1);
                bredLog.add(bredTrace2);
            } else {
                bredLog.add(t1);
                bredLog.add(t2);
            }
        }
        return bredLog;
    }

    /**
     * This method will perform a crossover operation for a given two traces, t1 and t2 for a shared k actions
     * @param t1 trace1
     * @param t2 trace2
     * @param k number of shared actions
     * @param site contains (p1, p2) : positions where crossover operation will be performed
     * @return trace obtained from crossover
     */
    protected Trace breedTraces(Trace t1, Trace t2, int k, Site site) {
        int prefixEnd = site.p1 + k - 1;
        int suffixStart = site.p2 + k - 1;

        // Extract the prefix and suffix substrings
        Trace prefix = new Trace(t1.subList(0, prefixEnd));
        Trace suffix = new Trace(t2.subList(suffixStart, t2.size()));

        // Concatenate the prefix and suffix
        Trace combinedTrace = new Trace(prefix);
        combinedTrace.addAll(suffix);
        return combinedTrace;
    }

    /**
     * This method will find all th e breeding sites for a given two traces and a length of subtrace
     * @param t1 trace1
     * @param t2 trace2
     * @param k legth of subtrace
     * @return list of breeding sites
     */
    protected List<Site> findBreedingSites(Trace t1, Trace t2, int k) {
        List<Site> sites = new ArrayList<>();
        
        for (int p1 = 1; p1 <= t1.size() - k + 1; p1++) {
            for (int p2 = 1; p2 <= t2.size() - k + 1; p2++) {
                Trace subseq1 = new Trace(t1.subList(p1 - 1, p1 + k - 1));
                Trace subseq2 = new Trace(t2.subList(p2 - 1, p2 + k - 1));

                if (subseq1.equals(subseq2)) {
                    sites.add(new Site(p1, p2));
                }
            }
        }
        return sites;
    }

    protected static class Site {
        int p1;
        int p2;

        Site(int p1, int p2) {
            this.p1 = p1;
            this.p2 = p2;
        }
    }

}