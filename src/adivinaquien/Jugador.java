package adivinaquien;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/** Estado de una búsqueda. No almacena el personaje secreto del adversario. */
public class Jugador {
    private final String nombre;
    private final boolean maquina;
    private List<Personaje> candidatos;
    private final Set<Pregunta> preguntasUsadas = EnumSet.noneOf(Pregunta.class);

    public Jugador(String nombre, boolean maquina, List<Personaje> catalogo) {
        this.nombre = nombre;
        this.maquina = maquina;
        this.candidatos = new ArrayList<>(catalogo);
    }

    public String getNombre() { return nombre; }
    public boolean esMaquina() { return maquina; }
    public int cantidadCandidatos() { return candidatos.size(); }
    public List<Personaje> getCandidatos() {
        return Collections.unmodifiableList(candidatos);
    }
    public Set<Pregunta> getPreguntasUsadas() {
        return Collections.unmodifiableSet(preguntasUsadas);
    }

    // Filtrado lineal O(n). Retorna los descartados para mostrarlos en consola.
    public List<Personaje> aplicarRespuesta(Pregunta pregunta, boolean respuesta) {
        if (preguntasUsadas.contains(pregunta)) {
            throw new IllegalArgumentException("Esa pregunta ya fue utilizada.");
        }
        List<Personaje> compatibles = new ArrayList<>();
        List<Personaje> descartados = new ArrayList<>();
        for (Personaje personaje : candidatos) {
            if (pregunta.evaluar(personaje) == respuesta) {
                compatibles.add(personaje);
            } else {
                descartados.add(personaje);
            }
        }
        if (compatibles.isEmpty()) {
            throw new IllegalStateException("Respuesta incompatible con todos los candidatos.");
        }
        candidatos = compatibles;
        preguntasUsadas.add(pregunta);
        return descartados;
    }

    public Personaje buscarCandidato(int id) {
        for (Personaje personaje : candidatos) {
            if (personaje.getId() == id) {
                return personaje;
            }
        }
        return null;
    }

    public void descartarSuposicion(int id) {
        for (int i = 0; i < candidatos.size(); i++) {
            if (candidatos.get(i).getId() == id) {
                candidatos.remove(i);
                return;
            }
        }
        throw new IllegalArgumentException("El personaje no está entre los candidatos.");
    }
}
