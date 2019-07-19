package frontend.teacher.view.fragment;

import frontend.teacher.model.AnswerPanelModel;

import javax.swing.*;

public class AnswerPanel extends JPanel {

    private AnswerPanelModel answerPanelModel;

    private JButton button = new JButton("Add");

    private AnswerList answerList;

    public AnswerPanel(AnswerPanelModel answerPanelModel) {
        this.answerPanelModel = answerPanelModel;
        answerList = new AnswerList(answerPanelModel);

        setBorder(BorderFactory.createTitledBorder("Answers:"));
//        setPreferredSize(new Dimension(250, 300));

        add(answerList);
    }
}
