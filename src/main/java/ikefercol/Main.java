package ikefercol;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.xml.validation.Schema;
import java.io.*;
import java.util.List;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException {
        File json = new File("src/main/resources/datos.json");
        File jsonschema = new File("src/main/resources/schema.json");

        Properties props = new Properties();
        try (FileInputStream inputStream = new FileInputStream("src/main/resources/config.ini")) {
            props.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String nombreconfig = props.getProperty("nombre");
        String temaconfig = props.getProperty("tema");

        // Leer los datos del JSON solo para la clase 'Liga'
        ObjectMapper mapper = new ObjectMapper();
        Liga liga = mapper.readValue(json, Liga.class); // Deserializa a la clase Liga

        // Configurar Thymeleaf
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        // Contexto de Thymeleaf
        Context context = new Context();
        context.setVariable("nombre", nombreconfig);
        context.setVariable("tema", temaconfig);
        context.setVariable("laliga", liga.getLaliga());
        context.setVariable("premierleague", liga.getPremierleague());

        // Generar y escribir el archivo index.html
        String indexTemplate = templateEngine.process("plantilla_index", context);
        System.out.println(indexTemplate); // Verifica si se está generando el HTML correctamente
        escribirHTML(indexTemplate, "src/main/resources/Webs/index.html");

        // Generar las páginas individuales de los equipos
        List<Equipo> equipos = liga.getLaliga();
        for (Equipo e : equipos) {
            context.setVariable("equipo", e.getEquipo());
            context.setVariable("lugar", e.getLugar());
            context.setVariable("mejor_jugador", e.getMejorJugador());
            String equipoTemplate = templateEngine.process("plantilla_equipo", context);
            String filename = "src/main/resources/Webs/equipo/" + e.getEquipo().replaceAll("\\s+", "_") + ".html";
            escribirHTML(equipoTemplate, filename);
        }
        List<Equipo> equipospremier = liga.getPremierleague();
        for (Equipo e : equipospremier) {
            context.setVariable("equipo", e.getEquipo());
            context.setVariable("lugar", e.getLugar());
            context.setVariable("mejor_jugador", e.getMejorJugador());
            String equipoTemplate = templateEngine.process("plantilla_equipo", context);
            String filename = "src/main/resources/Webs/equipo/" + e.getEquipo().replaceAll("\\s+", "_") + ".html";
            escribirHTML(equipoTemplate, filename);
        }
    }

    // Función para escribir el archivo HTML
    public static void escribirHTML(String html, String nombrefichero) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombrefichero))) {
            writer.write(html); // Escribe el contenido HTML
        } catch (IOException e) {
            e.printStackTrace(); // Imprime la excepción para ver si hay algún error
            throw new RuntimeException("Error al escribir el archivo HTML", e);
        }
    }
}
