# Adivina Quién - Programación III

Implementación en Java del juego **Adivina Quién**, desarrollada para Programación III.

El proyecto permite jugar en consola en dos modalidades:

- Humano vs. Máquina
- Máquina vs. Máquina

Cada participante intenta descubrir el personaje secreto del adversario mediante preguntas binarias sobre género, calvicie, lentes y color de pelo.

La solución aplica **Divide y Conquista mediante MergeSort** y una **estrategia Greedy** para seleccionar las preguntas de la máquina.

---

## Estructura del proyecto

El proyecto está dividido en paquetes según la responsabilidad de cada clase:

```text
src
├── algoritmos
│   ├── EstrategiaGreedy.java
│   └── OrdenadorPersonajes.java
│
├── app
│   └── App.java
│
├── datos
│   └── CatalogoPersonajes.java
│
├── juego
│   ├── Consola.java
│   ├── Juego.java
│   └── Pregunta.java
│
├── modelo
│   ├── Jugador.java
│   └── Personaje.java
│
└── test
    └── Pruebas.java