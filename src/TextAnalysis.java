import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextAnalysis {
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        String filepath = getFilepathFromUser();
        String fullText = getContentsOfFile(filepath);
        String[] tics = getTicsFromUser();

        System.out.println();
        System.out.println("...............................Analyzing text.................................");
        System.out.println();

        int totalTics = totalTicCount(tics, fullText);
        System.out.println("Total number of tics: " + totalTics);

        double density = calculateTicDensity(tics, fullText);
        
        System.out.println("Density of tics: " + String.format("%.2f", density) );

        System.out.println();
        System.out.println("...............................Tic breakdown..................................");
        System.out.println();


        for (String tic : tics) {
            int count = countOccurrences(tic, fullText);
            int percentage = calculatePercentage(count, totalTics);
            
            // Use String.format to align output
            System.out.printf("%-12s / %-18d / %d%% of all tics%n", tic, count, percentage);
        }
        
    }

    public static String getFilepathFromUser() {
        System.out.print("What file would you like to open?");
        System.out.println();
        return scanner.nextLine().trim();
    }


    public static String getContentsOfFile(String filepath) {
        String fullText = "";
        try {
            Scanner scn = new Scanner(new File(filepath));
            while (scn.hasNextLine()) {
                String line = scn.nextLine();
                fullText += line + "\n"; // nextLine() removes line breaks, so add them back in
            }
            scn.close(); // be nice and close the Scanner
        } catch (FileNotFoundException e) {
            System.out.println("Oh no... can't find the file!");
        }
        return fullText.toLowerCase(); 
    }


    public static String[] getTicsFromUser() {
        System.out.print("What words would you like to look for?");
        System.out.println();
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return new String[0]; // return an empty array if no input
        }
        String[] tics = input.split("[\\s.,?!]+"); // split by commas and trim whitespace
       
        List<String> nonEmptyTics = new ArrayList<>();
        for (String word : tics) {
            if (!word.isEmpty() && word.matches(".*[a-zA-Z]+.*")) { // Add only non-empty words
                nonEmptyTics.add(word);
            }
        }
    
        // Convert the ArrayList back to a String array
        return nonEmptyTics.toArray(new String[0]);
    }

    public static int countOccurrences(String needle, String haystack) {
        if (haystack == null || haystack.isEmpty() || needle == null || needle.isEmpty()) {
            return 0; // return 0 for null or empty inputs
        }

        int count = 0;
        String lowerHaystack = haystack.toLowerCase();
        String lowerNeedle = needle.toLowerCase();

        //String regex = "\\b" + Pattern.quote(lowerNeedle) + "\\b";
        String regex = "(?<=\\s|\\(|\\.|,|\\?|!|^)\\Q" + lowerNeedle + "\\E(?=\\s|\\)|\\.|,|\\?|!|$)";
 // use word boundaries to ensure whole words
    Matcher matcher = Pattern.compile(regex).matcher(lowerHaystack);

    while (matcher.find()) {
        count++;
    }

    return count;
    }

    public static int calculatePercentage(double num1, double num2) {
        if (num2 == 0) {
            return 0; // avoid division by zero
        }
        return (int) Math.round((num1 / num2) * 100);
    }

    public static double calculateTicDensity(String[] tics, String fullText) {
        if (fullText == null || fullText.isEmpty()) {
            System.out.println("warning: the text is empty! return 0.0");
            return 0.0;
        }

        String[] words = fullText.split("[\\p{Punct}\\s]+");
         // split the fullText by non-word characters
        int totalWords = words.length;

        int ticCount = totalTicCount(tics, fullText);
        return (double) ticCount / totalWords;
    }


    public static int totalTicCount(String[] tics, String fullText) {
        int totalCount = 0;
        List<String> uniqueTics = new ArrayList<>();
    
        for (String tic : tics) {
            boolean isDuplicate = false;
            boolean isPunctuation = !tic.matches("^[a-zA-Z]+(?:'[a-zA-Z]+)?$"); // True if tic has no alphabetic characters
    
            // Check if tic is already in uniqueTics
            for (String uniqueTic : uniqueTics) {
                if (tic.equalsIgnoreCase(uniqueTic)) {
                    isDuplicate = true;
                    break;
                }
            }
    
            // Add to uniqueTics only if it's not a duplicate and is not just punctuation
            if (!isDuplicate && !isPunctuation) {
                uniqueTics.add(tic);
            }
        }
    
        // Count occurrences for each unique tic in the fullText
        for (String tic : uniqueTics) {
            totalCount += countOccurrences(tic, fullText);
        }
    
        return totalCount;
    }
}
    
    