package com.dtr.oas.exception;

public class DuplicateRoleException extends Exception {
    private static final long serialVersionUID = 4867645708199153376L;

    public DuplicateRoleException() {
    }

    public DuplicateRoleException(String arg0) {
        super(arg0);
    }

    public DuplicateRoleException(Throwable arg0) {
        super(arg0);
    }

    public DuplicateRoleException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public DuplicateRoleException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

}
