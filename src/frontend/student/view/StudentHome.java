package frontend.student.view;

import frontend.student.model.GameState;
import frontend.student.model.StudentHomeModel;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Observable;
import java.util.Observer;

import static frontend.GotchaClientApp.frame;

/**
 * The Student UI will act as the home page for the frontend.student. From the homepage they can view the current leader
 * board, start the quiz or exit the system. The structure of this UI is based on the Java's CardLayout class:
 * https://docs.oracle.com/javase/7/docs/api/java/awt/CardLayout.html Pre-condition: The user should have successfully
 * logged into the system or signed up to GOTCHA
 *
 * @author Melisha Trout
 * @Version 16/03/2018
 */
public class StudentHome extends JPanel implements Observer {
    private static String PRE_EXIT = "PRE_EXIT";
    private static String HOME_SCREEN = "HOMESCREEN";
    private static String EXIT = "EXIT";
    private static String LOADING_QUESTIONS = "LOADING_QUESTIONS";
    private static String QUOTE = "Productivity is never an accident\r\n" +
            "\r\n" +
            "It is always the result of a commitment to excellence, intelligent planning, and focused effort.\r\n";
    private JPanel exitScreen, homeScreen, currentHomeScreen, startQuizPanel, quotePanel, startQuiz, displayUsersName, buttonOptions, cards;
    private StudentHomeModel studentHomeModel;


    private JTextPane randomQuote;
    private JLabel welcomeMessage;
    private JLabel exitQuestion;
    private JButton startNewQuiz, viewCurrentLB, quitButton, yesExit;
    private JPanel decisionButtons;
    private JPanel loadingQuiz;


    /**
     * Connect the view to the corresponding model
     *
     * @param studentHomeModel The corresponding model for this view
     */
    public StudentHome(StudentHomeModel studentHomeModel) {
        this.studentHomeModel = studentHomeModel;
        this.studentHomeModel.addObserver(this);

        setUpUI();

    }

    public void setUpUI() {
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);
        cards = new JPanel();
        cards.setBorder(null);
        cards.setBackground(new Color(255, 255, 255));
        add(cards, BorderLayout.CENTER);


        /**
         * start building the card panel for the home page
         */
        cards.setLayout(new CardLayout(0, 0));
        homeScreen = new JPanel();
        homeScreen.setBorder(null);
        cards.add(homeScreen, HOME_SCREEN);
        homeScreen.setLayout(new BorderLayout(0, 0));

        currentHomeScreen = new JPanel();
        homeScreen.add(currentHomeScreen);
        currentHomeScreen.setBackground(Color.WHITE);
        currentHomeScreen.setLayout(new BorderLayout(0, 0));

        startQuizPanel = new JPanel();
        currentHomeScreen.add(startQuizPanel, BorderLayout.CENTER);
        startQuizPanel.setBackground(Color.WHITE);
        startQuizPanel.setLayout(new BorderLayout(0, 0));
        quotePanel = new JPanel();
        quotePanel.setBackground(Color.WHITE);
        startQuizPanel.add(quotePanel, BorderLayout.CENTER);
        quotePanel.setLayout(new BorderLayout(0, 0));

