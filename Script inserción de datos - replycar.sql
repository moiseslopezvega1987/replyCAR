USE replycar;

-- =========================
-- formas_pago
-- =========================
INSERT INTO `formas_pago` (`id`, `tipo_pago`) VALUES
(1, 'CONTADO'),
(2, 'RECIBO 30 DÍAS'),
(3, 'TRANSFERENCIA'),
(4, 'PAGARÉ 30 DÍAS'),
(5, 'PAGARÉ 60 DÍAS');

-- =========================
-- familias
-- =========================
INSERT INTO `familias` (`id`, `nombre`, `sector`) VALUES
(1, 'FILTROS', 'MACROSECTOR'),
(2, 'ESCOBILLAS', 'MICROSECTOR'),
(3, 'LÍQUIDOS', 'MICROSECTOR'),
(4, 'NEUMÁTICOS', 'MICROSECTOR'),
(5, 'BATERÍAS', 'MACROSECTOR'),
(6, 'FAROS', 'MICROSECTOR'),
(7, 'CRISTALES', 'MICROSECTOR'),
(8, 'CARAROCERÍA', 'MACROSECTOR'),
(9, 'FRENOS', 'MACROSECTOR'),
(10, 'ELECTRICIDAD', 'MACROSECTOR');

-- =========================
-- clientes
-- =========================
INSERT INTO `clientes` (`id`, `nombre`, `apellido_1`, `apellido_2`, `cp`, `direccion`, `numero`, `tlf_movil`, `nie`, `tipo`, `email`, `ID_formaPago`, `poblacion`, `provincia`, `pais`, `n_cuenta`) VALUES
(1, 'CONCENTRACION Y VENTA INDUSTRIAL DE VEHICULOS, S.L.', '', '', '29004', 'CRTA. AZUCARERA-INTELHORCE', 78, '952176633', 'B92500586', 0, 'info@covei.com', 2, 'MALAGA', 'MALAGA', 'ESPAÑA', 'ES8701826112198806479323'),
(2, 'MOISES', 'LOPEZ', 'VEGA', '29140', 'CALLE BAEZA', 44, '952624565', '25729357Q', 1, 'moilito@hotmail.com', 1, 'CHURRIANA', 'MALAGA', 'ESPAÑA', ''),
(3, 'HERGUPE, S.L.', '', '', '39400', 'POL. IND. PARC.', 64, '942834067', 'B39346085', 0, 'info@hergupe.com', 2, 'LOS CORRALES DE BUELNA', 'CANTABRIA', 'ESPAÑA', 'ES9500494163442100252363'),
(4, 'MIGUEL', 'CAMPUZANO', 'ALVAREZ', '29040', 'CALLE REVILLA', 5, '630450111', '25728956H', 1, 'mcampuzano@hotmail.com', 3, 'ALMARGEN', 'MALAGA', 'ESPAÑA', ''),
(5, 'CRISTALERIA HORACIO, S.L.', '', '', '29010', 'CALLE YUNQUE', 120, '652123587', 'B52123456', 0, 'cristaleriahoracio@hotmail.com', 4, 'MALAGA', 'MALAGA', 'ESPAÑA', ''),
(6, 'LUCAS', 'HERNAN', 'VEGA', '29004', 'CALLE OBTUSO', 45, '952458956', '25729357M', 1, 'lhernan@hotmail.com', 5, 'MALAGA', 'MALAGA', 'ESPAÑA', '');

