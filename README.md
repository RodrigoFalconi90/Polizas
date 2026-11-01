# Sistema de Gestión de Pólizas - práctica 3

Proyecto Java Swing con arquitectura por capas para registrar y consultar marcas en MySQL. Conserva los paquetes `Polizas.General` y `Polizas.Marcas` solicitados en la práctica.

## Requisitos

- JDK 25 o superior.
- Apache NetBeans con soporte para proyectos Java con Ant.
- MySQL o MariaDB, por ejemplo el incluido en XAMPP.
- MySQL Connector/J, incluido en `lib/mysql-connector-j.jar`.

## Preparar MySQL

1. Inicia MySQL desde XAMPP.
2. Ejecuta `sql/polizas.sql` en MySQL Shell o en la consola de MySQL.
3. El script crea la base `polizas` y la tabla `marca` con `idMarca` y `nombreMarca`.
4. La tabla valida que el nombre no esté vacío y agrega unicidad insensible a mayúsculas/minúsculas.

El script también migra una tabla `marca` preexistente. Muestra registros vacíos o duplicados que ya existan y nunca los elimina automáticamente. Si hay duplicados, los conserva, omite temporalmente el índice único y crea triggers para bloquear nuevas repeticiones; cuando resuelvas los registros previos, vuelve a ejecutar el script para agregar el índice único. Si hay nombres vacíos, corrígelos manualmente para que pueda agregarse el CHECK.

## Ejecutar

1. En NetBeans selecciona **File > Open Project** y abre la carpeta `Polizas`.
2. Ejecuta **Run Project** o presiona F6.
3. En la aplicación abre **Base de Datos > Marcas**. **Consultar** carga las marcas, **Agregar** registra el nombre escrito, **Actualizar** modifica la marca seleccionada después de confirmar y **Eliminar** la borra después de confirmar. Selecciona la marca en la tabla o en la lista desplegable.

## Usar el proyecto desde VS Code y NetBeans

Abre en ambos programas la misma carpeta: `outputs/Polizas` (la carpeta que contiene `build.xml`, `src` y `nbproject`). Los cambios guardados en un IDE aparecerán en el otro porque ambos editan los mismos archivos.

En VS Code, usa **File > Open Folder** y selecciona esa carpeta. La configuración de `.vscode` ya apunta al JDK de NetBeans, incluye `src` y el conector JDBC, y ofrece estas acciones: **Ctrl+Shift+B** para compilar, **Terminal > Run Task** para limpiar/compilar o ejecutar, y **Run and Debug** para depurar.
## Conexión a la base

La conexión predeterminada es `jdbc:mysql://localhost:3306/polizas`, con usuario `root` y contraseña vacía, como suele configurarse XAMPP. Para otro entorno configura `POLIZAS_DB_URL`, `POLIZAS_DB_USER` y `POLIZAS_DB_PASSWORD`, o sus propiedades Java equivalentes (`polizas.db.url`, `polizas.db.user`, `polizas.db.password`).

## Capas incluidas

- **Vista:** ventanas Swing para navegación y administración de marcas.
- **Controlador:** valida entradas y conecta los eventos de la interfaz con el modelo.
- **Modelo/DAO:** consulta, inserta, actualiza y elimina marcas mediante sentencias preparadas.
- **Base de datos:** script SQL para crear y migrar la estructura usada por el programa.

La pantalla permite consultar, agregar, actualizar y eliminar marcas. Para actualizar, selecciona una marca, escribe el nuevo nombre en el campo y confirma; para eliminar, selecciona una marca y confirma. El controlador usa el ID seleccionado, valida nombres vacíos o duplicados y actualiza la vista.
