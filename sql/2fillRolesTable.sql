INSERT INTO roles(value, description) 
				values ('DIRECTOR', 'Can create (register / unregister) an administrator'),
						('CLIENT', 'Can register yourself, shange its own registration ddata, 
										make order on repair its gadjet, pay the order from its count, leave a review'),
						('ADMINISTRATOR', 'Can create (register / unregister) managers and masters'),
						('MANAGER', 'Can change client count, determine the cost of work,
											appoint a master for carying out an order, change oarder status to "WHAIT_FOR_PAY", "PAYED" and "CANCELED"'),
						('MASTER', 'Can choose an order, change order status to "WORKING" and "MADE"');