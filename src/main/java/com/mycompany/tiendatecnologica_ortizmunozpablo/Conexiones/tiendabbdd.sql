-- Crear la base de datos

CREATE DATABASE tiendaT;
USE tiendaT;

-- Crear tabla tienda
CREATE TABLE tienda (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL UNIQUE
);

-- Crear tabla categoria con referencia a tienda
CREATE TABLE categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    tienda_id INT,  -- Columna para referir a la tienda
    FOREIGN KEY (tienda_id) REFERENCES tienda(id)
);

-- Crear tabla producto
CREATE TABLE producto (
    id INT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    descripcion TEXT,
    inventario INT NOT NULL,
    categoria_id INT,  -- Columna para referir a la categoría
    FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);

-- Crear tabla producto_caracteristicas
CREATE TABLE producto_caracteristicas (
    id INT PRIMARY KEY,
    producto_id INT,  -- Columna para referir al producto
    caracteristica_nombre VARCHAR(255),
    caracteristica_valor VARCHAR(255),
    FOREIGN KEY (producto_id) REFERENCES producto(id)
);

-- Crear tabla usuario
CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    calle VARCHAR(255),
    numero VARCHAR(50),
    ciudad VARCHAR(255),
    pais VARCHAR(255)
);

-- Crear tabla historial_compras
CREATE TABLE historial_compras (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT,  -- Columna para referir al usuario
    producto_id INT,  -- Columna para referir al producto
    cantidad INT NOT NULL,
    fecha DATE,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    FOREIGN KEY (producto_id) REFERENCES producto(id)
);

-- Crear tabla producto_imagenes
CREATE TABLE producto_imagenes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    producto_id INT,  -- Columna para referir al producto
    url_imagen VARCHAR(255),
    FOREIGN KEY (producto_id) REFERENCES producto(id)
);


