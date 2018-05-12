package us.nullbytes.rcscript.builder.script;

import us.nullbytes.rcscript.Script;
import us.nullbytes.rcscript.data.ScriptData;

import javax.script.ScriptException;
import java.io.InputStreamReader;

/**
 * The most simple implementation of {@link GenericScript} for things with only evaluation.
 * Such as:
 * <pre>
 *     {@code eval(1 + 2)}
 * </pre>
 *
 * @author Corey Shupe
 * @see GenericScript
 */
public class SimpleScript extends GenericScript {

	/**
	 * Initializes a new {@link SimpleScript}.
	 *
	 * @param scriptData
	 * 		The {@link ScriptData} of the {@link Script}.
	 *
	 * @see GenericScript#GenericScript(ScriptData)
	 */
	public SimpleScript(ScriptData scriptData) {
		super(scriptData);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object execute() {
		try {
			return getScriptEngine().eval(new InputStreamReader(scriptData.getStream()));
		} catch (ScriptException ex) {
			exceptionHandler.handle(ex);
			return null;
		}
	}
}
