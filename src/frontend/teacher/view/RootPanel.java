package frontend.teacher.view;

import frontend.teacher.model.RootPanelModel;

import javax.swing.*;
import java.awt.*;

/**
 * The main panel for the frontend.teacher client
 *
 * @author Daohan Chong
 * @version 2018-02-26
 */
public class RootPanel extends JPanel {

    private JPanel currentPanel;
    private PanelID currentPanelID;

    // Panels:
    private QuestionPanel questionPanel;

    // View models:
    private RootPanelModel rootPanelModel;

    public enum PanelID {
        QUESTION,
    }


    /**
     * Construct the RootPanel with rootPanelModel
     *
     * @param rootPanelModel is the view model
     */
    public RootPanel(RootPanelModel rootPanelModel) {
        this.rootPanelModel = rootPanelModel;

        // Initial set-ups
        setLayout(new GridLayout(0, 1));
        setPreferredSize(new Dimension(800, 600));

        // Construct singleton panels
        setUpSingletonPanels();

        // Switch to the basic panel
        switchPanel(PanelID.QUESTION);
    }

    private void setUpSingletonPanels() {
        questionPanel = new QuestionPanel(rootPanelModel.getQuestionPanelModel());
    }

    public void switchPanel(PanelID panelID) {
        if (panelID != currentPanelID) {
            removeCurrentPanel();
            switch (panelID) {
                case QUESTION:
                    setCurrentPanel(questionPanel);
            }
            currentPanelID = panelID;
        }

    }

    public JPanel getCurrentPanel() {
        return currentPanel;
    }

    private void setCurrentPanel(JPanel newPanel) {
        this.currentPanel = newPanel;
        // add
        add(newPanel);
        // TODO: update bounds
    }

    private void removeCurrentPanel() {
        if (getCurrentPanel() != null) {
            remove(getCurrentPanel());
            setCurrentPanel(null);
        }
    }

}
