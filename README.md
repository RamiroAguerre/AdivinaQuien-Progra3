# Adivina Quién

Versión preliminar en Java para Programación III. Permite jugar en consola contra
una máquina y observar una partida entre dos máquinas. Cada participante intenta
descubrir el personaje secreto de su adversario mediante preguntas de sí o no.
La consola muestra los candidatos de cada búsqueda y el razonamiento de Greedy.

## Inicio rápido

Requisito: Java 17 o posterior. El proyecto no usa librerías externas, Maven ni Gradle.
Descomprimir el ZIP, abrir una terminal en la carpeta `adivina-quien` y ejecutar:

```bash
java -jar adivina-quien.jar
```

El JAR incluido está compilado y listo para ejecutar. Para editar y recompilar
se necesita un JDK 17 o posterior; JDK 25 también sirve.

En Linux Mint, macOS o una terminal Bash:

```bash
bash compilar.sh
java -jar adivina-quien.jar
```

En Windows, desde CMD:

```bat
compilar.bat
java -jar adivina-quien.jar
```

Desde PowerShell, usar `./compilar.bat`. Los scripts compilan, ejecutan las pruebas
y generan nuevamente el JAR. Si una prueba falla, se detienen.

En VS Code, abrir esta carpeta y ejecutar el método `main` de
`src/adivinaquien/App.java`. Si el editor no reconoce el proyecto automáticamente,
marcar `src` como carpeta de fuentes. El paquete de las clases es `adivinaquien`.

## Cómo jugar

El menú ofrece Humano vs. Máquina, Máquina vs. Máquina, ver personajes y salir.
Para una primera prueba conviene elegir la opción 2 y observar una partida completa.

En el modo humano:

1. Elegir el personaje propio por su ID. La máquina elige el suyo en secreto.
2. En cada turno elegir entre preguntar y arriesgar un personaje.
3. Para preguntar, seleccionar una pregunta del menú. El juego responde
   automáticamente según el secreto del adversario y aplica el descarte.
4. Para arriesgar, ingresar el ID de un candidato restante.
5. Un acierto termina la partida; un error descarta ese personaje y consume el turno.

Los números del menú de preguntas son posiciones de ese menú y pueden cambiar
cuando se elimina una pregunta usada. Los IDs de personajes permanecen fijos.
La opción 0 del menú de acciones abandona la partida; la del menú principal sale.

Cada turno imprime participantes, cantidad inicial de candidatos, lista actual,
pregunta o suposición, respuesta, descartes y cantidad restante de ambos jugadores.
En los turnos de máquina también se muestra el conteo sí/no de cada filtro evaluado.
Los secretos se revelan al terminar la partida. No hay pausas en el modo automático;
se puede revisar el historial desplazándose en la terminal.

## Clases y responsabilidades

| Archivo | Objetivo |
| --- | --- |
| `App.java` | Cargar y ordenar el catálogo, mostrar el menú e iniciar partidas. |
| `Personaje.java` | Representar un personaje con datos inmutables. Incluye los enums `Genero` y `ColorPelo`. |
| `CatalogoPersonajes.java` | Cargar los 23 personajes, asignar IDs y validar que las preguntas puedan distinguirlos. |
| `OrdenadorPersonajes.java` | Implementar MergeSort por nombre, con ID como desempate. |
| `Pregunta.java` | Enum que contiene las seis preguntas permitidas y cómo se responde cada una. |
| `Jugador.java` | Guardar candidatos y preguntas usadas; aplicar respuestas y descartar suposiciones erróneas. |
| `EstrategiaGreedy.java` | Contar las respuestas posibles y seleccionar la pregunta más equilibrada. Su clase auxiliar `Evaluacion` guarda un conteo. |
| `Juego.java` | Actuar como árbitro: administrar secretos, alternar turnos, responder y determinar el ganador. |
| `Consola.java` | Leer entradas validadas e imprimir el seguimiento. |

`test/adivinaquien/Pruebas.java` es una clase aparte para verificar el programa;
no forma parte del JAR del juego. El directorio `build` se genera al compilar.

Para estudiar el código, comenzar por `Personaje` y `Pregunta`, seguir por
`Jugador.aplicarRespuesta`, después `EstrategiaGreedy.evaluar/elegirMejor` y
finalmente `Juego.jugar`. `App` permite ver cómo se conectan esas piezas.

