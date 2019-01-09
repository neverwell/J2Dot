package com.nemo.game.util;

public class ScriptException extends RuntimeException {

    private String prompt;

    private AssertType assertType;

    public ScriptException(String prompt, AssertType assertType) {
        this.prompt = prompt;
        this.assertType = assertType;
    }

    public String getPrompt() {
        return prompt;
    }

    public AssertType getAssertType() {
        return assertType;
    }
}
