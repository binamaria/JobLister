package com.combind;

import ds.desktop.notify.DesktopNotify;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.swing.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;

public class DisplayJobList {

    private JFrame frame;
    private JPanel innerPanel;
    private JButton viewJobButton;
    private GridBagConstraints gridBagConstraints;
    static String path = "https://stackoverflow.com/jobs/";
    static int count;
    static Element lineNodesTitle;
    static String descriptionForTest;
    JScrollPane scrollPane;
    private int counter;

    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                //To show desktop notification once the program loads directing the user to do the required.
                DesktopNotify.showDesktopMessage("Job Postings", "Click here to view job postings that are listed within 50 miles of Bridgewater",DesktopNotify.INFORMATION,new ActionListener() {
                            public void actionPerformed(ActionEvent e) {

                                //To load the GUI window once the user clicks on the desktop notification popup.
                                new DisplayJobList().displayGUI();
                            }
                        }
                );
                }
        };
        EventQueue.invokeLater(runnable);
    }

    //Default constructor of the class
    public DisplayJobList() {

        gridBagConstraints = new GridBagConstraints();

        //Setting various grid bag constraints
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.gridx = 0;
        counter = 0;
    }

    //To display the GUI
    private void displayGUI() {

        frame = new JFrame("DisplayJobList");

        // make it easy to close the application
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel contentPane = new JPanel(new BorderLayout(5, 5));
        innerPanel = new JPanel(new GridBagLayout());
        innerPanel.setBackground(Color.pink);
        innerPanel.setForeground(Color.CYAN);
        scrollPane = new JScrollPane(innerPanel);
        viewJobButton = new JButton("View job postings that are within 50 miles of Bridgewater");
        viewJobButton.setBackground(Color.BLUE);
        viewJobButton.setForeground(Color.CYAN);
        viewJobButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

                viewJobButton.setText("Job postings that are within 50 miles of Bridgewater");
                viewJobButton.setBackground(Color.orange);
                viewJobButton.setForeground(Color.WHITE);
                getJobListing(0);
            }
        });

        //Adding all the components into the main panel - contentPane
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(viewJobButton, BorderLayout.PAGE_START);
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    //To adjust the scrollPane, panel, frame size while creating labels dynamically
    private void addComponent(JPanel panel, JComponent jComponent, int gridy) {
        gridBagConstraints.gridy = gridy;
        scrollPane.revalidate();
        scrollPane.repaint();
        panel.add(jComponent, gridBagConstraints);
        frame.setSize(640, 480);
    }

    //To fetch the Job list from the server and display it on the GUI window as well as the Terminal
    public void getJobListing(int flag)
    {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(path + "feed?l=Bridgewater%2c+MA%2c+USA&u=Miles&d=50");
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_XML);
        javax.ws.rs.core.Response response = invocationBuilder.get();
        String xml = response.readEntity(String.class);
        count = 0;

        try {
            //For parsing the xml
            DocumentBuilderFactory documentBuilderFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(xml));
            Document doc = documentBuilder.parse(inputSource);
            NodeList nodes = doc.getElementsByTagName("item");
            NodeList nodesTitle = doc.getElementsByTagName("title");
            lineNodesTitle = (Element) nodesTitle.item(0);

            //To check if the function - getJobListing() is called from the main function not from any other Testing class functions and to set labels
            if(flag==0) {
             final JLabel labelHeader = new JLabel(
                    "Job postings that are within 50 miles of Bridgewater :", JLabel.CENTER);

             addComponent(innerPanel, labelHeader, counter++);
             final JLabel labelHeaderUnderline = new JLabel(
            "-------------------------------------------------------------------------", JLabel.CENTER);
             addComponent(innerPanel, labelHeaderUnderline, counter++);
             final JLabel labelNewLine = new JLabel(
            "", JLabel.CENTER);
             addComponent(innerPanel, labelNewLine, counter++);
             }

            System.out.println("Job postings that are within 50 miles of Bridgewater :");
            System.out.println("------------------------------------------------------");
            System.out.println("");
            // Iterate the nodes and get the element values
            for (int i = 0; i < nodes.getLength(); i++) {

                System.out.println("");

                if(flag==0) {
                    final JLabel labelNewLineBeforeNewJobListing = new JLabel(
                            "", JLabel.CENTER);
                    addComponent(innerPanel, labelNewLineBeforeNewJobListing, counter++);
                }
                Element element = (Element) nodes.item(i);
                NodeList title = element.getElementsByTagName("title");
                Element line = (Element) title.item(0);
                System.out.println("Title: " + getCharacterDataFromElement(line));

                if(flag==0) {
                    final JLabel labelTitle = new JLabel(
                            String.valueOf((count + 1)) + ". Title: " + getCharacterDataFromElement(line), JLabel.CENTER);
                    addComponent(innerPanel, labelTitle, counter++);
                }
                NodeList location = element.getElementsByTagName("location");
                line = (Element) location.item(0);
                System.out.println("Location: " + getCharacterDataFromElement(line));

                if(flag==0) {
                    final JLabel labelLocation = new JLabel(
                            "Location: " + getCharacterDataFromElement(line), JLabel.CENTER);
                    addComponent(innerPanel, labelLocation, counter++);
                }

                NodeList pubDate = element.getElementsByTagName("pubDate");
                line = (Element) pubDate.item(0);
                System.out.println("Published date: " + getCharacterDataFromElement(line));

                if(flag==0) {
                final JLabel labelPublishedDate = new JLabel(
                        "Published date: " + getCharacterDataFromElement(line), JLabel.CENTER);
                addComponent(innerPanel, labelPublishedDate, counter++);}
                NodeList description = element.getElementsByTagName("description");
                line = (Element) description.item(0);
                String noHellipFinalDescription = removeHtmlTags(getCharacterDataFromElement(line));
                System.out.println("Description: " + noHellipFinalDescription);

                if(flag==0) {
                final JLabel labelDescription = new JLabel(
                        "Description: " + noHellipFinalDescription, JLabel.CENTER);
                addComponent(innerPanel, labelDescription, counter++);}
                else{
                    descriptionForTest = noHellipFinalDescription;
                }
                System.out.println("==============================================================================================================================================================================");

                if(flag==0) { final JLabel labelEndingLine = new JLabel(
                        "==================================================================================================================================================================================================================================", JLabel.CENTER);
                addComponent(innerPanel, labelEndingLine, counter++);}
                count = count+1;
            }

            DesktopNotify.showDesktopMessage("Job Postings", "Total number of job postings that are within 50 miles of Bridgewater is "+String.valueOf(count),DesktopNotify.INFORMATION);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //To replace all html tags and characters
    public String removeHtmlTags(String description) {

        String noHTMLtags = description.replaceAll("\\<.*?\\>", " ");
        String noNbsp = noHTMLtags.replaceAll("&nbsp;","  ");
        String noAmp = noNbsp.replaceAll("&amp;"," & ");
        String noRsquo = noAmp.replaceAll("&rsquo;","'");
        String noBull = noRsquo.replaceAll("&bull;","*");
        String noHellipFinalDescription = noBull.replaceAll("&hellip;","...");
        return noHellipFinalDescription;
    }

    //To find the status code of the response from the server
    public int getStatusCode(){

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(path + "feed?l=Bridgewater%2c+MA%2c+USA&u=Miles&d=50");
        Invocation.Builder builder = target.request(MediaType.APPLICATION_XML);
        int status = builder.get().getStatus();
        return status;
    }

    //To get character data from element
    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();

        if (child instanceof CharacterData) {
            CharacterData charData = (CharacterData) child;
            return charData.getData();
        }
        return "?";
    }

}

