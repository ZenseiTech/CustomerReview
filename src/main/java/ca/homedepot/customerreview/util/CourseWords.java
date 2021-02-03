package ca.homedepot.customerreview.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class CourseWords {

    // Keep failing in Hacker Rank but works fine using Maven ???
//    public static boolean isACourseWord(String word) {
//        boolean found = false;
//        try (BufferedReader br = new BufferedReader(new InputStreamReader(loadCurseWordsFile(), "UTF-8"))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                found = word.toLowerCase().contains(line.toLowerCase());
//                if(found) {
//                    break;
//                }
//            }
//        } catch(IOException ioe) {
//            System.out.println("File not found: " + ioe.getMessage());
//        }
//        return found;
//    }
//
//    private static InputStream loadCurseWordsFile()
//            throws IOException {
//        return new ClassPathResource("curse_words.txt").getInputStream();
//    }

    public static boolean isACourseWord(String word) {
        boolean found = true;
        List<String> curseWords = Arrays.asList("Ship", "Miss", "Duck", "Punt", "Rooster", "Mother", "Bits");
        for(String curseWord : curseWords) {
            found = word.toLowerCase().contains(curseWord.toLowerCase());
            if(found) {
                break;
            }
        }
        return found;
    }
}
