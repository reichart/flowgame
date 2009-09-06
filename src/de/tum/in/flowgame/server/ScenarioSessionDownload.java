package de.tum.in.flowgame.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

import de.tum.in.flowgame.dao.ScenarioSessionDAO;
import de.tum.in.flowgame.dao.ScenarioSessionDAOImpl;
import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.model.ScenarioSession;

public class ScenarioSessionDownload extends ActionSupport {

	private File person;
	private InputStream inputStream;

	@Override
	public String execute() throws Exception {
		FileInputStream fin = new FileInputStream(person);
		ObjectInputStream ois = new ObjectInputStream(fin);
		Object o = ois.readObject();
		if (o instanceof Person) {
			Person p = (Person) o;

			// TODO select depending on player
			ScenarioSessionDAO sdao = new ScenarioSessionDAOImpl();
			List<ScenarioSession> scenarioSessions = sdao.findAll();
			Integer id = 0;
			ScenarioSession session = null;
			if (id < scenarioSessions.size()) {
				session = scenarioSessions.get(id);
			}

			if (session != null) {
				writeObjectInInputStream(session);
			} else {
				writeObjectInInputStream("No valid session could be found for Person"
						+ person.getName());
			}
		} else {
			writeObjectInInputStream("The File did not refer to a valid Person. ScenarioSession could not be choosen");
		}
		return SUCCESS;
	}

	/*
	 * writes object into inputStream which can then be downloaded by the client
	 */
	private void writeObjectInInputStream(Object o) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);

		oos.writeObject(o);

		inputStream = new ByteArrayInputStream(bos.toByteArray());
	}

	public void setPerson(File person) {
		this.person = person;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

}
