package net.joedoe.index;

import net.joedoe.index.AnalyzerFactory.AnalyzerType;
import net.joedoe.util.Utils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class Indexer {
    private final File input;
    private final IndexWriter writer;
    private final Logger logger = Logger.getLogger(Indexer.class.getName());

    public Indexer() throws IOException {
        input = new File(Utils.getProperty("data"));
        String outputPath = Utils.getProperty("index"); // + "_test";
        Directory output = FSDirectory.open(Paths.get(new File(outputPath).getAbsolutePath()));
        IndexWriterConfig config = new IndexWriterConfig(AnalyzerFactory.getAnalyzer(AnalyzerType.STD));
        config.setOpenMode(OpenMode.CREATE);
        writer = new IndexWriter(output, config);
    }

    public void index() {
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(input))) {
                String[] headers = br.readLine().split(",");
                // [cord_uid, sha, source_x, title, doi, pmcid, pubmed_id, license, abstract, publish_time, authors, journal, Microsoft Academic Paper ID, WHO #Covidence, has_pdf_parse, has_pmc_xml_parse, full_text_file, url]
                // logger.info(Arrays.toString(headers));
                int count = 0;
                String line;
                while ((line = br.readLine()) != null) {
                    logger.info(count + "\n" + "LINE: " + line);
                    String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    if (values.length <= 3) continue;
                    Document doc = new Document();

                    String uid = values[0];
                    doc.add(new TextField("uid", uid, Field.Store.YES));

                    String title = values[3];
                    doc.add(new TextField("title", title, Field.Store.YES));

                    String doi = "";
                    if (values.length > 4) {
                        doi = values[4];
                        doc.add(new TextField("doi", doi, Field.Store.YES));
                    }
                    String summary = "";
                    if (values.length > 8) {
                        summary = values[8];
                        doc.add(new TextField("abstract", summary, Field.Store.YES));
                    }
                    String authors = "";
                    if (values.length > 10) {
                        authors = values[10];
                        doc.add(new TextField("authors", authors, Field.Store.YES));
                    }
                    String url = "";
                    if (values.length > 17) {
                        url = values[17];
                        doc.add(new TextField("url", url, Field.Store.YES));
                    }
                    writer.addDocument(doc);
                    logger.info("\n"
                            + "ID: " + uid + "\n"
                            + "TITLE: " + title + "\n"
                            + "DOI: " + doi + "\n"
                            + "ABSTRACT: " + summary + "\n"
                            + "AUTHORS: " + authors + "\n"
                            + "URL: " + url + "\n");
                    count++;
//                    if (count > 3) break; // for testing
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Indexer indexer = new Indexer();
            indexer.index();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
