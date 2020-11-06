package net.joedoe.search;

import org.apache.lucene.search.similarities.*;

import java.util.HashMap;

public class Similarity {

    private final HashMap<String, org.apache.lucene.search.similarities.Similarity> sim;
    private final ClassicSimilarity cs;
    private final BM25Similarity bm25;
    private final LMDirichletSimilarity diri;
    private final BooleanSimilarity bool;
    private final LMJelinekMercerSimilarity lmj;
    private final AxiomaticF3LOG ax;
    private final DFISimilarity dfi;
    private final IBSimilarity ibs;

    public Similarity(){
        this.sim = new HashMap<>();
        sim.put("cs", this.cs = new ClassicSimilarity());
        sim.put("bm25",this.bm25 = new BM25Similarity());
        sim.put("diri",this.diri = new LMDirichletSimilarity());
        sim.put("bool",this.bool = new BooleanSimilarity());
        sim.put("lmj",this.lmj = new LMJelinekMercerSimilarity(0.2F));
        sim.put("ax",this.ax = new AxiomaticF3LOG(0.5F,10));
        sim.put("dfi",this.dfi = new  DFISimilarity(new IndependenceChiSquared()));
        sim.put("ibs",this.ibs = new IBSimilarity(new DistributionLL(), new LambdaDF(), new NormalizationH3()));
     }

    public HashMap<String, org.apache.lucene.search.similarities.Similarity> getSims() {
        return sim;
    }

    public ClassicSimilarity getCs() {
        return cs;
    }

    public BM25Similarity getBm25() {
        return bm25;
    }

    public LMDirichletSimilarity getDiri() {
        return diri;
    }

    public BooleanSimilarity getBool() {
        return bool;
    }

    public LMJelinekMercerSimilarity getLmj() {
        return lmj;
    }

    public AxiomaticF3LOG getAx() {
        return ax;
    }

    public DFISimilarity getDfi() {
        return dfi;
    }

    public IBSimilarity getIbs() {
        return ibs;
    }
}
