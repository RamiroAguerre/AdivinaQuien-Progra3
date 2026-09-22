package modelo;

/** Datos de un personaje. Sus características no cambian durante la partida. */
public class Personaje {
    public enum Genero { HOMBRE, MUJER }
    public enum ColorPelo { NEGRO, COLORADO, RUBIO }

    private final int id;
    private final String nombre;
    private final Genero genero;
    private final boolean calvicie;
    private final boolean lentes;
    private final ColorPelo colorPelo;

//*Constructor de la clase Personaje. Inicializa al objeto con una validacion con excepcion a datos invalidos.**//
//**Luego inicializa los atributos de cada instancia**//
    public Personaje(int id, String nombre, Genero genero, boolean calvicie,
                     boolean lentes, ColorPelo colorPelo) {
        if (id <= 0 || nombre == null || nombre.trim().isEmpty()
                || genero == null || colorPelo == null) {
            throw new IllegalArgumentException("Datos de personaje inválidos.");
        }
        this.id = id;
        this.nombre = nombre;
        this.genero = genero;
        this.calvicie = calvicie;
        this.lentes = lentes;
        this.colorPelo = colorPelo;
    }

    public int getId() {
    	return id;
    }
    public String getNombre() {
    	return nombre; 
    }
    public Genero getGenero() {
    	return genero;
    }
    public boolean tieneCalvicie() {
    	return calvicie;
    }
    public boolean tieneLentes() {
    	return lentes;
    }
    public ColorPelo getColorPelo() {
    	return colorPelo;
    }

    @Override
    public String toString() {
        return id + " - " + nombre;
    }
}
