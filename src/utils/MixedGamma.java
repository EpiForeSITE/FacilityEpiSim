package utils;

import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;


public class MixedGamma extends GammaDistribution {

    private static final long serialVersionUID = 1L;

    private final double prob1;
    private final GammaDistribution comp1;
    private final GammaDistribution comp2;

    
    public MixedGamma(double shape1, double scale1,
                      double shape2, double scale2,
                      double prob1) {
        this(new Well19937c(), shape1, scale1, shape2, scale2, prob1);
    }

    public MixedGamma(RandomGenerator rng,
                      double shape1, double scale1,
                      double shape2, double scale2,
                      double prob1) {
        super(rng, Math.max(1e-12, shape1), Math.max(1e-12, scale1));
        if (prob1 < 0.0 || prob1 > 1.0) {
            throw new IllegalArgumentException("prob1 must be in [0, 1]");
        }
        this.prob1 = prob1;
        this.comp1 = new GammaDistribution(rng, Math.max(1e-12, shape1), Math.max(1e-12, scale1));
        this.comp2 = new GammaDistribution(rng, Math.max(1e-12, shape2), Math.max(1e-12, scale2));
    }

    @Override
    public double getNumericalMean() {
        return prob1 * comp1.getNumericalMean() + (1.0 - prob1) * comp2.getNumericalMean();
    }

    @Override
    public double sample() {
        if (random.nextDouble() < prob1) {
            return comp1.sample();
        } else {
            return comp2.sample();
        }
    }
}
