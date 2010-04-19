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
			
			log.info("executed " + getClass().getSimpleName() + " with " + input + " to " + output);
			
			this.response = new ByteArrayInputStream(Utils.objectToBytes(output));
		} catch (final Exception ex) {
			final String msg = "failed to process " + input + " to " + output + " in " + getClass().getName();
			log.error(msg, ex);
			
			// only relay message to client, not full exception
			// (not all classes available on client, security reasons)
			this.response = new ByteArrayInputStream(Utils.objectToBytes(new Exception(msg)));
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
