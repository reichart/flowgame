package de.tum.in.flowgame;

import java.io.IOException;
import java.net.URL;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;
import javax.media.j3d.Texture;
import javax.vecmath.Color4f;

import com.sun.j3d.loaders.Loader;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.image.TextureLoader;

public class Utils {

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

		texture.setMagFilter(Texture.NICEST); // filtering
		texture.setMinFilter(Texture.NICEST); // tri-linear filtering

		texture.setAnisotropicFilterMode(Texture.ANISOTROPIC_SINGLE_VALUE);
		texture.setAnisotropicFilterDegree(4);

		texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
		return texture;
	}
}
