package juego;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import algoritmos.EstrategiaGreedy;
import modelo.Jugador;
import modelo.Personaje;

/** Árbitro: guarda los secretos y responde; la estrategia nunca los recibe. */
public class Juego {
    private final List<Personaje> catalogo;
    private final Consola consola;
    private final Random azar;
    private final EstrategiaGreedy estrategia = new EstrategiaGreedy();

    public Juego(List<Personaje> catalogo, Consola consola, Random azar) {
        this.catalogo = new ArrayList<>(catalogo);
        this.consola = consola;
        this.azar = azar;
    }

    public void jugar(boolean automatico) {
        Jugador primero = new Jugador(automatico ? "Máquina 1" : "Humano", automatico, catalogo);
        Jugador segundo = new Jugador(automatico ? "Máquina 2" : "Máquina", true, catalogo);
        Personaje secretoPrimero;
        if (automatico) {
            secretoPrimero = elegirSecreto();
        } else {
            consola.mostrarPersonajes(catalogo);
            consola.mostrar("");
            secretoPrimero = consola.elegirPersonaje("Elegí tu personaje secreto por ID: ", catalogo);
            consola.mostrar("");
            consola.mostrarSeparador();
            consola.mostrar("PERSONAJE ELEGIDO");
            consola.mostrarSeparador();
            consola.mostrar("Elegiste: " + secretoPrimero);
            consola.mostrar("La estrategia no recibe este dato.");
            consola.mostrar("");
        }
        Personaje secretoSegundo = elegirSecreto();
        consola.mostrar("Cada participante comienza con 23 candidatos y busca el secreto del rival.");
        consola.mostrar("Un turno permite preguntar O arriesgar un personaje.");
        consola.mostrar("Una suposición incorrecta consume el turno y descarta ese personaje.");
        consola.mostrar("Los secretos se eligen independientemente y pueden coincidir.");
        consola.mostrar("Las respuestas las calcula el juego automáticamente.");
        if (automatico) {
            consola.mostrar("Prototipo: ambas máquinas utilizan Greedy, sin compartir información.");
        }
        consola.mostrarEstado(primero, segundo);

        int turno = 1;
        boolean turnoPrimero = true;
        while (true) {
            Jugador actual = turnoPrimero ? primero : segundo;
            Personaje objetivo = turnoPrimero ? secretoSegundo : secretoPrimero;
            consola.mostrarTitulo("TURNO " + turno + ": " + actual.getNombre());
            consola.mostrar("Candidatos antes: " + actual.cantidadCandidatos());
            boolean gano;
            if (actual.esMaquina()) {
                gano = turnoMaquina(actual, objetivo);
            } else {
                consola.mostrarPersonajes(actual.getCandidatos());
                consola.mostrar("");
                consola.mostrar("1. Preguntar\n2. Arriesgar personaje\n0. Abandonar partida");
                int accion = consola.leerEntero("Acción: ", 0, 2);
                if (accion == 0) {
                    consola.mostrar("Partida abandonada. Volviendo al menú.");
                    return;
                }
                if (accion == 1) {
                    Pregunta pregunta = elegirPreguntaHumana(actual);
                    if (pregunta == null) {
                        consola.mostrar("Ya usaste todas las preguntas. Elegí arriesgar un personaje.");
                        continue;
                    }
                    preguntar(actual, objetivo, pregunta);
                    gano = false;
                } else {
                    Personaje elegido = consola.elegirPersonaje("ID que querés arriesgar: ",
                            actual.getCandidatos());
                    gano = arriesgar(actual, objetivo, elegido);
                }
            }
            consola.mostrarEstado(primero, segundo);
            if (gano) {
            	consola.mostrarTitulo("GANADOR: " + actual.getNombre());
                consola.mostrar("Secreto de " + primero.getNombre() + ": " + secretoPrimero);
                consola.mostrar("Secreto de " + segundo.getNombre() + ": " + secretoSegundo);
                consola.mostrar("Turnos totales: " + turno);
                return;
            }
            turnoPrimero = !turnoPrimero;
            turno++;
        }
    }

