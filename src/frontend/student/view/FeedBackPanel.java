package frontend.student.view;

import static java.awt.EventQueue.invokeLater;
import static javax.swing.UIManager.*;

public class FeedBackPanel {

    private javax.swing.JFrame frame;

    public static void main(String[] args) {
        invokeLater(frontend.student.view.FeedBackPanel:: run
        );
    }

    private static void run() {
        try {
            setLookAndFeel(getSystemLookAndFeelClassName());
        } catch(ClassNotFoundException ex) {
        } catch(InstantiationException ex) {
        } catch(IllegalAccessException ex) {
        } catch(javax.swing.UnsupportedLookAndFeelException ex) {
        } finally {


            FeedBackPanel window = new FeedBackPanel();
            window.frame.setVisible(true);

        }
    }

    public FeedBackPanel() {
        initialize();
    }

    public void initialize(){
        frame = new javax.swing.JFrame();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.setBounds(100,100,874, 563);
        frame.getContentPane().setLayout(null);




    }
}
