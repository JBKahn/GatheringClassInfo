/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datagatherer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Boggy
 */
public class DataGatherer {

    /**
     * @param args the command line arguments
     */
    public static void Connect() throws Exception{
    URL courselist = new URL("http://www.artsandscience.utoronto.ca/ofr/calendar/crs_csc.htm");
    URLConnection spoof = courselist.openConnection();
 
    //Spoof the connection so we look like a web browser
    spoof.setRequestProperty( "User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)" );
    BufferedReader in = new BufferedReader(new InputStreamReader(spoof.getInputStream()));
    String strLine = "";
    String current_class = "";
    ArrayList Classes = new ArrayList();
    while ((strLine = in.readLine()) != null){
        Pattern isAClass = Pattern.compile("<a name=\"[A-Z]{3}[0-9]{3}[HY][01]");
        Matcher isAClassm = isAClass.matcher(strLine);
        if (isAClassm.lookingAt()) {
            current_class = strLine.substring(44);
            strLine = in.readLine();
            isAClass = Pattern.compile("<br>");
            isAClassm = isAClass.matcher(strLine);
            while (strLine != null && isAClassm.reset(strLine).matches() == false) {
                current_class += strLine;
                strLine = in.readLine();
                }
            String FormattedCourse = Format(current_class);
            System.out.println(FormattedCourse);
            //System.out.println(current_class);
            } 
        }
    }
    public static String Format(String toFormat) throws Exception {
        //Getting the course code
        String CourseCode = toFormat.substring(0, 8);
        //Getting the name of the course
        Pattern name = Pattern.compile("[0-9A-Z]+(&nbsp;)+ ?(([a-zA-Z0-9 :,&\\(\\);-]+))([^<]+)");
        Matcher namem = name.matcher(toFormat);
        namem.find();
        String Name = namem.group(3);
        //Getting rid of links.
        Pattern links = Pattern.compile("(<a href=[^#]+#(.{8})[^<]+</a>)");
        Matcher linksm = links.matcher(toFormat);
        if (linksm.find()){
            while (linksm.find()){
            linksm.replaceFirst(linksm.group(1));
            toFormat = toFormat.substring(0, linksm.start()) + linksm.group(2) + toFormat.substring(linksm.end());
            linksm.reset(toFormat);
                }
            }
        //Getting bracketthingy of the course
        String BracketThing = namem.group(4);
        //Getting description of the course
        Pattern desc = Pattern.compile("</span><p>");
        Pattern descend = Pattern.compile("</p>");
        Matcher descm = desc.matcher(toFormat);
        Matcher descendm = descend.matcher(toFormat);
        descm.find();
        descendm.find();
        String Description = "Description: " + toFormat.substring(descm.end(), descendm.start());
        //System.out.println(Description);
        //Removing links from the description
        links = Pattern.compile("(<a href=\"([^\"]+)\">([^<]+)</a>)");
        linksm = links.matcher(Description);
        if(linksm.find()){
            linksm.replaceFirst(linksm.group(3) + "(" + linksm.group(2) + ")");
            Description = Description.substring(0, linksm.start()) + linksm.group(3) + "(" + linksm.group(2) + ")" + Description.substring(linksm.end());
            }
        //Removing html from the description
        Pattern brac = Pattern.compile("(<[^>]+>)|(&nbsp;)");
        Matcher bracm = brac.matcher(Description);
        if (bracm.find()){
            Description = Description.substring(0, bracm.start()) + Description.substring(bracm.end());
            bracm.reset(Description);
            while (bracm.find()){
                Description = Description.substring(0, bracm.start()) + Description.substring(bracm.end());
                //System.out.println(Description.substring(0, bracm.start()));
                bracm.reset(Description);
                }
            }
        //Prerequisites
        Pattern prereq = Pattern.compile("Prerequisite: +");
        Matcher prereqm = prereq.matcher(toFormat);
        Pattern prereqend = Pattern.compile("<br>");
        Matcher prereqendm = prereqend.matcher(toFormat);
        String Prerequisites;
        if (prereqm.find() && prereqendm.find(prereqm.end())){
            prereqendm.find(prereqm.end());
            Prerequisites = "Prerequisites: " + toFormat.substring(prereqm.end(), prereqendm.start());
            } else {
            Prerequisites = "Prerequisites: None";
            }
        //System.out.println(Prerequisites);
        //Corequisites
        Pattern coreq = Pattern.compile("Corequisite: +");
        Matcher coreqm = coreq.matcher(toFormat);
        Pattern coreqend = Pattern.compile("<br>");
        Matcher coreqendm = coreqend.matcher(toFormat);
        String Corequisites;
        if (coreqm.find() && coreqendm.find(coreqm.end())){
            coreqendm.find(coreqm.end());
            Corequisites = "Corequisites: " + toFormat.substring(coreqm.end(), coreqendm.start());
            } else {
            Corequisites = "Corequisites: None";
            }
        //System.out.println(Corequisites);
        //Exclusions
        Pattern excl = Pattern.compile("Exclusion: +");
        Matcher exclm = excl.matcher(toFormat);
        Pattern exclend = Pattern.compile("<br>");
        Matcher exclendm = exclend.matcher(toFormat);
        String Exclusions;
        if (exclm.find() && exclendm.find(exclm.end())){
            exclendm.find(exclm.end());
            Exclusions = "Exclusions: " + toFormat.substring(exclm.end(), exclendm.start());
            } else {
            Exclusions = "Exclusions: None";
            }
        //System.out.println(Exclusions);
        //Recommended Preparation
        Pattern rec = Pattern.compile("Recommended Preparation: +");
        Matcher recm = rec.matcher(toFormat);
        Pattern recend = Pattern.compile("<br>");
        Matcher recendm = recend.matcher(toFormat);
        String ReccomendedCourses;
        if (recm.find() && recendm.find(recm.end())){
            exclendm.find(recm.end());
            ReccomendedCourses = "Recommended Preparation: " + toFormat.substring(recm.end(), recendm.start());
            } else {
            ReccomendedCourses = "Recommended Preparation: None";
            }
        //System.out.println(ReccomendedCourses);
        //Distribution Requirement Status
        Pattern dist = Pattern.compile("Distribution Requirement Status: +");
        Matcher distm = dist.matcher(toFormat);
        Pattern distend = Pattern.compile("<br>");
        Matcher distendm = distend.matcher(toFormat);
        String DistributionRequirementStatus;
        if (distm.find() && distendm.find(distm.end())){
            exclendm.find(distm.end());
            DistributionRequirementStatus = "Distribution Requirement Status: " + toFormat.substring(distm.end(), distendm.start());
            } else {
            DistributionRequirementStatus = "Distribution Requirement Status: None";
            }
        //System.out.println(DistributionRequirementStatus);
        //Breadth Requirement
        Pattern bred = Pattern.compile("Breadth Requirement: +");
        Matcher bredm = bred.matcher(toFormat);
        Pattern bredend = Pattern.compile("<br>");
        Matcher bredendm = bredend.matcher(toFormat);
        String BreadthRequirement;
        if (bredm.find() && bredendm.find(bredm.end())){
            exclendm.find(bredm.end());
            BreadthRequirement = "Breadth Requirement: " + toFormat.substring(bredm.end(), bredendm.start());
            } else {
            BreadthRequirement = "Breadth Requirement: None";
            }
        //System.out.println(BreadthRequirement);
        
        String course = CourseCode + " " + Name + " " + BracketThing + "\n" 
                + Description + "\n"
                + Prerequisites + "\n"
                + Corequisites + "\n"
                + Exclusions + "\n"
                + ReccomendedCourses + "\n"
                + DistributionRequirementStatus + "\n"
                + BreadthRequirement + "\n";
        
        //Getting rid of links.
        links = Pattern.compile("(<a href=[^>]+>(.{8})</a>)");
        //<a href="crs_eeb.htm#BIO120H1">BIO120H1</a>
        linksm = links.matcher(course);
        if (linksm.find()){
            course = course.substring(0, linksm.start()) + linksm.group(2) + course.substring(linksm.end());
            linksm.reset(course);
            while (linksm.find()){
            linksm.replaceFirst(linksm.group(1));
            course = course.substring(0, linksm.start()) + linksm.group(2) + course.substring(linksm.end());
            linksm.reset(course);
                }
            }
        
        return course;
    }
    public static void main(String [] args) throws Exception {
        DataGatherer.Connect();
        TimetableGrabber.Connect();
        String arguments[] = new String[2];
        arguments[0]= "CS.pdf";
        arguments[1]= "CS.txt";
        String antical = PDFTextParser.Connect(arguments);
        AntiCalSort.Connect(antical);
        
    }
}
