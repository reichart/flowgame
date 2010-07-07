package de.tum.in.flowgame.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.model.Answer;
import de.tum.in.flowgame.model.Question;
import de.tum.in.flowgame.model.Questionnaire;

public class QuestionnairePanel extends ChangeableComponent {

	private static final Log log = LogFactory.getLog(QuestionnairePanel.class);

	private Questionnaire questionnaire;
	private List<JPsychoSlider> sliders;

	private final ForceAnswersListener forceAnswers;

	public QuestionnairePanel(final Questionnaire q) {
		super(new BorderLayout());
		this.forceAnswers = new ForceAnswersListener();

		setQuestionnaire(q);
	}

	public List<Answer> getAnswers() {
		final List<Answer> answers = new ArrayList<Answer>();
		final List<Question> questions = questionnaire.getQuestions();
		for (int i = 0; i < questions.size(); i++) {
			final int value = (int) (100 * sliders.get(i).getValue());
			answers.add(new Answer(questions.get(i), value));
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
		sliders = new ArrayList<JPsychoSlider>();

		if (questionnaire.isLabelDriven()) {
			try {
				for (final Question question : qs) {
					final String[] split = question.getText().split(Question.separator);
					final JLabel label1 = new JLabel(split[0], SwingConstants.RIGHT);
					final JLabel label2 = new JLabel(split[1], SwingConstants.LEFT);
					final JPsychoSlider slider = new JPsychoSlider();
					slider.addChangeListener(forceAnswers);

					// build UI
					questions.add(Box.createHorizontalStrut(60));
					questions.add(label1);
					questions.add(slider);
					questions.add(label2);
					questions.add(Box.createHorizontalStrut(60));
					
					// remember slider
					sliders.add(slider);
				}
				
				// Lay out the panel.
				SpringUtilities.makeCompactGrid(questions,
						qs.size(), 5, // rows, cols
						6, 6, // initX, initY
						6, 18); // xPad, yPad
			} catch (final ArrayIndexOutOfBoundsException e) {
				log.error("Question is not defined.", e);
			}
		} else {
			for (final Question question : qs) {
				final JTextArea text = new JTextArea(question.getText());
				text.setColumns(20);
				text.setLineWrap(true);
				text.setWrapStyleWord(true);
				text.setForeground(Color.WHITE);

				final JLabel label1 = new JLabel("Trifft nicht zu", SwingConstants.RIGHT);
				final JLabel label2 = new JLabel("Trifft zu", SwingConstants.LEFT);
				
				final JPsychoSlider slider = new JPsychoSlider();
				slider.addChangeListener(forceAnswers);
				
				// "labels above slider" border layout:
				// <west east> (labels)
				// ---south--- (slider)
				final JPanel labelledSlider = new JPanel(new BorderLayout());
				labelledSlider.add(slider, BorderLayout.SOUTH);
				labelledSlider.add(label1, BorderLayout.WEST);
				labelledSlider.add(label2, BorderLayout.EAST);
				
				// build UI
				questions.add(text);
				questions.add(Box.createHorizontalGlue());
				questions.add(labelledSlider);
				
				// remember slider
				sliders.add(slider);
			}

			// Lay out the panel.
			SpringUtilities.makeCompactGrid(questions, qs.size(), 3, // rows, cols
					6, 6, // initX, initY
					6, 18); // xPad, yPad
		}

		this.add(questions, BorderLayout.CENTER);
	}

	private class ForceAnswersListener implements ChangeListener, Serializable {
		public void stateChanged(final ChangeEvent e) {
			fireChange(e);
		}
	}

	public boolean isCompletelyAnswered() {
		for (final JPsychoSlider slider : sliders) {
			if (slider.getValue() == null) {
				return false;
			}
		}
		return true;
	}

	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void reset() {
		// repaintQuestions();
	}

	@Override
	public String toString() {
		return getClass().getName() + "[" + questionnaire + "]";
	}
}
