package de.tum.in.flowgame.server;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.Utils;

/**
 * Abstract base class for invoking server-side code with a serialized object as
 * parameter and return value.
 */
public abstract class GameDataAction<I, O> extends DatabaseAction {

	private final static Log log = LogFactory.getLog(GameDataAction.class);

	private File data;

	private InputStream response;

	/**
	 * If you need to override this method, don't use {@link GameDataAction}.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final String execute() throws Exception {
		I input = null;
		O output = null;
		try {
			input = (I) Utils.bytesToObject(IOUtils.toByteArray(new FileInputStream(data)));
			output = execute(input);
			this.response = new ByteArrayInputStream(Utils.objectToBytes(output));
		} catch (final Exception ex) {
			log.error("failed to process " + input + " to " + output + " in " + getClass().getName(), ex);
			this.response = new ByteArrayInputStream(Utils.objectToBytes(ex));
		}
		return SUCCESS;
	}

	protected abstract O execute(I input) throws Exception;

	public void setData(final File data) {
		this.data = data;
	}

	public InputStream getInputStream() {
		return response;
	}
}
