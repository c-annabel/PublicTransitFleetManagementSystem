-- Disable foreign key checks for smooth schema and data load
SET FOREIGN_KEY_CHECKS=0;

-- Drop and recreate database
DROP DATABASE IF EXISTS ptfms;
CREATE DATABASE ptfms;
USE ptfms;

-- ===========================
-- 1. USERS TABLE
-- ===========================
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    user_type ENUM('Manager','Operator') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===========================
-- 2. ROUTES TABLE
-- ===========================
CREATE TABLE Routes (
    route_id INT AUTO_INCREMENT PRIMARY KEY,
    route_name VARCHAR(100) UNIQUE NOT NULL,
    start_point VARCHAR(100),
    end_point VARCHAR(100)
);

-- ===========================
-- 3. STATIONS TABLE
-- ===========================
CREATE TABLE Stations (
    station_id INT AUTO_INCREMENT PRIMARY KEY,
    station_name VARCHAR(100) NOT NULL,
    location VARCHAR(150)
);

-- ===========================
-- 4. ROUTE-STATION MAPPING
-- ===========================
CREATE TABLE RouteStations (
    route_station_id INT AUTO_INCREMENT PRIMARY KEY,
    route_id INT NOT NULL,
    station_id INT NOT NULL,
    stop_order INT NOT NULL,
    FOREIGN KEY (route_id) REFERENCES Routes(route_id),
    FOREIGN KEY (station_id) REFERENCES Stations(station_id)
);

-- ===========================
-- 5. VEHICLES TABLE
-- ===========================
CREATE TABLE Vehicles (
    vehicle_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_number VARCHAR(50) UNIQUE NOT NULL,
    vehicle_type ENUM('Diesel Bus','Electric Light Rail','Diesel-Electric Train') NOT NULL,
    fuel_type VARCHAR(50),
    consumption_rate DECIMAL(10,2),
    max_passengers INT,
    route_id INT,
    FOREIGN KEY (route_id) REFERENCES Routes(route_id)
);

-- ===========================
-- 6. VEHICLE TYPE CONFIG
-- ===========================
CREATE TABLE VehicleTypeConfig (
    config_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_type ENUM('Diesel Bus','Electric Light Rail','Diesel-Electric Train') UNIQUE NOT NULL,
    threshold DECIMAL(10,2) NOT NULL,
    unit VARCHAR(20) NOT NULL
);

-- ===========================
-- 7. GPS LOGS
-- ===========================
CREATE TABLE GPSLogs (
    gps_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    station_id INT NOT NULL,
    arrival_time DATETIME NOT NULL,
    departure_time DATETIME NULL,
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id),
    FOREIGN KEY (station_id) REFERENCES Stations(station_id)
);

-- ===========================
-- 8. BREAK LOGS
-- ===========================
CREATE TABLE BreakLogs (
    break_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    operator_id INT NOT NULL,
    start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    end_time TIMESTAMP NULL,
    status ENUM('Started', 'Paused', 'Ended') DEFAULT 'Started',
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id),
    FOREIGN KEY (operator_id) REFERENCES Users(user_id)
);

-- ===========================
-- 9. CONSUMPTION LOGS
-- ===========================
CREATE TABLE ConsumptionLogs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    log_datetime DATETIME DEFAULT CURRENT_TIMESTAMP,
    distance_travelled DECIMAL(10,2) NOT NULL,
    fuel_used DECIMAL(10,2) DEFAULT 0.00,
    energy_used DECIMAL(10,2) DEFAULT 0.00,
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id),
    CHECK (fuel_used > 0 OR energy_used > 0)
);

-- ===========================
-- 10. USAGE LOGS
-- ===========================
CREATE TABLE UsageLogs (
    usage_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    log_datetime DATETIME DEFAULT CURRENT_TIMESTAMP,
    hours_used DECIMAL(10,2) NOT NULL,
    brake_condition DECIMAL(5,2),
    tire_condition DECIMAL(5,2),
    axle_condition DECIMAL(5,2),
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id)
);

