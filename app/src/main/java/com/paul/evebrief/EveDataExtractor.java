package com.paul.evebrief;

import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * This class is used to extract data stored in an online google SpreadSheed file and then convert them into
 * Briefs that can then be later loaded into other components.
 *
 *
 * @author Paul Dines
 *
 */
public class EveDataExtractor {
    /**
     * This extracts the EveBrief data from an online google SpreadSheed Document that are then converted into
     * Brief Objects
     *
     * @return ArrayList<Brief>
     * @throws java.net.MalformedURLException
     * @throws java.io.IOException
     * @throws ServiceException
     */

    //These keys are used to identify the spread sheet where the sheet is stored
    private static String EVE_BRIEF_KEY = "1ffdJWvEaNAWuURYuLvzGep2Esl52O-A_YQgWwMXzbJ4";
    private static String INV_BRIEF_KEY = "1R-eV0VViIHC1VyNf8ax1DBTZGPAqk7RrNEyjEaURJ7s";
    private static String ENGAGE_KEY = "14Zu28ZeLOCLnPcIpik4k893IRujPJH3pYQLjPXtLIH4";
    private static String RATING_NEWS_UPDATE_KEY = "1jGybpd7leHiHU2ZhOzFFPFubDWe8q-fHiKC4mPkRaOg";

    public static ArrayList<Brief> extractEveBriefData(String briefType) throws MalformedURLException, IOException, ServiceException {
        //create a feed for the Worksheet
        SpreadsheetService service = new SpreadsheetService("Test");
        FeedURLFactory fact = FeedURLFactory.getDefault();
        URL spreadSheetUrl;
        //access the correct data store based on the brief type being displayed
              if(briefType.equalsIgnoreCase("EveBrief")) {
            spreadSheetUrl = fact.getWorksheetFeedUrl(EVE_BRIEF_KEY, "public", "basic");
        }else if(briefType.equalsIgnoreCase("InvBrief")){
            spreadSheetUrl = fact.getWorksheetFeedUrl(INV_BRIEF_KEY, "public", "basic");
        }else if (briefType.equalsIgnoreCase("Engage")){
            spreadSheetUrl = fact.getWorksheetFeedUrl(ENGAGE_KEY, "public", "basic");
              }else {
                  spreadSheetUrl = fact.getWorksheetFeedUrl(RATING_NEWS_UPDATE_KEY, "public", "basic");
              }
        //debug the spreadsheet url to the system output
        System.out.println("spreadsheetUrl: " + spreadSheetUrl.toString());
        WorksheetFeed feed = service.getFeed(spreadSheetUrl, WorksheetFeed.class);


        //gets the first Worksheet.
        WorksheetEntry entry = feed.getEntries().get(0);

        // gets the cell feed
        URL cellFeedURL = entry.getCellFeedUrl();
        CellFeed cellFeed = service.getFeed(cellFeedURL, CellFeed.class);

        //converts the cells into brief objects.
        int data = 1;
        ArrayList<Brief> briefs = new ArrayList<Brief>();
        String publication = "";
        String title = "";
        String date = "";
        String pdfLocation = "";
        String imageLocation = "";

        // extracts data from sheet into brief objects.
        for (CellEntry cell : cellFeed.getEntries()) {
            String shortId = cell.getId().substring(cell.getId().lastIndexOf('/') + 1);
            if (!shortId.contains("R1")) {

                switch (data) {
                    case 1:
                        publication = cell.getPlainTextContent();
                        data = 2;
                        break;
                    case 2:
                        title = cell.getPlainTextContent();
                        data = 3;
                        break;
                    case 3:
                        date = cell.getPlainTextContent();
                        data = 4;
                        break;
                    case 4:
                        pdfLocation = cell.getPlainTextContent();
                        data = 5;
                        break;
                    case 5:
                        imageLocation = cell.getPlainTextContent();
                        //add constructed brief to the list of known briefs
                        briefs.add(new Brief(title, date, pdfLocation, imageLocation, publication));
                        data = 1;
                }
            }

        }
        return briefs;
    }
}

