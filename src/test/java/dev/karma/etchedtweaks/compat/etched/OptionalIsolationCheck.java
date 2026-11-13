package dev.karma.etchedtweaks.compat.etched;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.*;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import com.google.gson.JsonParser;

/** Class-linkage check with optional packages deliberately denied; not a Forge launch. */
public final class OptionalIsolationCheck {
    public static void main(String[] args) throws Exception {
        URL[] urls = Arrays.stream(System.getProperty("java.class.path").split(java.io.File.pathSeparator))
                .map(Path::of).filter(p -> !p.getFileName().toString().startsWith("sophisticated"))
                .map(p -> { try { return p.toUri().toURL(); } catch(Exception e) { throw new RuntimeException(e); } }).toArray(URL[]::new);
        try (URLClassLoader isolated = new URLClassLoader(urls, ClassLoader.getPlatformClassLoader()) {
            @Override protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
                if (name.startsWith("net.p3pp3rf1y.sophisticated")) throw new ClassNotFoundException("Optional package denied: " + name);
                return super.loadClass(name, resolve);
            }
        }) {
            for (String name : List.of("dev.karma.etchedtweaks.EtchedTweaks",
                    "dev.karma.etchedtweaks.compat.etched.EtchedClientRegistration",
                    "dev.karma.etchedtweaks.mixin.OptionalMixinPlugin")) {
                Class<?> type = Class.forName(name, true, isolated);
                type.getDeclaredMethods(); type.getDeclaredConstructors();
            }
        }
        var config = JsonParser.parseString(Files.readString(Path.of("src/main/resources/etchedtweaks.mixins.json"))).getAsJsonObject();
        if (!config.get("plugin").getAsString().endsWith("OptionalMixinPlugin")) throw new AssertionError("Missing optional mixin gate");
        Path classes=Path.of("build/classes/java/main");
        try(var files=Files.walk(classes)) {
            for(Path file:files.filter(p->p.toString().endsWith(".class")).toList()) {
                ClassNode node=new ClassNode(); new ClassReader(Files.readAllBytes(file)).accept(node,ClassReader.SKIP_CODE);
                if (node.visibleAnnotations == null) continue;
                for(var annotation:node.visibleAnnotations) {
                    if(annotation.desc.contains("EventBusSubscriber") && !node.name.endsWith("EtchedClientRegistration"))
                        throw new AssertionError("Ungated automatic subscriber: "+node.name);
                }
            }
        }
        System.out.println("PASS: entry points and mixin plugin link with Sophisticated packages denied; no ungated integration subscribers.");
    }
}
