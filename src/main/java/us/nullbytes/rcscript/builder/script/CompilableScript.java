package us.nullbytes.rcscript.builder.script;

import us.nullbytes.rcscript.Script;
import us.nullbytes.rcscript.data.ScriptData;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptException;
import java.io.InputStreamReader;

/**
 * A {@link Compilable} implementation of {@link GenericScript}. Same as {@link SimpleScript}
 * however is able to be compiled and reused.
 *
 * @author Corey Shupe
 * @see GenericScript
 */
public class CompilableScript extends GenericScript {

	/**
	 * The {@link CompiledScript} compiled from the {@link ScriptData#getStream()}.
	 */
	private final CompiledScript script;

	/**
	 * Initializes a new {@link CompilableScript}.
	 *
	 * @param scriptData
	 * 		The {@link ScriptData} of the {@link Script}.
	 *
	 * @throws ScriptException
	 * 		When the {@link Script} fails to evaluate or compile.
	 * @see GenericScript#GenericScript(ScriptData)
	 */
	public CompilableScript(ScriptData scriptData) throws ScriptException {
		super(scriptData);
		script = ((Compilable) getScriptEngine()).compile(new InputStreamReader(scriptData.getStream()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object execute() {
		try {
			return script.eval();
		} catch (ScriptException ex) {
			exceptionHandler.handle(ex);
			return null;
		}
	}
}
