package orison.utils;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import orison.core.interfaces.OrisonExtension;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.evacipated.cardcrawl.modthespire.Loader;

public class OrisonExtensionScanner {

    private static final Logger logger = LogManager.getLogger(OrisonExtensionScanner.class);

    /** @deprecated */
    @Deprecated
    public static Set<String> scanOrisonExtensions() {
        Set<String> foundClasses = new HashSet<>();
        ClassPool pool = ClassPool.getDefault();

        // 把所有 mod jar 路徑放進 classpath
        List<File> modJars = new ArrayList<>();
        Arrays.stream(Loader.MODINFOS).forEach(modInfo -> {
            File jarFile = new File(modInfo.jarURL.getFile());
            if (!jarFile.exists())
                return;
            modJars.add(jarFile);
            try {
                pool.insertClassPath(jarFile.getAbsolutePath());
            } catch (Exception e) {
                logger.error("Failed to insert classpath: {}", jarFile, e);
            }
        });

        // 批量掃描所有 jar
        for (File jarFile : modJars) {
            try (JarFile jar = new JarFile(jarFile)) {
                Enumeration<? extends ZipEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    if (!entry.getName().endsWith(".class"))
                        continue;

                    String className = entry.getName().replace("/", ".").replace(".class", "");

                    AnnotationsAttribute attr;
                    try {
                        CtClass ctClass = pool.get(className);
                        attr = (AnnotationsAttribute) ctClass.getClassFile()
                                .getAttribute(AnnotationsAttribute.visibleTag);
                    } catch (Exception e) {
                        logger.warn("Failed to scan {}", className, e);
                        e.printStackTrace();
                        continue;
                    }
                    if (attr == null)
                        continue;
                    for (Annotation ann : attr.getAnnotations())
                        if (ann.getTypeName().equals(OrisonExtension.Initializer.class.getName()))
                            foundClasses.add(className);
                }
            } catch (Exception e) {
                logger.error("Error scanning jar {}", jarFile.getName(), e);
                e.printStackTrace();
            }
        }

        return foundClasses;
    }

    public static boolean isOrisonExtension(Class<?> clazz) {
        return OrisonExtension.class.isAssignableFrom(clazz)
                && !clazz.isInterface() && !clazz.isEnum()
                && !Modifier.isAbstract(clazz.getModifiers());
    }
}
