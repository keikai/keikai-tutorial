package io.keikai.tutorial;

/**
 * a server response for browsers to show a client request result, e.g. success, failure, message
 */
public class Result {
    public enum State {
        SUCCESS, ERROR
    }

    private State state;
    private String message;

    public static Result getErrorResult(String message){
        Result r = new Result();
        r.setState(State.ERROR);
        r.setMessage(message);
        return r;
    }

    public static Result getSuccess(String message){
        Result r = new Result();
        r.setState(State.SUCCESS);
        r.setMessage(message);
        return r;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
