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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

import de.tum.in.flowgame.dao.ScenarioSessionDAO;
import de.tum.in.flowgame.dao.ScenarioSessionDAOImpl;
import de.tum.in.flowgame.model.ScenarioSession;

public class ScenarioSessionDownload extends ActionSupport {

	private File person;
	private InputStream inputStream;

	@Override
	public String execute() {
		try {
//			FileInputStream fin = new FileInputStream(person);
//			ObjectInputStream ois = new ObjectInputStream(fin);
//			Object o = ois.readObject();
//			if (o instanceof Person) {
//				Person p = (Person) o;
//			}
			
			ScenarioSessionDAO sdao = new ScenarioSessionDAOImpl();
			List<ScenarioSession> scenarioSessions = sdao.findAll();
			ScenarioSession session = scenarioSessions.get(0);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);

			// TODO select depending on player
			oos.writeObject(session);
			
			inputStream = new ByteArrayInputStream(bos.toByteArray());

			return SUCCESS;
		} catch (Exception e) {
			System.out.println("ScenarioSessionDownload.execute() oh noes");
			// TODO Auto-generated catch block
			e.printStackTrace();
			return SUCCESS;
		}
	}

	public void setPerson(File person) {
		this.person = person;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

}
