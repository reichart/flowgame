package de.tum.in.flowgame.client;

import java.io.InputStream;
import java.io.ObjectInputStream;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;

import de.tum.in.flowgame.model.Person;

public class DownloadPerson {

	public static String url = "http://localhost:8080/flowgame/communicate/personDownload.action";

	public Person download(Long id) throws Exception {
		PostMethod post = new PostMethod(url);
		try {
			Part[] parts = { new StringPart("id", id.toString()) };

			// Create a method instance.
			// GetMethod method = new GetMethod(url);
			MultipartRequestEntity requestEntity = new MultipartRequestEntity(parts, post.getParams());
			post.setRequestEntity(requestEntity);

			// Provide custom retry handler is necessary
			post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

			// Create an instance of HttpClient.
			HttpClient client = new HttpClient();

			// Execute the method.
			int statusCode = client.executeMethod(post);
			System.out.println(statusCode);

			InputStream in = post.getResponseBodyAsStream();
			ObjectInputStream ois = new ObjectInputStream(in);
			Object o = ois.readObject();
			
			Person person = null;
			if (o instanceof Person) {
				person = (Person) o;
				System.out.println(person.getName());
				System.out.println(person.getPlace());
				System.out.println(person.getSex());
				System.out.println(person.getDateOfBirth());				
			} else if (o instanceof String) {
				System.out.println((String) o);
			}
			
			in.close();

			return person;
		} finally {
			// release the connection
			post.releaseConnection();
		}
	}

	public static void main(String[] args) throws Exception {
		DownloadPerson dp = new DownloadPerson();
		dp.download(new Long(12345L));
	}

}