## Decisiones de esta versión

### Personajes únicos

Se definieron 23 combinaciones diferentes de género, calvicie, lentes y color de pelo.
Pedro y Juan coinciden en género, color y ausencia de calvicie; los diferencia el uso
de lentes. La carga se agrupa por género: primero 12 hombres y luego 11 mujeres.

La calvicie se interpreta como **parcial**: un personaje puede conservar pelo negro,
colorado o rubio en los costados. Esto permite combinar libremente los atributos.
Hay 2 x 2 x 2 x 3 = 24 combinaciones posibles y se usan 23. Si se cambia esa
interpretación, habrá que revisar la posibilidad de distinguir a todos los personajes.
RUBIO representa el color que la consigna llama amarillo.

El programa valida las respuestas a las seis preguntas: no basta con que cambie el
nombre o el ID. Si dos personajes producen todas las mismas respuestas, rechaza
el catálogo. También rechaza IDs repetidos. Los nombres pueden editarse en
`CatalogoPersonajes.java`; después se debe recompilar.

### Ordenamiento con Divide y Conquista

Se eligió MergeSort por su división y combinación explícitas y su costo O(n log n).
El criterio es nombre alfabético sin distinguir mayúsculas, seguido por ID en empate.
Los nombres actuales no tienen tildes; no se implementa ordenación lingüística regional.

Los IDs se asignan al cargar y no cambian al ordenar. Así, el catálogo es fácil de
leer alfabéticamente y cada personaje mantiene su identificación. Como los IDs no
quedan ordenados, la búsqueda por ID es lineal; no se aplica búsqueda binaria.
El código del juego no utiliza `sort`, `Arrays.sort` ni `Collections.sort` para ordenar.
La prueba de MergeSort sí usa el ordenamiento estándar como referencia independiente.

### Selección Greedy y filtrado

Para cada pregunta no usada, la máquina cuenta cuántos candidatos responderían
sí y cuántos no. Descarta como opciones las preguntas que dejan un grupo vacío,
porque no aportan información. Entre las restantes elige el menor valor de
`max(cantidadSi, cantidadNo)`.

Ejemplo: entre divisiones 6/6 y 3/9 se elige 6/6, porque el peor grupo posible
queda en 6 en lugar de 9. Esa es la decisión local Greedy; no se exploran árboles
futuros de preguntas y no se afirma optimalidad global.

En empate se usa el orden declarado en `Pregunta`: género, calvicie, lentes,
pelo negro, pelo colorado, pelo rubio. La primera pregunta real divide 12/11
por género. Cada turno vuelve a evaluar solo los candidatos que quedan.

Después de recibir la respuesta, `Jugador.aplicarRespuesta` recorre la lista
y conserva los compatibles. Greedy elige el filtro; el descarte es un recorrido
lineal. Una pregunta sobre el color puede volver inútiles otras preguntas de color;
el conteo detecta esa dependencia sin repetirlas innecesariamente.

La máquina arriesga cuando queda exactamente un candidato. Llegar a un único
candidato no gana automáticamente: la suposición ocupa su siguiente turno.
Existe una salida defensiva si no quedan preguntas útiles: arriesgar el primer
candidato. Con este catálogo validado, las pruebas confirman que no se necesita.

### Reglas provisionales de las partidas

- Cada participante busca el personaje del contrario y comienza con 23 candidatos.
- Los secretos se eligen de manera independiente, pueden coincidir y no cambian.
- Empieza el humano en Humano vs. Máquina; empieza Máquina 1 en el modo automático.
- Cada turno permite una pregunta o una suposición. Una suposición errónea consume
  el turno y elimina al personaje intentado; no provoca una derrota inmediata.
- El juego calcula respuestas verdaderas automáticamente; el humano no las escribe.
- Ambas máquinas usan la misma estrategia Greedy. Cada una conserva su propio
  conjunto de candidatos y de preguntas utilizadas. No comparten historial.

Estas reglas permiten probar un juego completo. La estrategia más arriesgada y
la ventaja por historial de Máquina 2 mencionadas en el borrador quedan para una
etapa posterior, cuando se precise su significado.

### Separación de secretos y estrategia

