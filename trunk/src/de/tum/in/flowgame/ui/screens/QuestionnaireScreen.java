package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.util.List;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.model.Answer;
import de.tum.in.flowgame.model.Questionnaire;
import de.tum.in.flowgame.ui.GameMenu;
import de.tum.in.flowgame.ui.QuestionnairePanel;

/**
 * Abstract base class for displaying a {@link Questionnaire}.
 */
public abstract class QuestionnaireScreen extends MenuScreen {

	private final QuestionnairePanel qpanel;
	private final JScrollPane qscrollpane;
	private final JLabel title;

	public QuestionnaireScreen(final GameMenu menu) {
		super(menu);
		this.title = title("[notitle]");
		this.qpanel = new QuestionnairePanel();
		this.qscrollpane = new JScrollPane(qpanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	}

	@Override
	public Container getContents() {
		return centered(title, qscrollpane, new JButton(next()));
	}

	@Override
	public final void update(final GameLogic logic) throws Exception {
		final Questionnaire q = updateQuestionnaire(logic);
		title.setText(q.getName());
		qpanel.setQuestionnaire(q);
	}

	/**
	 * @return the list of {@link Answer}s for the currently displayed
	 *         {@link Questionnaire}
	 */
	protected List<Answer> getAnswers() {
		return qpanel.getAnswers();
	}

	/**
	 * @return action to proceed to next screen
	 */
	protected abstract Action next();

	/**
	 * Called every time before the screen is displayed.
	 * 
	 * @param logic
	 * @return a (possibly updated) {@link Questionnaire} to display
	 * 
	 * @see #update(GameLogic)
	 */
	protected abstract Questionnaire updateQuestionnaire(GameLogic logic) throws Exception;

}