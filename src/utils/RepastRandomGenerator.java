package utils;

import org.apache.commons.math3.random.RandomGenerator;
import repast.simphony.random.RandomHelper;

/**
 * Adapter to use Repast's RandomHelper with Apache Commons Math distributions.
 * This ensures all random number generation uses Repast's controlled RNG system
 * for reproducible batch runs.
 */
public class RepastRandomGenerator implements RandomGenerator {
    
    @Override
    public void setSeed(int seed) {
        // Repast manages seeds - ignore
    }

    @Override
    public void setSeed(int[] seed) {
        // Repast manages seeds - ignore
    }

    @Override
    public void setSeed(long seed) {
        // Repast manages seeds - ignore
    }

    @Override
    public void nextBytes(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) RandomHelper.nextIntFromTo(0, 255);
        }
    }

    @Override
    public int nextInt() {
        return RandomHelper.nextIntFromTo(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public int nextInt(int n) {
        return RandomHelper.nextIntFromTo(0, n - 1);
    }

    @Override
    public long nextLong() {
        // Combine two ints to make a long
        return ((long) nextInt() << 32) | (nextInt() & 0xFFFFFFFFL);
    }

    @Override
    public boolean nextBoolean() {
        return RandomHelper.nextDouble() < 0.5;
    }

    @Override
    public float nextFloat() {
        return (float) RandomHelper.nextDouble();
    }

    @Override
    public double nextDouble() {
        return RandomHelper.nextDouble();
    }

    @Override
    public double nextGaussian() {
        return RandomHelper.createNormal(0.0, 1.0).nextDouble();
    }
}
