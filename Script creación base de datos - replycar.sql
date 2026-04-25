DROP DATABASE IF EXISTS replycar;
CREATE DATABASE replycar
  CHARACTER SET utf8
  COLLATE utf8_general_ci;

USE replycar;

-- =========================
-- FORMAS DE PAGO
-- =========================
CREATE TABLE formas_pago (
  id INT(2) NOT NULL AUTO_INCREMENT,
  tipo_pago VARCHAR(50) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

-- =========================
-- FAMILIAS
-- =========================
CREATE TABLE familias (
  id INT(2) NOT NULL,
  nombre VARCHAR(100) NOT NULL,
  sector VARCHAR(100) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

-- =========================
-- CLIENTES
-- =========================
CREATE TABLE clientes (
  id INT(11) NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  apellido_1 VARCHAR(100) NOT NULL,
  apellido_2 VARCHAR(100) NOT NULL,
  cp VARCHAR(5) NOT NULL,
  direccion VARCHAR(100) NOT NULL,
  numero INT(11) NOT NULL,
  tlf_movil VARCHAR(15) NOT NULL,
  nie VARCHAR(15) NOT NULL,
  tipo INT(1) NOT NULL,
  email VARCHAR(100) NOT NULL,
  ID_formaPago INT(2) NOT NULL,
  poblacion VARCHAR(100) NOT NULL,
  provincia VARCHAR(100) NOT NULL,
  pais VARCHAR(100) NOT NULL,
  n_cuenta VARCHAR(100),
  PRIMARY KEY (id),
  FOREIGN KEY (ID_formaPago) REFERENCES formas_pago(id) ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================
-- PROVEEDORES
-- =========================
CREATE TABLE proveedores (
  id INT(11) NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  apellido_1 VARCHAR(100) NOT NULL,
  apellido_2 VARCHAR(100) NOT NULL,
  cp VARCHAR(5) NOT NULL,
  direccion VARCHAR(100) NOT NULL,
  numero INT(11) NOT NULL,
  tlf_movil VARCHAR(15) NOT NULL,
  nie VARCHAR(15) NOT NULL UNIQUE,
  tipo INT(1) NOT NULL,
  email VARCHAR(100) NOT NULL,
  ID_formaPago INT(2) NOT NULL,
  poblacion VARCHAR(100) NOT NULL,
  provincia VARCHAR(100) NOT NULL,
  pais VARCHAR(100) NOT NULL,
  n_cuenta VARCHAR(100),
  PRIMARY KEY (id),
  FOREIGN KEY (ID_formaPago) REFERENCES formas_pago(id) ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================
-- TARIFAS
-- =========================
CREATE TABLE tarifas (
  id VARCHAR(8) NOT NULL,
  MacroSector VARCHAR(50) NOT NULL,
  ID_Familia INT(2) NOT NULL,
  Descrip_Familia VARCHAR(50) NOT NULL,
  Descripcion VARCHAR(50) NOT NULL,
  Importe_PVP DECIMAL(10,2) NOT NULL,
  Importe_Coste DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (ID_Familia) REFERENCES familias(id) ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================
-- MOVIMIENTOS
-- =========================
CREATE TABLE movimientos (
  id INT(11) NOT NULL AUTO_INCREMENT,
  fecha_mov DATE,
  tipo VARCHAR(20) NOT NULL,
  ID_Tarifa VARCHAR(8) NOT NULL,
  descripcion VARCHAR(100),
  id_factura_cliente VARCHAR(100),
  id_factura_almacen VARCHAR(100),
  proveedor VARCHAR(100),
  unidades INT(11),
  precio_costo DECIMAL(10,2),
  precio_pvp DECIMAL(10,2),
  descuento INT(2) NOT NULL,
  precioDescuento DECIMAL(10,2) GENERATED ALWAYS AS (precio_pvp * (1 - descuento / 100.0)) STORED,
  PRIMARY KEY (id),
  FOREIGN KEY (ID_Tarifa) REFERENCES tarifas(id) ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================
-- FACTURAS
-- =========================
CREATE TABLE facturas_almacen (
  id_nfactura VARCHAR(20) NOT NULL,
  fecha_factura DATE NOT NULL,
  base_imponible DECIMAL(10,2) NOT NULL,
  iva DECIMAL(5,2) NOT NULL,
  importe_factura DECIMAL(10,2) NOT NULL,
  ID_Cliente INT(11) NOT NULL,
  PRIMARY KEY (id_nfactura),
  FOREIGN KEY (ID_Cliente) REFERENCES clientes(id) ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE facturas_taller (
  id_nfactura VARCHAR(20) NOT NULL,
  fecha_factura DATE NOT NULL,
  base_imponible DECIMAL(10,2) NOT NULL,
  iva DECIMAL(10,2) NOT NULL,
  importe_factura DECIMAL(10,2) NOT NULL,
  ID_Cliente INT(11) NOT NULL,
  PRIMARY KEY (id_nfactura),
  FOREIGN KEY (ID_Cliente) REFERENCES clientes(id) ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================
-- TARIFA TALLER
-- =========================
CREATE TABLE tarifa_taller (
  id INT(11) NOT NULL AUTO_INCREMENT,
  tipo_averia VARCHAR(50) NOT NULL,
  precio_averia DECIMAL(10,2) NOT NULL,
  texto VARCHAR(500) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

-- =========================
-- TARIFA VEHÍCULOS
-- =========================
CREATE TABLE tarifa_vehiculos (
  id INT(4) NOT NULL AUTO_INCREMENT,
  modelo VARCHAR(50) NOT NULL,
  precio DECIMAL(10,2) NOT NULL,
  color VARCHAR(15) NOT NULL,
  extras VARCHAR(100) NOT NULL,
  foto TEXT NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

-- =========================
-- LISTADO VEHÍCULOS
-- =========================
CREATE TABLE listado_vehiculos (
  id INT(11) NOT NULL AUTO_INCREMENT,
  fecha_mov DATE NOT NULL,
  modelo VARCHAR(50) NOT NULL,
  bastidor VARCHAR(50) NOT NULL UNIQUE,
  matricula VARCHAR(15) UNIQUE,
  precio_compra DECIMAL(10,2) NOT NULL,
  iva_compra DECIMAL(10,2) NOT NULL,
  total_compra DECIMAL(10,2) NOT NULL,
  color VARCHAR(15) NOT NULL,
  extras VARCHAR(100) NOT NULL,
  ID_Cliente INT(11),
  precio_venta DECIMAL(10,2),
  iva_venta DECIMAL(10,2),
  total_venta DECIMAL(10,2),
  foto TEXT NOT NULL,
  fecha_compra DATE NOT NULL,
  fecha_venta DATE,
  id_proveedor INT(11) NOT NULL,
  nfra_proveedor VARCHAR(30) NOT NULL,
  nfra_venta VARCHAR(30),
  comentarios TEXT,
  ID_TarifaVehiculo INT(11) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (ID_Cliente) REFERENCES clientes(id) ON UPDATE CASCADE,
  FOREIGN KEY (id_proveedor) REFERENCES proveedores(id) ON UPDATE CASCADE,
  FOREIGN KEY (ID_TarifaVehiculo) REFERENCES tarifa_vehiculos(id) ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================
-- LISTADO INTERVENCIONES
-- =========================
CREATE TABLE listado_intervenciones (
  id INT(11) NOT NULL AUTO_INCREMENT,
  modelo VARCHAR(50) NOT NULL,
  bastidor VARCHAR(50) NOT NULL,
  matricula VARCHAR(50) NOT NULL,
  numero_factura VARCHAR(20) NOT NULL,
  diag1 VARCHAR(100) NOT NULL,
  resum1 VARCHAR(100) NOT NULL,
  precio1 DECIMAL(10,2) NOT NULL,
  ID_Cliente INT(11) NOT NULL,
  ID_Tarifa INT(11) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (numero_factura) REFERENCES facturas_taller(id_nfactura) ON UPDATE CASCADE,
  FOREIGN KEY (ID_Cliente) REFERENCES clientes(id) ON UPDATE CASCADE,
  FOREIGN KEY (ID_Tarifa) REFERENCES tarifa_taller(id) ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =========================
-- USUARIOS
-- =========================
CREATE TABLE usuarios (
  id INT(4) NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  apellidos VARCHAR(100) NOT NULL,
  rol VARCHAR(100) NOT NULL,
  user VARCHAR(100) NOT NULL,
  password VARCHAR(100) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;