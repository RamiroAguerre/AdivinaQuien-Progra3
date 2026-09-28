package algoritmos;

import java.util.ArrayList;
import java.util.List;

import modelo.Personaje;

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

    private static List<Personaje> combinar(List<Personaje> izquierda, List<Personaje> derecha) {
        List<Personaje> resultado = new ArrayList<>();
        int indiceIzquierda = 0;
        int indiceDerecha = 0;
        while (indiceIzquierda < izquierda.size() && indiceDerecha < derecha.size()) {
            if (comparar(izquierda.get(indiceIzquierda), derecha.get(indiceDerecha)) <= 0) {
                resultado.add(izquierda.get(indiceIzquierda));
                indiceIzquierda++;
            } else {
                resultado.add(derecha.get(indiceDerecha));
                indiceDerecha++;
            }
        }
        while (indiceIzquierda < izquierda.size()) {
            resultado.add(izquierda.get(indiceIzquierda));
            indiceIzquierda++;
        }
        while (indiceDerecha < derecha.size()) {
            resultado.add(derecha.get(indiceDerecha));
            indiceDerecha++;
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
