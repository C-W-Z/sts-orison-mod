package orison.core.interfaces;

import java.util.List;

import orison.core.abstracts.AbstractOrison;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

public interface OrisonExtension {
    // 標記類別用的 Annotation
    @Retention(RetentionPolicy.RUNTIME) // 保留到執行時，反射可用
    @Target(ElementType.TYPE) // 只能用在類/介面上
    public @interface Initializer {}

    void registerOrisons();

    void addCommonOrisonsToPool(List<AbstractOrison> pool, boolean adv);
}
