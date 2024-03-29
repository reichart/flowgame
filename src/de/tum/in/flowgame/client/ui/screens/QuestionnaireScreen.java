package de.tum.in.flowgame.client.ui.screens;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.client.ui.QuestionnairePanel;
import de.tum.in.flowgame.model.Answer;
import de.tum.in.flowgame.model.Questionnaire;

/**
 * Abstract base class for displaying a {@link Questionnaire}.
 */
public abstract class QuestionnaireScreen extends MenuScreen {
	private final static Log log = LogFactory.getLog(QuestionnaireScreen.class);

	private final CardLayout cardLayout;
	private final JPanel cardPanel;

	private final JTextArea description;
	private final JLabel title = title("");

	private int currentPanel;

	private long answeringStartTime;

	private List<QuestionnairePanel> questionnairePanels;

	private final ForceAnswersListener forceAnswers;

	private static final String NEXT_ENABLED = UIMessages.CONTINUE;
	private static final String NEXT_DISABLED = UIMessages.getString("next.disabled");

	private final JButton next = new JButton(new AbstractAction() {

		public void actionPerformed(final ActionEvent e) {
			final boolean moreQuestionnaires = currentPanel < questionnairePanels.size() - 1;
			if (moreQuestionnaires) {
				currentPanel++;
				update();
			} else {
				next().actionPerformed(e);
			}
		}

	});

	public QuestionnaireScreen() {
		forceAnswers = new ForceAnswersListener();

		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);

		description = new JTextArea();
		description.setEditable(false);
		description.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		description.setWrapStyleWord(true);
		description.setLineWrap(true);
	}

	private JComponent mainUI() {
		final JComponent panel = new Box(BoxLayout.Y_AXIS);
		panel.add(description);
		panel.add(Box.createVerticalStrut(7));
		panel.add(cardPanel);
		return panel;
	}

	@Override
	public Container getContents() {
		return centered(BorderFactory.createEmptyBorder(BORDER_WIDTH_TOP, BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH), title, mainUI(), next);
	}

	@Override
	public void update(final GameLogic logic) throws Exception {
		log.info("building new qn panels");
		currentPanel = 0;

		cardPanel.removeAll();

		questionnairePanels = new ArrayList<QuestionnairePanel>();
		final List<Questionnaire> questionnaires = menu.getLogic().getClient().downloadQuestionnaires(
				getQuestionnaireNames());
		for (final Questionnaire questionnaire : questionnaires) {
			log.info("adding " + questionnaire.getName());
			final QuestionnairePanel qPanel = new QuestionnairePanel(questionnaire);
			qPanel.addChangeListener(forceAnswers);

			// TODO qn gets cut off with VERTICAL_SCROLLBAR_AS_NEEDED
			// work around is to always show vertical scrollbars
			final JScrollPane qscrollpane = new JScrollPane(qPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			cardPanel.add(qscrollpane, questionnaire.getName());
			questionnairePanels.add(qPanel);
		}

		update();
	}

	private void update() {
		// reset position of all scrollpanes to top
		for (final Component component : cardPanel.getComponents()) {
			if (component instanceof JScrollPane) {
				final JScrollPane scrollpane = (JScrollPane) component;
				scrollpane.getVerticalScrollBar().setValue(0);
			}
		}

		final QuestionnairePanel questionnairePanel = getCurrentPanel();
		questionnairePanel.reset();

		final Questionnaire qn = questionnairePanel.getQuestionnaire();
		title.setText(QuestionnaireMessages.getString(qn.getName() + ".title"));
		description.setText(QuestionnaireMessages.getString(qn.getName() + ".desc"));

		cardLayout.show(cardPanel, qn.getName());

		// disable until all questions are answered
		next.setEnabled(false);
		next.setText(NEXT_DISABLED);
		
		this.answeringStartTime = System.currentTimeMillis();
	}

	private QuestionnairePanel getCurrentPanel() {
		return questionnairePanels.get(currentPanel);
	}

	private class ForceAnswersListener implements ChangeListener {
		public void stateChanged(final ChangeEvent e) {
			// enable button only when all questions have been answered
			if (getCurrentPanel().isCompletelyAnswered()) {
				next.setEnabled(true);
				next.setText(NEXT_ENABLED);
			} else {
				next.setEnabled(false);
				next.setText(NEXT_DISABLED);
			}
		}
	}

	/**
	 * @return the list of {@link Answer}s for the currently displayed
	 *         {@link Questionnaire}
	 */
	protected List<Answer> getAnswers() {
		final List<Answer> answers = new ArrayList<Answer>();
		for (final QuestionnairePanel qPanel : questionnairePanels) {
			answers.addAll(qPanel.getAnswers());
		}
		return answers;
	}

	/**
	 * @return button to proceed to next screen
	 */
	protected abstract Action next();

	/**
	 * Called every time before the screen is displayed.
	 * 
	 * @param logic
	 * @return a (possibly updated) {@link Questionnaire} to display
	 * @see #update(GameLogic)
	 */

	/**
	 * Called once at Screen Creation
	 * 
	 * @return array with the names of the questionnaires the screen should
	 *         display
	 */
	protected abstract List<String> getQuestionnaireNames();

	/**
	 * @return the time in milliseconds since the questionnaire was displayed
	 */
	public long getAnsweringTime() {
		final long answeringTime = System.currentTimeMillis() - answeringStartTime;
		System.err.println("answering for " + getClass().getName() + ": " + answeringTime);
		return answeringTime;
	}
}
