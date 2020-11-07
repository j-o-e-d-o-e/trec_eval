package net.joedoe.search;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;

class QueryFactory {
    enum QueryType {
        ANALYZER, TERM, PREFIX, BOOL, PHRASE, FUZZY, WILDCARD
    }

    static Query getQuery(QueryType queryType, String field, String[] terms) throws ParseException {
        Query query;
        switch (queryType) {
            case ANALYZER:
                query = new QueryParser(field, new StandardAnalyzer()).parse(terms[0]);
                break;
            case TERM:
                query = new TermQuery(new Term(field, terms[0]));
                break;
            case PREFIX:
                query = new PrefixQuery(new Term(field, terms[0]));
                break;
            case BOOL:
                BooleanQuery.Builder builder = new BooleanQuery.Builder();
                for (String term : terms) {
                    query = new TermQuery(new Term(field, term));
                    builder.add(query, BooleanClause.Occur.MUST);
                }
                query = builder.build();
                break;
            case PHRASE:
                query = new PhraseQuery(1, field, terms);
                break;
            case FUZZY:
                query = new FuzzyQuery(new Term(field, terms[0]));
                break;
            case WILDCARD:
            default:
                query = new WildcardQuery(new Term(field, "*" + terms[0] + "*"));
        }
        return query;
    }
}
