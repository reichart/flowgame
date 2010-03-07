package de.tum.in.flowgame.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.PartSource;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.Utils;
import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.model.ScenarioSession;
import de.tum.in.flowgame.model.Score;

public class Client {

	private static final Log log = LogFactory.getLog(Client.class);

	private final String UPLOAD_URL;
	private final String DOWNLOAD_PERSON_URL;
	private final String DOWNLOAD_SCENARIOSESSION;
	private final String DOWNLOAD_HIGH_SCORE_URL;
	private final String DOWNLOAD_PERSONAL_HIGHSCORE;
	private final String DOWNLOAD_PERSONAL_HIGHSCORE_CHART;

	private final HttpClient client;

	public Client(final String server) {
		this.client = new HttpClient(new MultiThreadedHttpConnectionManager());

		this.UPLOAD_URL = server + "upload.action";
		this.DOWNLOAD_PERSON_URL = server + "personDownload.action";
		this.DOWNLOAD_SCENARIOSESSION = server + "scenarioSessionDownload.action";
		this.DOWNLOAD_HIGH_SCORE_URL = server + "highscoreDownload.action";
		this.DOWNLOAD_PERSONAL_HIGHSCORE = server + "personalHighscoreDownload.action";
		this.DOWNLOAD_PERSONAL_HIGHSCORE_CHART = server + "personalHighscoreChartDownload.action";
	}

	public void uploadQuietly(final Object entity) {
		try {
			upload(entity);
		} catch (final IOException ex) {
			log.error("failed to upload " + entity, ex);
		}
	}

	private void upload(final Object entity) throws IOException {
		execute(UPLOAD_URL, entity);
	}

	public Person downloadPerson(final long id) throws IOException {
		try {
			return execute(DOWNLOAD_PERSON_URL, id);
		} catch (final Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public Long getHighscore(final long id) {
		try {
			return execute(DOWNLOAD_HIGH_SCORE_URL, id);
		} catch (final Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public ScenarioSession downloadScenarioSession(final Person person) throws IOException {
		return execute(DOWNLOAD_SCENARIOSESSION, person);
	}

	public List<Score> downloadPersonHighscore(long personId, int numElements) throws IOException {
		HighscoreRequest highscoreRequest = new HighscoreRequest(personId, numElements);
		return execute(DOWNLOAD_PERSONAL_HIGHSCORE, highscoreRequest);
	}

	public BufferedImage downloadPersonHighscoreChart(final long personId) throws IOException {
		final PostMethod post = new PostMethod(DOWNLOAD_PERSONAL_HIGHSCORE_CHART + "?id=" + personId);
		try {
			final int statusCode = client.executeMethod(post);
			if (statusCode != HttpStatus.SC_OK) {
				throw new IOException(post.getStatusLine().toString());
			}
			return ImageIO.read(post.getResponseBodyAsStream());
		} finally {
			post.releaseConnection();
		}
	}

	/**
	 * Executes a POST request to a URL with a number of multipart parts.
	 * 
	 * @param url
	 *            the URL to POST to
	 * @param parts
	 *            the parts to send as multipart request entity
	 * @return the Java object returned by the server
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private <T> T execute(final String url, final Object parameter) throws IOException {
		final PostMethod post = new PostMethod(url);
		try {
			final PartSource source = new ByteArrayPartSource("data.ser", Utils.objectToBytes(parameter));
			final Part[] parts = { new FilePart("data", source) };

			final MultipartRequestEntity requestEntity = new MultipartRequestEntity(parts, post.getParams());
			post.setRequestEntity(requestEntity);

			final int statusCode = client.executeMethod(post);
			if (statusCode != HttpStatus.SC_OK) {
				throw new IOException(post.getStatusLine().toString());
			}

			final Object response = Utils.bytesToObject(IOUtils.toByteArray(post.getResponseBodyAsStream()));
			if (response instanceof Throwable) {
				throw new IOException((Throwable) response);
			} else {
				return (T) response;
			}
		} finally {
			post.releaseConnection();
		}
	}
}