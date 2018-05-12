package us.nullbytes.rcscript.data;

import us.nullbytes.rcscript.Script;
import us.nullbytes.rcscript.ScriptManager;

import javax.script.ScriptEngine;
import java.io.InputStream;

/**
 * A representation of a {@link Script}'s data. A data class to contain all of a {@link Script}'s
 * information.
 *
 * @author Corey Shupe
 */
public class ScriptData {

	/**
	 * A {@link ScriptManager} representing the parent manager.
	 */
	private final ScriptManager manager;

	/**
	 * A {@link ScriptEngine} representing the parent engine.
	 */
	private final ScriptEngine engine;

	/**
	 * An {@link InputStream} representing the stream of the {@link Script}.
	 */
	private InputStream stream;

	/**
	 * An initializer for new {@link ScriptData}.
	 *
	 * @param manager
	 * 		The {@link ScriptManager} for the {@link Script}.
	 * @param engine
	 * 		The {@link ScriptEngine} for the {@link Script}.
	 * @param stream
	 * 		The {@link InputStream} for the {@link Script}.
	 */
	public ScriptData(ScriptManager manager, ScriptEngine engine, InputStream stream) {
		this.manager = manager;
		this.engine = engine;
		this.stream = stream;
	}

	/**
	 * Gets the {@link ScriptManager} from the cached data.
	 *
	 * @return The {@link ScriptManager}.
	 */
	public ScriptManager getManager() {
		return manager;
	}

	/**
	 * Gets the {@link ScriptEngine} in charge of running the {@link Script}.
	 *
	 * @return The {@link ScriptEngine}.
	 */
	public ScriptEngine getEngine() {
		return engine;
	}

	/**
	 * Gets the {@link InputStream} containing the runnable script.
	 *
	 * @return The {@link InputStream}.
	 */
	public InputStream getStream() {
		return stream;
	}

	/**
	 * Sets the {@link InputStream}, representing a new or different script.
	 *
	 * @param stream
	 * 		The {@link InputStream} to read from.
	 */
	public void setStream(InputStream stream) {
		this.stream = stream;
	}
}
