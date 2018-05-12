package us.nullbytes.rcscript.builder.script;

import us.nullbytes.rcscript.Script;
import us.nullbytes.rcscript.data.InitFunc;
import us.nullbytes.rcscript.data.ScriptData;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptException;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * An implementation of {@link GenericScript} which has functions to invoke.
 * Such as:
 * <pre>
 *     {@code
 *     function func1() {
 *          func2();
 *          // do other stuff
 *     }
 *
 *     function func2() {
 *          // do stuff
 *     }
 *     }
 * </pre>
 *
 * @author Corey Shupe
 * @see GenericScript
 */
public class InvocableScript extends GenericScript {

	/**
	 * The {@link InitFunc} representing the initial function for the {@link Script}.
	 */
	private final InitFunc initFunc;

	/**
	 * The {@link Invocable} saved to allow for further execution.
	 */
	private final Invocable invocable;

	/**
	 * Initializes a new {@link InvocableScript}.
	 *
	 * @param scriptData
	 * 		The {@link ScriptData} used in the {@link GenericScript}.
	 * @param initFunc
	 * 		The initial function for the {@link Script}.
	 *
	 * @throws ScriptException
	 * 		Thrown when the script fails to eval or compile.
	 * @see GenericScript#GenericScript(ScriptData)
	 */
	public InvocableScript(ScriptData scriptData, InitFunc initFunc) throws ScriptException {
		super(scriptData);
		this.initFunc = initFunc;
		CompiledScript script = ((Compilable) getScriptEngine()).compile(new InputStreamReader(scriptData.getStream()));
		script.eval();
		this.invocable = ((Invocable) getScriptEngine());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object execute() {
		try {
			return invocable.invokeFunction(initFunc.getName(), initFunc.getParams());
		} catch (ScriptException ex) {
			exceptionHandler.handle(ex);
			return null;
		} catch (NoSuchMethodException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object execute(String func, Object... args) {
		try {
			return invocable.invokeFunction(func, args);
		} catch (ScriptException ex) {
			exceptionHandler.handle(ex);
			return null;
		} catch (NoSuchMethodException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Future<Object> executeAsync(String func, Object... args) {
		return executeService(() -> execute(func, args));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T expect(Class<T> classIdentifier, String func, Object... args) {
		return classIdentifier.cast(execute(func, args));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> CompletableFuture<T> expectLater(Class<T> classIdentifier, String func, Object... args) {
		return executeService(() -> expect(classIdentifier, func, args));
	}
}
