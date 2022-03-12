DELIMITER $$

CREATE TRIGGER log_assignee_updates
AFTER UPDATE
ON Kuber_Beverages.Orders FOR EACH ROW
BEGIN
    IF OLD.Assigned_Status <> new.Assigned_Status THEN
        INSERT INTO Assignee_History(Order_Id,Bill_No, Assigned_Status, Assignee_Name, Assigned_Updated_Date)
        VALUES(old.Order_Id, old.Bill_No, new.Assigned_Status, new.Assignee_Name, now());
    END IF;
END$$

DELIMITER ;