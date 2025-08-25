import java.util.*;
import java.text.SimpleDateFormat;

public class FileOrganizer {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<String> files = new ArrayList<>();
        while (true) {
            String line = sc.nextLine();
            if (line.trim().isEmpty()) break;
            files.add(line.trim());
        }
        List<FileInfo> fileInfos = new ArrayList<>();
        for (String f : files) fileInfos.add(extractFileComponents(f));
        Map<String, String> categories = categorizeFiles(fileInfos);
        Map<String, Integer> counts = countCategories(categories);
        Map<String, String> newNames = generateNewNames(fileInfos, categories);
        Map<String, String> subcategories = analyzeContent(fileInfos, categories);
        displayReport(fileInfos, categories, newNames, counts, subcategories);
        generateBatchRename(fileInfos, newNames);
    }

    static class FileInfo {
        String original;
        String name;
        String extension;
        FileInfo(String o, String n, String e) { original = o; name = n; extension = e; }
    }

    static FileInfo extractFileComponents(String file) {
        int idx = file.lastIndexOf('.');
        if (idx == -1 || idx == file.length() - 1) return new FileInfo(file, file, "");
        String name = file.substring(0, idx);
        String ext = file.substring(idx).toLowerCase();
        return new FileInfo(file, name, ext);
    }

    static Map<String, String> categorizeFiles(List<FileInfo> files) {
        Map<String, String> map = new HashMap<>();
        for (FileInfo f : files) {
            String cat = "Unknown";
            if (f.extension.equals(".txt") || f.extension.equals(".doc")) cat = "Document";
            else if (f.extension.equals(".jpg") || f.extension.equals(".png")) cat = "Image";
            else if (f.extension.equals(".mp3") || f.extension.equals(".wav")) cat = "Audio";
            else if (f.extension.equals(".mp4") || f.extension.equals(".avi")) cat = "Video";
            else if (f.extension.equals(".java") || f.extension.equals(".py")) cat = "Code";
            map.put(f.original, cat);
        }
        return map;
    }

    static Map<String, Integer> countCategories(Map<String, String> categories) {
        Map<String, Integer> counts = new HashMap<>();
        for (String cat : categories.values()) counts.put(cat, counts.getOrDefault(cat, 0) + 1);
        return counts;
    }

    static Map<String, String> generateNewNames(List<FileInfo> files, Map<String, String> categories) {
        Map<String, String> newNames = new HashMap<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String date = df.format(new Date());
        Map<String, Integer> dupCount = new HashMap<>();
        for (FileInfo f : files) {
            String cat = categories.get(f.original);
            String base = cat + "_" + date;
            int num = dupCount.getOrDefault(base, 0) + 1;
            dupCount.put(base, num);
            StringBuilder sb = new StringBuilder();
            sb.append(base);
            if (num > 1) sb.append("_").append(num);
            sb.append(f.extension);
            newNames.put(f.original, sb.toString());
        }
        return newNames;
    }

    static Map<String, String> analyzeContent(List<FileInfo> files, Map<String, String> categories) {
        Map<String, String> subs = new HashMap<>();
        for (FileInfo f : files) {
            String cat = categories.get(f.original);
            if (cat.equals("Document") && f.name.toLowerCase().contains("resume")) subs.put(f.original, "Resume");
            else if (cat.equals("Document") && f.name.toLowerCase().contains("report")) subs.put(f.original, "Report");
            else if (cat.equals("Code")) subs.put(f.original, "SourceCode");
            else subs.put(f.original, "General");
        }
        return subs;
    }

    static void displayReport(List<FileInfo> files, Map<String, String> categories,
                              Map<String, String> newNames, Map<String, Integer> counts,
                              Map<String, String> subs) {
        System.out.println("+----------------------+------------+----------------------+---------------+");
        System.out.println("| Original Filename    | Category   | New Name             | Subcategory   |");
        System.out.println("+----------------------+------------+----------------------+---------------+");
        for (FileInfo f : files) {
            String o = f.original;
            String c = categories.get(o);
            String n = newNames.get(o);
            String s = subs.get(o);
            System.out.printf("| %-20s | %-10s | %-20s | %-13s |\n", o, c, n, s);
        }
        System.out.println("+----------------------+------------+----------------------+---------------+");
        System.out.println("\nCategory Counts:");
        for (String cat : counts.keySet()) System.out.println(cat + ": " + counts.get(cat));
    }

    static void generateBatchRename(List<FileInfo> files, Map<String, String> newNames) {
        System.out.println("\nBatch Rename Commands:");
        for (FileInfo f : files) {
            System.out.println("rename \"" + f.original + "\" \"" + newNames.get(f.original) + "\"");
        }
    }
}
d