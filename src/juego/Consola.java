package juego;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import algoritmos.EstrategiaGreedy;
import modelo.Jugador;
import modelo.Personaje;

/** Entrada y presentación. No decide la estrategia de las máquinas. */
public class Consola {
    private final Scanner entrada = new Scanner(System.in, "UTF-8");

    public void mostrar(String texto) {
        System.out.println(texto);
    }

    public int leerEntero(String mensaje, int minimo, int maximo) {
        while (true) {
            System.out.print(mensaje);
            if (!entrada.hasNextLine()) {
                throw new NoSuchElementException("Fin de la entrada.");
            }
            try {
                int valor = Integer.parseInt(entrada.nextLine().trim());
                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // Volvemos a pedir la entrada sin interrumpir la partida.
            }
            mostrar("Ingresá un número entre " + minimo + " y " + maximo + ".");
        }
    }

    public Personaje elegirPersonaje(String mensaje, List<Personaje> personajes) {
        while (true) {
            int id = leerEntero(mensaje, 1, Integer.MAX_VALUE);
            for (Personaje personaje : personajes) {
                if (personaje.getId() == id) {
                    return personaje;
                }
            }
            mostrar("Ese ID no está entre los personajes disponibles.");
        }
    }

    public void mostrarPersonajes(List<Personaje> personajes) {
        System.out.printf("%-4s %-12s %-8s %-10s %-8s %-10s%n",
                "ID", "Nombre", "Género", "Calvicie", "Lentes", "Pelo");
        for (Personaje p : personajes) {
            System.out.printf("%-4d %-12s %-8s %-10s %-8s %-10s%n",
                    p.getId(), p.getNombre(), p.getGenero(),
                    p.tieneCalvicie() ? "Sí" : "No",
                    p.tieneLentes() ? "Sí" : "No", p.getColorPelo());
        }
    }

    public void mostrarEvaluaciones(List<EstrategiaGreedy.Evaluacion> evaluaciones) {
        mostrar("Evaluación Greedy de las preguntas no usadas:");
        for (EstrategiaGreedy.Evaluacion e : evaluaciones) {
            System.out.printf("  %-30s Sí: %2d | No: %2d | Mayor grupo: %2d | %s%n",
                    e.getPregunta().getTexto(), e.getCantidadSi(), e.getCantidadNo(),
                    e.getPeorGrupo(), e.esUtil() ? "Útil" : "No descarta candidatos");
        }
    }

    public void mostrarEstado(Jugador primero, Jugador segundo) {
        mostrar("Candidatos restantes: " + primero.getNombre() + " = "
                + primero.cantidadCandidatos() + " | " + segundo.getNombre()
                + " = " + segundo.cantidadCandidatos());
    }
}