-- =========================
-- proveedores
-- =========================
INSERT INTO `proveedores` (`id`, `nombre`, `apellido_1`, `apellido_2`, `cp`, `direccion`, `numero`, `tlf_movil`, `nie`, `tipo`, `email`, `ID_formaPago`, `poblacion`, `provincia`, `pais`, `n_cuenta`) VALUES
(1, 'AUTOMAX ESPAÑA, S.L.', '', '', '28108', 'AVDA DE BRUSELAS', 32, '915212141', 'B85432805', 0, 'info@automax.es', 1, 'ALCOBENDAS', 'MADRID', 'ESPAÑA', ''),
(2, 'LUIS MOLEON RECAMBIOS, S.L.', '', '', '18015', 'CRTA DE CORDOBA', 432, '958282500', 'B19578756', 0, 'info@luismoleon.com', 2, 'GRANADA', 'GRANADA', 'ESPAÑA', 'ES6112343456420456323532'),
(3, 'BATERIAS MALAGA, S.L.', '', '', '28821', 'CALLE DE SUIZA', 1, '952621455', 'B97576607', 0, 'info@batmalaga.com', 2, 'MADRID', 'MADRID', 'ESPAÑA', 'ES6112343456420456312345'),
(4, 'JUAN', 'GONZALEZ', 'ARMINIO', '29010', 'CALLE FRESADORES', 45, '952331456', '25729345G', 1, 'jgonzalez@hotmail.com', 4, 'MALAGA', 'MALAGA', 'ESPAÑA', ''),
(5, 'ROTULOS CARRASCO, S.L.', '', '', '29004', 'CALLE VALLE NIZA', 63, '952105001', 'B29361912', 0, 'info@jesuscarrasco.com', 5, 'MALAGA', 'MALAGA', 'ESPAÑA', ''),
(6, 'CONCENTRACION Y VENTA INDUSTRIAL DE VEHICULOS, S.L.', '', '', '29004', 'CRTA. AZUCARERA-INTELHORCE', 78, '617452325', 'B92500586', 0, 'info@covei.com', 2, 'MALAGA', 'MALAGA', 'ESPAÑA', 'ES5001824525621234567891'),
(7, 'MOISES', 'LOPEZ', 'VEGA', '29140', 'CALLE BAEZA', 44, '614525452', '25729357M', 1, 'mlv@gmail.com', 4, 'CHURRIANA', 'MALAGA', 'ESPAÑA', ''),
(8, 'CONCENTRO, S.A.', '', '', '29450', 'CRTA. PINEDA', 15, '645252456', 'A12325252', 0, 'cocentro@gmail.com', 5, 'ALH. DE LA TORRE', 'MALAGA', 'ESPAÑA', '');

-- =========================
-- tarifa_vehiculos
-- =========================
INSERT INTO `tarifa_vehiculos` (`id`, `modelo`, `precio`, `color`, `extras`, `foto`) VALUES
(1, 'Car eDeliver 3', 43000.00, 'Blanco', 'Radio Bluetooth, Sensores Aparcamiento', 'https://www.automovilesplaycar.com/slir/w900/files/20192/20192_2.jpg'),
(2, 'Car eDeliver 9', 53500.00, 'Blanco', 'Radio Bluetooth, Sensores Aparcamiento', 'https://www.automovilesplaycar.com/slir/w900/files/36876/36876_19935_img_3132.jpg'),
(3, 'Car eUniq 5', 33900.00, 'Gris', 'Radio Bluetooth, Sensores Aparcamiento', 'https://www.automovilesplaycar.com/slir/w900/files/22912/22912_img_3875.jpg'),
(4, 'Car DFSK 600', 22500.00, 'Verde Claro', 'Radio Bluetooth, Sensores Aparcamiento', 'https://www.automovilesplaycar.com/slir/w900/files/47651/47651_img_6947.jpeg'),
(5, 'Car DFSK E5 Intelligent PHEV', 31590.00, 'Blanco', 'Radio Bluetooth, Sensores Aparcamiento', 'https://www.automovilesplaycar.com/slir/w900/files/47653/47653_4.jpg');

