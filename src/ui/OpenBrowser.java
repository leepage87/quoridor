package src.ui;

import javax.swing.*;
import java.lang.reflect.Method;
/**
 * Allows opening of a browser from the help menu
*/
public class OpenBrowser {
	/**
	 * Opens the browser on the parameter URL
	 * @param url a string
	 */
    public static void openURL(String url) {
        String osName = System.getProperty("os.name");
        try {
            if (osName.startsWith("Windows"))
                Runtime.getRuntime().exec(
                        "rundll32 url.dll,FileProtocolHandler " + url);
            else {
                String[] browsers = { "firefox", "opera", "konqueror",
                        "epiphany", "mozilla", "netscape" };
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++)
                    if (Runtime.getRuntime().exec(
                            new String[] { "which", browsers[count] })
                            .waitFor() == 0)
                        browser = browsers[count];
                Runtime.getRuntime().exec(new String[] { browser, url });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error in opening browser"
                    + ":\n" + e.getLocalizedMessage());
        }
    }
}
