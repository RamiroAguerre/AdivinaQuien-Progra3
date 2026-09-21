package adivinaquien;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Pruebas sin dependencias externas. Se ejecutan aparte del juego. */
public class Pruebas {
    private static int comprobaciones;

    public static void main(String[] args) throws Exception {
        List<Personaje> originales = CatalogoPersonajes.cargar();
        comprobar(originales.size() == 23, "Deben existir 23 personajes.");
        for (int i = 0; i < originales.size(); i++) {
            comprobar(originales.get(i).getId() == i + 1, "IDs autoincrementales.");
        }
        List<Personaje> duplicados = new ArrayList<>(originales);
        duplicados.add(new Personaje(24, "Otro Pedro", Personaje.Genero.HOMBRE,
                false, true, Personaje.ColorPelo.NEGRO));
        boolean rechazo = false;
        try { CatalogoPersonajes.validar(duplicados); }
        catch (IllegalArgumentException e) { rechazo = true; }
        comprobar(rechazo, "Rechazar personajes indistinguibles aunque cambien ID y nombre.");

        List<Personaje> catalogo = probarOrdenamiento(originales);
        probarFiltrado(originales);
        probarBusquedaDeTodos(catalogo);
        probarTodasLasPartidas(catalogo);
        probarConsolaHumana(catalogo);
        System.out.println("OK: " + comprobaciones + " comprobaciones.");
        System.out.println("23 secretos identificados y 529 pares de secretos jugados sin errores.");
        System.out.println("Humano: victoria, error, pregunta, abandono y entradas inválidas verificados.");
    }

    private static List<Personaje> probarOrdenamiento(List<Personaje> originales) {
        List<Personaje> ordenados = OrdenadorPersonajes.ordenar(originales);
        List<Personaje> esperado = new ArrayList<>(originales);
        // El sort estándar se usa solo como referencia independiente en las pruebas.
        esperado.sort((a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));
        comprobar(ordenados.equals(esperado), "MergeSort coincide con la referencia.");
        comprobar(originales.get(0).getNombre().equals("Pedro"), "No modificar el original.");
        for (Personaje p : ordenados) {
            comprobar(originales.get(p.getId() - 1) == p, "Conservar identidad e ID.");
        }
        Collections.reverse(esperado);
        comprobar(OrdenadorPersonajes.ordenar(esperado).equals(ordenados), "Orden inverso.");
        comprobar(OrdenadorPersonajes.ordenar(new ArrayList<>()).isEmpty(), "Lista vacía.");
        comprobar(OrdenadorPersonajes.ordenar(Collections.singletonList(originales.get(0)))
                .get(0) == originales.get(0), "Un solo elemento.");
        List<Personaje> empate = new ArrayList<>();
        empate.add(new Personaje(2, "Ana", Personaje.Genero.MUJER, false, false,
                Personaje.ColorPelo.NEGRO));
        empate.add(new Personaje(1, "ana", Personaje.Genero.MUJER, false, true,
                Personaje.ColorPelo.NEGRO));
        comprobar(OrdenadorPersonajes.ordenar(empate).get(0).getId() == 1, "Desempate por ID.");
        return ordenados;
    }

    private static void probarFiltrado(List<Personaje> originales) {
        List<Personaje> pareja = originales.subList(0, 2); // Pedro y Juan.
        Jugador si = new Jugador("Sí", true, pareja);
        Jugador no = new Jugador("No", true, pareja);
        comprobar(si.aplicarRespuesta(Pregunta.LENTES, true).get(0).getNombre().equals("Juan"),
                "Descartar a Juan cuando la respuesta es sí.");
        no.aplicarRespuesta(Pregunta.LENTES, false);
        comprobar(si.buscarCandidato(1) != null && si.cantidadCandidatos() == 1,
                "Pedro usa lentes.");
        comprobar(no.buscarCandidato(2) != null && no.cantidadCandidatos() == 1,
                "Juan no usa lentes y tiene estado independiente.");
        comprobar(originales.size() == 23, "El filtrado no modifica el catálogo.");
        boolean rechazo = false;
        try { si.aplicarRespuesta(Pregunta.LENTES, true); }
        catch (IllegalArgumentException e) { rechazo = true; }
        comprobar(rechazo, "No repetir preguntas.");
        rechazo = false;
        try { si.aplicarRespuesta(Pregunta.GENERO, false); }
        catch (IllegalStateException e) { rechazo = true; }
        comprobar(rechazo && si.cantidadCandidatos() == 1
                && !si.getPreguntasUsadas().contains(Pregunta.GENERO),
                "Una respuesta imposible no debe corromper el estado.");
    }