-- =========================
-- tarifas
-- =========================
INSERT INTO `tarifas` (`MacroSector`, `ID_Familia`, `Descrip_Familia`, `id`, `Descripcion`, `Importe_PVP`, `Importe_Coste`) VALUES
('MA', 1, 'FILTROS', 'MA010001', 'FILTRO HABITACULO', 58.41, 45.01),
('MA', 1, 'FILTROS', 'MA010002', 'FILTRO PARTICULAS', 25.00, 10.00),
('MA', 1, 'FILTROS', 'MA010003', 'FILTRO AIRE', 15.00, 3.00),
('MA', 1, 'FILTROS', 'MA010004', 'FILTRO PORTON', 14.00, 2.00),
('MA', 1, 'FILTROS', 'MA010005', 'FILTRO ANTICONGELANTE', 25.00, 10.00),
('MA', 5, 'BATERIAS', 'MA050001', 'BATERIA MOD. 1', 115.00, 70.00),
('MA', 5, 'BATERIAS', 'MA050002', 'BATERIA MOD. 2', 125.00, 80.00),
('MA', 5, 'BATERIAS', 'MA050003', 'BATERIA MOD. 3', 130.00, 90.00),
('MA', 5, 'BATERIAS', 'MA050004', 'BATERIA MOD. 4', 140.00, 100.00),
('MA', 5, 'BATERIAS', 'MA050005', 'BATERIA MOD. 5', 150.00, 110.00),
('MA', 8, 'CARROCERIA', 'MA080001', 'PUERTA DEL.', 400.00, 200.00),
('MA', 8, 'CARROCERIA', 'MA080002', 'PUERTA TRAS.', 400.00, 200.00),
('MA', 8, 'CARROCERIA', 'MA080003', 'PUERTA CORREDERA', 250.00, 125.00),
('MA', 8, 'CARROCERIA', 'MA080004', 'SUELO LAMINADO', 1100.00, 500.00),
('MA', 8, 'CARROCERIA', 'MA080005', 'TECHO LAMINADO', 1100.00, 500.00),
('MA', 9, 'FRENOS', 'MA090001', 'PASTILLAS DEL.', 80.00, 40.00),
('MA', 9, 'FRENOS', 'MA090002', 'PASTILLAS TRAS.', 80.00, 40.00),
('MA', 9, 'FRENOS', 'MA090003', 'DISCOS DEL.', 90.00, 45.00),
('MA', 9, 'FRENOS', 'MA090004', 'DISCOS TRAS.', 90.00, 45.00),
('MA', 9, 'FRENOS', 'MA090005', 'FRENO DE MANO', 50.00, 25.00),
('MA', 10, 'ELECTRICIDAD', 'MA100001', 'INST. ELECTRICA', 2500.00, 1000.00),
('MA', 10, 'ELECTRICIDAD', 'MA100002', 'CENTRALITA MOTOR', 600.00, 300.00),
('MA', 10, 'ELECTRICIDAD', 'MA100003', 'CENTRALITA OBD', 600.00, 300.00),
('MA', 10, 'ELECTRICIDAD', 'MA100004', 'CUADRO ELECTRICO', 800.00, 400.00),
('MA', 10, 'ELECTRICIDAD', 'MA100005', 'CENTRALITA FRENOS', 600.00, 300.00),
('MI', 2, 'ESCOBILLAS', 'MI020001', 'ESCOBILLAS ANCHAS', 61.20, 32.35),
('MI', 2, 'ESCOBILLAS', 'MI020002', 'ESCOBILLAS FINAS', 55.00, 25.00),
('MI', 2, 'ESCOBILLAS', 'MI020003', 'ESCOBILLAS DELANTERAS', 31.20, 16.15),
('MI', 2, 'ESCOBILLAS', 'MI020004', 'ESCOBILLAS TRASERAS', 31.20, 16.15),
('MI', 2, 'ESCOBILLAS', 'MI020005', 'FALDILLAS TRASERAS', 44.00, 22.00),
('MI', 3, 'LIQUIDOS', 'MI030001', 'ANTICONGELANTE 5L', 35.00, 15.00),
('MI', 3, 'LIQUIDOS', 'MI030002', 'ENGRASADOR 8L', 15.00, 5.00),
('MI', 3, 'LIQUIDOS', 'MI030003', 'LAVAPARABRISAS 5L', 10.00, 2.00),
('MI', 3, 'LIQUIDOS', 'MI030004', 'LIMPIAPLASTICOS 5L', 25.00, 15.00),
('MI', 3, 'LIQUIDOS', 'MI030005', 'ACEITE CAJA DE CAMBIOS 5L', 45.00, 25.00),
('MI', 4, 'NEUMATICOS', 'MI040001', 'NEUMATICO DELANTERO', 50.00, 25.00),
('MI', 4, 'NEUMATICOS', 'MI040002', 'NEUMATICO TRASERO', 50.00, 25.00),
('MI', 4, 'NEUMATICOS', 'MI040003', 'RUEDA DE REPUESTO', 30.00, 15.00),
('MI', 4, 'NEUMATICOS', 'MI040004', 'KIT ANTIPINCHAZOS', 15.00, 7.50),
('MI', 4, 'NEUMATICOS', 'MI040005', 'REPARADOR SINTETICO', 12.00, 5.00),
('MI', 6, 'FAROS', 'MI060001', 'FARO DEL. IZQ.', 55.00, 20.00),
('MI', 6, 'FAROS', 'MI060002', 'FARO DEL. DER.', 55.00, 20.00),
('MI', 6, 'FAROS', 'MI060003', 'FARO TRAS. IZQ.', 65.00, 20.00),
('MI', 6, 'FAROS', 'MI060004', 'FARO TRAS. DER.', 65.00, 20.00),
('MI', 6, 'FAROS', 'MI060005', 'LAMPARA 12V.', 2.00, 0.30),
('MI', 7, 'CRISTALES', 'MI070001', 'CRISTAL IZQ.', 60.00, 30.00),
('MI', 7, 'CRISTALES', 'MI070002', 'CRISTAL DER.', 60.00, 30.00),
('MI', 7, 'CRISTALES', 'MI070003', 'LUNA DEL.', 180.00, 90.00),
('MI', 7, 'CRISTALES', 'MI070004', 'CRISTAL TRAS.', 60.00, 30.00),
('MI', 7, 'CRISTALES', 'MI070005', 'ESPEJO RETROVISOR', 55.00, 20.00);

