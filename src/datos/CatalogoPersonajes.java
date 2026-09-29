package datos;

import java.util.ArrayList;
import java.util.List;

import modelo.Personaje;

/** Carga inicial agrupada por género, con ID autoincremental y datos únicos. */
public class CatalogoPersonajes {
    public static List<Personaje> cargar() {
        List<Personaje> personajes = new ArrayList<>();
        // Parámetros: nombre, género, calvicie parcial, lentes, color de pelo.
        // Calvicie parcial permite conservar pelo de color en los costados.
        agregar(personajes, "Pedro", Personaje.Genero.HOMBRE, false, true, Personaje.ColorPelo.NEGRO);
        agregar(personajes, "Juan", Personaje.Genero.HOMBRE, false, false, Personaje.ColorPelo.NEGRO);
        agregar(personajes, "Carlos", Personaje.Genero.HOMBRE, true, false, Personaje.ColorPelo.NEGRO);
        agregar(personajes, "Diego", Personaje.Genero.HOMBRE, true, true, Personaje.ColorPelo.NEGRO);
        agregar(personajes, "Esteban", Personaje.Genero.HOMBRE, false, false, Personaje.ColorPelo.COLORADO);
        agregar(personajes, "Federico", Personaje.Genero.HOMBRE, false, true, Personaje.ColorPelo.COLORADO);
        agregar(personajes, "Gabriel", Personaje.Genero.HOMBRE, true, false, Personaje.ColorPelo.COLORADO);
        agregar(personajes, "Hugo", Personaje.Genero.HOMBRE, true, true, Personaje.ColorPelo.COLORADO);
        agregar(personajes, "Ivan", Personaje.Genero.HOMBRE, false, false, Personaje.ColorPelo.RUBIO);
        agregar(personajes, "Lucas", Personaje.Genero.HOMBRE, false, true, Personaje.ColorPelo.RUBIO);
        agregar(personajes, "Marcos", Personaje.Genero.HOMBRE, true, false, Personaje.ColorPelo.RUBIO);
        agregar(personajes, "Nicolas", Personaje.Genero.HOMBRE, true, true, Personaje.ColorPelo.RUBIO);
        agregar(personajes, "Ana", Personaje.Genero.MUJER, false, false, Personaje.ColorPelo.NEGRO);
        agregar(personajes, "Beatriz", Personaje.Genero.MUJER, false, true, Personaje.ColorPelo.NEGRO);
        agregar(personajes, "Carla", Personaje.Genero.MUJER, true, false, Personaje.ColorPelo.NEGRO);
        agregar(personajes, "Diana", Personaje.Genero.MUJER, true, true, Personaje.ColorPelo.NEGRO);
        agregar(personajes, "Elena", Personaje.Genero.MUJER, false, false, Personaje.ColorPelo.COLORADO);
        agregar(personajes, "Florencia", Personaje.Genero.MUJER, false, true, Personaje.ColorPelo.COLORADO);
        agregar(personajes, "Gabriela", Personaje.Genero.MUJER, true, false, Personaje.ColorPelo.COLORADO);
        agregar(personajes, "Helena", Personaje.Genero.MUJER, true, true, Personaje.ColorPelo.COLORADO);
        agregar(personajes, "Ines", Personaje.Genero.MUJER, false, false, Personaje.ColorPelo.RUBIO);
        agregar(personajes, "Julia", Personaje.Genero.MUJER, false, true, Personaje.ColorPelo.RUBIO);
        agregar(personajes, "Laura", Personaje.Genero.MUJER, true, false, Personaje.ColorPelo.RUBIO);
        validar(personajes);
        return personajes;
    }

    private static void agregar(List<Personaje> lista, String nombre, Personaje.Genero genero, boolean calvicie, boolean lentes, Personaje.ColorPelo color) {
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

        boolean mismoGenero =
                primero.getGenero() == segundo.getGenero();

        boolean mismaCalvicie =
                primero.tieneCalvicie() == segundo.tieneCalvicie();

        boolean mismosLentes =
                primero.tieneLentes() == segundo.tieneLentes();

        boolean mismoColorPelo =
                primero.getColorPelo() == segundo.getColorPelo();

        return mismoGenero&& mismaCalvicie&& mismosLentes&& mismoColorPelo;
    }
}
