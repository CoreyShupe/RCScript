package us.nullbytes.rcscript.builder;

import us.nullbytes.rcscript.Script;
import us.nullbytes.rcscript.ScriptBuilder;
import us.nullbytes.rcscript.ScriptManager;
import us.nullbytes.rcscript.builder.script.CompilableScript;
import us.nullbytes.rcscript.builder.script.InvocableScript;
import us.nullbytes.rcscript.builder.script.SimpleScript;
import us.nullbytes.rcscript.data.InitFunc;
import us.nullbytes.rcscript.data.ScriptData;

import javax.script.*;
import java.io.*;
import java.util.function.Consumer;

/**
 * The generic {@link ScriptBuilder} used for most implementations.
 *
 * @author Corey Shupe
 * @see ScriptBuilder
 */
public class GenericScriptBuilder implements ScriptBuilder {

	/**
	 * The {@link ScriptData} used for the built {@link Script}.
	 */
	private final ScriptData scriptData;

	/**
	 * The {@link InitFunc} used to define the initializer function.
	 */
	private InitFunc initFunc;

	/**
	 * Initializes a {@link GenericScriptBuilder}
	 *
	 * @param manager
	 * 		The {@link ScriptManager} to define the manager in the {@link #scriptData}.
	 * @param engine
	 * 		The {@link ScriptEngine} to define the manager in the {@link #scriptData}.
	 *
	 * @see #GenericScriptBuilder(ScriptManager, ScriptEngine, InputStream)
	 */
	public GenericScriptBuilder(ScriptManager manager, ScriptEngine engine) {
		this(manager, engine, null);
	}

	/**
	 * Initializes a {@link GenericScriptBuilder}
	 *
	 * @param manager
	 * 		The {@link ScriptManager} to define the manager in the {@link #scriptData}.
	 * @param engine
	 * 		The {@link ScriptEngine} to define the engine in the {@link #scriptData}.
	 * @param stream
	 * 		The {@link InputStream} to define the stream in the {@link #scriptData}.
	 */
	public GenericScriptBuilder(ScriptManager manager, ScriptEngine engine, InputStream stream) {
		scriptData = new ScriptData(manager, engine, stream);
		this.initFunc = manager.getDefaultInitFunction();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptBuilder place(String identifier, Object object) {
		scriptData.getEngine().put(identifier, object);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptBuilder consumeContext(Consumer<ScriptContext> contextConsumer) {
		contextConsumer.accept(scriptData.getEngine().getContext());
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptBuilder setStream(InputStream stream) {
		scriptData.setStream(stream);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptBuilder setScript(String script) {
		setStream(new ByteArrayInputStream(script.getBytes()));
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptBuilder setResourceLocation(String location) {
		setStream(getClass().getResourceAsStream(location.startsWith("/") ? location : '/' + location));
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptBuilder setLocation(String location) {
		setLocation(new File(scriptData.getManager().getScriptsDir(), location));
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptBuilder setLocation(File file) {
		try {
			setStream(new FileInputStream(file));
		} catch (IOException ex) {
			scriptData.getManager().getExceptionHandler().handle(ex);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptBuilder setInitFunction(String function, Object... params) {
		if (function == null) initFunc = null;
		else this.initFunc = new InitFunc(function, params);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptEngine extractEngine() {
		return scriptData.getEngine();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Script build() {
		if (scriptData.getStream() == null) {
			throw new UnsupportedOperationException("You cannot build a script without a source.");
		}
		ScriptEngine engine = scriptData.getEngine();
		try {
			if (engine instanceof Invocable) {
				if (initFunc == null) {
					return engine instanceof Compilable ?
							new CompilableScript(scriptData) :
							new SimpleScript(scriptData);
				}
				if (!(engine instanceof Compilable)) {
					return new SimpleScript(scriptData);
				}
				return new InvocableScript(scriptData, initFunc);
			} else if (engine instanceof Compilable) {
				return new CompilableScript(scriptData);
			} else {
				return new SimpleScript(scriptData);
			}
		} catch (ScriptException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
