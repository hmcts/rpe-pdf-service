package uk.gov.hmcts.reform.cmc.pdf;

import java.util.Map;

public interface Template {

    public enum Type {
        HTML, PDF
    }

    /**
     * Resource name.
     *
     * @return The name of resource
     */
    String getTemplateUri();

    /**
     * Template type used to distinguish correct renderer
     * @return The type of template
     */
    Type getTemplateType();

    /**
     * Run validation on template arguments.
     *
     * @param variables Template variables
     * @return Is valid or not
     */
    boolean validate(Map<String, Object> variables);
}

