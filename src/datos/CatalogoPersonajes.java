package datos;

import static modelo.Personaje.ColorPelo.*;
import static modelo.Personaje.Genero.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import juego.Pregunta;
import modelo.Personaje;

/** Carga inicial agrupada por género, con ID autoincremental y datos únicos. */
public class CatalogoPersonajes {
    public static List<Personaje> cargar() {
        List<Personaje> personajes = new ArrayList<>();
        // Parámetros: nombre, género, calvicie parcial, lentes, color de pelo.
        // Calvicie parcial permite conservar pelo de color en los costados.
        agregar(personajes, "Pedro", HOMBRE, false, true, NEGRO);
        agregar(personajes, "Juan", HOMBRE, false, false, NEGRO);
        agregar(personajes, "Carlos", HOMBRE, true, false, NEGRO);
        agregar(personajes, "Diego", HOMBRE, true, true, NEGRO);
        agregar(personajes, "Esteban", HOMBRE, false, false, COLORADO);
        agregar(personajes, "Federico", HOMBRE, false, true, COLORADO);
        agregar(personajes, "Gabriel", HOMBRE, true, false, COLORADO);
        agregar(personajes, "Hugo", HOMBRE, true, true, COLORADO);
        agregar(personajes, "Ivan", HOMBRE, false, false, RUBIO);
        agregar(personajes, "Lucas", HOMBRE, false, true, RUBIO);
        agregar(personajes, "Marcos", HOMBRE, true, false, RUBIO);
        agregar(personajes, "Nicolas", HOMBRE, true, true, RUBIO);
        agregar(personajes, "Ana", MUJER, false, false, NEGRO);
        agregar(personajes, "Beatriz", MUJER, false, true, NEGRO);
        agregar(personajes, "Carla", MUJER, true, false, NEGRO);
        agregar(personajes, "Diana", MUJER, true, true, NEGRO);
        agregar(personajes, "Elena", MUJER, false, false, COLORADO);
        agregar(personajes, "Florencia", MUJER, false, true, COLORADO);
        agregar(personajes, "Gabriela", MUJER, true, false, COLORADO);
        agregar(personajes, "Helena", MUJER, true, true, COLORADO);
        agregar(personajes, "Ines", MUJER, false, false, RUBIO);
        agregar(personajes, "Julia", MUJER, false, true, RUBIO);
        agregar(personajes, "Laura", MUJER, true, false, RUBIO);
        validar(personajes);
        return personajes;
    }

    private static void agregar(List<Personaje> lista, String nombre,
            Personaje.Genero genero, boolean calvicie, boolean lentes,
            Personaje.ColorPelo color) {
        lista.add(new Personaje(lista.size() + 1, nombre, genero, calvicie, lentes, color));
    }

    public static void validar(List<Personaje> personajes) {
        Set<Integer> ids = new HashSet<>();
        Set<String> combinaciones = new HashSet<>();
        for (Personaje personaje : personajes) {
            if (!ids.add(personaje.getId())) {
                throw new IllegalArgumentException("ID repetido: " + personaje.getId());
            }
            // Se valida lo que las preguntas realmente pueden distinguir.
            StringBuilder firma = new StringBuilder();
            for (Pregunta pregunta : Pregunta.values()) {
                firma.append(pregunta.evaluar(personaje) ? '1' : '0');
            }
            if (!combinaciones.add(firma.toString())) {
                throw new IllegalArgumentException(
                        "Características indistinguibles para: " + personaje.getNombre());
            }
        }
    }
}