        StyleContext context = new StyleContext();
        context.getFont("Consolas", Font.ITALIC, 30);
        StyledDocument document = new DefaultStyledDocument(context);
        Style style = context.getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);
        try {
            document.insertString(document.getLength(), QUOTE, style);
        } catch(BadLocationException e) {
            e.printStackTrace();
        }

        randomQuote = new JTextPane(document);
        randomQuote.setEditable(false);
        randomQuote.setBackground(Color.WHITE);
        quotePanel.add(randomQuote);

        displayUsersName = new JPanel();
        currentHomeScreen.add(displayUsersName, BorderLayout.NORTH);
        displayUsersName.setBackground(new Color(255, 255, 255));

        FlowLayout fl_displayUsersName = new FlowLayout(FlowLayout.CENTER, 0, 40);
        displayUsersName.setLayout(fl_displayUsersName);

        welcomeMessage = new JLabel("Welcome back {username}");

        welcomeMessage.setLabelFor(displayUsersName);
        welcomeMessage.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeMessage.setFont(new Font("Consolas", Font.BOLD, 50));
        welcomeMessage.setBackground(new Color(255, 255, 255));
        welcomeMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        displayUsersName.add(welcomeMessage);

        buttonOptions = new JPanel();
        buttonOptions.setPreferredSize(new Dimension(40, 80));
        buttonOptions.setBorder(null);
        currentHomeScreen.add(buttonOptions, BorderLayout.SOUTH);
        buttonOptions.setBackground(Color.WHITE);

        // View the current leader board
        viewCurrentLB = new JButton("View Leader Board");
        viewCurrentLB.setPreferredSize(new Dimension(180, 30));
        viewCurrentLB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                studentHomeModel.goToLeaderBoard();
            }
        });

        buttonOptions.setLayout(new GridLayout(0, 2, 0, 5));
        viewCurrentLB.setFont(new Font("Consolas", Font.BOLD, 30));
        viewCurrentLB.setBackground(Color.WHITE);
        buttonOptions.add(viewCurrentLB);

        // Start the quiz
        startQuiz = new JPanel();
        startQuiz.setPreferredSize(new Dimension(10, 140));
        startQuiz.setBorder(null);
        startQuiz.setBackground(Color.WHITE);
        startQuizPanel.add(startQuiz, BorderLayout.SOUTH);

        startNewQuiz = new JButton("Start Quiz");
        startNewQuiz.setBounds(391, 33, 221, 69);
        startNewQuiz.setBackground(Color.WHITE);
        startNewQuiz.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                studentHomeModel.loadQuestions4Student();
            }
        });

        startQuiz.setLayout(null);
        startNewQuiz.setFont(new Font("Consolas", Font.BOLD, 30));
        startQuiz.add(startNewQuiz);

        // quit the session
        quitButton = new JButton("Quit");
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (cards.getLayout());
                cl.show(cards, PRE_EXIT);
            }
        });
        quitButton.setAlignmentX(30.0f);
        quitButton.setFont(new Font("Consolas", Font.BOLD, 30));
        quitButton.setBackground(Color.WHITE);
        buttonOptions.add(quitButton);

        /**
         * start building the panel for the pre-exit view
         */

        exitScreen = new JPanel();
        exitScreen.setBorder(null);
        exitScreen.setBackground(Color.WHITE);
        cards.add(exitScreen, PRE_EXIT);
        exitScreen.setLayout(new BorderLayout(0, 0));

        exitQuestion = new JLabel("Are you sure....");
        exitQuestion.setHorizontalAlignment(SwingConstants.CENTER);
        exitScreen.add(exitQuestion, BorderLayout.CENTER);
        exitQuestion.setFont(new Font("Consolas", Font.BOLD, 40));

        decisionButtons = new JPanel();
        decisionButtons.setPreferredSize(new Dimension(10, 80));
        decisionButtons.setBackground(Color.WHITE);
        exitScreen.add(decisionButtons, BorderLayout.SOUTH);
        decisionButtons.setLayout(new GridLayout(0, 2, 0, 0));

        JButton noStay = new JButton("NO");
        noStay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout c2 = (CardLayout) cards.getLayout();
                c2.show(cards, HOME_SCREEN);
            }
        });
        noStay.setBackground(new Color(102, 204, 0));
        decisionButtons.add(noStay);
        noStay.setFont(new Font("Consolas", Font.BOLD, 30));
        noStay.setAlignmentX(Component.CENTER_ALIGNMENT);

        yesExit = new JButton("YES");
        yesExit.addActionListener(e -> {
            frame.dispose();
            // TODO: find a way to close the client socket
            System.exit(0);
        });
        yesExit.setBackground(Color.LIGHT_GRAY);
        yesExit.setFont(new Font("Consolas", Font.BOLD, 30));
        decisionButtons.add(yesExit);


        /**
         * Create panel for loading questions for the quiz
         */

        loadingQuiz = new JPanel();
        loadingQuiz.setBackground(Color.WHITE);
        cards.add(loadingQuiz, LOADING_QUESTIONS);
        loadingQuiz.setLayout(new BorderLayout(0, 0));

        JButton goQuiz = new JButton("Start Quiz");
        goQuiz.setBorderPainted(false);
        goQuiz.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                studentHomeModel.loadQuestions4Student();
            }
        });
        goQuiz.setVisible(true);
        goQuiz.setEnabled(true);
        goQuiz.setFont(new Font("Consolas", Font.BOLD, 40));
        goQuiz.setHideActionText(true);
        loadingQuiz.add(goQuiz, BorderLayout.CENTER);


        JLabel loadingMessage = new JLabel("Loading Game...");
        loadingMessage.setPreferredSize(new Dimension(82, 400));
        loadingMessage.setMinimumSize(new Dimension(82, 400));
        loadingMessage.setLabelFor(loadingQuiz);
        loadingMessage.setHorizontalTextPosition(SwingConstants.CENTER);
        loadingMessage.setHorizontalAlignment(SwingConstants.CENTER);
        loadingMessage.setFont(new Font("Consolas", Font.BOLD, 40));
        loadingMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadingQuiz.add(loadingMessage, BorderLayout.NORTH);

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                Component c = (Component) evt.getSource();
                System.out.println("Student home resized, new size: " + c.getSize());
            }
        });
    }


    /**
     * This method is called whenever the observed object is changed. An application calls an <tt>Observable</tt>
     * object's <code>notifyObservers</code> method to have all the object's observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        //If a student has successfully logged in, set the welcome message.
        if( arg instanceof String ) {
            if( studentHomeModel.isInView ) {
                this.welcomeMessage.setText("Welcome back " + arg + "!");
            }
        }

        if( arg instanceof Enum ) {
            System.out.println("update: " + arg);
            if( arg == GameState.LEADERBOARD ) {
                SwingUtilities.invokeLater(() -> {
                    frame.remove(frame.studentHome);
                    frame.setContentPane(frame.leaderBoardPanel);
                    frame.setBounds(frame.getX(), frame.getY(), 1000, 600);
                    frame.setTitle("StudentClient: Leader Board");
                    System.out.println("invokeLater thread: " + Thread.currentThread().getName());
                });
            }
            if( arg == GameState.EXIT ) {
                SwingUtilities.invokeLater(() -> {
                    frame.dispose();
                });
            }
            if( arg == GameState.STARTGAME ) {
                System.out.println("update: " + arg);
                System.out.println("arg == STARTGAME");
                SwingUtilities.invokeLater(() -> {
                    frame.remove(frame.studentHome);
                    frame.setContentPane(frame.questionPanel);
                    frame.setTitle("StudentClient: Questions");
                    frame.revalidate();
                    System.out.println("finished");
                });
            }

            if( arg == GameState.HOME ) {
                SwingUtilities.invokeLater(() -> {
                    CardLayout c2 = (CardLayout) cards.getLayout();
                    c2.show(cards, HOME_SCREEN);
                });
            }

        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

//    	this.setSize(new Dimension(1000, 600));
//    	this.setPreferredSize(new Dimension(1000, 600));
    }
}
