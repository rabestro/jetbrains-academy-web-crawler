import crawler.WebCrawler;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.stage.SwingTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.swing.SwingComponent;

public class CrawlerTest extends SwingTest {


    public CrawlerTest() {
        super(new WebCrawler());
    }

    @SwingComponent(name = "TextArea")
    JTextComponentFixture textArea;

    @DynamicTest
    CheckResult testTextArea() {

        requireVisible(textArea);
        requireDisabled(textArea);

        System.out.println(textArea.text());

        if (!textArea.text().equals("HTML code?")) {
            return CheckResult.wrong("TextArea should contain the text \"HTML code?\"");
        }

        return CheckResult.correct();
    }
}
