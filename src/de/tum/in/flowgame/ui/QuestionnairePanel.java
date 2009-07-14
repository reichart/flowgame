package de.tum.in.flowgame.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalSliderUI;

import de.tum.in.flowgame.model.Answer;
import de.tum.in.flowgame.model.Question;
import de.tum.in.flowgame.model.Questionnaire;

public class QuestionnairePanel extends JPanel implements ActionListener {

	private final JButton submitButton;
	private final Questionnaire questionnaire;
	private final List<JSlider> sliders;
	private ArrayList<Answer> answers;

	private class DirectClickSliderUI extends MetalSliderUI {
		JSlider slider;

		DirectClickSliderUI(JSlider slider) {
			this.slider = slider;
		}

		@Override
		protected void scrollDueToClickInTrack(int direction) {
			int value = slider.getValue();

			if (slider.getOrientation() == SwingConstants.HORIZONTAL) {
				value = this.valueForXPosition(slider.getMousePosition().x);
			} else if (slider.getOrientation() == SwingConstants.VERTICAL) {
				value = this.valueForYPosition(slider.getMousePosition().y);
			}
			slider.setValue(value);
		}
	}

	public QuestionnairePanel(final Questionnaire questionnaire) {
		super(new BorderLayout());

		this.questionnaire = questionnaire;

		// Create the label table
		final Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(0), new JLabel("Low"));
		labelTable.put(new Integer(100), new JLabel("High"));

		final JPanel questions = new JPanel(new SpringLayout());

		sliders = new ArrayList<JSlider>();
		// boolean first = true;
		// Create Labels with Questions and add sliders to them
		for (final Question question : this.questionnaire.getQuestions()) {
			questions.add(new JLabel(question.getText()));
			final JSlider slider = new JSlider(SwingConstants.HORIZONTAL);
			// if (first) {
			slider.setMinorTickSpacing(50);
			slider.setPaintTicks(true);
			slider.setLabelTable(labelTable);
			slider.setPaintLabels(true);
			// first = false;
			// }
			slider.setUI(new DirectClickSliderUI(slider));
			sliders.add(slider);
			questions.add(slider);
		}

		// Lay out the panel.
		SpringUtilities.makeCompactGrid(questions, this.questionnaire.getQuestions().size(), 2, // rows,
				// cols
				6, 6, // initX, initY
				6, 6); // xPad, yPad

		final JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		submitButton = new JButton("Submit");
		submitButton.addActionListener(this);
		submitPanel.add(submitButton);

		this.add(questions, BorderLayout.CENTER);
		this.add(new JLabel(this.questionnaire.getName()), BorderLayout.NORTH);
		this.add(submitPanel, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getSource() == submitButton) {
			answers = new ArrayList<Answer>();
			final List<Question> questions = questionnaire.getQuestions();
			for (int i = 0; i < questions.size(); i++) {
				final Answer answer = new Answer(questions.get(i), sliders.get(i).getValue());
				answers.add(answer);
			}
			for (final Answer answer : answers) {
				System.out.println(answer.getQuestion().getText() + ": " + answer.getAnswer());
			}
			System.out.println();
		}
	}

	public static void main(final String[] args) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		final Question q1 = new Question();
		q1.setText("I have a question 1. Please answer. You better answer quickly.");
		final Question q2 = new Question();
		q2.setText("I have a question 2. Please answer.");
		final Question q3 = new Question();
		q3.setText("I have a question 3. Please answer.");
		final Question q4 = new Question();
		q4.setText("I have a question 4. Please answer.");
		final Question q5 = new Question();
		q5.setText("I have a question 5. Please answer correctly.");
		final Question q6 = new Question();
		q6.setText("I have a question 6. Please answer.");
		final Question q7 = new Question();
		q7.setText("I have a question 7. Please answer.");

		final Questionnaire questionnaire = new Questionnaire();
		questionnaire.setName("Test");
		questionnaire.addQuestion(q1);
		questionnaire.addQuestion(q2);
		questionnaire.addQuestion(q3);
		questionnaire.addQuestion(q4);
		questionnaire.addQuestion(q5);
		questionnaire.addQuestion(q6);
		questionnaire.addQuestion(q7);

		final JPanel p = new QuestionnairePanel(questionnaire);

		// Create and set up the window.
		final JFrame frame = new JFrame("SpringForm");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set up the content pane.
		p.setOpaque(true); // content panes must be opaque
		frame.setContentPane(p);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

}