--Assignee_History
CREATE TABLE `Assignee_History` (
                                    `Order_Id` bigint DEFAULT NULL,
                                    `Bill_No` varchar(45) DEFAULT NULL,
                                    `Assigned_Status` tinyint DEFAULT NULL,
                                    `Assignee_Name` varchar(45) DEFAULT NULL,
                                    `Updated_Date` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--Order Details
CREATE TABLE `Order_Details` (
                                 `Order_Details_Id` bigint NOT NULL AUTO_INCREMENT,
                                 `Order_Id` bigint NOT NULL,
                                 `Product_Id` int NOT NULL,
                                 `Quantity` bigint DEFAULT NULL,
                                 `Rate` int NOT NULL,
                                 `Amount` int NOT NULL,
                                 PRIMARY KEY (`Order_Details_Id`),
                                 KEY `order_details_product_idx` (`Product_Id`),
                                 KEY `order_details_order_id` (`Order_Id`),
                                 CONSTRAINT `order_details_order_id` FOREIGN KEY (`Order_Id`) REFERENCES `Orders` (`Order_Id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                 CONSTRAINT `order_details_product` FOREIGN KEY (`Product_Id`) REFERENCES `Product` (`Product_Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--Orders

CREATE TABLE `Orders` (
                          `Order_Id` bigint NOT NULL AUTO_INCREMENT,
                          `Customer_Name` varchar(100) NOT NULL,
                          `Date` datetime DEFAULT NULL,
                          `TotalAmount` bigint NOT NULL,
                          `Bill_No` varchar(45) NOT NULL,
                          `Assigned_Status` tinyint NOT NULL DEFAULT '0',
                          `Assignee_Name` varchar(45) DEFAULT NULL,
                          `Updated_Date` datetime DEFAULT NULL,
                          PRIMARY KEY (`Order_Id`),
                          UNIQUE KEY `Bill_No` (`Bill_No`)
) ENGINE=InnoDB AUTO_INCREMENT=159 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--Payment_History
CREATE TABLE `Payment_History` (
  `Payment_Id` bigint NOT NULL AUTO_INCREMENT,
  `Order_Id` bigint DEFAULT NULL,
  `Received_Payment` bigint DEFAULT NULL,
  `Received_Date` datetime DEFAULT NULL,
  `Payment_Mode` varchar(45) DEFAULT NULL,
  `Receiver_Name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Payment_Id`),
  KEY `Payment_Order_id_idx` (`Order_Id`),
  CONSTRAINT `Payment_Order_id` FOREIGN KEY (`Order_Id`) REFERENCES `Orders` (`Order_Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--Product

CREATE TABLE `Product` (
                           `Product_Id` int NOT NULL AUTO_INCREMENT,
                           `Product_Type` varchar(255) NOT NULL,
                           PRIMARY KEY (`Product_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--Raw_Material

CREATE TABLE `Raw_Material` (
                                `Raw_Material_Id` int NOT NULL AUTO_INCREMENT,
                                `Raw_Material_Name` varchar(255) NOT NULL,
                                PRIMARY KEY (`Raw_Material_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--Raw_Material_Purchase
CREATE TABLE `Raw_Material_Purchase` (
  `Raw_Material_Purchase_Id` bigint NOT NULL AUTO_INCREMENT,
  `Raw_Material_Id` int DEFAULT NULL,
  `Product_Id` int DEFAULT NULL,
  `Quantity` bigint NOT NULL,
  `Status` tinyint NOT NULL,
  `Date` datetime DEFAULT NULL,
  PRIMARY KEY (`Raw_Material_Purchase_Id`),
  KEY `Purchase_Product_idx` (`Product_Id`),
  KEY `Purchase_Raw_Material_idx` (`Raw_Material_Id`),
  CONSTRAINT `Purchase_Product` FOREIGN KEY (`Product_Id`) REFERENCES `Product` (`Product_Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Purchase_Raw_Material` FOREIGN KEY (`Raw_Material_Id`) REFERENCES `Raw_Material` (`Raw_Material_Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--Inventory
CREATE TABLE `Inventory` (
  `Inventory_Id` bigint NOT NULL AUTO_INCREMENT,
  `Product_Id` int DEFAULT NULL,
  `Quantity` bigint NOT NULL,
  `Date` datetime DEFAULT NULL,
  PRIMARY KEY (`Inventory_Id`),
  KEY `Product_Id_idx` (`Product_Id`),
  CONSTRAINT `Inventory_Product` FOREIGN KEY (`Product_Id`) REFERENCES `Product` (`Product_Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;