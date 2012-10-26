/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datagatherer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Boggy
 */

public class AntiCalSort {
    public static void Connect(String unsorted) {
    
    String courseCode = "";
    String instructor = "";
    String retake = "";
    String presents = "";
    String explains = "";
    String communicates = "";
    String teaching = "";
    String workload = "";
    String difficulty = "";
    String learnExp = "";
    String review;

    //Remove the page numbers
    
    Pattern brac = Pattern.compile("(ASSU ANTI-CALENDAR     [0-9]{2}|[0-9]{2}     COMPUTER SCIENCE)");
    Matcher bracm = brac.matcher(unsorted);
    if (bracm.find()){
        unsorted = unsorted.substring(0, bracm.start()) + unsorted.substring(bracm.end());
        bracm.reset(unsorted);
        while (bracm.find()){
            unsorted = unsorted.substring(0, bracm.start()-2) + unsorted.substring(bracm.end());
            //System.out.println(Description.substring(0, bracm.start()));
            bracm.reset(unsorted);
            }
        }
    brac = Pattern.compile("DIASPORA STUDIES COURSE");
    bracm = brac.matcher(unsorted);
    if (bracm.find()){
        unsorted = unsorted.substring(0, bracm.start());
        }
    brac = Pattern.compile("\\n \\n");
    bracm = brac.matcher(unsorted);
    if (bracm.find()){
        bracm.find();
        System.out.println(unsorted.substring(0,bracm.start()));
        }
    
   //System.out.println(unsorted);
   String[] splitInLines = unsorted.split("\n");
    
    
    
    
    int i = 0;
    while (i < splitInLines.length){
        String currentLine = splitInLines[i];
        if (currentLine.startsWith("CSC ")) {
            courseCode = currentLine.substring(0, 10);
            i++;
            currentLine = splitInLines[i];
        }
        if (courseCode.startsWith("CSC ")) {
            while (!(currentLine.startsWith("Inst"))){
                i++;
                currentLine = splitInLines[i];
                }
            instructor = currentLine.substring(16);
            i++;
            currentLine = splitInLines[i];
            int retakeNum = currentLine.indexOf("Retake:");
            retake = currentLine.substring(retakeNum + 8);
            i = i + 2;
            presents = splitInLines[i].substring(splitInLines[i].length()-4);
            explains = splitInLines[i + 1].substring(splitInLines[i + 1].length()-4);
            communicates = splitInLines[i + 2].substring(splitInLines[i + 2].length()-4);
            teaching = splitInLines[i + 3].substring(splitInLines[i + 3].length()-4);
            workload = splitInLines[i + 4].substring(splitInLines[i + 4].length()-4);
            difficulty = splitInLines[i + 5].substring(splitInLines[i + 5].length()-4);
            learnExp = splitInLines[i + 6].substring(splitInLines[i + 6].length()-4);
            i = i + 7;
            review = "";
            if (i < splitInLines.length) {
                currentLine = splitInLines[i];
                while (!(currentLine.startsWith("CSC ") | currentLine.startsWith("Instructor(s):")) && i < splitInLines.length){
                    if (currentLine.contains("-")) {
                        currentLine = currentLine.substring(0,currentLine.length()-1);
                    }
                    review += currentLine.substring(0, currentLine.length() - 2);
                    i++;
                    currentLine = splitInLines[i];
                }
            }
            System.out.println("Course Code: " + courseCode);
            System.out.print("Instructor: " + instructor);
            System.out.print("Retake Rate: " + retake);
            System.out.print("Presents: " + presents);
            System.out.print("Explains: " + explains);
            System.out.print("Communicates: " + communicates);
            System.out.print("Teaching: " + teaching);
            System.out.print("Workload: " + workload);
            System.out.print("Difficulty: " + difficulty);
            System.out.print("Learning Experience: " + learnExp);
            System.out.print("Review: " + review);
            System.out.println();
    } else {
        i++;
        }
    }
    
}
}
