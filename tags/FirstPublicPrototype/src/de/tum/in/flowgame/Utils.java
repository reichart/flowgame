package de.tum.in.flowgame;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;
import javax.media.j3d.Texture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.j3d.loaders.Loader;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.image.TextureLoader;

public class Utils {

	private static final Log log = LogFactory.getLog(Utils.class);

	public static <T> T[] asArray(final T... t) {
		return t;
	}

	public void printRootPath(final Node node) {
		if (node == null) {
			System.out.println();
			return;
		} else {
			final String instance = node.getClass().getSimpleName() + "@" + Integer.toHexString(node.hashCode());
			System.out.print(" > " + instance);
			printRootPath(node.getParent());
		}
	}

	public static BranchGroup loadScene(final String resource) throws IOException {
		final Loader loader = new ObjectFile(ObjectFile.RESIZE);
		final URL url = GameApplet.class.getResource(resource);
		return loader.load(url).getSceneGroup();
	}

	public static Texture getTexture(final String path) {
		final URL url = GameApplet.class.getResource(path);
		final TextureLoader loader = new TextureLoader(url, TextureLoader.GENERATE_MIPMAP | TextureLoader.BY_REFERENCE,
				null);

		final Texture texture = loader.getTexture();
		texture.setBoundaryModeS(Texture.WRAP);
		texture.setBoundaryModeT(Texture.WRAP);

		// Do NOT use NICEST for min filter (breaks ATI on Linux)
		texture.setMinFilter(Texture.BASE_LEVEL_LINEAR);
		texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
		
		texture.setAnisotropicFilterMode(Texture.ANISOTROPIC_SINGLE_VALUE);
		texture.setAnisotropicFilterDegree(4);
		
		return texture;
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
}
