package com.combind;

import org.junit.Test;
import static org.junit.Assert.*;

public class DisplayJobListTest {

    DisplayJobList getJobListingsObj = new DisplayJobList();

    //Will check if the status code of the response from the server is 200. Else the test will fail.
    @Test
    public void responseFromServerCheck(){
        assertEquals(200,getJobListingsObj.getStatusCode());
    }

    //Verifies whether the parsed xml still contains any tags. If it contains any tags (eg. <item>) the test will fail.
    @Test
    public void xmlParseHtmlTagCheck(){
        getJobListingsObj.getJobListing(1);
        assertFalse(DisplayJobList.descriptionForTest.contains("\\<.*?\\>"));

    }

    //Verifies if the response we got from the server is having the title - "jobs in Bridgewater, MA, USA - Stack Overflow". Else the test will fail.
    @Test
    public void ResponseTitleCheck(){

        getJobListingsObj.getJobListing(1);
        assertTrue((String.valueOf(getJobListingsObj.getCharacterDataFromElement(DisplayJobList.lineNodesTitle))).contentEquals("jobs in Bridgewater, MA, USA - Stack Overflow"));

    }

    //Verifies whether the parsed xml still contains any non breaking space. If it contains any (eg. &nbsp;) the test will fail.
    @Test
    public void xmlParseNonBreakingSpaceCheck(){
        getJobListingsObj.getJobListing(1);
        assertFalse(DisplayJobList.descriptionForTest.contains("&nbsp;"));

    }

    //Verifies the total number of job postings within 50 miles of Bridgewater with its actual count 108. If the total number is not 108 the test will fail.
    @Test
    public void totJobListingsCheck(){
            getJobListingsObj.getJobListing(1);
        assertTrue((String.valueOf(DisplayJobList.count)).contentEquals("108"));

    }

}