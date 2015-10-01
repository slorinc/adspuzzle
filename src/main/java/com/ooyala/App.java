package com.ooyala;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class App {
    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("Please provide a filename!");
            return;
        }

        FileReader fileReader = null;
        CSVParser csvParser = null;
        List<Campaign> campaigns = new LinkedList<>();

        // set header format for convenience
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(new String[]{"customer", "impPerCampaign", "price"});

        try {
            fileReader = new FileReader(args[0]);
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
            return;
        }
        try {
            csvParser = new CSVParser(fileReader, csvFileFormat);
        } catch (IOException e) {
            System.err.println("Parsing error!");
            return;
        }
        List<CSVRecord> csvRecords;

        // read csv
        try {
            csvRecords = csvParser.getRecords();
        } catch (IOException e) {
            System.err.println("Parsing error!");
            return;
        } finally {
            try {
                fileReader.close();
                csvParser.close();
            } catch (IOException e) {
                System.err.println("Error closing the file!");
                return;
            }
        }

        // get "header" record -> totalImpressions
        int totalImpressions = new Integer(csvRecords.get(0).get(0));

        // create list containing campaigns -> remove items with zero values and header item
        csvRecords.stream().filter(a -> a.isConsistent()).filter(a -> new Integer(a.get("price")) > 0).forEach((a) -> {
            campaigns.add(new Campaign(a.get("customer"), new Integer(a.get("impPerCampaign")), new Integer(a.get("price"))));
        });

        // create arrays to store weights and values
        int[] values = new int[campaigns.size()];
        int[] weights = new int[campaigns.size()];

        for (int i = 0; i < campaigns.size(); i++) {
            values[i] = campaigns.get(i).getPrice();
            weights[i] = campaigns.get(i).getImpressions();
        }

        // get gcd
        int gcd = getGCD(totalImpressions, weights);

        // reduce size of necessary tables by dividing totalImpressions and weights with gcd
        if (gcd > 1) {
            totalImpressions /= gcd;
            for (int i = 0; i < weights.length; i++) {
                weights[i] /= gcd;
            }
        }

        // calculate
        UnboundedKnapsack unboundedKnapsack = new UnboundedKnapsack(totalImpressions, weights, values);

        // print result
        int maxRevenue = unboundedKnapsack.getMaxRevenue();
        int[] outputCombination = unboundedKnapsack.getResultCombination();


        for (int i = 0; i < outputCombination.length; i++) {
            Campaign campaign = campaigns.get(i);
            System.out.format("%s,%d,%d,%d\n", campaign.getCustomer(), outputCombination[i], campaign.getImpressions() * outputCombination[i], outputCombination[i] * campaign.getPrice());
        }

        if (gcd < 1) {
            gcd = 1;
        }

        System.out.format("%d,%d", totalImpressions * gcd, maxRevenue);

    }

    private static int getGCD(int totalImpressions, int[] weights) {
        int smallest = totalImpressions;
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] < smallest) {
                smallest = weights[i];
            }
        }

        while (smallest > 1) {

            int counter = 0;
            int totalModus = totalImpressions % smallest;

            while (counter < weights.length) {

                totalModus += weights[counter] % smallest;
                counter++;

            }

            if (totalModus == 0) {
                return smallest;
            }

            smallest--;

        }
        return -1;
    }

}
