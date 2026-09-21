package adivinaquien;

import java.util.ArrayList;
import java.util.List;

/** MergeSort: divide, ordena las mitades y las combina. */
public class OrdenadorPersonajes {
    public static List<Personaje> ordenar(List<Personaje> personajes) {
        if (personajes.size() <= 1) {
            return new ArrayList<>(personajes);
        }
        int mitad = personajes.size() / 2;
        List<Personaje> izquierda = ordenar(personajes.subList(0, mitad));
        List<Personaje> derecha = ordenar(personajes.subList(mitad, personajes.size()));
        return combinar(izquierda, derecha);
    }

    private static List<Personaje> combinar(List<Personaje> izquierda,
                                            List<Personaje> derecha) {
        List<Personaje> resultado = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < izquierda.size() && j < derecha.size()) {
            if (comparar(izquierda.get(i), derecha.get(j)) <= 0) {
                resultado.add(izquierda.get(i));
                i++;
            } else {
                resultado.add(derecha.get(j));
                j++;
            }
        }
        while (i < izquierda.size()) {
            resultado.add(izquierda.get(i++));
        }
        while (j < derecha.size()) {
            resultado.add(derecha.get(j++));
        }
        return resultado;
    }

    private static int comparar(Personaje primero, Personaje segundo) {
        int comparacion = primero.getNombre().compareToIgnoreCase(segundo.getNombre());
        if (comparacion == 0) {
            return Integer.compare(primero.getId(), segundo.getId());
        }
        return comparacion;
    }
}
