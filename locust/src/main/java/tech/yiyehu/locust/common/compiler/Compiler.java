package tech.yiyehu.locust.common.compiler;

import tech.yiyehu.locust.common.extension.SPI;

@SPI("javassist")
public interface Compiler {
    Class<?> compile(String code, ClassLoader classLoader);
}
