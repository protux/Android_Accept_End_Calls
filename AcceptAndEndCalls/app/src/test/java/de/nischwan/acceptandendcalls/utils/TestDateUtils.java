package de.nischwan.acceptandendcalls.utils;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Nico Schwanebeck
 */
public class TestDateUtils {

    @Test
    public void calculatedOffsetValuesInRange() {
        int probes[] = new int[100];
        int offsetTime = 5;
        int offsetInSeconds = 300;
        for (int i = 0; i < probes.length; i++) {
            probes[i] = DateUtils.calculateTimeOffsetInSeconds(offsetTime);
        }

        for (int probe : probes) {
            Assert.assertTrue(probe >= offsetInSeconds * -1);
            Assert.assertTrue(probe <= offsetInSeconds);
        }
    }

    @Test
    public void calculatedOffsetValuesContainsPositive() {
        int probes[] = new int[100];
        int offsetTime = 5;
        for (int i = 0; i < probes.length; i++) {
            probes[i] = DateUtils.calculateTimeOffsetInSeconds(offsetTime);
        }

        boolean containsPositive = false;
        for (int probe : probes) {
            if (probe > 0) {
                containsPositive = true;
            }
        }
        Assert.assertTrue(containsPositive);
    }

    @Test
    public void calculatedOffsetValuesContainsNegative() {
        int probes[] = new int[100];
        int offsetTime = 5;
        for (int i = 0; i < probes.length; i++) {
            probes[i] = DateUtils.calculateTimeOffsetInSeconds(offsetTime);
        }

        boolean containsNegative = false;
        for (int probe : probes) {
            if (probe < 0) {
                containsNegative = true;
            }
        }
        Assert.assertTrue(containsNegative);
    }
}
