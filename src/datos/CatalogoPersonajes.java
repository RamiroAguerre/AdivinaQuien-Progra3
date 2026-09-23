package datos;

import static modelo.Personaje.ColorPelo.*;
import static modelo.Personaje.Genero.*;

import java.util.ArrayList;
import java.util.List;

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

//*Complejidad O(n**2) pero se ejecuta unicamente cuando se carga el catálogo de personajes.*//
    public static void validar(List<Personaje> personajes) {

        for (int i = 0; i < personajes.size(); i++) {
            for (int j = i + 1; j < personajes.size(); j++) {

                Personaje primero = personajes.get(i);
                Personaje segundo = personajes.get(j);

                if (primero.getId() == segundo.getId()) {
                    throw new IllegalArgumentException("ID repetido.");
                }

                if (mismasCaracteristicas(primero, segundo)) {
                    throw new IllegalArgumentException(
                            "Hay personajes con las mismas características.");
                }
            }
        }
    }
    
    private static boolean mismasCaracteristicas(Personaje primero, Personaje segundo) {

        return primero.getGenero() == segundo.getGenero() && primero.tieneCalvicie() == segundo.tieneCalvicie()
                && primero.tieneLentes() == segundo.tieneLentes() && primero.getColorPelo() == segundo.getColorPelo();
    }
}
