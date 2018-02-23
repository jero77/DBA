package exceptions;

/**
 * This exception indicates if a HR Guy tried to invite a new candidate
 * without having any qouta left to do so.
 * It is thrown by the HRGuyGUI class.
 * @author Jero
 */
public class OutOfQuotaException extends Exception {

	public OutOfQuotaException() {
		// TODO Auto-generated constructor stub
	}

	public OutOfQuotaException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public OutOfQuotaException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public OutOfQuotaException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public OutOfQuotaException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

}
