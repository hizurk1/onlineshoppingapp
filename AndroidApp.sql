USE "db-android-app"

DROP TABLE like_list
DROP TABLE product_review
DROP TABLE product_img
DROP TABLE seller_review
DROP TABLE seller_address
DROP TABLE user_address
DROP TABLE order_detail
DROP TABLE category
DROP TABLE product
DROP TABLE user_order
DROP TABLE account

CREATE TABLE account (
	username VARCHAR(255) NOT NULL PRIMARY KEY,
	password VARCHAR(255) NOT NULL,
	account_type VARCHAR(50) NOT NULL,
	name VARCHAR(255) NOT NULL,
	date_of_birth DATE NOT NULL,
	phone_number VARCHAR(15) NOT NULL,
	status BIT
)

CREATE TABLE product (
	product_id INT identity(1,1) NOT NULL PRIMARY KEY,
	seller VARCHAR(255) NOT NULL,
	product_description VARCHAR(MAX),
	price INT,
	--Use trigger--
	rate DECIMAL(18,1),
	like_number INT,
	quantity_sold INT,
	----------------
	FOREIGN KEY (seller) REFERENCES account(username)
)

CREATE TABLE like_list (
	username VARCHAR(255) NOT NULL,
	product_id INT NOT NULL,
	PRIMARY KEY (username, product_id),
	FOREIGN KEY (product_id) REFERENCES product(product_id),
	FOREIGN KEY (username) REFERENCES account(username)
)

CREATE TABLE product_review (
	username VARCHAR(255) NOT NULL,
	product_id INT NOT NULL,
	star INT NOT NULL,
	content VARCHAR(MAX),
	FOREIGN KEY (product_id) REFERENCES product(product_id),
	FOREIGN KEY (username) REFERENCES account(username)
)

CREATE TABLE seller_review (
	username VARCHAR(255) NOT NULL,
	seller VARCHAR(255) NOT NULL,
	star INT NOT NULL,
	content VARCHAR(MAX),
	FOREIGN KEY (seller) REFERENCES account(username),
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
	category_name VARCHAR(255) NOT NULL,
	category_img_url VARCHAR(255)
)

CREATE TABLE product_img (
	product_id INT NOT NULL,
	product_img_url VARCHAR(255),
	PRIMARY KEY (product_id,product_img_url),
	FOREIGN KEY (product_id) REFERENCES product(product_id)
)

CREATE TABLE user_address (
	username VARCHAR(255) NOT NULL,
	user_address VARCHAR(255) NOT NULL,
	PRIMARY KEY (username, user_address),
	FOREIGN KEY (username) REFERENCES account(username)
)

CREATE TABLE seller_address (
	seller VARCHAR(255) NOT NULL,
	user_address VARCHAR(255) NOT NULL,
	PRIMARY KEY (seller, user_address),
	FOREIGN KEY (seller) REFERENCES account(username)
)


