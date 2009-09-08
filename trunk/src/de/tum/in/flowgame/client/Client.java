package de.tum.in.flowgame.client;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.PartSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.Utils;
import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.model.ScenarioSession;

public class Client {

	private static final Log log = LogFactory.getLog(Client.class);

	private static String BASE_URL = "http://localhost:8080/flowgame/";

	private static String UPLOAD_URL = BASE_URL + "upload.action";
	private static String DOWNLOAD_PERSON_URL = BASE_URL + "personDownload.action";
	private static String DOWNLOAD_SCENARIOSESSION = BASE_URL + "scenarioSessionDownload.action";

	private final static HttpClient client = new HttpClient();

	public static void uploadQuietly(final Object entity) {
		try {
			upload(entity);
		} catch (final IOException ex) {
			log.error("failed to upload " + entity, ex);
		}
	}

	private static void upload(final Object entity) throws IOException {
		execute(UPLOAD_URL, entity);
	}

	public static Person downloadPerson(final long id) throws IOException {
		try {
			return (Person) execute(DOWNLOAD_PERSON_URL, id);
		} catch (final Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static ScenarioSession downloadScenarioSession(final Person person) throws IOException {
		return (ScenarioSession) execute(DOWNLOAD_SCENARIOSESSION, person);
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
	private static Object execute(final String url, final Object parameter) throws IOException {
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

			return Utils.bytesToObject(post.getResponseBody());
		} finally {
			post.releaseConnection();
		}
	}
}