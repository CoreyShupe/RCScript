package us.nullbytes.rcscript;

import us.nullbytes.rcscript.builder.PackedScriptBuilder;
import us.nullbytes.rcscript.data.InitFunc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * An {@code interface} representing the base manager for scripts.
 *
 * @author Corey Shupe
 */
public interface ScriptManager {

	/**
	 * Creates a unique instance of an exact copy of this. (including the cache)
	 *
	 * @return A copied version of this.
	 */
	ScriptManager copyIntoUniqueInstance();

	/**
	 * Generates a unique instance of the same type of {@link ScriptManager}.
	 *
	 * @return A unique instance of this type of {@link ScriptManager}.
	 */
	ScriptManager generateUniqueInstance();

	/**
	 * Sets the default {@link InitFunc} of the script manager.
	 *
	 * @param function
	 * 		The function to initialize with.
	 * @param params
	 * 		The parameters of the function.
	 */
	void setDefaultInitFunction(String function, Object... params);

	/**
	 * Gets the {@link InitFunc} which is default with this manager.
	 *
	 * @return The default {@link InitFunc}.
	 */
	InitFunc getDefaultInitFunction();

	/**
	 * Sets the {@link ExceptionHandler} to handle all {@link IOException}s.
	 *
	 * @param exceptionHandler
	 * 		The {@link ExceptionHandler} to handle all {@link IOException}s.
	 */
	void setExceptionHandler(ExceptionHandler<IOException> exceptionHandler);

	/**
	 * Gets the {@link ExceptionHandler} currently handling all {@link IOException}s.
	 * Default:
	 * <pre>
	 *     {@code (exception -> exception.printStackTrace())}
	 * </pre>
	 *
	 * @return The currently set {@link ExceptionHandler}.
	 */
	ExceptionHandler<IOException> getExceptionHandler();

	/**
	 * Sets the {@link File} of the directory holding the scripts.
	 *
	 * @param location
	 * 		The directory holding the scripts.
	 */
	void setScriptsDir(File location);

	/**
	 * Gets the {@link File} currently set to holding the scripts.
	 *
	 * @return The directory holding the scripts, or null if not set.
	 */
	File getScriptsDir();

	/**
	 * Generates a new, empty {@link ScriptBuilder}.
	 *
	 * @return The generated {@link ScriptBuilder}.
	 */
	ScriptBuilder newScript();

	/**
	 * Generates a new {@link ScriptBuilder} based off an {@link InputStream}.
	 *
	 * @param stream
	 * 		The {@link InputStream} of the script.
	 *
	 * @return The generated {@link ScriptBuilder}.
	 */
	ScriptBuilder openScript(InputStream stream);

	/**
	 * Generates a new {@link ScriptBuilder} based off a {@link String}.
	 * This {@link String} is internally changed into a {@link java.io.ByteArrayInputStream}.
	 *
	 * @param script
	 * 		The script to read.
	 *
	 * @return The generated {@link ScriptBuilder}.
	 */
	ScriptBuilder openScript(String script);

	/**
	 * Generates a {@link ScriptBuilder} based off the script located at the specified resource.
	 *
	 * @param location
	 * 		The location of the script.
	 *
	 * @return The generated {@link ScriptBuilder}.
	 */
	ScriptBuilder openResourceScript(String location);

	/**
	 * Generates a {@link ScriptBuilder} based off the script located at the specified {@link File}.
	 *
	 * @param file
	 * 		The location of the script.
	 *
	 * @return The generated {@link ScriptBuilder}.
	 */
	ScriptBuilder openFileScript(File file);

	/**
	 * Generates a {@link ScriptBuilder} based off the script located at the specified location.
	 * If there's a DIR present, it combines the paths of the DIR and given location.
	 *
	 * @param location
	 * 		The location of the script.
	 *
	 * @return The generated {@link ScriptBuilder}.
	 */
	ScriptBuilder openFileScript(String location);

	/**
	 * Generates a new, empty {@link PackedScriptBuilder}.
	 *
	 * @param identifier
	 * 		The identifier used for the {@link Script}.
	 *
	 * @return The generated {@link PackedScriptBuilder}.
	 */
	ScriptBuilder newPackedScript(Object identifier);

	/**
	 * Generates a new {@link PackedScriptBuilder} based off an {@link InputStream}.
	 *
	 * @param stream
	 * 		The {@link InputStream} of the script.
	 * @param identifier
	 * 		The identifier used for the {@link Script}.
	 *
	 * @return The generated {@link PackedScriptBuilder}.
	 */
	ScriptBuilder packScript(InputStream stream, Object identifier);

