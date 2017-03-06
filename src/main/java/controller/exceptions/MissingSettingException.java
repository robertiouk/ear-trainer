package controller.exceptions;

public class MissingSettingException extends RuntimeException {
	public MissingSettingException(String string) {
		super(string);
	}

	private static final long serialVersionUID = -607686835704265035L;
}