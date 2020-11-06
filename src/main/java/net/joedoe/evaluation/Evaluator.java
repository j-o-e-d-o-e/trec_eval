package net.joedoe.evaluation;

import net.joedoe.evaluation.trec.Comparator;
import net.joedoe.evaluation.trec.EvalUtil;
import net.joedoe.search.Searcher;
import net.joedoe.utilities.Utilities;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Round 1: https://ir.nist.gov/covidSubmit/data.html
 * Maximize __P_20__ metric.
 */
public class Evaluator {
    private final ArrayList<String[]> topics;
    private final String outputDirPath;

    public Evaluator() throws IOException {
        Properties prop = new Utilities().getProperties();
        topics = EvalUtil.parseTopics(prop.get("topics").toString());

        String date = Long.toString(System.currentTimeMillis());
        File outputDir = new File(prop.get("output").toString() + date);
        if (!outputDir.mkdir()) throw new IOException();
        outputDirPath = outputDir.getAbsolutePath();
    }

    public void evaluate() throws IOException, ParseException {
        Searcher searcher = new Searcher();
        ArrayList<ScoreDoc[]> hitsList = new ArrayList<>();
        for (String[] topic : topics) {
            searcher.setQuery(topic);
            ScoreDoc[] hits = searcher.search();
            hitsList.add(hits);
        }
        File actualResults = EvalUtil.createResultsFile(outputDirPath, hitsList);
        Comparator comparator = new Comparator(outputDirPath, actualResults.getAbsolutePath());
        comparator.createTrecResultsFile();
    }

    public static void main(String[] args) {
        try {
            Evaluator evaluator = new Evaluator();
            evaluator.evaluate();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
