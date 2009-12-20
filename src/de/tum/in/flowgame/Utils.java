package de.tum.in.flowgame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.management.ManagementFactory;

import javax.imageio.ImageIO;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Utils {

	private static final Log log = LogFactory.getLog(Utils.class);

	/**
	 * Generic method to convert a variable number of objects to an array.
	 * 
	 * @param <T>
	 *            Type of object.
	 * @param t
	 *            The object(s).
	 * @return Array of objects.
	 */
	public static <T> T[] asArray(final T... t) {
		return t;
	}

	static {
		ImageIO.setUseCache(false); // stay in JWS sandbox

		try {
			mbs = ManagementFactory.getPlatformMBeanServer();
		} catch (final Exception ex) {
			log.info("Could not start mbean server (sandbox?)");
		}
	}

	private static MBeanServer mbs;

	/**
	 * Exports an MBean or MXBean via JMX.
	 */
	public static void export(final Object mbean) {
		if (mbs == null) {
			log.info("no mbean server available");
			return;
		}

		try {
			final Class<?> clazz = mbean.getClass();
			final String domain = clazz.getPackage().getName();
			final String value = clazz.getSimpleName();

			final ObjectName name = new ObjectName(domain, "type", value);

			log.info("registering mbean " + mbean + " as " + name);
			mbs.registerMBean(mbean, name);
		} catch (final Exception ex) {
			throw new RuntimeException("failed to export " + mbean, ex);
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
	public static byte[] objectToBytes(final Object obj) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
		} finally {
			IOUtils.closeQuietly(oos);
		}
		return baos.toByteArray();
	}

	/**
	 * Deserializes a byte array into a Java Object.
	 * 
	 * @param bytes
	 *            a byte array containing a serialized Java Object
	 * @return the deserialized Java Object
	 * @throws IOException
	 */
	public static Object bytesToObject(final byte[] bytes) throws IOException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (final ClassNotFoundException ex) {
			throw new IOException(ex);
		} finally {
			IOUtils.closeQuietly(ois);
		}
	}
}
