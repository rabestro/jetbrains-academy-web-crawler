import crawler.WebCrawler;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.mocks.web.WebServerMock;
import org.hyperskill.hstest.stage.SwingTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.swing.SwingComponent;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.Map;

public class CrawlerTest extends SwingTest {

    private static WebServerMock webServerMock;
    private static PageContent pageContent;
    private static final int PORT = 25555;


    public CrawlerTest() {
        super(new WebCrawler());
    }

    @BeforeClass
    public static void startServer() {
        System.out.println("Initializing server");
        pageContent = new PageContent();

        webServerMock = new WebServerMock(PORT);
        webServerMock.setPage("/exampleDotCom", pageContent.getContentWithLink("http://localhost:25555/exampleDotCom"));
        webServerMock.setPage("/circular1", pageContent.getContentWithLink("http://localhost:25555/circular1"));
        webServerMock.setPage("/circular2", pageContent.getContentWithLink("http://localhost:25555/circular2"));
        webServerMock.setPage("/circular3", pageContent.getContentWithLink("http://localhost:25555/circular3"));

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

    @SwingComponent(name = "HtmlTextArea")
    JTextComponentFixture textArea;

    @SwingComponent(name = "UrlTextField")
    JTextComponentFixture textField;

    @SwingComponent(name = "RunButton")
    JButtonFixture runButton;

    @DynamicTest(order = 1)
    CheckResult testTextArea() {

        requireVisible(textArea);
        requireDisabled(textArea);

        return CheckResult.correct();
    }

    @DynamicTest(order = 2)
    CheckResult testUrlFieldAndButton() {

        requireVisible(textField);
        requireVisible(runButton);

        requireEnabled(textField);
        requireEnabled(runButton);

        requireDisabled(textArea);

        return CheckResult.correct();
    }

    @DynamicTest(order = 3)
    CheckResult testHtmlCode() {

        Map<String, String> map = pageContent.getLinksNContent();

        for (Map.Entry<String, String> m : map.entrySet()) {

            textField.setText(String.valueOf(m.getKey()));
            runButton.click();

            String content = pageContent.getContentWithLink(String.valueOf(m.getKey()));

            if (!textArea.text().equals(content)) {
                return CheckResult.wrong("HtmlTextArea contains wrong HTML code");
            }
        }

        return CheckResult.correct();
    }
}
