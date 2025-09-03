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

INSERT INTO Customer (
    accountNumber, status, firstName, lastName, phoneNumber,
    password, email, role,
    address, country, postal_code, county
) VALUES
(
    'ACC12345', 'Active', 'John', 'Doe', '1234567890',
    'hashedpassword1', 'john.doe@example.com', 'USER',
    '123 Main Street', 'USA', '90210', 'Los Angeles'
),
(
    'ACC23456', 'Inactive', 'Jane', 'Smith', '9876543210',
    'hashedpassword2', 'jane.smith@example.com', 'ADMIN',
    '456 Queen St', 'Canada', 'M5V2B6', 'Toronto'
),
(
    'ACC34567', 'Active', 'Alice', 'Brown', '5556667777',
    'hashedpassword3', 'alice.brown@example.com', 'USER',
    '789 Oxford Rd', 'UK', 'OX1 2JD', 'Oxford'
);

