import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class KeyPress {
    public static void main(String[] args) throws AWTException, InterruptedException {
        Robot robot = new Robot();
        while (true) {
            robot.keyPress(KeyEvent.VK_SPACE);
            Thread.sleep(400);
            robot.keyRelease(KeyEvent.VK_SPACE);
            Random r = new Random();
            Thread.sleep(r.nextInt(20000));
        }
    }
}
