package de.tum.in.flowgame.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;

import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.model.Question;
import de.tum.in.flowgame.model.ScenarioRound;
import de.tum.in.flowgame.model.ScenarioSession;

public class DownloadScenarioSession {

	public static String url = "http://localhost:8080/flowgame/communicate/scenarioSessionDownload.action";

	public ScenarioSession download(Person person) {
		PostMethod post = new PostMethod(url);
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(person);

			Part[] parts = { new FilePart("person", new ByteArrayPartSource("person.dat", bos.toByteArray())) };

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
			
			
			ScenarioSession scenarioSession = null;
			if (o instanceof ScenarioSession) {
				scenarioSession = (ScenarioSession) o;
				System.out.println(scenarioSession.getId());
				for (ScenarioRound round : scenarioSession.getRounds()) {
					System.out.println(round.getId());				
					System.out.println(round.getBaselineModifier());
					System.out.println(round.getExpectedPlaytime());
					
					round.getDifficutyFunction().getIntervald().getId();
					round.getDifficutyFunction().getRatio().getId();
					round.getDifficutyFunction().getSpeed().getId();
					
					System.out.println(round.getQuestionnaire().getName());
					System.out.println(round.getQuestionnaire().getDescription());
					for (Question question : round.getQuestionnaire().getQuestions()) {
						System.out.println(question.getText());
					}
				}				
			} else if (o instanceof String) {
				System.out.println((String) o);
			}

			in.close();

			return scenarioSession;
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			// release the connection
			post.releaseConnection();
		}
		return null;
	}

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		DownloadScenarioSession dp = new DownloadScenarioSession();
		dp.download(new Person(12345L));
	}

}