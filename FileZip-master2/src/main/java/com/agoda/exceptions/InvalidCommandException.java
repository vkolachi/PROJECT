package com.agoda.exceptions;

public class InvalidCommandException extends RuntimeException {

	private static final long serialVersionUID = -1016576572534350582L;

	public InvalidCommandException(String exception) {
		super(exception);
	}

}
