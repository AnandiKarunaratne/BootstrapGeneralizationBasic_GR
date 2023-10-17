package org.bootstrap;

import org.bootstrap.log.EventLog;
import org.bootstrap.lsm.LogSamplingMethod;

public class Main {
    final private static int sampleSize = 10000; // n
    final private static int numberOfSamples = 10; // m
    final private static LogSamplingMethod logSamplingMethod = LogSamplingMethod.SEMI_PARAMETRIC;

    public static void main(String[] args) throws Exception {

//        for (int noise = 5; noise <= 20; noise = noise + 5) {
//            for (int logSize = 100; logSize <= 100000; logSize = logSize * 10) {
//
//                System.out.println(logSize + " " + noise);
//                // create logs
//                // already done
//
//                // get the model
//                List<String> log = new XESReader().readXES("/Users/anandik/Documents/LogQuality/Logs/OmitActivity/log-" + logSize + "/log-" + logSize + "-" + noise + ".xes");
//                Map<String, HashSet<String>> model = new ModelBasedNoiseIdentifier(log).getModel(log);
//
//                // convert the model to a pnml
//                Set<String> actions = model.keySet();
//                actions.remove("i");
//                actions.remove("o");
//                Set<String> directlyFollows = new HashSet<>();
//                for (Map.Entry<String, HashSet<String>> entry : model.entrySet()) {
//                    String key = entry.getKey();
//                    HashSet<String> values = entry.getValue();
//
//                    for (String value : values) {
//                        directlyFollows.add(key + "->" + value);
//                    }
//                }
//
//                DFG dfg = new DFG(actions, directlyFollows);
//                SDFA sdfa = new DFGToSDFAConverter().convertToSDFA(dfg);
//                String modelFilePath = "/Users/anandik/Documents/LogQuality/Models/OmitActivity/model-" + logSize + "/model-" + logSize + "-" +noise + ".pnml";
//                new PNMLGenerator().generatePNML(new SDFAToPetriNetConverter(sdfa).convertToPetriNet(), modelFilePath);
//
//                // read xes
//                String logFilePath = "/Users/anandik/Documents/LogQuality/Logs/OmitActivity/log-" + logSize + "/log-" + logSize + "-" + noise + ".xes";
//                EventLog eventLog = new EventLog(logFilePath);
//
//                for (int i = 0; i < 1; i++) {
//                    new BootstrapGen(sampleSize, numberOfSamples, eventLog, logSamplingMethod, modelFilePath).calculateBootstrapGen();
//                }
//            }
//        }



        // int noise = 10;
        for (int logSize = 10000; logSize <= 100000; logSize = logSize * 10) {
            for (int noise = 5; noise < 11; noise = noise + 5) {
                String logFilePath = "/Users/anandik/Documents/LogQuality/Logs/InjectActivity/log-" + logSize + "/log-" + logSize + "-" + noise + ".xes";
                String modelFilePath = "/Users/anandik/Documents/LogQuality/Models/InjectActivity/model-" + logSize + "/model-" + logSize + "-" +noise + ".pnml";

                EventLog eventLog = new EventLog(logFilePath);

                for (int i = 0; i < 1; i++) {
                    new BootstrapGen(sampleSize, numberOfSamples, eventLog, logSamplingMethod, modelFilePath).calculateBootstrapGen();
                }
            }
        }
    }

}