package com.company.task03.entity;

import com.company.task03.util.TerminalIdGenerator;

public class Terminal {

    private final int idTerminal;
    private boolean isFree;

    public Terminal() {
        idTerminal = TerminalIdGenerator.generateId();
        isFree = true;
    }

    public boolean getIsFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        this.isFree = free;
    }

    public int getIdTerminal() {
        return idTerminal;
    }

}
