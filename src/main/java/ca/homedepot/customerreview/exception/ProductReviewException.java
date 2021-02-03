package ca.homedepot.customerreview.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class ProductReviewException extends RuntimeException {
    public ProductReviewException(final String message)
    {
        super("There was an error processing review form: " + message);
    }
}
