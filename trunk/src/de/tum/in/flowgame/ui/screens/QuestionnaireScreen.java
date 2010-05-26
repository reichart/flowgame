package de.tum.in.flowgame.ui.screens;

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
	
	private final JPanel cardPanel;
	private final JTextArea task = new JTextArea();
	private CardLayout layout;
	private int currentPanel;
	private JLabel title;
	private final JButton next = new JButton(new AbstractAction("Continue") {
		
		public void actionPerformed(ActionEvent e) {
			if (currentPanel < questionnaires.size()-1) { // more questionnaires to display?
				layout.next(cardPanel);
				title.setText(questionnaires.get(currentPanel).getName());
				task.setText(questionnaires.get(currentPanel).getDescription());
				currentPanel++;
			} else {
				next().actionPerformed(e);
			}
		}
		
	});
	private List<Questionnaire> questionnaires;
	private final JPanel main;
	

	public QuestionnaireScreen() {
		super(BorderFactory.createEmptyBorder(BORDER_WIDTH_TOP, BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH));
		layout = new CardLayout();
		cardPanel = new JPanel(layout);
//		cardPanel.setPreferredSize(new Dimension(500, 350));
		currentPanel = 0;
		main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		
		task.setWrapStyleWord(true);
		task.setLineWrap(true);
		Font f = new Font(Font.SANS_SERIF, Font.BOLD, 12);
		task.setFont(f );
		
		main.add(task);
		main.add(Box.createVerticalStrut(7));
		main.add(cardPanel);
		
		questionnaires = new ArrayList<Questionnaire>();
	}

	@Override
	public Container getContents() {
		return centered(title = title(""), main, next);
	}

	@Override
	public final void update(final GameLogic logic) throws Exception {
		questionnaires = updateQuestionnaire(logic);
		currentPanel = 0;
		
		log.info("building new qn panels");
		
		cardPanel.removeAll();		
		for (Questionnaire questionnaire : questionnaires) {
			log.info("adding " + questionnaire.getName());
			QuestionnairePanel qPanel = new QuestionnairePanel(questionnaire);
			
			JScrollPane qscrollpane = new JScrollPane(qPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			cardPanel.add(qscrollpane, "Questionnaire" + questionnaires.indexOf(questionnaire));
		}
		
		title.setText(questionnaires.get(currentPanel).getName());
		task.setText(questionnaires.get(currentPanel).getDescription());
	}

	/**
	 * @return the list of {@link Answer}s for the currently displayed
	 *         {@link Questionnaire}
	 */
	protected List<Answer> getAnswers() {
		List<Answer> answers = new ArrayList<Answer>();
		for (Component component : cardPanel.getComponents()) {
			if (component instanceof QuestionnairePanel) {
				List<Answer> tempAnswers = ((QuestionnairePanel) component).getAnswers();
				for (Answer answer : tempAnswers) {
					answers.add(answer);
				}
			}
			
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
	 * 
	 * @see #update(GameLogic)
	 */
	protected abstract List<Questionnaire> updateQuestionnaire(GameLogic logic) throws Exception;

}