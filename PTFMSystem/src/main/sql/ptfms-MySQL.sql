-- Disable foreign key checks for smooth schema and data load
SET FOREIGN_KEY_CHECKS=0;

-- Drop and recreate database
DROP DATABASE IF EXISTS ptfms;
CREATE DATABASE ptfms;
USE ptfms;

-- ===========================
-- 1. USERS TABLE (FR-01)
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
-- 5. VEHICLES TABLE (FR-02)
-- ===========================
CREATE TABLE Vehicles (
    vehicle_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_number VARCHAR(50) UNIQUE NOT NULL,
    vehicle_type ENUM('Diesel Bus','Electric Light Rail','Diesel-Electric Train') NOT NULL,
    fuel_type VARCHAR(50),
    consumption_rate DECIMAL(10,2), -- liters or kWh per 100km
    max_passengers INT,
    route_id INT,
    FOREIGN KEY (route_id) REFERENCES Routes(route_id)
);

-- ===========================
-- 6. GPS LOGS (FR-03)
-- ===========================
CREATE TABLE GPSLogs (
    gps_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    latitude DECIMAL(9,6),
    longitude DECIMAL(9,6),
    log_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('In-Service','Break','Out-of-Service') DEFAULT 'In-Service',
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id)
);

-- ===========================
-- 7. FUEL/ENERGY CONSUMPTION (FR-04)
-- ===========================
CREATE TABLE FuelLogs (
    fuel_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    fuel_used DECIMAL(10,2),
    log_date DATE,
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id)
);

-- ===========================
-- 8. MAINTENANCE ALERTS (FR-05)
-- ===========================
CREATE TABLE MaintenanceAlerts (
    alert_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    alert_type VARCHAR(100), -- e.g. Brake Wear, Engine Check
    severity ENUM('Low','Medium','High'),
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id)
);

-- ===========================
-- 9. MAINTENANCE TASKS (FR-05)
-- ===========================
CREATE TABLE MaintenanceTasks (
    task_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    description VARCHAR(255),
    scheduled_date DATE,
    completed BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id)
);

-- ===========================
-- 10. BREAK LOGS (FR-03)
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
-- 11. SAMPLE DATA
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
('Laura Evans','laura.operator@ptfms.com','cst8288','Operator');

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
INSERT INTO GPSLogs (vehicle_id, latitude, longitude, status) VALUES
(1,45.4215,-75.6972,'In-Service'),
(2,45.4245,-75.6950,'Break'),
(3,45.4250,-75.6930,'In-Service'),
(4,45.4300,-75.6890,'In-Service'),
(5,45.4350,-75.6845,'Break'),
(6,45.4400,-75.6800,'In-Service'),
(7,45.4455,-75.6755,'Out-of-Service'),
(8,45.4500,-75.6700,'In-Service'),
(9,45.4555,-75.6655,'Break'),
(10,45.4600,-75.6600,'In-Service');

-- Fuel/Energy Logs
INSERT INTO FuelLogs (vehicle_id, fuel_used, log_date) VALUES
(1,40.5,'2025-08-01'),
(2,20.0,'2025-08-01'),
(3,70.0,'2025-08-01'),
(4,30.0,'2025-08-02'),
(5,25.5,'2025-08-02'),
(6,72.0,'2025-08-03'),
(7,68.5,'2025-08-03'),
(8,29.0,'2025-08-04'),
(9,18.0,'2025-08-04'),
(10,32.0,'2025-08-05');

-- Maintenance Alerts
INSERT INTO MaintenanceAlerts (vehicle_id, alert_type, severity) VALUES
(1,'Brake Wear','High'),
(2,'Pantograph Inspection','Medium'),
(3,'Engine Oil Low','High'),
(4,'Wheel Alignment','Medium'),
(5,'Brake Pads Wear','High'),
(6,'Electrical Circuit Check','Low'),
(7,'Cooling System Leak','High'),
(8,'Fuel Injector Maintenance','Medium'),
(9,'Axle Bearing Check','Medium'),
(10,'Brake System Diagnostics','High');

-- Maintenance Tasks
INSERT INTO MaintenanceTasks (vehicle_id, description, scheduled_date) VALUES
(1,'Replace Brake Pads','2025-08-10'),
(2,'Electrical Checkup','2025-08-15'),
(3,'Engine Oil Replacement','2025-08-16'),
(4,'Adjust Wheel Alignment','2025-08-18'),
(5,'Install New Brake Pads','2025-08-19'),
(6,'Inspect Electrical Wiring','2025-08-21'),
(7,'Repair Cooling System Leak','2025-08-22'),
(8,'Clean Fuel Injectors','2025-08-23'),
(9,'Lubricate Axle Bearings','2025-08-25'),
(10,'Run Brake Diagnostics','2025-08-27');

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

-- Operator 12 (Operator - 5 logs)
(1, 12, '2025-07-22 08:30:00', '2025-07-22 09:00:00', 'Ended'),
(2, 12, '2025-07-24 10:00:00', '2025-07-24 10:25:00', 'Ended'),
(3, 12, '2025-07-27 09:15:00', '2025-07-27 09:45:00', 'Ended'),
(4, 12, '2025-07-29 11:30:00', '2025-07-29 12:00:00', 'Ended'),
(5, 12, '2025-08-03 14:20:00', '2025-08-03 14:50:00', 'Ended');



-- Enable foreign key checks after all inserts. Avoid constraint error during bulk inserts. 
SET FOREIGN_KEY_CHECKS=1;