package tech.yiyehu.locust.common.compiler;

import tech.yiyehu.locust.common.compiler.Compiler;

public class AdaptiveCompiler implements Compiler {

    private static volatile String DEFAULT_COMPILER;

    public static void setDefaultCompiler(String compiler) {
        DEFAULT_COMPILER = compiler;
    }

    @Override
    public Class<?> compile(String code, ClassLoader classLoader) {
        return null;
    }
}
