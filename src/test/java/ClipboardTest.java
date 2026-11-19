
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;

public class ClipboardTest {

    public static void main(String[] args) throws Exception {

        System.out.println("Copy your HCLTech SMS now.");

        System.out.println("Press Enter after copying the SMS.");

        System.in.read();

        String text = (String) Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .getData(DataFlavor.stringFlavor);

        System.out.println("CLIPBOARD CONTENT:");
        System.out.println("[" + text + "]");
    }
}
