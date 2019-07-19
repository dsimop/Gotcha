package frontend.common.ui;

import frontend.teacher.TeacherClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import static frontend.GotchaClientApp.frame;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * @author Dimitrios Simopoulos, Mel Trout, Daohan Chong
 * @version 2018-03-17
 */
public class LoginScreen extends JPanel implements Observer {

    private JPasswordField passwordField;
    private JTextField userField;
    private JButton signUpButton;
    private LoginModel loginModel;

    /**
     * Create the application.
     */
    public LoginScreen(LoginModel loginModel) {
        this.loginModel = loginModel;
        loginModel.addObserver(this);

        setLayout(null);
        this.setBounds(0, 0, 400, 280);

        javax.swing.JLabel userLabel = new javax.swing.JLabel("Username:");
        userLabel.setFont(new Font("EB Garamond 12", Font.BOLD, 12));
        userLabel.setBounds(100 - 20, 65, 76 + 20, 14);
        add(userLabel);

        javax.swing.JLabel passLabel = new javax.swing.JLabel("Password: ");
        passLabel.setFont(new Font("EB Garamond 12", Font.BOLD, 12));
        passLabel.setBounds(100 - 20, 93, 71 + 20, 14);
        add(passLabel);

        passwordField = new javax.swing.JPasswordField();
        passwordField.setBounds(189, 92, 81, 15);
        add(passwordField);

        userField = new javax.swing.JTextField();
        userField.setBounds(189, 63, 81, 18);
        userField.setColumns(10);
        add(userField);

        javax.swing.JButton loginButton = new javax.swing.JButton("Login");
        loginButton.setFont(new Font("Caladea", Font.BOLD, 12));
        loginButton.setBounds(189, 132, 81, 24);
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = loginModel.convertPassword(passwordField.getPassword());

            if (username.isEmpty() && password.isEmpty()) {
                showMessageDialog(javax.swing.JOptionPane.getRootFrame(), "Both username and password cannot be empty!",
                        "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            } else if (password.isEmpty()) {
                showMessageDialog(javax.swing.JOptionPane.getRootFrame(), "Please insert your password!", "Warning",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            } else if (username.isEmpty()) {
                showMessageDialog(javax.swing.JOptionPane.getRootFrame(), "Please insert your username!", "Warning",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            } else {
                try {
                    RootRegistrationModel.getLoginModel().sendUserDetailToSever(username, password);
                } catch (java.io.IOException e1) {
                    e1.printStackTrace();
                }
            }

        });
        add(loginButton);

        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginButton.doClick();
            }
        };
        userField.addActionListener(action);
        passwordField.addActionListener(action);

        signUpButton = new javax.swing.JButton("Sign Up");
        signUpButton.setFont(new Font("Caladea", Font.BOLD, 12));
        signUpButton.addActionListener((java.awt.event.ActionEvent arg0) -> {
            frame.remove(frame.login);
            frame.repaint();
            frame.setContentPane(frame.signUp);
            frame.setTitle("Sign up");
        });

        signUpButton.setBounds(189, 195, 81, 24);
        add(signUpButton);

        javax.swing.JButton exitButton = new javax.swing.JButton("Exit");
        exitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent arg0) {
                frame.dispose();
                System.exit(0);
            }
        });
        exitButton.setForeground(new java.awt.Color(0, 0, 0));
        exitButton.setBackground(new java.awt.Color(192, 192, 192));
        exitButton.setFont(new Font("Caladea", Font.PLAIN, 12));
        exitButton.setBounds(340, 12, 98, 24);
        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource("resources/logout_logo.png")));
            exitButton.setIcon(icon);
            exitButton.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            exitButton.setContentAreaFilled(false);
            exitButton.setFocusable(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        add(exitButton);

        JLabel lblGotcha = new JLabel("Gotcha");
        lblGotcha.setFont(new Font("URW Chancery L", Font.BOLD, 25));
        lblGotcha.setBounds(189, 2, 120, 37);
        add(lblGotcha);
    }

    /**
     * This method is called whenever the observed object is changed. An application
     * calls an <tt>Observable</tt> object's <code>notifyObservers</code> method to
     * have all the object's observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Boolean) {
            if (((Boolean) arg)) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "Successful Log-in!");
                    {
                        if (loginModel.isStudent) {
                            SwingUtilities.invokeLater(() -> {
                                frame.remove(frame.login);
                                //frame.setBounds(frame.getX(), frame.getY(), 1000, 600);
                                frame.studentHome.setSize(1000, 600);
                                frame.setContentPane(frame.studentHome);
                                //frame.revalidate();
                                //frame.repaint();
                                frame.setSize(1000, 600);
                                frame.setPreferredSize(new Dimension(1000, 600));
                                //frame.setLocationRelativeTo(null);
                                frame.revalidate();
                                frame.repaint();
                                frame.setTitle("StudentClient: Home page");
                                System.out.println("invokeLater thread: " + Thread.currentThread().getName());
                            });
                        } else {
                            SwingUtilities.invokeLater(() -> {
                                frame.remove(frame.login);

                                TeacherClient teacherClient = new TeacherClient();

                                frame.setContentPane(teacherClient.getRootPanel());
                                frame.revalidate();
                                frame.repaint();
                                frame.setBounds(frame.getX(), frame.getY(), 800, 600);
                                frame.setSize(800, 600);
                                frame.studentHome.setSize(new Dimension(1000, 600));
                                frame.setLocationRelativeTo(null);
                                frame.revalidate();
                                frame.repaint();
                                frame.setTitle("Gotcha: TeacherClient");
                                System.out.println("invokeLater thread: " + Thread.currentThread().getName());
                            });
                        }

                    }
                });
            } else {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Invalid username or password, please try again."));
            }
        }
    }
}