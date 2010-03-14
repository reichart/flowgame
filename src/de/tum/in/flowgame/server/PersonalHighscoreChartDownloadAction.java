package de.tum.in.flowgame.server;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;

import de.tum.in.flowgame.model.GameRound;
import de.tum.in.flowgame.model.GameSession;
import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.model.Score;

public class PersonalHighscoreChartDownloadAction extends DatabaseAction {

	private InputStream response;
	private long personId;

	@Override
	public String execute() throws Exception {
		final Person player = em.find(Person.class, personId);
		final SortedSet<Score> scores = getPersonalScores(personId);
		
		System.err.println(scores);
		
		final JFreeChart chart = new JFreeChart(player.getName(), JFreeChart.DEFAULT_TITLE_FONT, createPlot(createSeries(scores, player)), false);
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(chart.createBufferedImage(500, 300), "png", bos);
		this.response = new ByteArrayInputStream(bos.toByteArray());
		
		return SUCCESS;
	}

	private static DefaultTableXYDataset createSeries(final SortedSet<Score> scores, final Person player) {
		final XYSeries series = new XYSeries(player.getName(), false, false);
		int i = 0;
		for (final Score s : scores) {
			series.add(i++, s.getScore());
		}
		
		final DefaultTableXYDataset dataset = new DefaultTableXYDataset();
		dataset.addSeries(series);
		return dataset;
	}

	private static XYPlot createPlot(final DefaultTableXYDataset xyd) {
		final NumberAxis xAxis = new NumberAxis("Game");
		xAxis.setAutoRangeIncludesZero(false);
		final NumberAxis yAxis = new NumberAxis("Points");
		final XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
		// XYSplineRenderer renderer = new XYSplineRenderer();
		// renderer.setSeriesShapesVisible(0, false);
		renderer.setBaseOutlinePaint(Color.BLUE);
		renderer.setSeriesStroke(0, new BasicStroke(3f));
		renderer.setSeriesPaint(0, Color.BLUE);
		final XYPlot plot = new XYPlot(xyd, xAxis, yAxis, renderer);
		plot.setOrientation(PlotOrientation.VERTICAL);
		return plot;
	}

	public SortedSet<Score> getPersonalScores(final long personId) {
		final List<GameSession> sessions = em
			.createQuery("SELECT gs FROM GameSession gs WHERE gs.player.id=:id", GameSession.class)
			.setParameter("id", personId)
			.getResultList();
		
		final SortedSet<Score> result = new TreeSet<Score>();
		for (final GameSession session : sessions) {
			for (final GameRound round : session.getRounds()) {
				System.err.println(round.getStartTime() + ": " + round.getScore());
				result.add(new Score(round.getStartTime(), round.getScore()));
			}
		}
		return result;
	}
	
	public InputStream getInputStream() {
		return response;
	}

	public void setId(final int id) {
		this.personId = id;
	}
}
