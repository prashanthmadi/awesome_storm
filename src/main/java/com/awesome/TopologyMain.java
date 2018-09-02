package com.awesome;

import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;

public class TopologyMain {

    public static void main(String[] args){
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("awesome-spout",new AwesomeSpout());
        builder.setBolt("awesome-bolt", new AwesomeBolt()).shuffleGrouping("awesome-spout");

        StormTopology stormTopology = builder.createTopology();

        Config config = new Config();
        config.setDebug(true);

        try {
            StormSubmitter.submitTopology("awesome-topology", config, stormTopology);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
