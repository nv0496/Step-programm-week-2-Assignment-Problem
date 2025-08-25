import java.util.*;

public class SpellChecker {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter dictionary words (comma separated):");
        String[] dict = sc.nextLine().toLowerCase().split(",");
        for (int i = 0; i < dict.length; i++) dict[i] = dict[i].trim();
        System.out.println("Enter a sentence:");
        String sentence = sc.nextLine().toLowerCase();

        List<String> words = extractWords(sentence);
        System.out.printf("%-15s %-15s %-10s %-15s%n", "Word", "Suggestion", "Distance", "Status");
        for (String word : words) {
            String suggestion = findClosest(word, dict);
            int dist = suggestion.equals(word) ? 0 : stringDistance(word, suggestion);
            String status = suggestion.equals(word) ? "Correct" : (dist <= 2 ? "Misspelled" : "Unknown");
            System.out.printf("%-15s %-15s %-10d %-15s%n", word, suggestion, dist, status);
        }
    }

    public static List<String> extractWords(String text) {
        List<String> words = new ArrayList<>();
        int start = -1;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetter(c)) {
                if (start == -1) start = i;
            } else {
                if (start != -1) {
                    words.add(text.substring(start, i));
                    start = -1;
                }
            }
        }
        if (start != -1) words.add(text.substring(start));
        return words;
    }

    public static int stringDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= b.length(); j++) dp[0][j] = j;
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1))
                    dp[i][j] = dp[i - 1][j - 1];
                else
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1],
                                   Math.min(dp[i - 1][j], dp[i][j - 1]));
            }
        }
        return dp[a.length()][b.length()];
    }

    public static String findClosest(String word, String[] dict) {
        int minDist = Integer.MAX_VALUE;
        String best = word;
        for (String d : dict) {
            int dist = stringDistance(word, d);
            if (dist < minDist) {
                minDist = dist;
                best = d;
            }
        }
        return best;
    }
}
