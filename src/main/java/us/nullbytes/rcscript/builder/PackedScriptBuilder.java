package us.nullbytes.rcscript.builder;

import us.nullbytes.rcscript.Script;
import us.nullbytes.rcscript.ScriptManager;
import us.nullbytes.rcscript.builder.script.SimpleScript;

import javax.script.ScriptEngine;
import java.io.InputStream;

/**
 * An implementation of {@link GenericScriptBuilder} used for packed scripts.
 *
 * @author Corey Shupe
 * @see GenericScriptBuilder
 */
public class PackedScriptBuilder extends GenericScriptBuilder {

	/**
	 * An {@link Object} to identify the {@link Script} in the {@link ScriptManager}.
	 */
	private Object identifier;

	/**
	 * Initializes a new {@link PackedScriptBuilder}.
	 *
	 * @param manager
	 * 		The {@link ScriptManager} used in the {@link GenericScriptBuilder}.
	 * @param engine
	 * 		The {@link ScriptEngine} used in the {@link GenericScriptBuilder}.
	 * @param identifier
	 * 		The identifier to use for {@link #identifier}.
	 *
	 * @see #PackedScriptBuilder(ScriptManager, ScriptEngine, InputStream, Object)
	 */
	public PackedScriptBuilder(ScriptManager manager, ScriptEngine engine, Object identifier) {
		this(manager, engine, null, identifier);
	}

	/**
	 * Initializes a new {@link PackedScriptBuilder}.
	 *
	 * @param manager
	 * 		The {@link ScriptManager} used in the {@link GenericScriptBuilder}.
	 * @param engine
	 * 		The {@link ScriptEngine} used in the {@link GenericScriptBuilder}.
	 * @param stream
	 * 		The {@link InputStream} used in the {@link GenericScriptBuilder}.
	 * @param identifier
	 * 		The identifier to use for {@link #identifier}.
	 *
	 * @see GenericScriptBuilder#GenericScriptBuilder(ScriptManager, ScriptEngine, InputStream)
	 */
	public PackedScriptBuilder(ScriptManager manager, ScriptEngine engine, InputStream stream, Object identifier) {
		super(manager, engine, stream);
		this.identifier = identifier;
	}

	/**
	 * Gets the identifier of the {@link Script}.
	 *
	 * @return The identifier of the {@link Script} defined in {@link #identifier}.
	 */
	public Object getIdentifier() {
		return identifier;
	}

	/**
	 * Sets the identifier of the {@link Script}.
	 *
	 * @param identifier
	 * 		To set {@link #identifier} to set the identifier of the {@link Script}.
	 */
	public void setIdentifier(Object identifier) {
		this.identifier = identifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Script build() {
		Script script = super.build();
		if (script instanceof SimpleScript) {
			throw new UnsupportedOperationException("A packed script must be compilable or invocable.");
		}
		script.getScriptManager().insert(script, identifier);
		return script;
	}
}
