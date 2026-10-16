# Papelería Campus

## Descripción

Aplicación de escritorio para administrar los productos de una papelería, desarrollada con JavaFX y PostgreSQL.

## Tecnologías

* Java JDK 21
* JavaFX 21
* Maven
* PostgreSQL
* IntelliJ IDEA

## Funcionalidades

* Listar productos.
* Buscar por nombre o código.
* Filtrar por categoría y existencias.
* Registrar productos.
* Actualizar productos.
* Eliminar productos con confirmación.
* Validar los datos antes de guardar.

## Estructura del proyecto

* `src/main/java`: clases Java.
* `src/main/resources`: archivos FXML y CSS.
* `pom.xml`: dependencias y configuración Maven.
* `evidencias`: capturas de las pruebas.

## Requisitos

* JDK 21.
* PostgreSQL.
* IntelliJ IDEA con soporte para Maven.

## Ejecución

1. Crear y configurar la base de datos PostgreSQL.
2. Configurar las credenciales en la clase de conexión existente.
3. Abrir el proyecto en IntelliJ IDEA.
4. Recargar las dependencias Maven.
5. Ejecutar `edu.uam.ni.evaluacion.Main`.

## Pruebas

* Búsqueda de productos.
* Registro de productos.
* Actualización de productos.
* Eliminación de productos.
* Validación de campos obligatorios.
* Validación de precio y stock.
* Prevención de códigos duplicados.
* Cancelación de eliminación.

## Autor

Andrés Eduardo Centeno Zavala

## Fecha de entrega

15 - Octubre - 2026
