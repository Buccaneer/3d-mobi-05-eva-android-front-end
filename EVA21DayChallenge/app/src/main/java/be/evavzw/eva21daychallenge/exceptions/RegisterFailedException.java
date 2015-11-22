package be.evavzw.eva21daychallenge.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jasper De Vrient on 11/10/2015.
 */
public class RegisterFailedException extends IllegalArgumentException {

    private Map<String, List<String>> messages = new HashMap<>();

    public Map<String, List<String>> getMessages() {
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

    public RegisterFailedException(Map<String, List<String>> messages) {
        this.messages = messages;
    }

    public RegisterFailedException(String detailMessage, Map<String, List<String>> messages) {
        super(detailMessage);
        this.messages = messages;
    }

    public RegisterFailedException(String message, Throwable cause, HashMap<String, List<String>> messages) {
        super(message, cause);
        this.messages = messages;
    }

    public RegisterFailedException(Throwable cause, Map<String, List<String>> messages) {
        super(cause);
        this.messages = messages;
    }
}
