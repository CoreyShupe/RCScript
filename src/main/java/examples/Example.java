package examples;

import us.nullbytes.rcscript.DefaultScriptManager;
import us.nullbytes.rcscript.Script;
import us.nullbytes.rcscript.ScriptBuilder;

// This is temporary, better examples are in my list of things to do
public class Example {

	public static void main(String[] args) {
		// Process a new `js` or javascript engine, then if present continue (lambda expression)
		DefaultScriptManager.processEngine("js").ifPresent(engine -> {
			// Open a new script builder after defining the script
			ScriptBuilder builder = engine.openScript("function run(numb) { return eval(value + numb); }");
			// Place the value `value` as 5 in the engine
			builder.place("value", 3);
			// Build the script from the builder
			Script script = builder.build();
			// Get the result from the script using the defined function run
			int result = script.expect(Integer.TYPE, "run", 10);
			// Print the result should be 13 or (10 + 3)
			System.out.println(result);
		});
	}

}