-- =========================
-- tarifas de taller
-- =========================
INSERT INTO `tarifa_taller` (`id`, `tipo_averia`, `precio_averia`, `texto`) VALUES
(1, 'REVISION L1', 350.00, '-Diagnóstico electrónico alta tensión.\r\n-84 puntos de revisión.\n-Cambio de filtro de habitáculo.\r\n-R'),
(2, 'REVISION L2', 550.00, '-Diagnóstico electrónico alta tensión.\r\n-112 puntos de revisión.\r\n-Cambio de filtro de habitáculo, l'),
(3, 'SUSTITUIR BATERIA', 250.00, 'Sustitución de la batería.'),
(4, 'SUSTITUIR FRENOS', 550.00, 'Sustitución del sistema de frenado.'),
(5, 'SUSTITUIR SIST. CARGA', 1050.00, 'Sustitución de sistema de carga completo.');

-- =========================
-- usuarios
-- =========================
INSERT INTO `usuarios` (`id`, `nombre`, `apellidos`, `rol`, `user`, `password`) VALUES
(1, 'Moisés', 'López Vega', 'Administrador', 'moic', '123456'),
(2, 'Agustín', 'Pérez García', 'Usuario Avanzado', 'agarcia', '123456'),
(3, 'Falisnopio', 'Huertas Martín', 'Usuario Comercial', 'fhuertas', '123456'),
(4, 'Jesús', 'Puente Laredo', 'Usuario Comercial', 'lpuente', '123456'),
(5, 'Luis', 'Totana Forlán', 'Usuario Postventa', 'lforlan', '123456'),
(6, 'Trinidad', 'Dueñas Ramos', 'Usuario Avanzado', 'tduenas', '123456');

