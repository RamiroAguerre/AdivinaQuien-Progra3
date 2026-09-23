package app;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import algoritmos.OrdenadorPersonajes;
import datos.CatalogoPersonajes;
import juego.Consola;
import juego.Juego;
import modelo.Personaje;

/** Punto de entrada del proyecto. */
public class App {
    public static void main(String[] args) {

    	Random azar = new Random();

        Consola consola = new Consola();
        List<Personaje> originales = CatalogoPersonajes.cargar();
        consola.mostrar("ADIVINA QUIÉN");
        consola.mostrar("Catálogo validado: 23 personajes con características únicas.");
        consola.mostrar("Calvicie = parcial, se conserva el color del pelo de los costados.");
        consola.mostrar("Carga original agrupada por género: " + originales);
        consola.mostrar("Ordenamiento inicial: MergeSort por nombre, con ID como desempate.");
        List<Personaje> catalogo = OrdenadorPersonajes.ordenar(originales);
        consola.mostrar("Resultado: " + catalogo);
        consola.mostrar("Los IDs asignados al cargar no cambian al ordenar.");
        Juego juego = new Juego(catalogo, consola, azar);

        try {
            while (true) {
                consola.mostrar("\n1. Humano vs. Máquina"
                		+ "\n2. Máquina vs. Máquina"
                        + "\n3. Ver personajes"
                        + "\n0. Salir");
                int opcion = consola.leerEntero("Opción: ", 0, 3);
                switch (opcion) {
                    case 1: juego.jugar(false); break;
                    case 2: juego.jugar(true); break;
                    case 3: consola.mostrarPersonajes(catalogo); break;
                    case 0:
                        consola.mostrar("Programa finalizado.");
                        return;
                    default: throw new IllegalStateException("Opción inválida.");
                }
            }
        } catch (NoSuchElementException e) {
            consola.mostrar("\nEntrada finalizada. Programa cerrado.");
        }
    }
}
