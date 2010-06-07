package de.tum.in.flowgame.ui.screens;

import java.awt.CardLayout;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.model.Answer;
import de.tum.in.flowgame.model.Questionnaire;
import de.tum.in.flowgame.ui.QuestionnairePanel;

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

	private List<QuestionnairePanel> questionnairePanels;

	private final JButton next = new JButton(new AbstractAction("Continue") {

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
		super(BorderFactory.createEmptyBorder(BORDER_WIDTH_TOP, BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH));

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
		return centered(title, mainUI(), next);
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
			final JScrollPane qscrollpane = new JScrollPane(qPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			cardPanel.add(qscrollpane, questionnaire.getName());
			questionnairePanels.add(qPanel);
		}

		update();
	}

	private void update() {
		final QuestionnairePanel questionnairePanel = questionnairePanels.get(currentPanel);
		questionnairePanel.reset();

		final Questionnaire qn = questionnairePanel.getQuestionnaire();
		title.setText(qn.getTitel());
		description.setText(qn.getDescription());

		cardLayout.show(cardPanel, qn.getName());
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

}
