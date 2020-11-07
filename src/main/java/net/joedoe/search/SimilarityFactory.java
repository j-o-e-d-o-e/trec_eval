package net.joedoe.search;

import org.apache.lucene.search.similarities.*;

import java.util.HashMap;

class SimilarityFactory {
    enum SimType {
        CS, BM_25, DIRI, BOOL, LMJ, AX, DFI, IBS
    }

    @SuppressWarnings("SameParameterValue")
    static Similarity getSim(SimType type) {
        switch (type) {
            case CS:
                return new ClassicSimilarity();
            case BM_25:
                return new BM25Similarity();
            case DIRI:
                return new LMDirichletSimilarity();
            case BOOL:
                return new BooleanSimilarity();
            case LMJ:
                return new LMJelinekMercerSimilarity(0.2F);
            case AX:
                return new AxiomaticF3LOG(0.5F, 10);
            case DFI:
                return new DFISimilarity(new IndependenceChiSquared());
            default:
            case IBS:
                return new IBSimilarity(new DistributionLL(), new LambdaDF(), new NormalizationH3());
        }
    }
}
