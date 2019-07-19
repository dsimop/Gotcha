package frontend.common.ui;

import frontend.GotchaClientApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.Observer;

import static frontend.GotchaClientApp.frame;
import static frontend.common.ui.RootRegistrationModel.getLoginModel;

/**
 * The SignupScreen GUI allows a user to sign up to the application.
 *
 * @author Melisha Trout, Dimitrios Simopoulos
 */

public class SignupScreen extends JPanel implements Observer {

    private JPasswordField passwordField;
    private JTextField userField;
    private JButton signupButton;
    private JLabel reEnterPasswordLabel, userLabel, passLabel, isTeacherLabel;
    private JPasswordField passwordConfirmField;
    private JComboBox isTeacherBox;

    private RegistrationModel registrationModel;


    public SignupScreen(RegistrationModel registrationModel) {
        this.registrationModel = registrationModel;
        registrationModel.addObserver(this);

        setLayout(null);
        this.setBounds(0, 0, 400, 280);

        userLabel = new JLabel("Username:");
        userLabel.setBounds(118, 65, 76, 14);
        add(userLabel);

        passLabel = new JLabel("Password: ");
        passLabel.setBounds(118, 91, 71, 14);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(251, 92, 81, 15);
        add(passwordField);

        userField = new JTextField();
        userField.setBounds(251, 63, 81, 18);
        userField.setColumns(10);
        add(userField);

        signupButton = new JButton("Signup");

        signupButton.addActionListener(arg0 -> {
            String user = userField.getText();
            String pass1 = getLoginModel().convertPassword(passwordField.getPassword());
            String pass2 = getLoginModel().convertPassword(passwordConfirmField.getPassword());
            Boolean isTeacher = Objects.equals(isTeacherBox.getSelectedItem(), "Yes");
            if( (user.isEmpty()) ||
                    (pass1.isEmpty() || pass2.isEmpty())
                    || (! pass1.equals(pass2)) ) {
                javax.swing.JOptionPane.showMessageDialog(GotchaClientApp.frame,
                        "The username/password fields are empty or you have to re-enter your password correctly.",
                        "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            getLoginModel().sendNewUsersDetails(user, pass2, isTeacher);

        });

        signupButton.setBounds(189, 207, 193, 24);
        signupButton.setBounds(189, 179, 81, 24);
        add(signupButton);

        JButton backButton = new JButton("Back");
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                GotchaClientApp.frame.setContentPane(GotchaClientApp.frame.login);
                GotchaClientApp.frame.setTitle("Login");
            }
        });
        backButton.setForeground(new Color(0, 0, 0));
        backButton.setBackground(new Color(192, 192, 192));
        backButton.setFont(new Font("Arial Black", Font.PLAIN, 11));
        backButton.setBounds(2, 12, 98, 24);
        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource("resources/back_logo.png")));
            backButton.setIcon(icon);
            backButton.setBorder(BorderFactory.createEmptyBorder());
            backButton.setContentAreaFilled(false);
            backButton.setFocusable(false);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        add(backButton);

        reEnterPasswordLabel = new JLabel("Re-enter Password: ");
        reEnterPasswordLabel.setBounds(100, 115, 133, 24);
        add(reEnterPasswordLabel);

        passwordConfirmField = new JPasswordField();
        passwordConfirmField.setBounds(251, 119, 81, 15);
        add(passwordConfirmField);

        isTeacherLabel = new JLabel("Are you a Teacher?");
        isTeacherLabel.setBounds(100, 140, 143, 27);
        add(isTeacherLabel);

        isTeacherBox = new JComboBox<>();
        isTeacherBox.setModel(new DefaultComboBoxModel(new String[]{"Yes", "No"}));
        isTeacherBox.setBounds(251, 146, 81, 15);
        add(isTeacherBox);
    }

    /**
     * This method is called whenever the observed object is changed. An application calls an <tt>Observable</tt>
     * object's <code>notifyObservers</code> method to have all the object's observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(java.util.Observable o, Object arg) {
        final Object finalArg = arg;
        final GotchaClientApp finalFrame = frame;
        SwingUtilities.invokeLater(() -> {

            if( finalArg instanceof Boolean ) {
                System.out.println("arg: " + finalArg);
                System.out.println("update thread: " + Thread.currentThread().getName());

                if( (boolean) arg ) {
                    JOptionPane.showMessageDialog(finalFrame,
                            "Sign-up successful.",
                            "Information", JOptionPane.INFORMATION_MESSAGE);

//                    finalFrame.revalidate();
//                    finalFrame.repaint();

                    SwingUtilities.invokeLater(() -> {
                        finalFrame.setContentPane(finalFrame.login);
                        finalFrame.setTitle("Sign in");
                        System.out.println("invokeLater thread: " + Thread.currentThread().getName());

                    });


                } else {
                    JOptionPane.showMessageDialog(null,
                            "Sign-up failed: duplicated username.",
                            "Information", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }
}
