package net.joedoe.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.io.IOException;

class AnalyzerFactory {
    enum AnalyzerType {
        STD, WS, CUSTOM
    }

    @SuppressWarnings("SameParameterValue")
    static Analyzer getAnalyzer(AnalyzerType type) throws IOException {
        switch (type) {
            case STD:
                return new StandardAnalyzer();
            case WS:
                return new WhitespaceAnalyzer();
            default:
            case CUSTOM:
                return CustomAnalyzer.builder()
                        .withTokenizer("standard")
                        .addTokenFilter("lowercase")
                        .addTokenFilter("stop")
                        // .addTokenFilter("ngram", "minGramSize", "1", "maxGramSize", "25")
                        .build();
        }
    }
}
