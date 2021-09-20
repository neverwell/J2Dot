package com.neverwell.markbook.processor;

import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * @author neverwell
 * @Description:
 */
public interface Processor {
    void process(SourceNoteData sourceNoteData) throws TemplateException, IOException;
}
