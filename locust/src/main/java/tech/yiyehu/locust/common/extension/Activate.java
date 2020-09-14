package tech.yiyehu.locust.common.extension;

public @interface Activate {
    /**
     * Activate the current extension when the specified keys appear in the URL's parameters.
     * @return URL parameter keys
     * @see ExtensionLoader#getActivateExtension(URL, String)
     */
    String[] value() default {};

    int order() default 0;
}
