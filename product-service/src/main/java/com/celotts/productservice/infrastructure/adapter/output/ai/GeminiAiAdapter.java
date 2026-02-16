package com.celotts.productservice.infrastructure.adapter.output.ai;

import com.celotts.productservice.domain.port.output.ai.GenerativeAiPort;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GeminiAiAdapter implements GenerativeAiPort {

    private final ChatClient chatClient;

    @Override
    public String generateProductDescription(String productName, List<String> tags) {
        String template = """
                Actúa como un experto en marketing de comercio electrónico.
                Genera una descripción atractiva, corta y profesional (máximo 200 caracteres) para un producto.
                
                Producto: {name}
                Etiquetas/Keywords: {tags}
                
                Descripción:
                """;
        PromptTemplate promptTemplate = new PromptTemplate(template);
        Prompt prompt = promptTemplate.create(Map.of("name", productName, "tags", String.join(", ", tags)));
        
        return chatClient.call(prompt).getResult().getOutput().getContent();
    }
}