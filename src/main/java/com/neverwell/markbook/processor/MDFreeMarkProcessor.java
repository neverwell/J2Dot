package com.neverwell.markbook.processor;

import com.intellij.ide.fileTemplates.impl.UrlUtil;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author neverwell
 * @Description:
 */
public class MDFreeMarkProcessor extends AbstractFreeMarkProcessor {


    @Override
    protected Template getTemplate() throws IOException {
        Configuration configuration = new Configuration();
        String templateContext = UrlUtil.loadText(MDFreeMarkProcessor.class.getResource("/template/md.ftl"));

        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("MDTemplate", templateContext);

        configuration.setTemplateLoader(stringTemplateLoader);
        return configuration.getTemplate("MDTemplate");
    }

    @Override
    protected Object getModel(SourceNoteData sourceNoteData) {
        Map<String, Object> model = new HashMap<>();
        model.put("topic", sourceNoteData.getTopic());
        model.put("noteList", sourceNoteData.getNoteList());
        return model;
    }

    @Override
    protected Writer getWriter(SourceNoteData sourceNoteData) throws FileNotFoundException {
        String fileName = sourceNoteData.getFileName();
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));

    }
}
