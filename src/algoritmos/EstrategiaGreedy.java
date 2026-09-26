package algoritmos;

import java.util.List;
import juego.Pregunta;
import modelo.Personaje;

/** Decide usando solamente los candidatos y las preguntas ya realizadas. **/
public class EstrategiaGreedy {

    /** Cuenta cuántos candidatos responderían "Sí" a una pregunta.**/
    public int contarSi(List<Personaje> candidatos, Pregunta pregunta) {

        int cantidad = 0;

        for (Personaje personaje : candidatos) {

            if (pregunta.evaluar(personaje)) {
                cantidad++;
            }
        }

        return cantidad;
    }

    /** O(f * n): se recorren n candidatos para cada uno de los f filtros.**/
    /**Como hay 6 filtros fijos, equivale a O(n).**/
    public Pregunta elegirMejor(List<Personaje> candidatos, List<Pregunta> preguntasUsadas) {

        Pregunta mejorPregunta = null;
        int menorGrupoMayor = Integer.MAX_VALUE;

        for (Pregunta pregunta : Pregunta.values()) {

            if (!preguntasUsadas.contains(pregunta)) {

                int cantidadSi = contarSi(candidatos, pregunta);
                int cantidadNo = candidatos.size() - cantidadSi;

                // Solo sirve si puede dividir a los candidatos.
                if (cantidadSi > 0 && cantidadNo > 0) {

                    int grupoMayor = Math.max(cantidadSi, cantidadNo);

                    if (grupoMayor < menorGrupoMayor) {
                        menorGrupoMayor = grupoMayor;
                        mejorPregunta = pregunta;
                    }
                }
            }
        }

        return mejorPregunta;
    }
}