-- ===========================
-- 11. DIAGNOSTICS LOGS
-- ===========================
CREATE TABLE DiagnosticsLogs (
    diag_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    log_datetime DATETIME DEFAULT CURRENT_TIMESTAMP,
    engine_health DECIMAL(5,2),
    catenary_condition DECIMAL(5,2),
    pantograph_condition DECIMAL(5,2),
    circuit_breaker_condition DECIMAL(5,2),
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id)
);

-- ===========================
-- 12. ALERTS
-- ===========================
CREATE TABLE Alerts (
    alert_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    alert_type ENUM('Consumption','Maintenance') NOT NULL,
    alert_message VARCHAR(255),
    consumption_value DECIMAL(10,2) DEFAULT NULL,
    threshold DECIMAL(10,2) DEFAULT NULL,
    severity ENUM('Low','Medium','High') DEFAULT NULL,
    generated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    resolved BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id)
);

-- ===========================
-- 13. MAINTENANCE TASKS
-- ===========================
CREATE TABLE MaintenanceTasks (
    task_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    alert_id INT,
    description VARCHAR(255) NOT NULL,
    scheduled_datetime DATETIME NOT NULL,
    cost DECIMAL(10,2) DEFAULT 0.00,
    completed BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id),
    FOREIGN KEY (alert_id) REFERENCES Alerts(alert_id),
    UNIQUE(vehicle_id, scheduled_datetime)
);

-- ===========================
-- 14. TRIP SCHEDULES
-- ===========================
CREATE TABLE TripSchedules (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    route_id INT NOT NULL,
    station_id INT NOT NULL,
    planned_arrival_time DATETIME NOT NULL,
    FOREIGN KEY (route_id) REFERENCES Routes(route_id),
    FOREIGN KEY (station_id) REFERENCES Stations(station_id)
);

-- ===========================
-- 15. OPERATOR ASSIGNMENTS
-- ===========================
CREATE TABLE OperatorAssignments (
    assignment_id INT AUTO_INCREMENT PRIMARY KEY,
    operator_id INT NOT NULL,
    vehicle_id INT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME,
    FOREIGN KEY (operator_id) REFERENCES Users(user_id),
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id)
);

-- ===========================
-- 16. OPERATOR ASSIGNMENTS
-- ===========================
CREATE TABLE PriceConfig (
    config_id INT AUTO_INCREMENT PRIMARY KEY,
    fuel_type VARCHAR(50),
    price_per_unit DECIMAL(10,2),
    effective_date DATE
);

-- ===========================
--  SAMPLE DATA
-- ===========================

-- Users
INSERT INTO Users (name,email,password,user_type) VALUES
('Alice Johnson','alice.manager@ptfms.com','cst8288','Manager'),
('Bob Smith','bob.operator@ptfms.com','cst8288','Operator'),
('Charlie Green','charlie.manager@ptfms.com','cst8288','Manager'),
('Julia Roberts','julia.operator@ptfms.com','cst8288','Operator'),
('Kevin Scott','kevin.manager@ptfms.com','cst8288','Manager'),
('George Brown','george.manager@ptfms.com','cst8288','Manager'),
('Hannah White','hannah.operator@ptfms.com','cst8288','Operator'),
('Diana Lopez','diana.operator@ptfms.com','cst8288','Operator'),
('Edward King','edward.manager@ptfms.com','cst8288','Manager'),
('Fiona Adams','fiona.operator@ptfms.com','cst8288','Operator'),
('Laura Evans','laura.operator@ptfms.com','cst8288','Operator'),
('Operator', 'op@algonquin.com', 'cst8288', 'Operator'),
('Transit Manager', 'tm@algonquin.com', 'cst8288', 'Manager');

-- Routes
INSERT INTO Routes (route_name,start_point,end_point) VALUES
('Route A','Downtown','Airport'),
('Route B','City Center','University'),
('Route C','East End','West End'),
('Route D','North Station','South Station'),
('Route E','Harbor Point','Industrial Park'),
('Route F','Central Station','Tech Hub');

