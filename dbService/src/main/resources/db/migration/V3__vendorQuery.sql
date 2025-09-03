CREATE TABLE If NOT EXISTS vendor(
     vendorId VARCHAR(50),
     vendorName VARCHAR(50),
     phoneNumber VARCHAR(50),
     emailId VARCHAR(50),
     LatestPickupTime VARCHAR(50),
     distCost VARCHAR(50),
     minCost VARCHAR(50),
     mindist VARCHAR(50)
);

INSERT INTO vendor (
    vendorId, vendorName, phoneNumber, emailId,
    LatestPickupTime,  distCost, minCost, mindist
) VALUES
('V001', 'GreenGrocer London', '+44 20 7946 0958', 'info@greengrocerlondon.co.uk', '17:30',  '1.5', '50', '3'),
('V002', 'Fresh Picks Manchester', '+44 161 850 4455', 'orders@freshpicksmcr.co.uk', '18:00', '1.8', '60', '4'),
('V003', 'EcoVeg Bristol', '+44 117 456 7890', 'support@ecovegbristol.co.uk', '17:00', '2.0', '55', '5'),
('V004', 'Harvest Leeds', '+44 113 234 5678', 'hello@harvestleeds.co.uk', '19:00', '1.6', '45', '3'),
('V005', 'UrbanFarm Glasgow', '+44 141 998 1122', 'contact@urbanfarmglasgow.co.uk','17:00', '1.7', '50', '4'),
('V006', 'Local Fields Liverpool', '+44 151 223 4455', 'info@localfieldsliv.co.uk', '17:45',  '1.9', '52', '5'),
('V007', 'The Veg Shed', '+44 20 8123 4567', 'orders@thevegshed.co.uk', '18:15',  '1.4', '48', '3'),
('V008', 'Growers Guild Birmingham', '+44 121 334 7788', 'contact@growersguildbham.co.uk', '19:00',  '1.6', '55', '4'),
('V009', 'NatureBox Nottingham', '+44 115 998 7766', 'hello@natureboxnotts.co.uk', '18:45',  '1.8', '60', '5'),
('V010', 'PlantDrop Edinburgh', '+44 131 456 7890', 'orders@plantdropedin.co.uk', '17:30', '2.1', '58', '4');
