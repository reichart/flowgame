package de.tum.in.flowgame.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;

import de.tum.in.flowgame.model.Answer;
import de.tum.in.flowgame.model.GameRound;
import de.tum.in.flowgame.model.GameSession;
import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.model.Question;

public class Client {

	private static String url = "http://localhost:8080/flowgame/upload.action";
	
	public void updatePerson(Person person) {
		update(person);
	}
	
	public void updateGameSession(GameSession gameSession) {
		update(gameSession);
	}
	
	/*!allows to update either a person or a gameSession */
	private void update(Object entity) {
		PostMethod post = new PostMethod(url);
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(entity);

			// Create an instance of HttpClient.
			HttpClient client = new HttpClient();

			Part[] parts = { new FilePart("file", new ByteArrayPartSource("file.dat", bos.toByteArray()))};

			// Create a method instance.
			// GetMethod method = new GetMethod(url);
			MultipartRequestEntity requestEntity = new MultipartRequestEntity(parts, post.getParams());
			post.setRequestEntity(requestEntity);

			// Provide custom retry handler is necessary
			post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

			// Execute the method.
			int statusCode = client.executeMethod(post);

			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + post.getStatusLine());
			}

			// Read the response body.
			byte[] responseBody = post.getResponseBody();
			// Deal with the response.
			// Use caution: ensure correct character encoding and is not binary
			// data
			System.out.println(new String(responseBody));
		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// Release the connection.
			post.releaseConnection();
		}
	}

	public static void main(String[] args) {
		Person p = new Person(52121L);
		p.setName("blubbaaaa");
		p.setPlace("Lernuf");
		p.setSex(Person.Sex.MALE);
		Calendar gc = new GregorianCalendar(86 + 1900, 8, 1);
		p.setDateOfBirth(new Date(gc.getTimeInMillis()));
		
		Question q = new Question();
		q.setText("blubberbla");
		Answer a = new Answer(q, 1);
		
		List<Answer> answers = new ArrayList<Answer>();
		answers.add(a);
		GameRound r = new GameRound();
		r.setAnswers(answers);
		
		GameSession gs = new GameSession();
		gs.setPlayer(p);
		gs.getRounds().add(r);
		
		Client client = new Client();
		client.update(p);
		client.update(gs);
	}

}