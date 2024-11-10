package ikefercol;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File json = new File("src/main/resources/datos.json");
        Liga ligas = objectMapper.readValue(json, Liga.class);

        System.out.println(ligas.getLaliga().get(0).getEquipo());

    }
}
