package com.awesome;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

public class AwesomeSpout extends BaseRichSpout {

    private SpoutOutputCollector spoutOutputCollector;
    private String start_url = "https://github.com/sindresorhus/awesome/blob/master/readme.md";
    private Map<String, String> awesomeLinks;

    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        awesomeLinks = getLinks(start_url);
        this.spoutOutputCollector = spoutOutputCollector;
    }

    public void nextTuple() {
        Map.Entry<String, String> entry = awesomeLinks.entrySet().iterator().next();
        String name = entry.getKey();
        String url = entry.getValue();
        spoutOutputCollector.emit(new Values(name, url));
        awesomeLinks.remove(name);
    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("name", "url"));
    }

    private Map<String, String> getLinks(String url) {
        Map<String, String> linksInUrl = new HashMap<String, String>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("article ul li a[abs:href]");
            for (Element link : links) {
                linksInUrl.put(link.text(), link.attr("href"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return linksInUrl;
    }
}
