import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class HoldDownKeyListener implements KeyListener {

    private boolean isUpDown = false;
    private boolean isDownDown = false;
    private boolean isLeftDown = false;
    private boolean isRightDown = false;

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            isUpDown = true;

        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            isDownDown = true;
            isUpDown = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            isLeftDown = true;
            isRightDown = false;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            isRightDown = true;
            isLeftDown = false;
        }

        if(e.getKeyCode() == KeyEvent.VK_P) {
            GameLoop.togglePauseGame();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            isUpDown = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            isDownDown = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            isLeftDown = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            isRightDown = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public  void resetState(){
        isUpDown = false;
        isDownDown = false;
        isLeftDown = false;
        isRightDown = false;
    }

    public boolean isUpDown() {
        return isUpDown;
    }

    public boolean isDownDown() {
        return isDownDown;
    }

    public boolean isLeftDown() {
        return isLeftDown;
    }

    public boolean isRightDown() {
        return isRightDown;
    }
}