`Juego` conserva los secretos como variables locales de la partida. `Jugador`
almacena el estado de búsqueda, no el secreto rival. `EstrategiaGreedy` recibe
únicamente una lista de candidatos y un conjunto de preguntas usadas. El árbitro
evalúa la pregunta elegida contra el secreto y actualiza el estado con esa respuesta.
No se usa el secreto para calcular los puntajes de Greedy.

### Complejidad del núcleo

Sea n la cantidad de candidatos y f la cantidad de filtros disponibles:

| Operación | Tiempo | Motivo |
| --- | --- | --- |
| Ordenamiento inicial MergeSort | O(n log n) | Divide en mitades y combina linealmente. |
| Evaluar preguntas Greedy | O(f x n) | Recorre los candidatos por cada filtro. |
| Elegir entre los conteos | O(f) | Recorre las evaluaciones. |
| Aplicar una respuesta | O(n) | Examina cada candidato una vez. |
| Buscar o descartar por ID | O(n) | La lista está ordenada por nombre. |

En este juego hay seis filtros como máximo, por lo que la evaluación por turno
puede expresarse como O(n) si f se considera constante. No se debe deducir que el
programa completo cuesta O(log n) solo porque una buena pregunta reduzca mucho
los candidatos: contar y filtrar también requiere recorrerlos. Imprimir listas
tiene su propio costo lineal y no debe mezclarse con futuras mediciones del algoritmo.

Se usa `ArrayList` para candidatos, `EnumSet` para preguntas usadas y `HashSet`
para validar IDs y firmas de características. MergeSort y el filtrado necesitan
espacio auxiliar O(n). La validación inicial usa O(f x n) tiempo esperado con HashSet.

## Verificación y ejemplo reproducible

`compilar.sh` y `compilar.bat` ejecutan automáticamente las pruebas. El archivo
`resultados-pruebas.txt` contiene la salida real obtenida al preparar esta versión.

- 23 personajes con IDs correlativos y combinaciones distinguibles.
- MergeSort sobre lista original, inversa, vacía, de un elemento y con empate de nombres.
- Filtrado con respuestas sí/no, aislamiento de las listas y rechazo de respuestas imposibles.
- Identificación correcta de los 23 posibles secretos y verificación del criterio local Greedy.
- 529 partidas automáticas: todas las parejas de secretos, incluyendo coincidencias.
- Entradas inválidas, pregunta humana, suposición correcta, incorrecta y abandono.

Con el catálogo actual, la búsqueda individual requiere entre 4 y 5 preguntas,
106 en total al probar los 23 secretos. El promedio es 106/23, aproximadamente
4,61 preguntas; después hace falta una suposición. Es un resultado observado de
esta estrategia y estos datos, no una prueba de optimalidad.

`ejemplo-partida.txt` contiene una partida real con semilla 42. Para reproducirla:

```bash
java -jar adivina-quien.jar --semilla 42
```

Seleccionar 2 en el primer menú y 0 al terminar. La semilla fija los sorteos;
para repetir la misma partida se deben realizar las mismas acciones desde el inicio.
En el ejemplo, Máquina 1 gana en el turno total 9. Su búsqueda pasa por las
cantidades 23, 11, 5, 2 y 1, y luego arriesga a Diana.

## Próximas etapas del TP

Esta entrega es el prototipo funcional, no el informe oficial terminado.
Falta incorporar la comparación de tiempos MergeSort vs. un algoritmo cuadrático
sobre copias de la misma lista, y luego preparar UML, capturas, bitácora del equipo,
reflexión y bibliografía en el formato solicitado. No se inventaron mediciones
de tiempo ni asignaciones de tareas a integrantes.

También quedan pendientes el comportamiento diferenciado de Máquina 2, su posible
acceso al historial y el marcador persistente mencionados en el borrador. El marcador
no se guarda en esta versión. El código y esta guía fueron desarrollados con asistencia
de IA; esa participación debe registrarse en la bitácora según lo solicitado por la cátedra.

## Material de referencia

- `Documentación para primer TP Programación III(1).pdf`: consigna y requisitos del informe.
- `TP Adivina Quien - Analisis y Diseño Algoritmico.docx`: borrador y pseudocódigos de filtrado y Greedy.
- `2023 04 Greedy parte 1.pdf`: marco de algoritmos Greedy de la materia.
- Aclaraciones del grupo: consola, personajes diferentes, seguimiento por turno y libertad para definir el orden.
