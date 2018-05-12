package us.nullbytes.rcscript.builder.script;

import us.nullbytes.rcscript.ExceptionHandler;
import us.nullbytes.rcscript.Script;
import us.nullbytes.rcscript.ScriptManager;
import us.nullbytes.rcscript.data.ScriptData;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * The {@code abstract} generic {@link Script} used for most implementations.
 *
 * @author Corey Shupe
 * @see Script
 */
abstract class GenericScript implements Script {

	/**
	 * The {@link ExecutorService} to handle all async computations within {@link GenericScript}s.
	 */
	private final static ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);

	/**
	 * The {@link ScriptData} help within this {@link Script}.
	 */
	ScriptData scriptData;

	/**
	 * The {@link ExceptionHandler} handling all {@link ScriptException}s.
	 * By default {@code ScriptException::printStackTrace}
	 */
	ExceptionHandler<ScriptException> exceptionHandler;

	/**
	 * Initializes a new {@link GenericScript}. All implementations called as a {@code super(...)}.
	 *
	 * @param scriptData
	 * 		The {@link ScriptData} of the {@link Script}.
	 */
	GenericScript(ScriptData scriptData) {
		this.scriptData = scriptData;
		this.exceptionHandler = ScriptException::printStackTrace;
	}

	/**
	 * Executes a new {@link Callable} inside of the {@link #EXECUTOR_SERVICE}.
	 *
	 * @param callable
	 * 		The {@link Callable} to process inside of the {@link #EXECUTOR_SERVICE}.
	 * @param <T>
	 * 		The type of {@link Callable} to define the {@link Future} return type.
	 *
	 * @return The {@link Future} of type {@link T}.
	 */
	<T> CompletableFuture<T> executeService(Supplier<T> callable) {
		return CompletableFuture.supplyAsync(callable, EXECUTOR_SERVICE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptManager getScriptManager() {
		return scriptData.getManager();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptEngine getScriptEngine() {
		return scriptData.getEngine();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleExceptions(ExceptionHandler<ScriptException> exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object execute(String func, Object... args) {
		throw new UnsupportedOperationException("This type of script cannot perform function calls.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Future<Object> executeAsync() {
		return executeService(this::execute);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Future<Object> executeAsync(String func, Object... args) {
		throw new UnsupportedOperationException("This type of script cannot perform function calls.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T expect(Class<T> classIdentifier) {
		return classIdentifier.cast(execute());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T expect(Class<T> classIdentifier, String func, Object... args) {
		throw new UnsupportedOperationException("This type of script cannot perform function calls.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> CompletableFuture<T> expectLater(Class<T> classIdentifier) {
		return executeService(() -> expect(classIdentifier));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> CompletableFuture<T> expectLater(Class<T> classIdentifier, String func, Object... args) {
		throw new UnsupportedOperationException("This type of script cannot perform function calls.");
	}
}
