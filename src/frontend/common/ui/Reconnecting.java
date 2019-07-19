package frontend.common.ui;

import javax.swing.*;
import java.awt.*;

/**
 * @author Yang He
 * @version 2018-03-17
 */
public class Reconnecting extends JDialog {
    public Reconnecting(JFrame frame) {
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.CENTER;
        c.insets = new Insets(10, 10, 10, 10);
        contentPane.add(new JLabel("Reconnecting..."), c);
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 0, 0, 0);
        JButton exit = new JButton("Exit");
        exit.addActionListener(e -> {
            System.exit(0);
        });
        contentPane.add(exit, c);
        this.setContentPane(contentPane);
        this.setSize(130, 70);
        this.setLocationRelativeTo(frame);
        this.setUndecorated(true);
    }

    public void pop() {
        this.setModal(true);
        this.setVisible(true);
    }

    public void close() {
        this.setModal(false);
        this.setVisible(false);
    }

}
