INSERT INTO CATEGORY (ID, DESCRIPTION) VALUES (1000, 'Comic Books');  
INSERT INTO CATEGORY (ID, DESCRIPTION) VALUES (1001, 'Novies');  
INSERT INTO CATEGORY (ID, DESCRIPTION) VALUES (1002, 'Books'); 

INSERT INTO SUPPLIER (ID, NAME) VALUES (1000, 'Panini Comics'); 
INSERT INTO SUPPLIER (ID, NAME) VALUES (1002, 'Amazon'); 

INSERT INTO PRODUCT (ID, NAME, FK_CATEGORY, FK_SUPPLIER, QUANTITY_AVAILABLE, CREATED_AT) VALUES (1001, 'Crise nas infinitas terras', 1000, 1000, 10, CURRENT_TIMESTAMP); 

INSERT INTO PRODUCT (ID, NAME, FK_CATEGORY, FK_SUPPLIER, QUANTITY_AVAILABLE, CREATED_AT) VALUES (1002, 'Interestelar', 1000, 1002, 5, CURRENT_TIMESTAMP); 

INSERT INTO PRODUCT (ID, NAME, FK_CATEGORY, FK_SUPPLIER, QUANTITY_AVAILABLE, CREATED_AT) VALUES (1003, 'Harry Potter e a Pedra Filosofal', 1002, 1002, 3, CURRENT_TIMESTAMP); 