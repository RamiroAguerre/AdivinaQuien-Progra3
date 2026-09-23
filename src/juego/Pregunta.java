package juego;

import modelo.Personaje;

/** El orden declarado también resuelve los empates de la estrategia Greedy. */
public enum Pregunta {
    GENERO("¿Es hombre?"),
    CALVICIE("¿Tiene calvicie parcial?"),
    LENTES("¿Usa lentes?"),
    PELO_NEGRO("¿Tiene pelo negro?"),
    PELO_COLORADO("¿Tiene pelo colorado?"),
    PELO_RUBIO("¿Tiene pelo rubio?");

    private final String texto;

    Pregunta(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
    	return texto;
    }

    // Una única definición sirve para responder, contar y descartar.
    public boolean evaluar(Personaje personaje) {
        switch (this) {
            case GENERO:
                return personaje.getGenero() == Personaje.Genero.HOMBRE;
            case CALVICIE:
                return personaje.tieneCalvicie();
            case LENTES:
                return personaje.tieneLentes();
            case PELO_NEGRO:
                return personaje.getColorPelo() == Personaje.ColorPelo.NEGRO;
            case PELO_COLORADO:
                return personaje.getColorPelo() == Personaje.ColorPelo.COLORADO;
            case PELO_RUBIO:
                return personaje.getColorPelo() == Personaje.ColorPelo.RUBIO;
            default:
                throw new IllegalStateException("Pregunta desconocida.");
        }
    }
}
