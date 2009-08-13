/*
 * $Id: HelloWorld.java 471756 2006-11-06 15:01:43Z husted $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

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
