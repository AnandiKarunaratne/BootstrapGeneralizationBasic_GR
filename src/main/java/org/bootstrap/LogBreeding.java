package org.bootstrap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LogBreeding {

    /**
     * This method will choose traces from L1 and L2 and breed traces with a probability of p checking for k-overlaps
     *
     * @param L1 Log1, a multiset of traces
     * @param L2 Log2, a multiset of traces
     * @param k length of common subtrace
     * @param p breeding probability
     * @return List of traces which is a result of breeding L1 and L2
     */
    public List<String> breedLogs(List<String> L1, List<String> L2, int k, double p) {
        List<String> bredLog = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < L1.size() / 2; i++) {
            String t1 = L1.get(rand.nextInt(L1.size()));
            String t2 = L2.get(rand.nextInt(L2.size()));

            List<Site> sites = findBreedingSites(t1, t2, k);

            if (rand.nextDouble() < p && !sites.isEmpty()) {
                int randomIndex = rand.nextInt(sites.size());
                Site site = sites.get(randomIndex);

                String bredTrace1 = breedTraces(t1, t2, k, site);
                String bredTrace2 = breedTraces(t2, t1, k, new Site(site.p2, site.p1));

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
    private String breedTraces(String t1, String t2, int k, Site site) {
        int prefixEnd = site.p1 + k - 1;
        int suffixStart = site.p2 + k;

        // Extract the prefix and suffix substrings
        String prefix = t1.substring(0, prefixEnd);
        String suffix = t2.substring(suffixStart - 1);

        // Concatenate the prefix and suffix
        return prefix + suffix;
    }

    /**
     * This method will find all th e breeding sites for a given two traces and a length of subtrace
     * @param t1 trace1
     * @param t2 trace2
     * @param k legth of subtrace
     * @return list of breeding sites
     */
    public List<Site> findBreedingSites(String t1, String t2, int k) {
        List<Site> sites = new ArrayList<>();
        
        for (int p1 = 1; p1 <= t1.length() - k + 1; p1++) {
            for (int p2 = 1; p2 <= t2.length() - k + 1; p2++) {
                String subseq1 = t1.substring(p1 - 1, p1 + k - 1);
                String subseq2 = t2.substring(p2 - 1, p2 + k - 1);

                if (subseq1.equals(subseq2)) {
                    sites.add(new Site(p1, p2));
                }
            }
        }
        return sites;
    }

    static class Site {
        int p1;
        int p2;

        Site(int p1, int p2) {
            this.p1 = p1;
            this.p2 = p2;
        }
    }

}