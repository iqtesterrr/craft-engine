package net.momirealms.craftengine.bukkit.plugin.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class HelloWorldAgent {

    public static void agentmain(String args, Instrumentation instrumentation) {
        new AgentBuilder.Default()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .with(AgentBuilder.RedefinitionStrategy.REDEFINITION)
                .type(ElementMatchers.named("net.minecraft.server.level.ServerLevel"))
                .transform((builder, typeDescription, classLoader, module, protectionDomain) ->
                        builder.visit(Advice.to(HelloWorldAdvice.class)
                                .on(ElementMatchers.named("getServer")))
                )
                .installOn(instrumentation);
    }

    public static class HelloWorldAdvice {
        @Advice.OnMethodExit
        public static void onExit() {
            System.out.println("Hello World");
        }
    }
}
