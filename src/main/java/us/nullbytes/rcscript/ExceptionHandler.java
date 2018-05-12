package us.nullbytes.rcscript;

/**
 * The {@code interface} used for handling {@link Exception}s. Defined as a {@link
 * FunctionalInterface}.
 *
 * @param <T>
 * 		The type of exception to handle.
 *
 * @author Corey Shupe
 */
@FunctionalInterface
public interface ExceptionHandler<T extends Exception> {

	/**
	 * Handles the {@link T} exception.
	 *
	 * @param ex
	 * 		The {@link T} exception thrown.
	 */
	void handle(T ex);
}
