import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class KeyPress {

    private static Robot robot;
    private static Random random;

    public static void main(String[] args) throws AWTException, InterruptedException {
        KeyPress keyPress = new KeyPress();
        random = keyPress.getRandom();
        while (true) {
            keyPress.join();
            keyPress.jump();
//            keyPress.exchange();
            keyPress.online();
            keyPress.aoe();
            Thread.sleep(random.nextInt(20000) + 2000);
        }
    }
    //放奉献
    public void aoe() throws InterruptedException, AWTException {
        press(KeyEvent.VK_F);
    }

    //挂机
    public void online () throws InterruptedException, AWTException {
        press(KeyEvent.VK_SPACE);
        press(KeyEvent.VK_8);
    }

    //换牌子
    public void exchange() throws InterruptedException, AWTException {
        press(KeyEvent.VK_9);
        press(KeyEvent.VK_EQUALS);
        press(KeyEvent.VK_9);
        press(KeyEvent.VK_9);
        press(KeyEvent.VK_9);
    }

    //跳，防暂离
    public void jump() throws InterruptedException, AWTException {
        press(KeyEvent.VK_SPACE);
        press(KeyEvent.VK_TAB);
        press(KeyEvent.VK_5);
    }

    //加入战场
    public void join() throws InterruptedException, AWTException {
        press(KeyEvent.VK_0);
        press(KeyEvent.VK_EQUALS);
        press(KeyEvent.VK_0);
        press(KeyEvent.VK_0);
    }

    public void press(Integer key) throws AWTException, InterruptedException {
        Random rd = getRandom();
        Robot robot = getRobot();
        robot.keyPress(key);
        // 按下400-600毫秒弹起
        Thread.sleep(rd.nextInt(400) + 200);
        robot.keyRelease(key);
        // 一个按键后休息2秒
        Thread.sleep(rd.nextInt(2000) + 200);
    }

    public Robot getRobot() throws AWTException {
        if (robot == null) {
            robot = new Robot();
        }
        return robot;
    }

    public Random getRandom() {
        if (random == null) {
            random = new Random();
        }
        return random;
    }
}
