package de.tum.in.flowgame.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.model.Answer;
import de.tum.in.flowgame.model.Question;
import de.tum.in.flowgame.model.Questionnaire;

public class QuestionnairePanel extends JPanel {

	private static final Log log = LogFactory.getLog(QuestionnairePanel.class);

	private Questionnaire questionnaire;
	private List<JSlider> sliders;
	
	public QuestionnairePanel(final Questionnaire q) {
		super(new BorderLayout());
		setQuestionnaire(q);
	}

	public List<Answer> getAnswers() {
		final List<Answer> answers = new ArrayList<Answer>();
		final List<Question> questions = questionnaire.getQuestions();
		for (int i = 0; i < questions.size(); i++) {
			answers.add(new Answer(questions.get(i), sliders.get(i).getValue()));
		}
		return answers;
	}

	private void setQuestionnaire(final Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
		repaintQuestions();
	}
	
	private void repaintQuestions() {
		removeAll();
		// Create Labels with Questions and add sliders to them
		final List<Question> qs = this.questionnaire.getQuestions();
		Collections.shuffle(qs);

		final JPanel questions = new JPanel(new SpringLayout());
		sliders = new ArrayList<JSlider>();

		if (questionnaire.isLabelDriven()) {
			try {
				for (final Question question : qs) {
					final String[] split = question.getText().split(Question.separator);
					final JLabel label1 = new JLabel(split[0]);
					final JLabel label2 = new JLabel(split[1]);
					final JSlider slider = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 50);
					questions.add(label1);
					questions.add(slider);
					sliders.add(slider);
					questions.add(label2);
				}
				// Lay out the panel.
				SpringUtilities.makeCompactGrid(questions,
						qs.size(), 3, // rows, cols
						6, 6, // initX, initY
						6, 6); // xPad, yPad
			} catch (final ArrayIndexOutOfBoundsException e) {
				log.error("Question is not defined.", e);
			}
		} else {
			// Create the label table
			final Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
			labelTable.put(0, new JLabel("Trifft nicht zu"));
			labelTable.put(100, new JLabel("Trifft zu"));
			for (final JLabel label : labelTable.values()) {
				label.setForeground(Color.WHITE);
			}
			for (final Question question : qs) {
				final JTextArea text = new JTextArea(question.getText());
				text.setLineWrap(true);
				text.setWrapStyleWord(true);
				text.setForeground(Color.WHITE);
				questions.add(text);

				final JSlider slider = new JSlider(SwingConstants.HORIZONTAL);
				slider.setValue(50);
				slider.setPaintTicks(true);
				slider.setLabelTable(labelTable);
				slider.setPaintLabels(true);

				sliders.add(slider);
				questions.add(slider);
			}

			// Lay out the panel.
			SpringUtilities.makeCompactGrid(questions, qs.size(), 2, // rows, cols
					6, 6, // initX, initY
					6, 6); // xPad, yPad
		}

		this.add(questions, BorderLayout.CENTER);
	}

	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void reset() {
//		repaintQuestions();
	}
	
}