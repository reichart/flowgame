package de.tum.in.flowgame.client;

import java.io.IOException;

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

public class Client {

	private static final Log log = LogFactory.getLog(Client.class);

	private final String UPLOAD_URL;
	private final String DOWNLOAD_PERSON_URL;
	private final String DOWNLOAD_SCENARIOSESSION;

	private final HttpClient client;

	public Client(final String server) {
		this.client = new HttpClient(new MultiThreadedHttpConnectionManager());
		
		this.UPLOAD_URL = server + "upload.action";
		this.DOWNLOAD_PERSON_URL = server + "personDownload.action";
		this.DOWNLOAD_SCENARIOSESSION = server + "scenarioSessionDownload.action";
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
			return (Person) execute(DOWNLOAD_PERSON_URL, id);
		} catch (final Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public ScenarioSession downloadScenarioSession(final Person person) throws IOException {
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
	private Object execute(final String url, final Object parameter) throws IOException {
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

			return Utils.bytesToObject(IOUtils.toByteArray(post.getResponseBodyAsStream()));
		} finally {
			post.releaseConnection();
		}
	}
}