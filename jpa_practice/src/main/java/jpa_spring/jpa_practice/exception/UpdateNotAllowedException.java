package jpa_spring.jpa_practice.exception;

public class UpdateNotAllowedException extends RuntimeException {
    public UpdateNotAllowedException(Long articleId) {
        super("Article with ID " + articleId + " has been updated within the last 10 minutes and cannot be updated again.");
    }
}

