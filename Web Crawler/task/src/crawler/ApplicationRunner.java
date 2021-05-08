package crawler;

import crawler.view.WebCrawler;

public class ApplicationRunner {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(WebCrawler::new);
    }
}