    private Personaje elegirSecreto() {
        return catalogo.get(azar.nextInt(catalogo.size()));
    }

    private boolean turnoMaquina(Jugador jugador, Personaje objetivo) {

        if (jugador.cantidadCandidatos() == 1) {
            consola.mostrar("Queda un único candidato: la máquina decide arriesgar.");

            return arriesgar(
                    jugador,
                    objetivo,
                    jugador.getCandidatos().get(0));
        }

        mostrarEvaluacionGreedy(jugador);

        Pregunta pregunta = estrategia.elegirMejor(
                jugador.getCandidatos(),
                jugador.getPreguntasUsadas());

        if (pregunta == null) {
            consola.mostrar("No quedan preguntas útiles. Se arriesga el primer candidato.");

            return arriesgar(
                    jugador,
                    objetivo,
                    jugador.getCandidatos().get(0));
        }

        consola.mostrarTitulo("DECISIÓN GREEDY");
        consola.mostrar("Greedy elige: " + pregunta.getTexto());
        consola.mostrar("Criterio: minimizar el mayor grupo. Empates: orden fijo de preguntas.");

        preguntar(jugador, objetivo, pregunta);

        return false;
    }

    private void mostrarEvaluacionGreedy(Jugador jugador) {

        consola.mostrar("");
        consola.mostrarSeparador();
        consola.mostrar("EVALUACIÓN GREEDY");
        consola.mostrarSeparador();

        for (Pregunta pregunta : Pregunta.values()) {

            if (!jugador.getPreguntasUsadas().contains(pregunta)) {

                int cantidadSi =
                        estrategia.contarSi(jugador.getCandidatos(),pregunta);

                int cantidadNo =
                        jugador.cantidadCandidatos() - cantidadSi;

                consola.mostrarEvaluacion(pregunta,cantidadSi,cantidadNo);
            }
        }

        consola.mostrarSeparador();
    }
    
    private Pregunta elegirPreguntaHumana(Jugador jugador) {
        List<Pregunta> disponibles = new ArrayList<>();
        for (Pregunta pregunta : Pregunta.values()) {
            if (!jugador.getPreguntasUsadas().contains(pregunta)) {
                disponibles.add(pregunta);
                consola.mostrar(disponibles.size() + ". " + pregunta.getTexto());
            }
        }
        if (disponibles.isEmpty()) {
            return null;
        }
        int opcion = consola.leerEntero("Pregunta: ", 1, disponibles.size());
        return disponibles.get(opcion - 1);
    }

    private void preguntar(Jugador jugador, Personaje objetivo, Pregunta pregunta) {
        int antes = jugador.cantidadCandidatos();
        // El árbitro responde. La estrategia solo recibirá los candidatos compatibles.
        boolean respuesta = pregunta.evaluar(objetivo);
        consola.mostrar("");
        consola.mostrar("Pregunta: " + pregunta.getTexto());
        consola.mostrar("Respuesta del rival: " + (respuesta ? "Sí" : "No"));
        List<Personaje> descartados = jugador.aplicarRespuesta(pregunta, respuesta);
        consola.mostrar("Descartados (" + descartados.size() + "): " + descartados);
        consola.mostrar("Candidatos: " + antes + " -> " + jugador.cantidadCandidatos());
        consola.mostrar("Quedan: " + jugador.getCandidatos());
        consola.mostrar("");
    }

    private boolean arriesgar(Jugador jugador, Personaje objetivo, Personaje elegido) {
        consola.mostrar(jugador.getNombre() + " arriesga: " + elegido);
        if (elegido.getId() == objetivo.getId()) {
            consola.mostrar("¡Acierto!");
            return true;
        }
        int antes = jugador.cantidadCandidatos();
        jugador.descartarSuposicion(elegido.getId());
        consola.mostrar("No era ese personaje. Se descarta y termina el turno.");
        consola.mostrar("Candidatos: " + antes + " -> " + jugador.cantidadCandidatos());
        consola.mostrar("Quedan: " + jugador.getCandidatos());
        return false;
    }
}
