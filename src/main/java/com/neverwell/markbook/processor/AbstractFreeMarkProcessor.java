package com.neverwell.markbook.processor;

import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;

/**
 * @author neverwell
 * @Description:
 */
public abstract class AbstractFreeMarkProcessor implements Processor {

    protected abstract Template getTemplate() throws IOException;

    protected abstract Object getModel(SourceNoteData sourceNoteData);

    protected abstract Writer getWriter(SourceNoteData sourceNoteData) throws FileNotFoundException;


    @Override
    public final void process(SourceNoteData sourceNoteData) throws TemplateException, IOException {
        Template template = getTemplate();
        Object model = getModel(sourceNoteData);
        Writer writer = getWriter(sourceNoteData);
        template.process(model, writer);
    }
}
