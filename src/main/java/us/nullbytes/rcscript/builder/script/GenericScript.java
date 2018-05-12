package us.nullbytes.rcscript.builder.script;

import us.nullbytes.rcscript.ExceptionHandler;
import us.nullbytes.rcscript.Script;
import us.nullbytes.rcscript.ScriptManager;
import us.nullbytes.rcscript.data.ScriptData;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
	 * Manually casts down a primitive type, a workaround to incapability of casting int to Integer.
	 *
	 * @param classIdentifier
	 * 		The {@link Class} to cast with.
	 * @param o
	 * 		The {@link Object} to cast.
	 * @param <T>
	 * 		The type which the {@link Object} should return as, defined by the {@link Class}.
	 *
	 * @return The casted {@link T} value from the {@link Object}.
	 */
	@SuppressWarnings("unchecked")
	<T> T manualCast(Class<T> classIdentifier, Object o) {
		if (classIdentifier.isPrimitive()) {
			switch (classIdentifier.getName()) {
				case "byte":
				case "java.lang.Byte": {
					return (T) Byte.valueOf(o.toString());
				}
				case "char":
				case "java.lang.Character": {
					return (T) Character.valueOf(o.toString().charAt(0));
				}
				case "short":
				case "java.lang.Short": {
					return (T) Short.valueOf(o.toString());
				}
				case "int":
				case "java.lang.Integer": {
					return (T) Integer.valueOf(o.toString());
				}
				case "long":
				case "java.lang.Long": {
					return (T) Long.valueOf(o.toString());
				}
				case "double":
				case "java.lang.Double": {
					return (T) Double.valueOf(o.toString());
				}
				case "float":
				case "java.lang.Float": {
					return (T) Float.valueOf(o.toString());
				}
				case "boolean":
				case "java.lang.Boolean": {
					return (T) Boolean.valueOf(o.toString());
				}
				default:
					break;
			}
		}
		return classIdentifier.cast(o);
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
	<T> Future<T> executeService(Callable<T> callable) {
		return EXECUTOR_SERVICE.submit(callable);
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
		return manualCast(classIdentifier, execute());
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
	public <T> Future<T> expectLater(Class<T> classIdentifier) {
		return executeService(() -> expect(classIdentifier));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> Future<T> expectLater(Class<T> classIdentifier, String func, Object... args) {
		throw new UnsupportedOperationException("This type of script cannot perform function calls.");
	}
}
