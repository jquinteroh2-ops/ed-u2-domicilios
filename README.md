# Empresa de domicilios — Lista, Cola y Pila (Ejercicio 25)

Actividad de aprendizaje Unidad 2 — Estructuras de Datos — Universidad de Cartagena.

Aplicación Java de consola que gestiona los domicilios (pedidos a domicilio) de una empresa
usando tres estructuras de datos propias construidas con nodos doblemente enlazados:

| Estructura | Clase | Uso en el sistema |
|---|---|---|
| Lista | `Lista.java` | Registro general de todos los domicilios |
| Cola (FIFO) | `Cola.java` | Domicilios pendientes por despachar, en orden de llegada |
| Pila (LIFO) | `Pila.java` | Historial de domicilios entregados y deshacer la última entrega |

Entidad principal: `Domicilio` (identificador: **número de orden**).

## Ejecutar

Requiere Java 17 o superior.

```bash
javac -encoding UTF-8 -d out src/*.java
java -cp out SistemaDomicilios
```

Al iniciar, el programa pregunta si se cargan 5 domicilios de prueba (órdenes 101 a 105).

## Menú

1. Registrar domicilio
2. Ver todos los domicilios registrados
3. Ver domicilios pendientes
4. Despachar siguiente domicilio
5. Ver historial de domicilios entregados
6. Buscar domicilio por número de orden
7. Cancelar domicilio pendiente (con cola auxiliar)
8. Deshacer última entrega
9. Ver cantidad de domicilios
10. Salir
