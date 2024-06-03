package com.libraryportal.exception;

/**
 * Class representing an error response containing an error code and message.
 */
public class ErrorResponse {
    
    private int errorCode;
    private String errorMessage;

    /**
     * Constructs a new ErrorResponse object with the specified error code and message.
     *
     * @param errorCode The error code indicating the type of error.
     * @param errorMessage The error message providing information about the error.
     */
    public ErrorResponse(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * Retrieves the error code.
     *
     * @return The error code indicating the type of error.
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the error code.
     *
     * @param errorCode The error code indicating the type of error.
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Retrieves the error message.
     *
     * @return The error message providing information about the error.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the error message.
     *
     * @param errorMessage The error message providing information about the error.
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}


