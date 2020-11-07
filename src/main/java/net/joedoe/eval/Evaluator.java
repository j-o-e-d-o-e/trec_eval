package net.joedoe.eval;

import net.joedoe.search.Searcher;
import net.joedoe.util.Utils;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Round 1: https://ir.nist.gov/covidSubmit/data.html
 * Maximize <b>P_20</b> metric.
 */
public class Evaluator {
    private final ArrayList<String[]> topics;
    private final String outputDirPath;

    public Evaluator() throws IOException {
        topics = EvalUtils.parseTopics(Utils.getProperty("topics"));
        String date = Long.toString(System.currentTimeMillis());
        File outputDir = new File(Utils.getProperty("output")+ date);
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
        File actualResults = EvalUtils.createResultsFile(outputDirPath, hitsList);
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