	/**
	 * Generates a new {@link ScriptBuilder} based off a {@link String}.
	 * This {@link String} is internally changed into a {@link java.io.ByteArrayInputStream}.
	 *
	 * @param script
	 * 		The script to read.
	 * @param identifier
	 * 		The identifier used for the {@link Script}.
	 *
	 * @return The generated {@link PackedScriptBuilder}.
	 */
	ScriptBuilder packScript(String script, Object identifier);

	/**
	 * Generates a {@link PackedScriptBuilder} based off the script located at the specified
	 * resource.
	 *
	 * @param location
	 * 		The location of the script.
	 * @param identifier
	 * 		The identifier used for the {@link Script}.
	 *
	 * @return The generated {@link PackedScriptBuilder}.
	 */
	ScriptBuilder packResourceScript(String location, Object identifier);

	/**
	 * Generates a {@link PackedScriptBuilder} based off the script located at the specified {@link
	 * File}.
	 *
	 * @param file
	 * 		The location of the script.
	 * @param identifier
	 * 		The identifier used for the {@link Script}.
	 *
	 * @return The generated {@link PackedScriptBuilder}.
	 */
	ScriptBuilder packFileScript(File file, Object identifier);

	/**
	 * Generates a {@link PackedScriptBuilder} based off the script located at the specified
	 * location.
	 * If there's a DIR present, it combines the paths of the DIR and given location.
	 *
	 * @param location
	 * 		The location of the script.
	 * @param identifier
	 * 		The identifier used for the {@link Script}.
	 *
	 * @return The generated {@link PackedScriptBuilder}.
	 */
	ScriptBuilder packFileScript(String location, Object identifier);

	/**
	 * Inserts the {@link Script} along with an identifier.
	 *
	 * @param script
	 * 		The {@link Script} to insert.
	 * @param identifier
	 * 		The identifier used for the {@link Script}.
	 */
	void insert(Script script, Object identifier);

	/**
	 * Tries to receive a {@link Script} under a specified identifier.
	 *
	 * @param identifier
	 * 		The identifier for the {@link Script}.
	 *
	 * @return The {@link Optional} return of the {@link Script}.
	 */
	Optional<Script> unpack(Object identifier);

	/**
	 * Checks if there is a {@link Script} which exists under the identifier.
	 *
	 * @param identifier
	 * 		The identifier for the {@link Script}.
	 *
	 * @return True if the {@link Script} exists under the identifier.
	 */
	boolean exists(Object identifier);

	/**
	 * Removes the {@link Script} which exists under the identifier.
	 *
	 * @param identifier
	 * 		The identifier for the {@link Script}.
	 *
	 * @return True if the removal is successful.
	 */
	boolean remove(Object identifier);

	/**
	 * Applies a new {@link IdentifierRule} to the manager.
	 *
	 * @param rule
	 * 		The {@link IdentifierRule} to follow.
	 */
	void applyIdentifierRule(IdentifierRule rule);

	/**
	 * Clears the cache of all scripts held within.
	 */
	void clearScriptCache();

	/**
	 * The {@code enum} referencing ways to equate the identifiers.
	 */
	enum IdentifierRule {

		/**
		 * Strict equality rule.
		 * {@code this == that}
		 */
		IDENTITY(IdentityHashMap::new),
		/**
		 * Simple equality rule.
		 * {@code this.equals(that)}
		 */
		EQUALITY(HashMap::new),
		/**
		 * Concurrent equality rule.
		 * {@code this.equals(that)} with concurrency in mind.
		 */
		CONCURRENT(ConcurrentHashMap::new);

		/**
		 * The {@link Supplier} to generate a new {@link Map} for holding identifiers.
		 */
		private final Supplier<Map<Object, Script>> mapGenerator;

		/**
		 * Initializer with the {@link Map} generator.
		 *
		 * @param mapGenerator
		 * 		The {@link Supplier} to generate {@link Map}s with.
		 */
		IdentifierRule(Supplier<Map<Object, Script>> mapGenerator) {
			this.mapGenerator = mapGenerator;
		}

		/**
		 * Generates a new {@link Map} with the defined {@link Supplier}.
		 *
		 * @return The generated {@link Map}.
		 */
		public Map<Object, Script> generateNewMap() {
			return mapGenerator.get();
		}

	}
}