-- Stations
INSERT INTO Stations (station_name,location) VALUES
('Central Station','Downtown'),
('Airport Terminal','Airport Blvd'),
('City Mall','City Center'),
('Trim','Ottawa East'),
('Place Orléans','Ottawa East'),
('Blair','Ottawa East'),
('St-Laurent','Ottawa East-Central'),
('Hurdman','Ottawa Central'),
('Rideau','Ottawa Downtown'),
('Pimisi','Ottawa Central'),
('Bayview','Ottawa West'),
('Tunney Pasture','Ottawa West'),
('Algonquin','Ottawa West'),
('University Stop','Campus Road');

-- RouteStations
INSERT INTO RouteStations (route_id,station_id,stop_order) VALUES
-- Route A
(1,1,1),   -- Central Station
(1,2,2),   -- Airport Terminal
-- Route B
(2,3,1),   -- City Mall
(2,4,2),   -- Trim
(2,14,3),  -- University Stop
-- Route C
(3,4,1),   -- Trim
(3,5,2),   -- Place Orléans
(3,6,3),   -- Blair
-- Route D
(4,7,1),   -- St-Laurent
(4,8,2),   -- Hurdman
(4,9,3),   -- Rideau
-- Route E
(5,10,1),  -- Pimisi
(5,11,2),  -- Bayview
-- Route F
(6,12,1),  -- Tunney Pasture
(6,13,2);  -- Algonquin

-- Vehicles
INSERT INTO Vehicles (vehicle_number,vehicle_type,fuel_type,consumption_rate,max_passengers,route_id) VALUES
('BUS101','Diesel Bus','Diesel',30.5,50,1),
('LRT201','Electric Light Rail','Electric',15.0,200,2),
('TRAIN301','Diesel-Electric Train','Diesel-Electric',50.0,500,1),
('BUS102','Diesel Bus','Diesel',28.7,48,2),     -- Another bus on Route B
('BUS103','Diesel Bus','Diesel',31.2,52,3),     -- Bus on Route C
('LRT202','Electric Light Rail','Electric',14.5,210,3), -- LRT for Route C
('LRT203','Electric Light Rail','Electric',15.2,200,4), -- LRT for Route D
('TRAIN302','Diesel-Electric Train','Diesel-Electric',49.8,510,4), -- Train for Route D
('TRAIN303','Diesel-Electric Train','Diesel-Electric',52.0,495,5), -- Train for Route E
('BUS104','Diesel Bus','Diesel',29.9,50,6);     -- Bus for Route F;

-- GPS Logs
INSERT INTO GPSLogs (vehicle_id, station_id, arrival_time, departure_time) VALUES
-- Vehicle 1 (Route A)
(1, 1, '2025-07-15 08:05:00', '2025-07-15 08:15:00'),
(1, 2, '2025-07-15 08:20:00', '2025-07-15 08:30:00'),
(1, 1, '2025-07-22 08:35:00', '2025-07-22 08:45:00'),
(1, 2, '2025-07-22 08:50:00', '2025-07-22 09:00:00'),

-- Vehicle 2 (Route B)
(2, 3, '2025-07-16 10:20:00', '2025-07-16 10:25:00'),
(2, 4, '2025-07-16 10:30:00', '2025-07-16 10:35:00'),
(2, 14,'2025-07-16 10:38:00', '2025-07-16 10:40:00'),
(2, 3, '2025-07-24 10:05:00', '2025-07-24 10:10:00'),
(2, 4, '2025-07-24 10:15:00', '2025-07-24 10:20:00'),
(2, 14,'2025-07-24 10:22:00', '2025-07-24 10:25:00'),

-- Vehicle 3 (Route A train)
(3, 1, '2025-07-17 09:15:00', '2025-07-17 09:25:00'),
(3, 2, '2025-07-17 09:30:00', '2025-07-17 09:40:00'),
(3, 1, '2025-07-27 09:20:00', '2025-07-27 09:30:00'),
(3, 2, '2025-07-27 09:35:00', '2025-07-27 09:45:00'),

