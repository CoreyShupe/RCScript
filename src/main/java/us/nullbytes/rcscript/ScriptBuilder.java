package us.nullbytes.rcscript;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import java.io.File;
import java.io.InputStream;
import java.util.function.Consumer;

/**
 * An {@code interface} representing a builder for scripts.
 *
 * @author Corey Shupe
 */
public interface ScriptBuilder {

	/**
	 * Places an identifier and an {@link Object} to use inside of the {@link Script}.
	 *
	 * @param identifier
	 * 		The identifier of the {@link Object}.
	 * @param object
	 * 		The object to be referenced with the identifier.
	 *
	 * @return {@code this}
	 */
	ScriptBuilder place(String identifier, Object object);

	/**
	 * Provides a way to modify or retrieve info from the {@link ScriptContext}.
	 *
	 * @param contextConsumer
	 * 		The {@link Consumer} to process the {@link ScriptContext}.
	 *
	 * @return {@code this}
	 */
	ScriptBuilder consumeContext(Consumer<ScriptContext> contextConsumer);

	/**
	 * Updates the {@link InputStream} of the {@link Script}.
	 *
	 * @param stream
	 * 		The {@link InputStream} of the {@link Script}.
	 *
	 * @return {@code this}
	 */
	ScriptBuilder setStream(InputStream stream);

	/**
	 * Sets the script based off a {@link String}.
	 *
	 * @param script
	 * 		The {@link String} representing the {@link Script}.
	 *
	 * @return {@code this}
	 */
	ScriptBuilder setScript(String script);

	/**
	 * Sets the location of the {@link Script}.
	 *
	 * @param location
	 * 		The location of the resource.
	 *
	 * @return {@code this}
	 */
	ScriptBuilder setResourceLocation(String location);

	/**
	 * Sets the location of the {@link Script}.
	 *
	 * @param location
	 * 		The location of the file.
	 *
	 * @return {@code this}
	 */
	ScriptBuilder setLocation(String location);

	/**
	 * Sets the location of the {@link Script}.
	 *
	 * @param file
	 * 		The file reference for the {@link Script}.
	 *
	 * @return {@code this}
	 */
	ScriptBuilder setLocation(File file);

	/**
	 * Overrides the default init function from the {@link ScriptManager} with its own.
	 * Set the function name to null for simple evaluation.
	 *
	 * @param function
	 * 		The name of the function.
	 * @param params
	 * 		The parameters used in the function.
	 *
	 * @return {@code this}
	 */
	ScriptBuilder setInitFunction(String function, Object... params);

	/**
	 * Gets the {@link ScriptEngine} linked to this {@link ScriptBuilder}.
	 *
	 * @return The {@link ScriptEngine} running the {@link Script} built.
	 */
	ScriptEngine extractEngine();

	/**
	 * Builds the Script with the current settings.
	 *
	 * @return The built {@link Script} ready to be executed.
	 */
	Script build();
}
