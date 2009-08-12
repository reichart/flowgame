package de.tum.in.flowgame.client;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import de.tum.in.flowgame.model.Person;

public class Test {
	public static void main(String args[]) {
		  Person person = new Person(53221L);
		  person.setName("Barbara Ballalu");
		  person.setPlace("Mallor");
		  person.setSex(Person.Sex.FEMALE);
		  Calendar gc = new GregorianCalendar(86 + 1900, 8, 1);
		  person.setDateOfBirth(new Date(gc.getTimeInMillis()));
		  
		  // serialize the Queue
		  System.out.println("serializing Player");
		  try {
		      FileOutputStream fout = new FileOutputStream("person.dat");
		      ObjectOutputStream oos = new ObjectOutputStream(fout);
		      oos.writeObject(person);
		      oos.close();
		      }
		   catch (Exception e) { e.printStackTrace(); }
		}
}
