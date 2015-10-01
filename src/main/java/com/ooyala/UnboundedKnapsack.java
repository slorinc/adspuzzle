package com.ooyala;

import java.util.Arrays;

/**
 * Created by s_lor_000 on 9/30/2015.
 */
public class UnboundedKnapsack {

    private final int maxWeight;
    private final int[] weights;
    private final int[] values;

    private int[] tableValues;
    private int[] tableItems;

    private int maxRevenue;
    private int[] resultCombination;

    public UnboundedKnapsack(final int maxWeight, final int[] weights, final int[] values) {
        this.maxWeight = maxWeight;
        this.weights = weights;
        this.values = values;
        solve();
    }

    private int fillUnboundedKnapsack() {

        tableValues = new int[maxWeight + 1];
        tableItems = new int[maxWeight + 1];
        Arrays.fill(tableValues, 0);
        Arrays.fill(tableItems, -1);


        for (int i = 1; i <= maxWeight; i++) {
            for (int j = 0; j < weights.length; j++) {
                if (weights[j] <= i
                        && (values[j] + tableValues[i - weights[j]]) > tableValues[i]) {
                    tableValues[i] = values[j] + tableValues[i - weights[j]];
                    tableItems[i] = j;
                }
            }
        }

        return tableValues[tableValues.length - 1];
    }


    private int[] trackCombination() {
        int[] combination = new int[weights.length];
        int weightTracker = maxWeight;
        int itemTracker = tableItems[weightTracker];


        while (itemTracker != -1 && weightTracker > 0) {
            combination[itemTracker]++;
            weightTracker = weightTracker - weights[itemTracker];
            itemTracker = tableItems[weightTracker];
        }

        return combination;
    }


    private void solve() {
        this.maxRevenue = fillUnboundedKnapsack();
        this.resultCombination = trackCombination();
    }


    public int getMaxRevenue() {
        return maxRevenue;
    }


    public int[] getResultCombination() {
        return resultCombination;
    }
}
