DROP TABLE IF EXISTS listings_tags;
DROP TABLE IF EXISTS listings_favorite;
DROP TABLE IF EXISTS listings_images;
DROP TABLE IF EXISTS ordered_listings;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS listings_options;
DROP TABLE IF EXISTS user_cart;
DROP TABLE IF EXISTS listings;
DROP TABLE IF EXISTS sections;

CREATE TABLE IF NOT EXISTS users 
(
    id         int  AUTO_INCREMENT NOT NULL,
    chat_id    varchar (20)     UNIQUE  NOT NULL,
    name       varchar (100)    NOT NULL,
    is_admin  tinyint(1) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS countries
(
    id         int  AUTO_INCREMENT NOT NULL,
    country_id int  UNIQUE   NOT NULL,
    name       varchar (250) NOT NULL,
    ru_name    varchar (250) NOT NULL,
    code       int NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_info 
(
    id          int  AUTO_INCREMENT NOT NULL,
    user_id     int,
    full_name   varchar (250),
    country_id  int,
    town        varchar (250),
    address     varchar (250),
    post_index  varchar (250),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (country_id) REFERENCES countries (id)
);

CREATE TABLE IF NOT EXISTS tags
(
    id              int  AUTO_INCREMENT NOT NULL,
    tag_name        varchar (50) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE sections
(
    id              int  AUTO_INCREMENT NOT NULL,
    name            varchar (100) NOT NULL,
    section_identifier int UNIQUE NOT NULL,
    updated  timestamp NOT NULL,
    active  tinyint(1) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE listings
(
    id          int  AUTO_INCREMENT NOT NULL,
    title       varchar (250)       NOT NULL,
    price       int  NOT NULL,
    section_id  int,
    listing_identifier int UNIQUE NOT NULL,
    sku_number varchar(50) NOT NULL,
    updated  timestamp NOT NULL,
    active  tinyint(1) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (section_id) REFERENCES sections (id)
);

CREATE TABLE listings_images
(
    id             int  AUTO_INCREMENT NOT NULL,
    listing_id     int,
    image_url      varchar (250),
    updated timestamp NOT NULL,
    active  tinyint(1) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (listing_id) REFERENCES listings (id)
);

CREATE TABLE listings_options
(
    id             int  AUTO_INCREMENT NOT NULL,
    listing_id     int,
    option1_name      varchar (150),
    option1_value     varchar (150),
    option2_name      varchar (150),
    option2_value     varchar (150),
    price int,
    updated timestamp NOT NULL,
    active  tinyint(1) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (listing_id) REFERENCES listings (id)
);

CREATE TABLE orders
(
    id          int  AUTO_INCREMENT NOT NULL IDENTITY(545,1),
    user_id     int,
    status      varchar (20),
    created     timestamp NOT NULL,
    summary     int,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE ordered_listings
(
    id           int  AUTO_INCREMENT NOT NULL,
    order_id     int,
    listing_id   int,
    option_id    int,
    quantity     int,
    PRIMARY KEY (id),
    FOREIGN KEY (order_id) REFERENCES orders (id),
    FOREIGN KEY (listing_id) REFERENCES listings (id),
    FOREIGN KEY (option_id) REFERENCES listings_options (id)
);

CREATE TABLE listings_favorite
(
    id           int  AUTO_INCREMENT NOT NULL,
    user_id      int,
    listing_id   int,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (listing_id) REFERENCES listings (id)
);

CREATE TABLE listings_tags
(
    id             int  AUTO_INCREMENT NOT NULL,
    listing_id     int,
    tag_id         int,
    updated  timestamp NOT NULL,
    active  tinyint(1) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (listing_id) REFERENCES listings (id),
    FOREIGN KEY (tag_id) REFERENCES tags (id)
);

CREATE TABLE user_cart
(
    id           int  AUTO_INCREMENT NOT NULL,
    user_id      int,
    listing_id   int,
    option_id    int,
    quantity     int,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (listing_id) REFERENCES listings (id),
    FOREIGN KEY (option_id) REFERENCES listings_options (id)
);