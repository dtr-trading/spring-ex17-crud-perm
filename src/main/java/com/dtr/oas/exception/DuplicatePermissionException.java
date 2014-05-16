package com.dtr.oas.exception;

public class DuplicatePermissionException extends Exception {

    private static final long serialVersionUID = 4248723875829158068L;

    public DuplicatePermissionException() {
    }

    public DuplicatePermissionException(String arg0) {
        super(arg0);
    }

    public DuplicatePermissionException(Throwable arg0) {
        super(arg0);
    }

    public DuplicatePermissionException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public DuplicatePermissionException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

}
