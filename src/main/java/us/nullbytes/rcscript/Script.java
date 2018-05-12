package us.nullbytes.rcscript;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * An {@code interface} representing an active script.
 *
 * @author Corey Shupe
 */
public interface Script {

	/**
	 * Gets the {@link ScriptManager} which created this {@link Script}.
	 *
	 * @return The {@link ScriptManager} which is used for this type of {@link Script}.
	 */
	ScriptManager getScriptManager();

	/**
	 * Gets the {@link ScriptEngine} which runs this {@link Script}.
	 *
	 * @return The {@link ScriptEngine} running this {@link Script}.
	 */
	ScriptEngine getScriptEngine();

	/**
	 * Executes this {@link Script} with the init function provided from the {@link ScriptManager}.
	 */
	Object execute();

	/**
	 * Executes a function with specified parameters.
	 *
	 * @param func
	 * 		The function to call.
	 * @param args
	 * 		The parameters used in the function.
	 *
	 * @return The return value of the function.
	 */
	Object execute(String func, Object... args);

	/**
	 * Executes the function asynchronously.
	 *
	 * @return The {@link Future} result of the function.
	 */
	Future<Object> executeAsync();

	/**
	 * Executes a function async with specified parameters.
	 *
	 * @param func
	 * 		The function to call.
	 * @param args
	 * 		The parameters used in the function.
	 *
	 * @return The {@link Future} result of the function.
	 */
	Future<Object> executeAsync(String func, Object... args);

	/**
	 * Executes a function with an expected outcome.
	 *
	 * @param classIdentifier
	 * 		The {@link Class} for the expected outcome.
	 * @param <T>
	 * 		The type of expected outcome provided by the {@link Class} identifier;
	 *
	 * @return The return value of the function casted to {@link T} or null if it's an invalid cast.
	 */
	<T> T expect(Class<T> classIdentifier);

	/**
	 * Executes a function with specified parameters with an expected outcome.
	 *
	 * @param classIdentifier
	 * 		The {@link Class} for the expected outcome.
	 * @param func
	 * 		The function to call.
	 * @param args
	 * 		The parameters used in the function.
	 * @param <T>
	 * 		The type of expected outcome provided by the {@link Class} identifier.
	 *
	 * @return The return value of the function casted to {@link T} or null if it's an invalid cast.
	 */
	<T> T expect(Class<T> classIdentifier, String func, Object... args);

	/**
	 * Executes a function async with specified parameters with an expected outcome.
	 *
	 * @param classIdentifier
	 * 		The {@link Class} for the expected outcome.
	 * @param <T>
	 * 		The type of expected outcome provided by the {@link Class} identifier.
	 *
	 * @return The {@link CompletableFuture} result of the function typed by {@link T}.
	 */
	<T> CompletableFuture<T> expectLater(Class<T> classIdentifier);

	/**
	 * Executes a function async with specified parameters with an expected outcome.
	 *
	 * @param classIdentifier
	 * 		The {@link Class} for the expected outcome.
	 * @param func
	 * 		The function to call.
	 * @param args
	 * 		The parameters used in the function.
	 * @param <T>
	 * 		The type of expected outcome provided by the {@link Class} identifier.
	 *
	 * @return The {@link CompletableFuture} result of the function typed by {@link T}.
	 */
	<T> CompletableFuture<T> expectLater(Class<T> classIdentifier, String func, Object... args);

	/**
	 * Sets the {@link ExceptionHandler} for all {@link ScriptException}s.
	 *
	 * @param exceptionHandler
	 * 		The {@link ExceptionHandler} to handle {@link ScriptException}s.
	 */
	void handleExceptions(ExceptionHandler<ScriptException> exceptionHandler);
}
