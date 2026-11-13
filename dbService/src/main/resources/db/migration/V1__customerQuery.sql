CREATE TABLE IF NOT EXISTS Customer (
    customerID VARCHAR(50),
    accountNumber VARCHAR(50) NOT NULL,
    status VARCHAR(20),
    firstName VARCHAR(100),
    lastName VARCHAR(100),
    phoneNumber VARCHAR(20),
    password VARCHAR(255),
    email VARCHAR(150),
    role VARCHAR(50),

    -- Address Fields
    address VARCHAR(255),
    country VARCHAR(100),
    postal_code VARCHAR(20),
    county VARCHAR(100)
);


