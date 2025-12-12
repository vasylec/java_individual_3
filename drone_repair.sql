-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 11, 2025 at 06:53 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `drone_repair`
--

-- --------------------------------------------------------

--
-- Table structure for table `chirie`
--

CREATE TABLE `chirie` (
  `id` int(11) NOT NULL,
  `drone_id` int(11) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `client` int(11) NOT NULL,
  `price` decimal(10,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `chirie`
--

INSERT INTO `chirie` (`id`, `drone_id`, `start_date`, `end_date`, `client`, `price`) VALUES
(1, 1, '2025-01-05', '2025-01-07', 2, 150),
(21, 5, '2025-12-09', '2025-12-16', 2, 350),
(26, 19, '2025-12-09', '2025-12-25', 2, 800),
(28, 16, '2025-12-10', '2026-01-02', 2, 1150);

-- --------------------------------------------------------

--
-- Table structure for table `drones`
--

CREATE TABLE `drones` (
  `id` int(11) NOT NULL,
  `model` varchar(50) NOT NULL,
  `serial_number` varchar(100) NOT NULL,
  `status` enum('disponibilă','închiriată','în reparație','reparată','returnată','personală','în așteptare','spre confirmare') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `drones`
--

INSERT INTO `drones` (`id`, `model`, `serial_number`, `status`) VALUES
(1, 'DJI Mavic 3', 'SN-M3-0001', 'disponibilă'),
(2, 'DJI Mini 4 Pro', 'SN-M4P-0002', 'închiriată'),
(3, 'DJI Air 3', 'SN-A3-0003', 'în reparație'),
(4, 'Autel EVO II Pro', 'SN-AE2-0004', 'reparată'),
(5, 'Parrot Anafi AI', 'SN-PAI-0005', 'închiriată'),
(6, 'DJI Matrice 300', 'SN-M300-0006', 'returnată'),
(7, 'DJI Mini 3', 'SN-M3-0007', 'închiriată'),
(8, 'DJI Air 2S', 'SN-A2S-0008', 'în reparație'),
(9, 'Autel EVO Nano+', 'SN-AEN-0009', 'returnată'),
(10, 'Parrot Anafi', 'SN-PA-0010', 'disponibilă'),
(11, 'DJI Phantom 4 Pro', 'SN-P4P-0011', 'închiriată'),
(12, 'DJI Inspire 2', 'SN-I2-0012', 'în reparație'),
(13, 'Autel EVO Lite', 'SN-AEL-0013', 'disponibilă'),
(14, 'DJI FPV', 'SN-FPV-0014', 'în reparație'),
(15, 'DJI Avata', 'SN-AV-0015', 'returnată'),
(16, 'DJI Mini 2', 'SN-M2-0016', 'închiriată'),
(17, 'DJI Matrice 200', 'SN-M200-0017', 'închiriată'),
(18, 'DJI Inspire 3', 'SN-I3-0018', 'reparată'),
(19, 'Parrot Disco', 'SN-PD-0019', 'închiriată'),
(20, 'Autel EVO II', 'SN-AE2-0020', 'în reparație'),
(27, 'AAC KPW', 'SN-AAA-0123', 'în reparație');

-- --------------------------------------------------------

--
-- Table structure for table `drone_user`
--

CREATE TABLE `drone_user` (
  `id` int(11) NOT NULL,
  `id_drone` int(11) NOT NULL,
  `id_user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `drone_user`
--

INSERT INTO `drone_user` (`id`, `id_drone`, `id_user`) VALUES
(1, 14, 2),
(7, 27, 2);

-- --------------------------------------------------------

--
-- Table structure for table `reparatii`
--

CREATE TABLE `reparatii` (
  `id` int(11) NOT NULL,
  `drone_id` int(11) NOT NULL,
  `data_reparatie` date NOT NULL,
  `descriere` text NOT NULL,
  `cost` decimal(10,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reparatii`
--

INSERT INTO `reparatii` (`id`, `drone_id`, `data_reparatie`, `descriere`, `cost`) VALUES
(1, 1, '2025-01-05', 'Înlocuire elice față-stânga', 50),
(2, 2, '2025-01-06', 'Recalibrare senzori GPS', 80),
(3, 3, '2025-01-07', 'Schimbare baterie', 120),
(4, 4, '2025-01-10', 'Reparație cameră 4K', 250),
(5, 5, '2025-01-12', 'Actualizare firmware și testare', 40),
(6, 6, '2025-01-13', 'Înlocuire motor spate-dreapta', 140),
(7, 7, '2025-01-15', 'Refacere conexiune telecomandă', 60),
(8, 8, '2025-01-18', 'Reparație gimbal', 180),
(9, 9, '2025-01-20', 'Înlocuire elice și calibrare IMU', 90),
(10, 10, '2025-01-22', 'Reparare senzor obstacole', 160),
(11, 11, '2025-01-25', 'Curățare și mentenanță completă', 70),
(12, 12, '2025-01-26', 'Reparație port încărcare', 55),
(13, 13, '2025-01-28', 'Resetare software și testare', 45),
(15, 15, '2025-02-01', 'Reparație modul transmisie video', 200),
(16, 16, '2025-02-03', 'Recalibrare gimbal + update', 85),
(17, 17, '2025-02-05', 'Schimbare motor față-dreapta', 135),
(18, 18, '2025-02-07', 'Reparație modul WiFi', 150),
(19, 19, '2025-02-10', 'Schimbare baterie + testare', 130),
(20, 20, '2025-02-12', 'Reparare și curățare gimbal', 175),
(23, 14, '2025-12-10', 'S-a stricat motorul	', 2000),
(24, 27, '2025-12-11', 'a murit aripa', 500);

--
-- Triggers `reparatii`
--
DELIMITER $$
CREATE TRIGGER `trg_reparatii_set_date` BEFORE INSERT ON `reparatii` FOR EACH ROW BEGIN
    SET NEW.data_reparatie = NOW();   -- pentru data + ora
    -- SET NEW.data_reparatie = CURDATE();   -- doar data (dacă vrei doar YYYY-MM-DD)
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `login` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `isAdmin` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `login`, `password`, `isAdmin`) VALUES
(1, 'vasile', 'cozma1401', b'1'),
(2, 'cozma', 'vasile1401', b'0');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `chirie`
--
ALTER TABLE `chirie`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_droneid_2` (`drone_id`),
  ADD KEY `fk_user` (`client`);

--
-- Indexes for table `drones`
--
ALTER TABLE `drones`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `serial_number_unique` (`serial_number`);

--
-- Indexes for table `drone_user`
--
ALTER TABLE `drone_user`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_drone` (`id_drone`),
  ADD KEY `fk_droneUser` (`id_user`);

--
-- Indexes for table `reparatii`
--
ALTER TABLE `reparatii`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `drone_id` (`drone_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `chirie`
--
ALTER TABLE `chirie`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `drones`
--
ALTER TABLE `drones`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT for table `drone_user`
--
ALTER TABLE `drone_user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `reparatii`
--
ALTER TABLE `reparatii`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `chirie`
--
ALTER TABLE `chirie`
  ADD CONSTRAINT `fk_droneid_2` FOREIGN KEY (`drone_id`) REFERENCES `drones` (`id`),
  ADD CONSTRAINT `fk_user` FOREIGN KEY (`client`) REFERENCES `user` (`id`);

--
-- Constraints for table `drone_user`
--
ALTER TABLE `drone_user`
  ADD CONSTRAINT `fk_drone` FOREIGN KEY (`id_drone`) REFERENCES `drones` (`id`),
  ADD CONSTRAINT `fk_droneUser` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`);

--
-- Constraints for table `reparatii`
--
ALTER TABLE `reparatii`
  ADD CONSTRAINT `fk_droneid` FOREIGN KEY (`drone_id`) REFERENCES `drones` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
