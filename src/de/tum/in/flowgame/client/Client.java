package de.tum.in.flowgame.client;

import java.io.IOException;
import java.util.List;
import java.util.SortedSet;

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
import de.tum.in.flowgame.model.Highscore;
import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.model.Questionnaire;
import de.tum.in.flowgame.model.ScenarioSession;
import de.tum.in.flowgame.model.Score;

public class Client {

	private static final Log log = LogFactory.getLog(Client.class);

	private final String UPLOAD_URL;
	private final String DOWNLOAD_PERSON_URL;
	private final String DOWNLOAD_PERCENTAGE;
	private final String DOWNLOAD_SCENARIOSESSION;
	private final String DOWNLOAD_HIGHSCORES_URL;
	private final String DOWNLOAD_PERSONAL_HIGHSCORES_SET;
	private final String DOWNLOAD_QUESTIONNAIRES;
	
	private final HttpClient client;

	public Client(final String server) {
		this.client = new HttpClient(new MultiThreadedHttpConnectionManager());

		final String ext = ".action";
		
		this.UPLOAD_URL = server + "upload" + ext;
		this.DOWNLOAD_PERSON_URL = server + "personDownload" + ext;
		this.DOWNLOAD_PERCENTAGE = server + "percentageDownload" + ext;
		this.DOWNLOAD_SCENARIOSESSION = server + "scenarioSessionDownload" + ext;
		this.DOWNLOAD_HIGHSCORES_URL = server + "highscoresDownload" + ext;
		this.DOWNLOAD_PERSONAL_HIGHSCORES_SET = server + "personalHighscoresSetDownload" + ext;
		this.DOWNLOAD_QUESTIONNAIRES = server + "questionnaireDownload" + ext;
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

	public Person downloadPerson(final long id) {
		try {
			return execute(DOWNLOAD_PERSON_URL, id);
		} catch (final Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public List<Questionnaire> downloadQuestionnaires(final List<String> names) {
		try {
			return execute(DOWNLOAD_QUESTIONNAIRES, names);
		} catch (final Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	//Return highscore of a list of persons depending on their id
	public List<Highscore> getHighscores(final List<Long> persons) {
		try {
			return execute(DOWNLOAD_HIGHSCORES_URL, persons);
		} catch (final Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public Integer getPercentage(final long score){
		try {
			return execute(DOWNLOAD_PERCENTAGE, score);
		} catch (final Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public ScenarioSession downloadScenarioSession(final long playerId) throws IOException {
		return execute(DOWNLOAD_SCENARIOSESSION, playerId);
	}

	public SortedSet<Score> downloadPersonHighscoreSet(final long playerId) throws IOException {
		return execute(DOWNLOAD_PERSONAL_HIGHSCORES_SET, playerId);
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
				throw new IOException(post.getURI() + ": " + post.getStatusLine().toString());
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