    private static void probarBusquedaDeTodos(List<Personaje> catalogo) {
        EstrategiaGreedy greedy = new EstrategiaGreedy();
        int minimo = 6;
        int maximo = 0;
        int total = 0;
        for (Personaje secreto : catalogo) {
            Jugador jugador = new Jugador("Prueba", true, catalogo);
            int preguntas = 0;
            while (jugador.cantidadCandidatos() > 1) {
                Pregunta elegida = greedy.elegirMejor(greedy.evaluar(
                        jugador.getCandidatos(), jugador.getPreguntasUsadas()));
                comprobar(elegida != null, "Todo conjunto con más de uno debe poder separarse.");
                int siElegida = contarSi(jugador.getCandidatos(), elegida);
                int costo = Math.max(siElegida, jugador.cantidadCandidatos() - siElegida);
                for (Pregunta alternativa : Pregunta.values()) {
                    if (!jugador.getPreguntasUsadas().contains(alternativa)) {
                        int si = contarSi(jugador.getCandidatos(), alternativa);
                        int no = jugador.cantidadCandidatos() - si;
                        if (si > 0 && no > 0) {
                            comprobar(costo <= Math.max(si, no), "Elección local mínima.");
                        }
                    }
                }
                int antes = jugador.cantidadCandidatos();
                jugador.aplicarRespuesta(elegida, elegida.evaluar(secreto));
                comprobar(jugador.cantidadCandidatos() < antes, "Cada pregunta Greedy reduce.");
                comprobar(jugador.buscarCandidato(secreto.getId()) != null, "No perder el secreto.");
                preguntas++;
                comprobar(preguntas <= 6, "No repetir preguntas ni entrar en un bucle.");
            }
            comprobar(jugador.getCandidatos().get(0) == secreto, "Identificar el secreto exacto.");
            minimo = Math.min(minimo, preguntas);
            maximo = Math.max(maximo, preguntas);
            total += preguntas;
        }
        System.out.println("Búsquedas individuales: mínimo " + minimo + ", máximo " + maximo
                + " preguntas; total " + total + " para 23 secretos.");
    }

    private static int contarSi(List<Personaje> personajes, Pregunta pregunta) {
        int cantidad = 0;
        for (Personaje p : personajes) {
            if (pregunta.evaluar(p)) { cantidad++; }
        }
        return cantidad;
    }

    private static void probarTodasLasPartidas(List<Personaje> catalogo) throws Exception {
        for (int i = 0; i < 23; i++) {
            for (int j = 0; j < 23; j++) {
                String salida = ejecutar(catalogo, true, "", i, j);
                boolean ganoPrimera = salida.contains("GANADOR: Máquina 1");
                Personaje objetivoGanador = catalogo.get(ganoPrimera ? j : i);
                String ganador = ganoPrimera ? "Máquina 1" : "Máquina 2";
                comprobar(salida.contains("GANADOR: " + ganador), "La partida termina.");
                comprobar(salida.contains(ganador + " arriesga: " + objetivoGanador),
                        "El ganador adivina el secreto del adversario.");
                comprobar(!salida.contains("No era ese personaje"), "Greedy espera certeza.");
                comprobar(salida.indexOf("Secreto de Máquina") > salida.indexOf("GANADOR:"),
                        "Revelar secretos solo después de ganar.");
            }
        }
    }

    private static void probarConsolaHumana(List<Personaje> catalogo) throws Exception {
        // El índice 0 del catálogo ordenado corresponde a Ana (ID 13).
        String salida = ejecutar(catalogo, false, "1\n2\n13\n", 0);
        comprobar(salida.contains("GANADOR: Humano"), "Victoria humana por suposición correcta.");
        salida = ejecutar(catalogo, false, "1\n2\n1\n0\n", 0);
        comprobar(salida.contains("No era ese personaje") && salida.contains("23 -> 22"),
                "Una suposición incorrecta descarta el personaje.");
        comprobar(salida.contains("TURNO 2: Máquina") && salida.contains("Partida abandonada"),
                "El error consume el turno y se puede abandonar después.");
        salida = ejecutar(catalogo, false, "texto\n999\n1\n8\n1\n1\n0\n", 0);
        comprobar(salida.contains("Ingresá un número") && salida.contains("Ese ID no está"),
                "Validar entradas inválidas.");
        comprobar(salida.contains("23 -> 11") && salida.contains("Respuesta del rival: No"),
                "La pregunta humana obtiene la respuesta del secreto rival.");
    }

    private static String ejecutar(List<Personaje> catalogo, boolean automatico,
                                   String entrada, int... indices) throws Exception {
        InputStream anteriorEntrada = System.in;
        PrintStream anteriorSalida = System.out;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (PrintStream captura = new PrintStream(bytes, true, "UTF-8")) {
            System.setIn(new ByteArrayInputStream(entrada.getBytes(StandardCharsets.UTF_8)));
            System.setOut(captura);
            new Juego(catalogo, new Consola(), new Random() {
                private int posicion;
                @Override public int nextInt(int limite) { return indices[posicion++]; }
            }).jugar(automatico);
        } finally {
            System.setIn(anteriorEntrada);
            System.setOut(anteriorSalida);
        }
        return bytes.toString("UTF-8");
    }

    private static void comprobar(boolean condicion, String mensaje) {
        comprobaciones++;
        if (!condicion) { throw new AssertionError(mensaje); }
    }
}
