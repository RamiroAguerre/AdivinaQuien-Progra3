package algoritmos;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import juego.Pregunta;
import modelo.Personaje;

/** Decide usando solamente los candidatos y las preguntas ya realizadas. */
public class EstrategiaGreedy {
    /** Resultado auxiliar del conteo para una pregunta. */
    public static class Evaluacion {
        private final Pregunta pregunta;
        private final int cantidadSi;
        private final int cantidadNo;

        public Evaluacion(Pregunta pregunta, int cantidadSi, int cantidadNo) {
            this.pregunta = pregunta;
            this.cantidadSi = cantidadSi;
            this.cantidadNo = cantidadNo;
        }

        public Pregunta getPregunta() { return pregunta; }
        public int getCantidadSi() { return cantidadSi; }
        public int getCantidadNo() { return cantidadNo; }
        public int getPeorGrupo() { return Math.max(cantidadSi, cantidadNo); }
        public boolean esUtil() { return cantidadSi > 0 && cantidadNo > 0; }
    }

    // O(f * n): se recorren n candidatos para cada uno de los f filtros.
    public List<Evaluacion> evaluar(List<Personaje> candidatos, Set<Pregunta> usadas) {
        List<Evaluacion> evaluaciones = new ArrayList<>();
        for (Pregunta pregunta : Pregunta.values()) {
            if (!usadas.contains(pregunta)) {
                int cantidadSi = 0;
                for (Personaje personaje : candidatos) {
                    if (pregunta.evaluar(personaje)) {
                        cantidadSi++;
                    }
                }
                evaluaciones.add(new Evaluacion(pregunta, cantidadSi,
                        candidatos.size() - cantidadSi));
            }
        }
        return evaluaciones;
    }

    public Pregunta elegirMejor(List<Evaluacion> evaluaciones) {
        Evaluacion mejor = null;
        for (Evaluacion evaluacion : evaluaciones) {
            // Elegimos el menor de los grupos máximos: MIN(MAX(sí, no)).
            if (evaluacion.esUtil()
                    && (mejor == null || evaluacion.getPeorGrupo() < mejor.getPeorGrupo())) {
                mejor = evaluacion;
            }
        }
        // En empate se conserva la primera pregunta del enum.
        return mejor == null ? null : mejor.getPregunta();
    }
}