-- Vehicle 4 (Route B bus)
(4, 3, '2025-07-19 13:05:00', '2025-07-19 13:10:00'),
(4, 4, '2025-07-19 13:15:00', '2025-07-19 13:20:00'),
(4, 14,'2025-07-19 13:22:00', '2025-07-19 13:25:00'),
(4, 3, '2025-07-29 11:35:00', '2025-07-29 11:40:00'),
(4, 4, '2025-07-29 11:45:00', '2025-07-29 11:50:00'),
(4, 14,'2025-07-29 11:55:00', '2025-07-29 12:00:00'),

-- Vehicle 5 (Route C)
(5, 4, '2025-07-20 09:05:00', '2025-07-20 09:10:00'),
(5, 5, '2025-07-20 09:15:00', '2025-07-20 09:20:00'),
(5, 6, '2025-07-20 09:22:00', '2025-07-20 09:25:00'),
(5, 4, '2025-08-03 14:25:00', '2025-08-03 14:30:00'),
(5, 5, '2025-08-03 14:35:00', '2025-08-03 14:40:00'),
(5, 6, '2025-08-03 14:45:00', '2025-08-03 14:50:00');

-- Break Logs
INSERT INTO BreakLogs (vehicle_id, operator_id, start_time, end_time, status) VALUES
-- Operator 2 (Bob)
(1, 2, '2025-07-15 08:00:00', '2025-07-15 08:30:00', 'Ended'),
(5, 2, '2025-07-20 09:00:00', '2025-07-20 09:25:00', 'Ended'),

-- Operator 4 (Julia)
(2, 4, '2025-07-16 10:15:00', '2025-07-16 10:40:00', 'Ended'),
(6, 4, '2025-07-25 14:00:00', '2025-07-25 14:30:00', 'Ended'),

-- Operator 7 (Hannah)
(7, 7, '2025-07-18 11:00:00', '2025-07-18 11:30:00', 'Ended'),
(7, 7, '2025-07-26 15:15:00', '2025-07-26 15:40:00', 'Ended'),

-- Operator 8 (Diana)
(3, 8, '2025-07-17 09:10:00', '2025-07-17 09:40:00', 'Ended'),
(8, 8, '2025-07-30 16:00:00', '2025-07-30 16:30:00', 'Ended'),

-- Operator 10 (Fiona)
(4, 10, '2025-07-19 13:00:00', '2025-07-19 13:25:00', 'Ended'),
(9, 10, '2025-07-28 17:10:00', '2025-07-28 17:40:00', 'Ended'),

-- Operator 11 (Laura)
(10, 11, '2025-07-21 14:20:00', '2025-07-21 14:50:00', 'Ended'),

-- Operator 12 (at least 5 logs)
(1, 12, '2025-07-22 08:30:00', '2025-07-22 09:00:00', 'Ended'),
(2, 12, '2025-07-24 10:00:00', '2025-07-24 10:25:00', 'Ended'),
(3, 12, '2025-07-27 09:15:00', '2025-07-27 09:45:00', 'Ended'),
(4, 12, '2025-07-29 11:30:00', '2025-07-29 12:00:00', 'Ended'),
(5, 12, '2025-08-03 14:20:00', '2025-08-03 14:50:00', 'Ended');

-- VehicleTypeConfig (Updated Thresholds)
INSERT INTO VehicleTypeConfig (vehicle_type, threshold, unit) VALUES
('Diesel Bus', 45.00, 'L/100km'),
('Electric Light Rail', 25.00, 'kWh/100km'),
('Diesel-Electric Train', 80.00, 'L/100km');