-- =========================
-- vehículos de muestra (comprados)
-- =========================
INSERT INTO `listado_vehiculos` (`id`, `fecha_mov`, `modelo`, `bastidor`, `matricula`, `precio_compra`, `iva_compra`, `total_compra`, `color`, `extras`, `ID_Cliente`, `precio_venta`, `iva_venta`, `total_venta`, `foto`, `fecha_compra`, `fecha_venta`, `id_proveedor`, `nfra_proveedor`, `nfra_venta`, `comentarios`, `ID_TarifaVehiculo`) VALUES
(1, '2024-11-09', 'Car eDeliver 3', '1HGBH41JXMN109181', NULL, 45000.00, 9450.00, 54450.00, 'Blanco', 'Radio Bluetooth, Sensores Aparcamiento', NULL, NULL, NULL, NULL, 'https://www.automovilesplaycar.com/slir/w900/files/20192/20192_2.jpg', '2024-11-09', NULL, 1, 'A1200000000', NULL, NULL, 1),
(2, '2024-11-09', 'Car eDeliver 3', '1HGBH41JXMN109182', NULL, 45000.00, 9450.00, 54450.00, 'Blanco', 'Radio Bluetooth, Sensores Aparcamiento', NULL, NULL, NULL, NULL, 'https://www.automovilesplaycar.com/slir/w900/files/20192/20192_2.jpg', '2024-11-09', NULL, 1, 'A1200000001', NULL, NULL, 1),
(3, '2024-11-09', 'Car eDeliver 3', '1HGBH41JXMN109183', NULL, 45000.00, 9450.00, 54450.00, 'Blanco', 'Radio Bluetooth, Sensores Aparcamiento', NULL, NULL, NULL, NULL, 'https://www.automovilesplaycar.com/slir/w900/files/20192/20192_2.jpg', '2024-11-09', NULL, 1, 'A1200000002', NULL, NULL, 1),
(4, '2024-11-09', 'Car eDeliver 9', '1HGBH41JXMN109184', NULL, 53500.00, 11235.00, 64735.00, 'Blanco', 'Radio Bluetooth, Sensores Aparcamiento', NULL, NULL, NULL, NULL, 'https://www.automovilesplaycar.com/slir/w900/files/36876/36876_19935_img_3132.jpg', '2024-11-09', NULL, 1, 'A1200000003', NULL, NULL, 2),
(5, '2024-11-11', 'Car eDeliver 9', '1HGBH41JXMN109185', NULL, 53500.00, 11235.00, 64735.00, 'Blanco', 'Radio Bluetooth, Sensores Aparcamiento', NULL, NULL, NULL, NULL, 'https://www.automovilesplaycar.com/slir/w900/files/36876/36876_19935_img_3132.jpg', '2024-11-09', NULL, 1, 'A1200000004', NULL, NULL, 2),
(6, '2024-11-11', 'Car eUniq 5', '1HGBH41JXMN109186', NULL, 33900.00, 7119.00, 41019.00, 'Gris', 'Radio Bluetooth, Sensores Aparcamiento', NULL, NULL, NULL, NULL, 'https://www.automovilesplaycar.com/slir/w900/files/22912/22912_img_3875.jpg', '2024-11-09', NULL, 1, 'A1200000005', NULL, NULL, 3),
(7, '2024-11-11', 'Car eUniq 5', '1HGBH41JXMN109187', NULL, 33900.00, 7119.00, 41019.00, 'Gris', 'Radio Bluetooth, Sensores Aparcamiento', NULL, NULL, NULL, NULL, 'https://www.automovilesplaycar.com/slir/w900/files/22912/22912_img_3875.jpg', '2024-11-09', NULL, 1, 'A1200000006', NULL, NULL, 3),
(8, '2024-11-11', 'Car DFSK 600', '1HGBH41JXMN109188', NULL, 22500.00, 4725.00, 27225.00, 'Verde Claro', 'Radio Bluetooth, Sensores Aparcamiento', NULL, NULL, NULL, NULL, 'https://www.automovilesplaycar.com/slir/w900/files/47651/47651_img_6947.jpeg', '2024-11-09', NULL, 1, 'A1200000007', NULL, NULL, 4),
(9, '2024-11-11', 'Car DFSK 600', '1HGBH41JXMN109189', NULL, 22500.00, 4725.00, 27225.00, 'Verde Claro', 'Radio Bluetooth, Sensores Aparcamiento', NULL, NULL, NULL, NULL, 'https://www.automovilesplaycar.com/slir/w900/files/47651/47651_img_6947.jpeg', '2024-11-09', NULL, 1, 'A1200000008', NULL, NULL, 4),
(10, '2024-11-11', 'Car DFSK E5 Intelligent PHEV', '1HGBH41JXMN109190', NULL, 31590.00, 6633.90, 38223.90, 'Blanco', 'Radio Bluetooth, Sensores Aparcamiento', NULL, NULL, NULL, NULL, 'https://www.automovilesplaycar.com/slir/w900/files/47653/47653_4.jpg', '2024-11-09', NULL, 1, 'A1200000009', NULL, NULL, 5),
(11, '2024-11-11', 'Car DFSK E5 Intelligent PHEV', '1HGBH41JXMN109191', NULL, 31590.00, 6633.90, 38223.90, 'Blanco', 'Radio Bluetooth, Sensores Aparcamiento', NULL, NULL, NULL, NULL, 'https://www.automovilesplaycar.com/slir/w900/files/47653/47653_4.jpg', '2024-11-09', NULL, 1, 'A1200000010', NULL, NULL, 5),
(12, '2024-11-11', 'Car eDeliver 3', '1HGBH41JXMN109192', NULL, 45000.00, 9450.00, 54450.00, 'Blanco', 'Radio Bluetooth, Sensores Aparcamiento', NULL, NULL, NULL, NULL, 'https://www.automovilesplaycar.com/slir/w900/files/20192/20192_2.jpg', '2024-11-10', NULL, 1, 'A1200000011', NULL, NULL, 1),
(13, '2024-11-11', 'Car eDeliver 3', '1HGBH41JXMN109193', NULL, 45000.00, 9450.00, 54450.00, 'Blanco', 'Radio Bluetooth, Sensores Aparcamiento', NULL, NULL, NULL, NULL, 'https://www.automovilesplaycar.com/slir/w900/files/20192/20192_2.jpg', '2024-11-10', NULL, 1, 'A1200000012', NULL, NULL, 1),
(14, '2024-11-11', 'Car eDeliver 9', '1HGBH41JXMN109194', NULL, 53500.00, 11235.00, 64735.00, 'Blanco', 'Radio Bluetooth, Sensores Aparcamiento', NULL, NULL, NULL, NULL, 'https://www.automovilesplaycar.com/slir/w900/files/36876/36876_19935_img_3132.jpg', '2024-11-10', NULL, 1, 'A1200000013', NULL, NULL, 2),
(15, '2024-11-11', 'Car eDeliver 3', '1HGBH41JXMN109195', NULL, 45000.00, 9450.00, 54450.00, 'Blanco', 'Radio Bluetooth, Sensores Aparcamiento', NULL, NULL, NULL, NULL, 'https://www.automovilesplaycar.com/slir/w900/files/20192/20192_2.jpg', '2024-11-11', NULL, 1, 'A1200000014', NULL, NULL, 1),
(16, '2024-11-11', 'Car eDeliver 3', '1HGBH41JXMN109196', NULL, 45000.00, 9450.00, 54450.00, 'Blanco', 'Radio Bluetooth, Sensores Aparcamiento', NULL, NULL, NULL, NULL, 'https://www.automovilesplaycar.com/slir/w900/files/20192/20192_2.jpg', '2024-11-10', NULL, 1, 'A1200000015', NULL, NULL, 1),
(17, '2025-09-22', 'Car DFSK E5 Intelligent PHEV', '1HGBH41JXMN109197', NULL, 31590.00, 6633.90, 38223.90, 'Blanco', 'Radio Bluetooth, Sensores Aparcamiento', NULL, NULL, NULL, NULL, 'https://www.automovilesplaycar.com/slir/w900/files/47653/47653_4.jpg', '2025-09-22', NULL, 1, 'A1200000016', NULL, NULL, 5);