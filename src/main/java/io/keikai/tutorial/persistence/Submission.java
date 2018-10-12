package io.keikai.tutorial.persistence;

import java.io.*;
import java.time.LocalDateTime;

/**
 * represent a form submission
 */
public class Submission {
    public enum State{
        WAITING, APPROVED, REJECTED
    }

    private int id;
    private State state = State.WAITING;
    private LocalDateTime lastUpdate = LocalDateTime.now();
    private ByteArrayOutputStream form;
    private String formName;
    private String owner;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public ByteArrayOutputStream getForm() {
        return form;
    }

    public void setForm(ByteArrayOutputStream form) {
        this.form = form;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
