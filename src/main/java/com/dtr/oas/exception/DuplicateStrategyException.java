package com.dtr.oas.exception;

public class DuplicateStrategyException extends Exception {
    private static final long serialVersionUID = 4952390298437287493L;

    public DuplicateStrategyException() {
    }

    public DuplicateStrategyException(String arg0) {
        super(arg0);
    }

    public DuplicateStrategyException(Throwable arg0) {
        super(arg0);
    }

    public DuplicateStrategyException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public DuplicateStrategyException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

}
