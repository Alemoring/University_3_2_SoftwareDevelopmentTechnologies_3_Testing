package GUI;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public abstract class Window extends JFrame {
    protected String title;

    public Window(String title) {
        this.title = title;
        setTitle(title);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent event) {
                Object[] options = {"Да", "Нет!"};
                int rc = JOptionPane.showOptionDialog(
                        event.getWindow(), "Закрыть окно?",
                        "Подтверждение", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, options, options[0]);
                if (rc == 0) {
                    event.getWindow().setVisible(false);
                    System.exit(0);
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }

    public void rendering(){
        setVisible(true);
    }

}
