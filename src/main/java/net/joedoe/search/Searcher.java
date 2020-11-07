package net.joedoe.search;

import net.joedoe.search.QueryFactory.QueryType;
import net.joedoe.search.SimilarityFactory.SimType;
import net.joedoe.util.Utils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class Searcher {
    @SuppressWarnings("FieldCanBeLocal")
    private final String[] terms = new String[]{"covid"};
    private Query query;
    private final String field;
    private final IndexSearcher searcher;
    private final Logger logger = Logger.getLogger(Searcher.class.getName());


    public Searcher() throws IOException, ParseException {
        field = Utils.getProperty("field2");
        query = QueryFactory.getQuery(QueryType.BOOL, field, terms);

        File index = new File(Utils.getProperty("index"));
        Directory directory = FSDirectory.open(index.toPath());
        IndexReader indexReader = DirectoryReader.open(directory);
        searcher = new IndexSearcher(indexReader);
        searcher.setSimilarity(SimilarityFactory.getSim(SimType.CS));
    }

    public ScoreDoc[] search() throws IOException {
        TopDocs result = searcher.search(query, 20);
        return result.scoreDocs;
    }

    private void printResults(ScoreDoc[] scores) throws IOException {
        int count = 1;
        for (ScoreDoc score : scores) {
            Document doc = searcher.doc(score.doc);
            logger.info(count + "\n"
                    + "SCORE: " + score.score + "\n"
                    + "TITLE: " + doc.get("title") + "\n"
                    + "DOI: " + doc.get("doi") + "\n"
                    + "URL: " + doc.get("url") + "\n");
            count++;
        }
    }

    public void setQuery(String[] topic) throws ParseException {
        query = QueryFactory.getQuery(QueryType.PHRASE, field, topic);
    }

    public static void main(String[] args) {
        try {
            Searcher searcher = new Searcher();
            ScoreDoc[] scores = searcher.search();
            searcher.printResults(scores);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
