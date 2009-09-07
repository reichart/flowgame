package de.tum.in.flowgame.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.model.ScenarioSession;

public class Client {

	private static final Log log = LogFactory.getLog(Client.class);

	private static String UPLOAD_URL = "http://localhost:8080/flowgame/upload.action";
	private static String DOWNLOAD_PERSON_URL = "http://localhost:8080/flowgame/communicate/personDownload.action";
	private static String DOWNLOAD_SCENARIOSESSION = "http://localhost:8080/flowgame/communicate/scenarioSessionDownload.action";

	private final static HttpClient client = new HttpClient();

	public static void uploadQuietly(final Object entity) {
		try {
			upload(entity);
		} catch (final IOException ex) {
			log.error("failed to upload " + entity, ex);
		}
	}

	private static void upload(final Object entity) throws IOException {
		final Part[] parts = { new FilePart("file", new ByteArrayPartSource("file.dat", objectToBytes(entity))) };
		execute(UPLOAD_URL, parts);
	}

	public static Person downloadPerson(final long id) throws IOException {
		final Part[] parts = { new StringPart("id", String.valueOf(id)) };
		return (Person) execute(DOWNLOAD_PERSON_URL, parts);
	}

	public static ScenarioSession downloadScenarioSession(final Person person) throws IOException {
		final Part[] parts = { new FilePart("person", new ByteArrayPartSource("person.dat", objectToBytes(person))) };
		return (ScenarioSession) execute(DOWNLOAD_SCENARIOSESSION, parts);
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
	private static Object execute(final String url, final Part[] parts) throws IOException {
		final PostMethod post = new PostMethod(url);
		try {
			final MultipartRequestEntity requestEntity = new MultipartRequestEntity(parts, post.getParams());
			post.setRequestEntity(requestEntity);

			final int statusCode = client.executeMethod(post);
			if (statusCode != HttpStatus.SC_OK) {
				throw new IOException(post.getStatusLine().toString());
			}

			return parseResponse(post);
		} finally {
			post.releaseConnection();
		}
	}

	/**
	 * Serializes an Object into a byte array.
	 * 
	 * @param obj
	 *            the object to serialize
	 * @return a byte array
	 * @throws IOException
	 */
	private static byte[] objectToBytes(final Object obj) throws IOException {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		oos.close();
		return bos.toByteArray();
	}

	/**
	 * Parses the response body by trying to deserialize it into a Java Object.
	 * 
	 * @param post
	 *            the already executed POST method
	 * @return the deserialized Java Object
	 * @throws IOException
	 */
	private static Object parseResponse(final PostMethod post) throws IOException {
		final InputStream in = post.getResponseBodyAsStream();
		try {
			final ObjectInputStream ois = new ObjectInputStream(in);
			final Object obj = ois.readObject();

			if (obj instanceof String) {
				// TODO use HTTP status codes for errors so we can actually use
				// String payloads
				throw new IOException("Server reported error: " + obj);
			} else {
				return obj;
			}
		} catch (final ClassNotFoundException ex) {
			throw new IOException(ex);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

}