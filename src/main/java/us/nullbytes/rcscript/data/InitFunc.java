package us.nullbytes.rcscript.data;

/**
 * A representation of an initializer function. A simple data class to pass through specifically
 * a string and object.
 *
 * @author Corey Shupe
 */
public class InitFunc {

	/**
	 * A {@link String} representing the name of the function.
	 */
	private String name;

	/**
	 * An array of {@link Object}s representing the parameters of the function.
	 */
	private Object[] params;

	/**
	 * An initializer for a script {@link InitFunc}.
	 *
	 * @param name
	 * 		The name of the function.
	 * @param params
	 * 		The parameters of the function.
	 */
	public InitFunc(String name, Object[] params) {
		this.name = name;
		this.params = params;
	}

	/**
	 * Gets the name of the function.
	 *
	 * @return The name of the function.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the parameters of the function.
	 *
	 * @return The parameters of the function.
	 */
	public Object[] getParams() {
		return params;
	}
}
