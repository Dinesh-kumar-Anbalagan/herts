CREATE TABLE if not exists shipment (
    shipment_id VARCHAR(50) PRIMARY KEY,
    shipment_date VARCHAR(50),
    customerId VARCHAR(50),
    trackingId VARCHAR(50),
    pickupNumber VARCHAR(50),

    -- Sender Address
    sender_address VARCHAR(255),
    sender_country VARCHAR(100),
    sender_locality VARCHAR(100),
    sender_postal_code VARCHAR(20),
    sender_county VARCHAR(100),

    -- Receiver Address
    receiver_address VARCHAR(255),
    receiver_country VARCHAR(100),
    receiver_locality VARCHAR(100),
    receiver_postal_code VARCHAR(20),
    receiver_county VARCHAR(100),

    -- Handling Unit
    height VARCHAR(10),
    length VARCHAR(10),
    weight VARCHAR(10),
    stackable VARCHAR(10),
    type VARCHAR(50),
    units INT,
    width VARCHAR(10),
    size VARCHAR(50)
);
