package de.tum.in.flowgame.server;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;

import com.opensymphony.xwork2.ActionSupport;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import de.tum.in.flowgame.dao.GameSessionDAO;
import de.tum.in.flowgame.dao.GameSessionDAOImpl;
import de.tum.in.flowgame.dao.PersonDAO;
import de.tum.in.flowgame.dao.PersonDAOImpl;
import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.model.Score;

public class PersonalHighscoreChartDownloadAction extends ActionSupport {

	private class ScoreComparator implements Comparator<Score> {
		public int compare(Score a, Score b) {
			if (a.getId() == b.getId())
				return 0;
			else if (a.getId() > b.getId())
				return 1;
			else
				return -1;
		}
	}
	
	private final static Log log = LogFactory.getLog(PersonalHighscoreChartDownloadAction.class);
	public InputStream response;
	private long personId;

	public void setId(int id) {
		this.personId = id;
	}

	// public String execute(Object highscoreRequest) throws Exception {
	// long personId = ((HighscoreRequest) highscoreRequest).getPersonId();
	// int numElements = ((HighscoreRequest) highscoreRequest).getNumElements();
	public String execute() throws Exception {
		GameSessionDAO gsDAO = new GameSessionDAOImpl();
		List<Score> scores = gsDAO.getPersonalScores(personId);
		Collections.sort(scores, new ScoreComparator());
		DefaultTableXYDataset xyd = new DefaultTableXYDataset();
		PersonDAO pDAO = new PersonDAOImpl();
		Person player = pDAO.find(personId);
		String playerName = player.getName();
		XYSeries xys = new XYSeries(playerName, false, false);
		int i = 0;
		for (Score s : scores) {
			xys.add(i++, s.getScore());
		}
		xyd.addSeries(xys);

		JFreeChart chart = createXYLineChart(playerName, "Game", "Points", xyd, PlotOrientation.VERTICAL, false);
		BufferedImage bi = chart.createBufferedImage(500, 300);
		ByteOutputStream bos = new ByteOutputStream();
		ImageIO.write(bi, "png", bos);
		this.response = new ByteArrayInputStream(bos.getBytes());
		return SUCCESS;
	}

	public InputStream getInputStream() {
		return response;
	}

	private JFreeChart createXYLineChart(String title, String xAxisLabel, String yAxisLabel, XYDataset dataset,
			PlotOrientation orientation, boolean legend) {

		if (orientation == null) {
			throw new IllegalArgumentException("Null 'orientation' argument.");
		}
		NumberAxis xAxis = new NumberAxis(xAxisLabel);
		xAxis.setAutoRangeIncludesZero(false);
		NumberAxis yAxis = new NumberAxis(yAxisLabel);
		XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
//		XYSplineRenderer renderer = new XYSplineRenderer();
//		renderer.setSeriesShapesVisible(0, false);
		renderer.setBaseOutlinePaint(Color.BLUE);
		renderer.setSeriesStroke(0, new BasicStroke(3f));
		renderer.setSeriesPaint(0, Color.BLUE);
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
		plot.setOrientation(orientation);

		JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, legend);
		return chart;

	}

}
