USE AndroidApp
CREATE TABLE account (
	username VARCHAR(255) NOT NULL PRIMARY KEY,
	password VARCHAR(255) NOT NULL,
	account_type VARCHAR(50) NOT NULL,
	name VARCHAR(255) NOT NULL,
	date_of_brith DATE NOT NULL,
	phone_number VARCHAR(15) NOT NULL
)

CREATE TABLE product (
	product_id int identity(1,1) NOT NULL PRIMARY KEY,
	seller VARCHAR(255) NOT NULL,
	product_description VARCHAR(MAX),

	--Use trigger--
	rate DECIMAL(18,1),
	like_number INT,
	quantity_sold INT,
	----------------
	FOREIGN KEY (seller) REFERENCES account(username)
)

CREATE TABLE like_list (
	username VARCHAR(255) NOT NULL,
	product_id int NOT NULL,
	PRIMARY KEY (username, product_id),
	FOREIGN KEY (product_id) REFERENCES product(product_id),
	FOREIGN KEY (username) REFERENCES account(username)
)

CREATE TABLE review (
	username VARCHAR(255) NOT NULL,
	product_id int NOT NULL,
	star int NOT NULL,
	content VARCHAR(MAX),
	FOREIGN KEY (product_id) REFERENCES product(product_id),
	FOREIGN KEY (username) REFERENCES account(username)
)

CREATE TABLE user_order (
	order_id INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
	username VARCHAR(255) NOT NULL,
	order_time DATETIME NOT NULL,
	total INT NOT NULL,
	order_code VARCHAR(50),
	order_status VARCHAR(100),
	FOREIGN KEY (username) REFERENCES account(username)
)

CREATE TABLE order_detail (
	order_id INT NOT NULL,
	product_id INT NOT NULL,
	quatity INT NOT NULL,
	PRIMARY KEY (order_id, product_id),
	FOREIGN KEY (order_id) REFERENCES user_order(order_id),
	FOREIGN KEY (product_id) REFERENCES product(product_id)
)

CREATE TABLE category (
	category_id INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
	category_name VARCHAR(255) NOT NULL
)