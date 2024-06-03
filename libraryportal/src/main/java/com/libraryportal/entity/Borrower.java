package com.libraryportal.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Borrower {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    
    private String name;
    private String email;
    
    /**
     * Get the unique ID of the borrower.
     * 
     * @return The ID of the borrower.
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the unique ID of the borrower.
     * 
     * @param id The ID of the borrower.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the name of the borrower.
     * 
     * @return The name of the borrower.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the borrower.
     * 
     * @param name The name of the borrower.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the email address of the borrower.
     * 
     * @return The email address of the borrower.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email address of the borrower.
     * 
     * @param email The email address of the borrower.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
