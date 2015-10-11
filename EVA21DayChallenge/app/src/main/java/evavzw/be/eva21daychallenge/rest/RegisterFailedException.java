package evavzw.be.eva21daychallenge.rest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasper De Vrient on 11/10/2015.
 */
public class RegisterFailedException extends IllegalArgumentException{

    private List<String> messages = new ArrayList<>();

    public List<String> getMessages() {
        return messages;
    }

    public RegisterFailedException(String detailMessage) {
        super(detailMessage);
    }

    public RegisterFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegisterFailedException(Throwable cause) {
        super(cause);
    }

    public RegisterFailedException(List<String> messages) {
        this.messages = messages;
    }

    public RegisterFailedException(String detailMessage, List<String> messages) {
        super(detailMessage);
        this.messages = messages;
    }

    public RegisterFailedException(String message, Throwable cause, List<String> messages) {
        super(message, cause);
        this.messages = messages;
    }

    public RegisterFailedException(Throwable cause, List<String> messages) {
        super(cause);
        this.messages = messages;
    }
}
