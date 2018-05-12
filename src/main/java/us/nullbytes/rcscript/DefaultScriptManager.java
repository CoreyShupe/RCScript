package us.nullbytes.rcscript;

import us.nullbytes.rcscript.builder.GenericScriptBuilder;
import us.nullbytes.rcscript.builder.PackedScriptBuilder;
import us.nullbytes.rcscript.data.InitFunc;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The default {@link ScriptManager} implementation.
 *
 * @author Corey Shupe
 * @see ScriptManager
 */
public class DefaultScriptManager implements ScriptManager {

	/**
	 * The {@link Map} holding the {@link DefaultScriptManager}s for global usage.
	 */
	private final static Map<String, DefaultScriptManager> scriptManagers = new HashMap<>();

	/**
	 * Gets the cached {@link DefaultScriptManager} based on a name.
	 *
	 * @param name
	 * 		The name of the {@link DefaultScriptManager}.
	 *
	 * @return The {@link DefaultScriptManager} linked or null if not found.
	 */
	public static DefaultScriptManager get(String name) {
		return scriptManagers.get(name);
	}

	/**
	 * Tries to register a new {@link DefaultScriptManager} into the cache.
	 *
	 * @param name
	 * 		The name of the {@link DefaultScriptManager} and the {@link ScriptEngineFactory}.
	 *
	 * @return An {@link Optional} containing null if failed, or the {@link DefaultScriptManager} if
	 * succeeded.
	 */
	public static Optional<DefaultScriptManager> processEngine(String name) {
		ScriptEngine received = new ScriptEngineManager().getEngineByName(name);
		if (received == null) {
			return Optional.empty();
		}
		DefaultScriptManager newManager = new DefaultScriptManager(received.getFactory());
		scriptManagers.put(name, newManager);
		return Optional.of(newManager);
	}

	/**
	 * The {@link Map} holding all of the cached {@link Script}s.
	 */
	private Map<Object, Script> scriptCache = IdentifierRule.EQUALITY.generateNewMap();

	/**
	 * The {@link ScriptEngineFactory} to produce {@link ScriptEngine}s.
	 */
	private ScriptEngineFactory engineFactory;

	/**
	 * The {@link File} directory for scripts.
	 */
	private File scriptsDir = null;

	/**
	 * The {@link ExceptionHandler} for all {@link IOException}s.
	 */
	private ExceptionHandler<IOException> ioExceptionHandler = IOException::printStackTrace;

	/**
	 * The {@link InitFunc} representing the default function for initializing scripts.
	 */
	private InitFunc initFunc = new InitFunc("init", new Object[]{});

	/**
	 * Initializes a new {@link DefaultScriptManager}.
	 *
	 * @param engineName
	 * 		The name of the {@link ScriptEngine} to receive the {@link ScriptEngineFactory}.
	 *
	 * @see #DefaultScriptManager(ScriptEngineFactory)
	 */
	private DefaultScriptManager(String engineName) {
		this(new ScriptEngineManager().getEngineByName(engineName).getFactory());
	}

	/**
	 * Initializes a new {@link DefaultScriptManager}.
	 *
	 * @param factory
	 * 		The {@link ScriptEngineFactory} which provides {@link ScriptEngine}s
	 */
	private DefaultScriptManager(ScriptEngineFactory factory) {
		this.engineFactory = factory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptManager copyIntoUniqueInstance() {
		DefaultScriptManager that = new DefaultScriptManager(engineFactory.getEngineName());
		that.scriptCache.putAll(this.scriptCache);
		that.ioExceptionHandler = this.ioExceptionHandler;
		that.initFunc = new InitFunc(this.initFunc.getName(), this.initFunc.getParams());
		return that;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptManager generateUniqueInstance() {
		return new DefaultScriptManager(engineFactory.getEngineName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefaultInitFunction(String function, Object... params) {
		this.initFunc = new InitFunc(function, params);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InitFunc getDefaultInitFunction() {
		return initFunc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setExceptionHandler(ExceptionHandler<IOException> exceptionHandler) {
		this.ioExceptionHandler = exceptionHandler;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExceptionHandler<IOException> getExceptionHandler() {
		return ioExceptionHandler;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setScriptsDir(File location) {
		this.scriptsDir = location;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getScriptsDir() {
		return scriptsDir;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptBuilder newScript() {
		return new GenericScriptBuilder(this, engineFactory.getScriptEngine()).place("rcsm", this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptBuilder openScript(InputStream stream) {
		return new GenericScriptBuilder(this, engineFactory.getScriptEngine(), stream).place("rcsm", this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptBuilder openScript(String script) {
		return openScript(new ByteArrayInputStream(script.getBytes()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptBuilder openResourceScript(String location) {
		return openScript(getClass().getResourceAsStream(location));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptBuilder openFileScript(File file) {
		try {
			return openScript(new FileInputStream(file));
		} catch (IOException ex) {
			getExceptionHandler().handle(ex);
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptBuilder openFileScript(String location) {
		if (getScriptsDir() == null) return openFileScript(new File(location));
		return openFileScript(new File(getScriptsDir(), location));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptBuilder newPackedScript(Object identifier) {
		return new PackedScriptBuilder(this, engineFactory.getScriptEngine(), identifier).place("rcsm", this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptBuilder packScript(InputStream stream, Object identifier) {
		return new PackedScriptBuilder(this, engineFactory.getScriptEngine(), stream, identifier).place("rcsm", this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptBuilder packScript(String script, Object identifier) {
		return packScript(new ByteArrayInputStream(script.getBytes()), identifier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptBuilder packResourceScript(String location, Object identifier) {
		return packScript(getClass().getResourceAsStream(location.startsWith("/") ? location : '/' + location), identifier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptBuilder packFileScript(File file, Object identifier) {
		try {
			return packScript(new FileInputStream(file), identifier);
		} catch (IOException ex) {
			getExceptionHandler().handle(ex);
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScriptBuilder packFileScript(String location, Object identifier) {
		if (getScriptsDir() == null) return packFileScript(new File(location), identifier);
		return packFileScript(new File(getScriptsDir(), location), identifier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insert(Script script, Object identifier) {
		scriptCache.put(identifier, script);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Script> unpack(Object identifier) {
		return Optional.ofNullable(scriptCache.get(identifier));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean exists(Object identifier) {
		return scriptCache.containsKey(identifier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object identifier) {
		return scriptCache.remove(identifier) != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void applyIdentifierRule(IdentifierRule rule) {
		Map<Object, Script> nextCache = rule.generateNewMap();
		scriptCache.forEach(nextCache::putIfAbsent);
		scriptCache = nextCache;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearScriptCache() {
		scriptCache.clear();
	}
}
