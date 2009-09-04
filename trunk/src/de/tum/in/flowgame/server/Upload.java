package de.tum.in.flowgame.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import com.opensymphony.xwork2.ActionSupport;

import de.tum.in.flowgame.dao.GameSessionDAO;
import de.tum.in.flowgame.dao.GameSessionDAOImpl;
import de.tum.in.flowgame.dao.PersonDAO;
import de.tum.in.flowgame.dao.PersonDAOImpl;
import de.tum.in.flowgame.model.GameSession;
import de.tum.in.flowgame.model.Person;

public class Upload extends ActionSupport {

	private File file;

	@Override
	public String execute() {
		try {
			FileInputStream fin = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fin);
			Object o = ois.readObject();

			if (o instanceof Person) {
				Person person = (Person) o;
				PersonDAO pdao = new PersonDAOImpl();
				pdao.update(person);
			} else if (o instanceof GameSession) {
				GameSession gameSession = (GameSession) o;
				GameSessionDAO gdao = new GameSessionDAOImpl();
				gdao.update(gameSession);
			} else {
				System.out.println("invalid content");
			}
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return SUCCESS;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
