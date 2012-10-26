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
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Boggy
 */
public class TimetableGrabber {
    
    public static void Connect() throws Exception{
    URL courselist = new URL("http://www.artsandscience.utoronto.ca/ofr/timetable/winter/csc.html");
    URLConnection spoof = courselist.openConnection();
 
    //Spoof the connection so we look like a web browser
    spoof.setRequestProperty( "User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)" );
    BufferedReader in = new BufferedReader(new InputStreamReader(spoof.getInputStream()));
    String strLine = "";
    ArrayList TableCol = new ArrayList();
    String CurrentRow = "";
    Pattern item = Pattern.compile(">([^<]+)<");
    Matcher itemm;
    List<String> WholeTable = new ArrayList();
    while ((strLine = in.readLine()) != null){
        if (strLine.startsWith("<TD VALIGN=")) {
            itemm = item.matcher(strLine);
            //System.out.println(itemm.find());
            if (itemm.find()) {
                TableCol.add(itemm.group(1));
                }
            }
        if (strLine.startsWith("<tr><td align=LEFT>")) {
            CurrentRow = strLine;
            while (!strLine.contains("</tr>")) {
                strLine = in.readLine();
                CurrentRow += strLine.trim();
                }
            //Put into comma seperarated string
            String FormattedRow = Format(CurrentRow);
            //System.out.println(FormattedRow);
            //Put into an array
            List<String> SplitRowData = new ArrayList(Arrays.asList(FormattedRow.split(" ,")));
            
            for(int i=0;i<SplitRowData.size();i++) {
                WholeTable.add(SplitRowData.get(i));
                }
            }
        }
        //System.out.println(WholeTable.size());
        for(int i=0;i<WholeTable.size();i++) {
            if (i%10 < 4) {
                if ((WholeTable.get(i)).equals("&nbsp;")) {
                    WholeTable.set(i, WholeTable.get(i-10));
                    }
            } else if (i%10 < 9) {
                if ((WholeTable.get(i)).equals("&nbsp;")) {
                    WholeTable.set(i, "n/a");
                    }
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            //} else if (i%10 == 7) {
            //Unsure if professor is supposed to be copied down the table. 
            //Assuming not.
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            } else if (i%10 == 9) {
                if ((WholeTable.get(i)).compareTo("&nbsp;") == 1 && (WholeTable.get(i-6)).startsWith("L")) {
                    WholeTable.set(i, WholeTable.get(i-10));
                    } else {
                    WholeTable.set(i, "n/a");
                    }
            }
        }
        for(int i=0;WholeTable.size() - i > 10;i+=10) {
                String S = (WholeTable.get(i) + "    " +
                        WholeTable.get(i+1) + "    " +
                        WholeTable.get(i+2) + "    " +
                        WholeTable.get(i+3) + "    " +
                        WholeTable.get(i+4) + "    " +
                        WholeTable.get(i+5) + "    " +
                        WholeTable.get(i+6) + "    " +
                        WholeTable.get(i+7) + "    " +
                        WholeTable.get(i+8) + "    " +
                        WholeTable.get(i+9));
                System.out.println(S);
                }
    }
    public static String Format(String RawRow) throws Exception {
        //Seperating the relevant information into one string.
        Pattern item = Pattern.compile(">([^<]+)<");
        Matcher itemm = item.matcher(RawRow);
        String RowData = "";
        while (itemm.find()) {
            RowData += itemm.group(1) + " ,";
            }
        //Getting rid of the extra comma
        RowData = RowData.substring(0, RowData.length()-2);
        //Making sure there are extra 10 CSVs
        if (!RowData.matches("([^,]+,){9}([^,]+)")) {
            if (RowData.matches("([^,]+,){8}([^,]+)")) {
                RowData += " ,&nbsp;";
                }
            if (RowData.matches("([^,]+,){10}([^,]+)")) {
                item = Pattern.compile("([^,]+,[^,]+,[^,]+,)([^,]+,)");
                itemm = item.matcher(RowData);
                itemm.find();
                RowData = RowData.substring(0,itemm.group(1).length()-1) + RowData.substring(itemm.group(1).length());
                System.out.println(RowData);
                System.out.println(RowData.matches("([^,]+,){9}([^,]+)"));
                }
            }
        return RowData;
        }
     
        
    
    public static void main(String [] args) throws Exception {
        DataGatherer.Connect();
        }
    
}
