package crawler;

import crawler.component.WebCrawler;

public class ApplicationRunner {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(WebCrawler::new);
    }
}
