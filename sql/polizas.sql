CREATE DATABASE IF NOT EXISTS polizas
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE polizas;

CREATE TABLE IF NOT EXISTS marca (
    idMarca INT AUTO_INCREMENT PRIMARY KEY,
    nombreMarca VARCHAR(100) NOT NULL,
    CONSTRAINT uq_marca_nombre UNIQUE (nombreMarca),
    CONSTRAINT chk_marca_nombre_no_vacio
        CHECK (CHAR_LENGTH(TRIM(nombreMarca)) > 0),
    CONSTRAINT chk_marca_nombre_con_caracter
        CHECK (nombreMarca REGEXP '[^[:space:]]')
) ENGINE=InnoDB
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- Actualiza tablas de una version anterior sin eliminar sus filas.
ALTER TABLE marca
    MODIFY nombreMarca VARCHAR(100)
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci
    NOT NULL;

-- Revisa estos resultados. No se eliminan registros automaticamente.
SELECT idMarca, nombreMarca
FROM marca
WHERE nombreMarca IS NULL OR nombreMarca NOT REGEXP '[^[:space:]]';

SELECT LOWER(TRIM(REGEXP_REPLACE(nombreMarca, '[[:space:]]+', ' '))) AS nombre_normalizado,
       GROUP_CONCAT(CONCAT(idMarca, ': ', nombreMarca) ORDER BY idMarca SEPARATOR ' | ') AS registros,
       COUNT(*) AS cantidad
FROM marca
GROUP BY LOWER(TRIM(REGEXP_REPLACE(nombreMarca, '[[:space:]]+', ' ')))
HAVING COUNT(*) > 1;

-- Agrega CHECK de nombre no vacio a tablas ya creadas.
SET @check_vacio_existe = (
    SELECT COUNT(*)
    FROM information_schema.table_constraints
    WHERE constraint_schema = DATABASE()
      AND table_name = 'marca'
      AND constraint_name = 'chk_marca_nombre_no_vacio'
      AND constraint_type = 'CHECK'
);
SET @sql_check_vacio = IF(
    @check_vacio_existe = 0,
    'ALTER TABLE marca ADD CONSTRAINT chk_marca_nombre_no_vacio CHECK (CHAR_LENGTH(TRIM(nombreMarca)) > 0)',
    'SELECT 1'
);
PREPARE sentencia_check FROM @sql_check_vacio;
EXECUTE sentencia_check;
DEALLOCATE PREPARE sentencia_check;

-- El REGEXP tambien rechaza tabs y saltos de linea, no solo espacios normales.
SET @check_caracter_existe = (
    SELECT COUNT(*)
    FROM information_schema.table_constraints
    WHERE constraint_schema = DATABASE()
      AND table_name = 'marca'
      AND constraint_name = 'chk_marca_nombre_con_caracter'
      AND constraint_type = 'CHECK'
);
SET @sql_check_caracter = IF(
    @check_caracter_existe = 0,
    'ALTER TABLE marca ADD CONSTRAINT chk_marca_nombre_con_caracter CHECK (nombreMarca REGEXP ''[^[:space:]]'')',
    'SELECT 1'
);
PREPARE sentencia_check_caracter FROM @sql_check_caracter;
EXECUTE sentencia_check_caracter;
DEALLOCATE PREPARE sentencia_check_caracter;

-- Si no hay duplicados previos, crea el indice unico insensible a mayusculas.
-- Si ya existen, se conservan y los triggers de abajo bloquean nuevas repeticiones.
SET @hay_duplicados = (
    SELECT COUNT(*) FROM (
        SELECT LOWER(TRIM(REGEXP_REPLACE(nombreMarca, '[[:space:]]+', ' ')))
        FROM marca
        GROUP BY LOWER(TRIM(REGEXP_REPLACE(nombreMarca, '[[:space:]]+', ' ')))
        HAVING COUNT(*) > 1
    ) AS nombres_repetidos
);
SET @indice_unico_existe = (
    SELECT COUNT(*)
    FROM information_schema.statistics
    WHERE table_schema = DATABASE()
      AND table_name = 'marca'
      AND index_name = 'uq_marca_nombre'
);
SET @sql_indice_unico = IF(
    @hay_duplicados = 0 AND @indice_unico_existe = 0,
    'ALTER TABLE marca ADD CONSTRAINT uq_marca_nombre UNIQUE (nombreMarca)',
    'SELECT ''Indice unico omitido: resuelve primero los duplicados existentes'' AS aviso'
);
PREPARE sentencia_indice FROM @sql_indice_unico;
EXECUTE sentencia_indice;
DEALLOCATE PREPARE sentencia_indice;

-- Proteccion adicional al escribir directamente en MySQL, aun si hay filas duplicadas antiguas.
DROP TRIGGER IF EXISTS marca_validar_insert;
DROP TRIGGER IF EXISTS marca_validar_update;
DELIMITER //
CREATE TRIGGER marca_validar_insert BEFORE INSERT ON marca
FOR EACH ROW
BEGIN
    IF NEW.nombreMarca IS NULL OR NEW.nombreMarca NOT REGEXP '[^[:space:]]' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El nombre de la marca es obligatorio';
    END IF;
    IF EXISTS (
        SELECT 1 FROM marca m
        WHERE LOWER(TRIM(REGEXP_REPLACE(m.nombreMarca, '[[:space:]]+', ' ')))
            = LOWER(TRIM(REGEXP_REPLACE(NEW.nombreMarca, '[[:space:]]+', ' ')))
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ya existe una marca con ese nombre';
    END IF;
END//
CREATE TRIGGER marca_validar_update BEFORE UPDATE ON marca
FOR EACH ROW
BEGIN
    IF NEW.nombreMarca IS NULL OR NEW.nombreMarca NOT REGEXP '[^[:space:]]' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El nombre de la marca es obligatorio';
    END IF;
    IF EXISTS (
        SELECT 1 FROM marca m
        WHERE m.idMarca <> NEW.idMarca
          AND LOWER(TRIM(REGEXP_REPLACE(m.nombreMarca, '[[:space:]]+', ' ')))
            = LOWER(TRIM(REGEXP_REPLACE(NEW.nombreMarca, '[[:space:]]+', ' ')))
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ya existe otra marca con ese nombre';
    END IF;
END//
DELIMITER ;