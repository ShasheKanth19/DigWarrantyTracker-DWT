-- Create renewal_training table for KNN machine learning
CREATE TABLE IF NOT EXISTS renewal_training (
  id INT AUTO_INCREMENT PRIMARY KEY,
  category VARCHAR(64),
  price DOUBLE,
  days_to_expiry INT,
  previous_renewals INT,
  extended TINYINT,       -- 0 or 1
  renewed TINYINT         -- target: 0 or 1
);

-- Seed sample training data
INSERT INTO renewal_training (category, price, days_to_expiry, previous_renewals, extended, renewed) VALUES
('electronics',12000,30,1,0,1),
('appliance',5000,10,0,0,0),
('electronics',25000,120,2,1,1),
('electronics',8000,15,0,0,0),
('appliance',15000,60,1,1,1),
('electronics',30000,90,3,1,1),
('appliance',3000,5,0,0,0),
('electronics',18000,45,1,0,1),
('appliance',20000,100,2,1,1),
('electronics',10000,20,1,0,0),
('electronics',22000,75,2,1,1),
('appliance',7000,25,0,0,0),
('electronics',16000,50,1,1,1),
('appliance',12000,40,1,0,1),
('electronics',9000,18,0,0,0);
