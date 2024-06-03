package com.libraryportal.exception;

import java.util.Date;

/**
 * Class representing details of an error response.
 */
public class ErrorDetails {
    
    private Date timestamp;
    private String message;
    private String details;

    /**
     * Constructs a new ErrorDetails object with the specified timestamp, message, and details.
     *
     * @param timestamp The timestamp indicating when the error occurred.
     * @param message The error message providing information about the error.
     * @param details Additional details about the error.
     */
    public ErrorDetails(Date timestamp, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    /**
     * Retrieves the timestamp of the error.
     *
     * @return The timestamp indicating when the error occurred.
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Retrieves the error message.
     *
     * @return The error message providing information about the error.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Retrieves additional details about the error.
     *
     * @return Additional details about the error.
     */
    public String getDetails() {
        return details;
    }
}
