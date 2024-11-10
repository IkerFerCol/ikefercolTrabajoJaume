package ikefercol;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class Thymeleaf {
    public static void main(String[] args) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();

        Liga ligas = obtenerDatosLigas();

        context.setVariable("ligas", ligas);

        try (FileWriter writer = new FileWriter("ligas_output.html")) {
            templateEngine.process("ligas", context, writer);
            System.out.println("HTML generado exitosamente en ligas_output.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Liga obtenerDatosLigas() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File json = new File("src/main/resources/datos.json");
            return objectMapper.readValue(json, Liga.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new Liga();
        }
    }
}
