package de.tum.in.flowgame.client.engine.util;

import java.io.IOException;
import java.net.URL;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;
import javax.media.j3d.Texture;

import com.sun.j3d.loaders.Loader;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.image.TextureLoader;

import de.tum.in.flowgame.client.GameApplet;

/**
 * This class provides some utility functions related to Java3D.
 * <p>
 * Do not use this class in server-side code!
 * <p>
 * The server does not have any Java3D classes on its classpath, resulting in
 * {@link ClassNotFoundException}s.
 */
public class Java3DUtils {

	/**
	 * This method prints the path of the node in the scenegraph to the console.
	 * 
	 * @param node
	 *            The {@link Node} in the scenegraph.
	 */
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

	/** 
	 * This method loads 3D objects from resource files.
	 * @param resource The resource (as {@link String}).
	 * @return A {@link BranchGroup} that contains the 3D object.
	 * @throws IOException
	 */
	public static BranchGroup loadScene(final String resource) throws IOException {
		final Loader loader = new ObjectFile(ObjectFile.RESIZE);
		final URL url = GameApplet.class.getResource(resource);
		return loader.load(url).getSceneGroup();
	}

	/**
	 * This method loads a {@link Texture} from a resource file.
	 * @param path The path (as {@link String}) to the resource file.
	 * @return A {@link Texture} object.
	 */
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
}
