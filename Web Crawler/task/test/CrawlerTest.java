import crawler.WebCrawler;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JLabelFixture;
import org.assertj.swing.fixture.JTableFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.mocks.web.WebPage;
import org.hyperskill.hstest.mocks.web.WebServerMock;
import org.hyperskill.hstest.stage.SwingTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.swing.SwingComponent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CrawlerTest extends SwingTest {

    private static WebServerMock webServerMock;
    private static PageContent pageContent;
    private static final int PORT = 25555;
    private static List<String> parsedPages;

    Map<String, String> mapOfLinksNTitles = pageContent.getLinksNTitles();
    private final String EXPORT_DIRECTORY = Paths.get("").toAbsolutePath().toString() + "/temp.txt";


    public CrawlerTest() {
        super(new WebCrawler());
    }

    @BeforeClass
    public static void initWebServer() {
        System.out.println("Initializing server");
        pageContent = new PageContent();
        parsedPages = new ArrayList<>();

        WebPage exampleDotComPage = new WebPage();
        exampleDotComPage.setContent(pageContent.getContentWithLink("http://localhost:25555/exampleDotCom"));
        exampleDotComPage.setContentType("text/html");

        WebPage circular1Page = new WebPage();
        circular1Page.setContent(pageContent.getContentWithLink("http://localhost:25555/circular1"));
        circular1Page.setContentType("text/html");

        WebPage circular2Page = new WebPage();
        circular2Page.setContent(pageContent.getContentWithLink("http://localhost:25555/circular2"));
        circular2Page.setContentType("text/html");

        WebPage circular3Page = new WebPage();
        circular3Page.setContent(pageContent.getContentWithLink("http://localhost:25555/circular3"));
        circular3Page.setContentType("text/html");

        WebPage unavailablePage = new WebPage();
        unavailablePage.setContent("Web Page not found");

        webServerMock = new WebServerMock(PORT);
        webServerMock.setPage("/exampleDotCom", exampleDotComPage);
        webServerMock.setPage("/circular1", circular1Page);
        webServerMock.setPage("/circular2", circular2Page);
        webServerMock.setPage("/circular3", circular3Page);
        webServerMock.setPage("/unavailablePage", unavailablePage);

        Thread thread = new Thread(() -> {
            webServerMock.start();
            webServerMock.run();
        });

        thread.start();

    }

    @AfterClass
    public static void stopServer() {
        System.out.println("Stopping server");
        webServerMock.stop();
    }

    @After
    public void deleteFile() {
        File file = new File(EXPORT_DIRECTORY);
        if (file.exists()) {
            boolean deleted = file.delete();
        }
    }

    @SwingComponent(name = "TitlesTable")
    JTableFixture titlesTable;

    @SwingComponent(name = "UrlTextField")
    JTextComponentFixture textField;

    @SwingComponent(name = "RunButton")
    JButtonFixture runButton;

    @SwingComponent(name = "TitleLabel")
    JLabelFixture titleLabel;

    @SwingComponent(name = "ExportUrlTextField")
    JTextComponentFixture exportUrlTextField;

    @SwingComponent(name = "ExportButton")
    JButtonFixture exportButton;

    @DynamicTest(order = 1)
    CheckResult testComponents() {

        requireVisible(textField);
        requireVisible(runButton);
        requireVisible(titleLabel);
        requireVisible(titlesTable);
        requireVisible(exportUrlTextField);
        requireVisible(exportButton);

        requireEnabled(textField);
        requireEnabled(runButton);
        requireEnabled(titleLabel);
        requireEnabled(exportUrlTextField);
        requireEnabled(exportButton);

        requireDisabled(titlesTable);
        titlesTable.requireColumnCount(2);

        return CheckResult.correct();
    }


    @DynamicTest(order = 2)
    CheckResult testTitles() {


        for (Map.Entry<String, String> m : mapOfLinksNTitles.entrySet()) {
            String link = m.getKey();
            textField.setText(link);
            runButton.click();
            String title = pageContent.getTitleWithLink(link);
            if (!titleLabel.text().equals(title)) {
                return CheckResult.wrong("TitleLabel shows the wrong title");
            }
        }

        return CheckResult.correct();
    }

    @DynamicTest(order = 3)
    CheckResult testTitlesTable() {

        Map<String, String> mapOfLinksNTitles = pageContent.getLinksNTitles();

        for (Map.Entry<String, String> m : mapOfLinksNTitles.entrySet()) {
            String link = m.getKey();
            textField.setText(link);
            runButton.click();
            boolean validContent = checkTableContent(true);
            if (!validContent) {
                return CheckResult.wrong("TitlesTable contains link(s) that is neither a base link nor a sub-link");
            }
        }
        return CheckResult.correct();
    }

    @DynamicTest(order = 4)
    CheckResult testTitlesTableForWrongLinks() {

        for (Map.Entry<String, String> m : mapOfLinksNTitles.entrySet()) {
            String link = m.getKey();
            textField.setText(link);
            runButton.click();
            boolean validContent = checkTableContent(false);
            if (!validContent) {
                return CheckResult.wrong("TitlesTable contains wrong link and title pair after parsing.");
            }
        }

        return CheckResult.correct();
    }

    @DynamicTest(order = 5)
    CheckResult testForDoubleLinks() {

        for (Map.Entry<String, String> m : mapOfLinksNTitles.entrySet()) {
            String link = m.getKey();
            textField.setText(link);
            runButton.click();
            boolean doubleLinks = checkForDoubleLinks();
            if (doubleLinks) {
                return CheckResult.wrong("You shouldn't save a links that you have previously saved");
            }
        }

        return CheckResult.correct();
    }

    @DynamicTest(order = 6)
    CheckResult testForUnavailableLinks() {

        for (Map.Entry<String, String> m : mapOfLinksNTitles.entrySet()) {
            String link = (String) m.getKey();
            textField.setText(link);
            runButton.click();
            boolean validContent = checkForUnavailablePage();
            if (!validContent) {
                return CheckResult.wrong("TitlesTable shows a link to the page that is unavailable." +
                    " You shouldn't add to the table unavailable links.");
            }
        }

        return CheckResult.correct();
    }

    @DynamicTest(order = 7)
    CheckResult testForRowNumber() {

        for (Map.Entry<String, String> m : mapOfLinksNTitles.entrySet()) {
            String link = m.getKey();
            textField.setText(link);
            runButton.click();
            int numOfSubLinks = pageContent.getSubLinksWithLink(link);
            if (numOfSubLinks != titlesTable.rowCount()) {
                return CheckResult.wrong("TitlesTable has wrong number of rows after parsing");
            }
        }

        return CheckResult.correct();
    }

    @DynamicTest(order = 8)
    CheckResult testFileCreation() {

        for (Map.Entry<String, String> m : mapOfLinksNTitles.entrySet()) {
            String link = m.getKey();
            textField.setText(link);
            runButton.click();
            exportUrlTextField.setText(EXPORT_DIRECTORY);
            exportButton.click();
            boolean fileExists = checkFileExistence();
            if (!fileExists) {
                return CheckResult.wrong("Your app did not save a file after exporting.");
            }
            deleteFile();
        }

        return CheckResult.correct();
    }

    @DynamicTest(order = 9)
    CheckResult testFileContentLength() {

        for (Map.Entry<String, String> m : mapOfLinksNTitles.entrySet()) {
            String link = m.getKey();
            int originalNumberOfLines = pageContent.getSubLinksWithLink(link);
            textField.setText(link);
            runButton.click();
            exportUrlTextField.setText(EXPORT_DIRECTORY);
            exportButton.click();
            boolean correctLines = checkFileNumberOfLines(originalNumberOfLines);
            if (!correctLines) {
                return CheckResult.wrong("The file your app saves contains wrong number of lines");
            }
        }

        return CheckResult.correct();
    }

    @DynamicTest(order = 10)
    CheckResult testTitlesInFile() {

        for (Map.Entry<String, String> m : mapOfLinksNTitles.entrySet()) {
            String link = (String) m.getKey();
            textField.setText(link);
            runButton.click();
            exportUrlTextField.setText(EXPORT_DIRECTORY);
            exportButton.click();
            boolean valid = checkEvenLines();
            if (!valid) {
                return CheckResult.wrong("The file your app saves contains wrong title for it's parent url");
            }
        }

        return CheckResult.correct();
    }


    private boolean checkTableContent(boolean testForValidLinks) {
        String[][] tableContent = titlesTable.contents();

        if (testForValidLinks) {
            for (String[] s : tableContent) {
                for (int j = 0; j < tableContent[0].length; j++) {
                    String tableLink = s[0];
                    if (!mapOfLinksNTitles.containsKey(tableLink)) {
                        return false;
                    }
                }
            }
        } else {
            for (String[] s : tableContent) {
                for (int j = 0; j < tableContent[0].length; j++) {
                    String tableTitle = s[1];
                    String originalTitle = pageContent.getTitleWithLink(s[0]);
                    if (!tableTitle.equals(originalTitle)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkForDoubleLinks() {
        parsedPages.clear();

        String[][] tableContent = titlesTable.contents();
        for (String[] s : tableContent) {
            String link = s[0];
            System.out.println(link);
            if (parsedPages.contains(link)) {
                return true;
            }
            parsedPages.add(link);
        }
        return false;
    }

    private boolean checkForUnavailablePage() {
        String[][] tableContent = titlesTable.contents();
        for (String[] s : tableContent) {
            for (int i = 0; i < s.length; i += 2) {
                if (!mapOfLinksNTitles.containsKey(s[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkFileExistence() {
        File file = new File(EXPORT_DIRECTORY);
        return file.exists();
    }

    private boolean checkFileNumberOfLines(int originalNumberOfLines) {
        //Multiplied by 2 because titles are placed after their corresponding link hence the total lines is doubled
        originalNumberOfLines *= 2;
        int fileLines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(EXPORT_DIRECTORY))) {
            while (reader.readLine() != null) {
                fileLines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return originalNumberOfLines == fileLines;
    }

    private boolean checkEvenLines() {
        boolean valid = true;
        int lineNumber = 1;
        String line;
        String originalTitle = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(EXPORT_DIRECTORY))) {
            while ((line = reader.readLine()) != null) {
                //Every odd line contains a link
                if (lineNumber % 2 != 0) {
                    originalTitle = pageContent.getTitleWithLink(line);
                } else {
                    //Every even line contains a title
                    if (!line.equals(originalTitle)) {
                        valid = false;
                    }
                }
                lineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return valid;
    }
}
