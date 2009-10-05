package de.tum.in.flowgame.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import de.tum.in.flowgame.model.Answer;
import de.tum.in.flowgame.model.Question;
import de.tum.in.flowgame.model.Questionnaire;

public class QuestionnairePanel extends JPanel {

	private Questionnaire questionnaire;
	private List<JSlider> sliders;
	private List<Answer> answers;

//	/**
//	 * Scrolls directly to the clicked value instead of only moving the knob
//	 * slightly in the direction.
//	 */
//	private static class DirectClickSliderUI extends BasicSliderUI {
//
//		private DirectClickSliderUI(final JSlider slider) {
//			super(slider);
//		}
//
//		@Override
//		protected void scrollDueToClickInTrack(final int direction) {
//			final int value;
//
//			final Point mouse = slider.getMousePosition();
//			switch (slider.getOrientation()) {
//			case SwingConstants.HORIZONTAL:
//				value = valueForXPosition(mouse.x);
//				break;
//
//			case SwingConstants.VERTICAL:
//				value = valueForYPosition(mouse.y);
//				break;
//
//			default:
//				value = slider.getValue();
//				break;
//			}
//			slider.setValue(value);
//		}
//	}

	public QuestionnairePanel() {
		super(new BorderLayout());
	}

	public List<Answer> getAnswers() {
		answers = new ArrayList<Answer>();
		final List<Question> questions = questionnaire.getQuestions();
		for (int i = 0; i < questions.size(); i++) {
			answers.add(new Answer(questions.get(i), sliders.get(i).getValue()));
		}
		return answers;
	}
	
	public void setQuestionnaire(Questionnaire questionnaire) {
		this.removeAll();
		
		this.questionnaire = questionnaire;
		
		// Create the label table
		final Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(0, new JLabel("Low"));
		labelTable.put(100, new JLabel("High"));

		JPanel questions = new JPanel(new SpringLayout());

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
//			slider.setUI(new DirectClickSliderUI(slider));
			sliders.add(slider);
			questions.add(slider);
		}

		// Lay out the panel.
		SpringUtilities.makeCompactGrid(questions,
				this.questionnaire.getQuestions().size(), 2, // rows, cols
				6, 6, // initX, initY
				6, 6); // xPad, yPad

		final JTextArea textArea = new JTextArea(questionnaire.getDescription());
		textArea.setEditable(false);
		textArea.setEnabled(false);
		textArea.setLineWrap(true);
		textArea.setOpaque(false);
		
		final JPanel titlePanel = new JPanel(new BorderLayout());
		titlePanel.add(new JLabel(questionnaire.getName()), BorderLayout.NORTH);
		titlePanel.add(textArea, BorderLayout.CENTER);

		this.add(questions, BorderLayout.CENTER);
		this.add(titlePanel, BorderLayout.NORTH);
	}
}