-- ConsumptionLogs (10 entries: dates 7/15 to 8/03)
INSERT INTO ConsumptionLogs (vehicle_id, distance_travelled, fuel_used, energy_used, log_datetime) VALUES
(1, 100.0, 50.0, 0.0, '2025-07-15 10:00:00'),
(2, 110.0, 0.0, 28.0, '2025-07-16 11:00:00'),
(3, 200.0, 170.0, 0.0, '2025-07-17 12:00:00'),
(4, 130.0, 48.0, 0.0, '2025-07-18 09:30:00'),
(5, 140.0, 49.0, 0.0, '2025-07-19 10:00:00'),
(6, 120.0, 0.0, 30.0, '2025-07-20 11:00:00'),
(7, 150.0, 0.0, 27.0, '2025-07-21 12:00:00'),
(8, 210.0, 185.0, 0.0, '2025-07-22 13:00:00'),
(9, 230.0, 190.0, 0.0, '2025-07-23 08:00:00'),
(10,150.0, 46.0, 0.0, '2025-07-24 09:00:00');

-- UsageLogs (10 entries)
INSERT INTO UsageLogs (vehicle_id, hours_used, brake_condition, tire_condition, axle_condition, log_datetime) VALUES
(1, 8.5, 85.0, 90.0, 88.0, '2025-07-15 10:05:00'),
(2, 10.0, 96.0, 94.0, 91.0, '2025-07-16 11:05:00'),
(3, 12.5, 70.0, 80.0, 75.0, '2025-07-17 12:05:00'),
(4, 9.0, 88.0, 87.0, 86.0, '2025-07-18 09:35:00'),
(5, 9.5, 82.0, 85.0, 83.0, '2025-07-19 10:05:00'),
(6, 11.0, 95.0, 93.0, 92.0, '2025-07-20 11:05:00'),
(7, 10.2, 94.0, 94.0, 93.0, '2025-07-21 12:05:00'),
(8, 14.0, 68.0, 72.0, 70.0, '2025-07-22 13:05:00'),
(9, 14.5, 60.0, 65.0, 63.0, '2025-07-23 08:05:00'),
(10,7.5, 89.0, 90.0, 88.0, '2025-07-24 09:05:00');

-- DiagnosticsLogs (10 entries)
INSERT INTO DiagnosticsLogs (vehicle_id, engine_health, catenary_condition, pantograph_condition, circuit_breaker_condition, log_datetime) VALUES
(1, 92.0, NULL, NULL, NULL, '2025-07-15 10:10:00'),
(2, NULL, 94.0, 96.0, 95.0, '2025-07-16 11:10:00'),
(3, 82.0, NULL, NULL, NULL, '2025-07-17 12:10:00'),
(4, 88.0, NULL, NULL, NULL, '2025-07-18 09:40:00'),
(5, 86.0, NULL, NULL, NULL, '2025-07-19 10:10:00'),
(6, NULL, 97.0, 98.0, 96.0, '2025-07-20 11:10:00'),
(7, NULL, 95.0, 94.0, 96.0, '2025-07-21 12:10:00'),
(8, 78.0, NULL, NULL, NULL, '2025-07-22 13:10:00'),
(9, 75.0, NULL, NULL, NULL, '2025-07-23 08:10:00'),
(10,90.0, NULL, NULL, NULL, '2025-07-24 09:10:00');

-- Alerts (10 entries: updated dates and threshold logic)
INSERT INTO Alerts (vehicle_id, alert_type, alert_message, consumption_value, threshold, severity, generated_at) VALUES
(1,'Consumption','Fuel usage exceeded threshold',50.0,45.0,'High','2025-07-15 10:20:00'),
(2,'Consumption','Energy usage exceeded threshold',28.0,25.0,'High','2025-07-16 11:20:00'),
(3,'Consumption','Train fuel usage exceeded threshold',170.0,80.0,'High','2025-07-17 12:20:00'),
(4,'Maintenance','Brake wear warning',NULL,NULL,'Medium','2025-07-18 09:50:00'),
(5,'Maintenance','Engine health below 85%',NULL,NULL,'Low','2025-07-19 10:50:00'),
(6,'Consumption','Energy usage exceeded threshold',30.0,25.0,'High','2025-07-20 11:20:00'),
(7,'Consumption','Energy usage exceeded threshold',27.0,25.0,'Medium','2025-07-21 12:20:00'),
(8,'Maintenance','Engine health critical',NULL,NULL,'High','2025-07-22 13:20:00'),
(9,'Consumption','Train fuel usage exceeded threshold',190.0,80.0,'High','2025-07-23 08:20:00'),
(10,'Maintenance','Brake system check required',NULL,NULL,'Low','2025-07-24 09:20:00');


