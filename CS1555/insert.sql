/* GROUP 16 INSERT.SQL*/

--inserting everything
--ALTER SESSION SET nls_date_format= 'mm/dd/yyyy hh24:mi:ss';
--INSERT INTO ourSysDATE VALUES (to_date('01.01/2000 00:00:00' ,'mm/dd/yyyy hh24:mi:ss'));
INSERT INTO ourSysDATE VALUES(sysdate);
INSERT INTO Customer VALUES ('tammy4life', 'starwars', 'Tammy Jones', '420 D St, Bham AL 21', 'taminator@yahoo.com');
INSERT INTO Customer VALUES ('juice26', 'playhard', 'LeVeon Bell', '8 B Ln, Miami FL 1', 'juice@nfl.com');
INSERT INTO Customer VALUES ('prucker_', '12345', 'Jean Prucker', '22 C Rd, Pburgh PA 5', 'jprucker@aol.com');

INSERT INTO Administrator VALUES ('admin', 'root', 'Barry McCockner ', '1 O St, Chicago IL 6', 'barry@gmail.com');

INSERT INTO Product VALUES (1, 'Ball', 'a baseball', 'tammy4life', getCurDate, 50, 1, 'under auction', null, initSellDate(1), null);
INSERT INTO Product VALUES (2, 'Book', null, 'tammy4life', getCurDate, 12, 5, 'under auction', null, initSellDate(5), null);
INSERT INTO Product VALUES (3, 'Bat', 'a baseball bat', 'tammy4life', getCurDate, 5, 7, 'under auction', null, initSellDate(7), null);
INSERT INTO Product VALUES (4, 'Broom', 'slightly swept', 'prucker_', getCurDate, 10, 9, 'under auction', null, initSellDate(9), null);
INSERT INTO Product VALUES (5, 'Jet Ski', null, 'juice26', getCurDate, 17000, 20, 'under auction', null, initSellDate(20), null);

INSERT INTO Bidlog VALUES (1, 1, 'juice26', getcurdate, 50);
INSERT INTO Bidlog VALUES (2, 1, 'prucker_', getcurdate, 55);
INSERT INTO Bidlog VALUES (3, 2, 'juice26', getcurdate, 12);
INSERT INTO Bidlog VALUES (4, 2, 'prucker_', getcurdate, 13);
INSERT INTO Bidlog VALUES (5, 2, 'juice26', getcurdate, 19);
INSERT INTO Bidlog VALUES (6, 2, 'prucker_', getcurdate, 7);
INSERT INTO Bidlog VALUES (7, 3, 'tammy4life', getcurdate, 20);
INSERT INTO Bidlog VALUES (8, 4, 'juice26', getcurdate, 10);
INSERT INTO Bidlog VALUES (9, 4, 'tammy4life', getcurdate, 20);
INSERT INTO Bidlog VALUES (10, 4, 'juice26', getcurdate, 20);


INSERT INTO Category VALUES ('Clothes', null);
INSERT INTO Category VALUES ('Pants','Clothes');
INSERT INTO Category VALUES ('Shirts','Clothes');
INSERT INTO Category VALUES ('Socks','Clothes');
INSERT INTO Category VALUES ('Underwear','Clothes');

INSERT INTO Category VALUES ('Electronics', null);
INSERT INTO Category VALUES ('Phones','Electronics');
INSERT INTO Category VALUES ('Computers','Electronics');
INSERT INTO Category VALUES ('Stereos','Electronics');
INSERT INTO Category VALUES ('Radios','Electronics');

INSERT INTO Category VALUES ('Automotive', null);
INSERT INTO Category VALUES ('Electric','Automotive');
INSERT INTO Category VALUES ('Engine','Automotive');
INSERT INTO Category VALUES ('Body','Automotive');
INSERT INTO Category VALUES ('Accesories','Automotive');

INSERT INTO Category VALUES ('Household', null);
INSERT INTO Category VALUES ('Cleaning', 'Household');
INSERT INTO Category VALUES ('Tools', 'Household');
INSERT INTO Category VALUES ('Furniture', 'Household');
INSERT INTO Category VALUES ('Decorations', 'Household');

INSERT INTO Category VALUES ('Misc', null);


INSERT INTO BelongsTo VALUES (1, 'Misc');
INSERT INTO BelongsTo VALUES (2, 'Misc');
INSERT INTO BelongsTo VALUES (3, 'Misc');
INSERT INTO BelongsTo VALUES (4, 'Cleaning');

commit;

--TEST CONSTRAINTS
INSERT INTO Customer VALUES ('prucker_', '12345', 'Jean Prucker', '22 C Rd, Pburgh PA 5', 'jprucker@aol.com');
INSERT INTO Administrator VALUES ('admin', 'root', 'Barry McCockner ', '1 O St, Chicago IL 6', 'barry@gmail.com');
INSERT INTO Product VALUES (4, 'Broom', 'slightly swept', 'prucker44', getcurdate, 10, 9, 'close', 'tammy4life', sysdate, 20);
INSERT INTO Product VALUES (5, 'Jets Ski', null, 'juice26', getcurdate, 17000, 20, 'under auction', 'gg', sysdate, null);
INSERT INTO Bidlog VALUES (11, 8, 'juice26', getcurdate, 20);
INSERT INTO Bidlog VALUES (11, 8, 'juice265', getcurdate, 20);
INSERT INTO Category VALUES ('Equipment', 'Electronics');
INSERT INTO BelongsTo VALUES (1, 'gardening');
INSERT INTO BelongsTo VALUES (8, 'Equipment');


CALL proc_putProduct('juice26', 'turtle', 'Misc,Tree,Household,APE,ANIMAL', 5, 'SUPER SLOW', 10);

--select * from Product;

