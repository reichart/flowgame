package de.tum.in.flowgame.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;

import com.opensymphony.xwork2.ActionSupport;

import de.tum.in.flowgame.dao.PersonDAO;
import de.tum.in.flowgame.dao.PersonDAOImpl;
import de.tum.in.flowgame.model.Person;

public class PersonDownload extends ActionSupport {

	private Long id;
	private InputStream inputStream;

	@Override
	public String execute() throws Exception {
		if (id > 0) {
			PersonDAO pdao = new PersonDAOImpl();
			System.out.println(id);
			Person person = pdao.find(id);
			
			if (person != null) {
				writeObjectInInputStream(person);
			} else {
				writeObjectInInputStream("No person found for id " + id);
			}
			return SUCCESS;
		} else {
			return ERROR;
		}
		
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

	public void setId(String id) {
		try {
			this.id = Long.decode(id);
		} catch (NumberFormatException e) {
			this.id = -1L;
		}
	}

	public InputStream getInputStream() {
		return inputStream;
	}

}