-- MaintenanceTasks (linked to alerts)
INSERT INTO MaintenanceTasks (vehicle_id, alert_id, description, scheduled_datetime, cost, completed) VALUES
(1,1,'Inspect fuel system','2025-08-25 09:00:00',750.00,FALSE),
(2,2,'Check LRT electrical systems','2025-08-26 10:00:00',1200.00,FALSE),
(3,3,'Engine overhaul','2025-08-27 11:00:00',1800.00,FALSE),
(4,4,'Replace brake pads','2025-08-28 09:00:00',950.00,FALSE),
(5,5,'Engine diagnostics','2025-08-29 10:00:00',1100.00,FALSE),
(6,6,'Energy optimization check','2025-08-30 13:00:00',1300.00,FALSE),
(7,7,'Inspect pantograph','2025-08-31 14:00:00',1600.00,FALSE),
(8,8,'Engine tune-up','2025-09-01 15:00:00',1750.00,FALSE),
(9,9,'Fuel system calibration','2025-09-02 16:00:00',1400.00,FALSE),
(10,10,'Brake system overhaul','2025-09-03 17:00:00',900.00,FALSE);

INSERT INTO TripSchedules (route_id, station_id, planned_arrival_time) VALUES
(1, 1, '2025-07-15 08:00:00'),
(1, 2, '2025-07-15 08:20:00'),
(2, 3, '2025-07-16 10:15:00'),
(2, 4, '2025-07-16 10:30:00'),
(2,14, '2025-07-16 10:40:00'),
(3, 4, '2025-07-20 09:00:00'),
(3, 5, '2025-07-20 09:15:00'),
(3, 6, '2025-07-20 09:25:00'),
(4, 7, '2025-07-19 13:00:00'),
(4, 8, '2025-07-19 13:15:00');

INSERT INTO OperatorAssignments (operator_id, vehicle_id, start_time, end_time) VALUES
(2, 1, '2025-07-15 07:50:00', '2025-07-15 09:00:00'),
(4, 2, '2025-07-16 10:10:00', '2025-07-16 11:30:00'),
(8, 3, '2025-07-17 09:00:00', '2025-07-17 10:00:00'),
(10,4, '2025-07-19 12:50:00', '2025-07-19 14:00:00'),
(12,5, '2025-07-20 08:50:00', '2025-07-20 10:00:00'),
(7, 6, '2025-07-21 11:30:00', '2025-07-21 12:40:00'),
(2, 7, '2025-07-22 13:00:00', '2025-07-22 14:15:00'),
(4, 8, '2025-07-23 07:50:00', '2025-07-23 09:15:00'),
(8, 9, '2025-07-24 08:10:00', '2025-07-24 09:30:00'),
(10,10,'2025-07-25 14:00:00', '2025-07-25 15:15:00');

INSERT INTO PriceConfig (fuel_type, price_per_unit, effective_date) VALUES
('Diesel', 1.45, '2025-01-01'),
('Diesel', 1.50, '2025-04-01'),
('Diesel', 1.55, '2025-07-01'),
('Electric', 0.18, '2025-01-01'),
('Electric', 0.20, '2025-04-01'),
('Electric', 0.22, '2025-07-01'),
('Diesel-Electric', 1.60, '2025-01-01'),
('Diesel-Electric', 1.65, '2025-04-01'),
('Diesel-Electric', 1.70, '2025-07-01'),
('Biofuel', 1.80, '2025-07-01'); -- Optional fuel type for expansion


-- Enable foreign key checks after all inserts. Avoid constraint error during bulk inserts. 
SET FOREIGN_KEY_CHECKS=1;