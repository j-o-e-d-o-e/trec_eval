package net.joedoe.evaluation.trec;

import org.apache.lucene.search.ScoreDoc;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class EvalUtil {

    public static File createResultsFile(String outputDirPath, ArrayList<ScoreDoc[]> results) throws IOException {
        File output = new File(outputDirPath + "/results.txt");
        int lines;
        int cnt = 0;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(output), 131072)) { // 128 kb buffer
            lines = 0;
            for (ScoreDoc[] scores : results) { // iterate sorted list of <docId, sim> map-entries for current query
                for (ScoreDoc score : scores) {
                    String line = String.join("\t", Integer.toString(cnt),
                            "0", String.valueOf(score.doc), Integer.toString(++lines),
                            Double.toString(score.score), "test\n");
                    writer.write(line);
                }
                cnt++;
            }
        }
        return output;
    }

    public static ArrayList<String[]> parseTopics(String topicsFilePath) throws IOException {
        ArrayList<String[]> topics = new ArrayList<>();
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new File(topicsFilePath));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("query");
            for (int item = 0; item < nodeList.getLength(); item++) {
                Node node = nodeList.item(item);
                String[] topic = node.getTextContent().split(" ");
                topics.add(topic);
            }
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return topics;
    }
}
