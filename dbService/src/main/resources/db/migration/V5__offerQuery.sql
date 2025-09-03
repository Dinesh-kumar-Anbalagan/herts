CREATE TABLE if not exists offers (
    customer_id         VARCHAR(50),
    shipment_id         VARCHAR(50),
    offer_id            VARCHAR(50) PRIMARY KEY,
    offer_date          VARCHAR(50),
    expiration_date     VARCHAR(50),
    vendor_id           VARCHAR(50),
    vendor_name         VARCHAR(100),
    vendor_email        VARCHAR(100),
    offer_cost          VARCHAR(100),
    has_insurance       BOOLEAN,
    status              VARCHAR(50),
    estimate_delivery   VARCHAR(100)
);