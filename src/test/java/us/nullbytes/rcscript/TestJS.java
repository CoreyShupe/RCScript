package us.nullbytes.rcscript;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TestJS {

	private final static DefaultScriptManager manager = DefaultScriptManager.processEngine("js").orElse(null);

	/**
	 * Tests scripts which have no functions but only simple evaluations.
	 */
	@Test
	public void testFunctionlessEval() {
		int result = manager.openScript("eval(1 + 4)").setInitFunction(null).build().expect(Integer.class);
		Assert.assertEquals(result, 5);
		try {
			manager.openScript("eval(1 + 4)").setInitFunction(null).build().execute("init");
			Assert.fail();
		} catch (Exception ex) {
			Assert.assertEquals(ex.getClass(), UnsupportedOperationException.class);
		}
	}

	/**
	 * Tests scripts which require separate functions to run.
	 */
	@Test
	public void testFunctionEval() {
		UUID scriptID = UUID.randomUUID();
		manager.packScript("function init() { return \"Example String\"; }" +
				"function secondary(numb) { return eval(numb + typer); }", scriptID).place("typer", 3).build();
		Optional<Script> script = manager.unpack(scriptID);
		Assert.assertTrue(script.isPresent());
		script.ifPresent(scr -> {
			try {
				Assert.assertEquals("Example String", scr.expectLater(String.class).get(10, TimeUnit.SECONDS));
				Assert.assertEquals(Integer.valueOf(10), scr.expect(Integer.class, "secondary", 7));
			} catch (InterruptedException | ExecutionException | TimeoutException ex) {
				ex.printStackTrace();
				Assert.fail("Exception thrown.");
			}
		});
	}

}
