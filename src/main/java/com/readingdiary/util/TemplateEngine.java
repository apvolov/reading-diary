package com.readingdiary.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class TemplateEngine {

    private static final Configuration cfg;

    static {
        cfg = new Configuration(Configuration.VERSION_2_3_33);
        cfg.setClassLoaderForTemplateLoading(TemplateEngine.class.getClassLoader(), "templates");
        cfg.setDefaultEncoding("UTF-8");
    }

    public static String render(String templateName, Map<String, Object> model) {
        try {
            Template template = cfg.getTemplate(templateName);
            StringWriter writer = new StringWriter();
            template.process(model, writer);
            return writer.toString();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException("Ошибка рендеринга шаблона: " + templateName, e);
        }
    }
}
