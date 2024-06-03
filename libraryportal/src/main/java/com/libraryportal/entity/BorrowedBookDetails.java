package com.libraryportal.entity;

public class BorrowedBookDetails {
    private Long borrowerId;
    private String borrowerName;
    private String borrowerEmail;
    private Long bookId;
    private String isbn;
    private String title;
    private String author;
    private String status;

    // Constructors
    public BorrowedBookDetails() {
    }

    public BorrowedBookDetails(Borrower borrower, Book book) {
        this.borrowerId = borrower.getId();
        this.borrowerName = borrower.getName();
        this.borrowerEmail = borrower.getEmail();
        this.bookId = book.getId();
        this.isbn = book.getIsbn();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.status = book.getStatus().toString();
    }

    // Getters and setters
    public Long getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(Long borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getBorrowerEmail() {
        return borrowerEmail;
    }

    public void setBorrowerEmail(String borrowerEmail) {
        this.borrowerEmail = borrowerEmail;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

