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
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.basic.BasicSliderUI;

import de.tum.in.flowgame.model.Answer;
import de.tum.in.flowgame.model.Question;
import de.tum.in.flowgame.model.Questionnaire;

public class QuestionnairePanel extends JPanel implements ActionListener {

	private final JButton submitButton;
	private final Questionnaire questionnaire;
	private final List<JSlider> sliders;
	private List<Answer> answers;

	/**
	 * Scrolls directly to the clicked value instead of only moving the knob
	 * slightly in the direction.
	 */
	private static class DirectClickSliderUI extends BasicSliderUI {

		private DirectClickSliderUI(final JSlider slider) {
			super(slider);
		}

		@Override
		protected void scrollDueToClickInTrack(final int direction) {
			final int value;

			switch (slider.getOrientation()) {
				case SwingConstants.HORIZONTAL:
					value = valueForXPosition(slider.getMousePosition().x);
					break;

				case SwingConstants.VERTICAL:
					value = valueForYPosition(slider.getMousePosition().y);
					break;

				default:
					value = slider.getValue();
					break;
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

		JPanel titlePanel = new JPanel(new BorderLayout());
		titlePanel.add(new JLabel(questionnaire.getName()), BorderLayout.NORTH);
		final JTextArea textArea = new JTextArea(questionnaire.getDescription());
		textArea.setEditable(false);
		textArea.setEnabled(false);
		textArea.setLineWrap(true);
		textArea.setOpaque(false);
		titlePanel.add(textArea, BorderLayout.CENTER);
		
		final JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		submitButton = new JButton("Submit");
		submitButton.addActionListener(this);
		submitPanel.add(submitButton);

		this.add(questions, BorderLayout.CENTER);
		this.add(titlePanel, BorderLayout.NORTH);
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
		//TODO: get all questionnaires via httpclient
//		QuestionnaireDAO qdao = new QuestionnaireDAOImpl();
//		List<Questionnaire> qs = qdao.findAll();
		List<Questionnaire> qs = new ArrayList<Questionnaire>();
		final JPanel p = new QuestionnairePanel(qs.get(0));

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