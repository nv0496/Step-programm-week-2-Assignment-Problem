import java.util.*;

public class TextCompressor {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter text to compress:");
        String text = sc.nextLine();
        Object[] freqData = countFrequency(text);
        char[] chars = (char[]) freqData[0];
        int[] freqs = (int[]) freqData[1];
        String[][] mapping = createCodes(chars, freqs);
        String compressed = compress(text, mapping);
        String decompressed = decompress(compressed, mapping);
        displayAnalysis(text, compressed, decompressed, chars, freqs, mapping);
    }

    public static Object[] countFrequency(String text) {
        char[] chars = new char[text.length()];
        int[] freqs = new int[text.length()];
        int unique = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            int idx = -1;
            for (int j = 0; j < unique; j++) if (chars[j] == c) { idx = j; break; }
            if (idx == -1) {
                chars[unique] = c;
                freqs[unique] = 1;
                unique++;
            } else freqs[idx]++;
        }
        return new Object[]{Arrays.copyOf(chars, unique), Arrays.copyOf(freqs, unique)};
    }

    public static String[][] createCodes(char[] chars, int[] freqs) {
        Integer[] idx = new Integer[chars.length];
        for (int i = 0; i < chars.length; i++) idx[i] = i;
        Arrays.sort(idx, (a, b) -> freqs[b] - freqs[a]);
        String[][] mapping = new String[chars.length][2];
        for (int i = 0; i < chars.length; i++) {
            mapping[i][0] = String.valueOf(chars[idx[i]]);
            mapping[i][1] = Integer.toString(i, 36);
        }
        return mapping;
    }

    public static String compress(String text, String[][] mapping) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            String c = String.valueOf(text.charAt(i));
            for (String[] map : mapping) if (map[0].equals(c)) { sb.append(map[1]); break; }
        }
        return sb.toString();
    }

    public static String decompress(String compressed, String[][] mapping) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < compressed.length()) {
            String token = String.valueOf(compressed.charAt(i));
            boolean found = false;
            for (String[] map : mapping) {
                if (map[1].equals(token)) {
                    sb.append(map[0]);
                    found = true;
                    break;
                }
            }
            i++;
        }
        return sb.toString();
    }

    public static void displayAnalysis(String original, String compressed, String decompressed, char[] chars, int[] freqs, String[][] mapping) {
        System.out.println("Character Frequency:");
        for (int i = 0; i < chars.length; i++) System.out.println(chars[i] + " : " + freqs[i]);
        System.out.println("\nMapping Table:");
        for (String[] map : mapping) System.out.println(map[0] + " -> " + map[1]);
        System.out.println("\nOriginal Text: " + original);
        System.out.println("Compressed Text: " + compressed);
        System.out.println("Decompressed Text: " + decompressed);
        double ratio = (double) compressed.length() / original.length() * 100;
        System.out.println("Compression Efficiency: " + String.format("%.2f", 100 - ratio) + "%");
    }
}
