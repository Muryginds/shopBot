DROP TABLE IF EXISTS listings_tags;
DROP TABLE IF EXISTS listings_favorite;
DROP TABLE IF EXISTS listings_images;
DROP TABLE IF EXISTS ordered_listings;
DROP TABLE IF EXISTS user_messages;
DROP TABLE IF EXISTS user_chat_activity;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS user_cart;
DROP TABLE IF EXISTS listings_options;
DROP TABLE IF EXISTS listings;
DROP TABLE IF EXISTS sections;

CREATE TABLE IF NOT EXISTS users
(
    id         integer  GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    chat_id    varchar (20)     UNIQUE  NOT NULL,
    name       varchar (100)    NOT NULL,
    bot_state  varchar (50)     NOT NULL,
    is_moderator boolean NOT NULL,
    is_admin  boolean NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS countries_id_seq;
CREATE TABLE IF NOT EXISTS countries
(
    id         integer  NOT NULL DEFAULT nextval('countries_id_seq') PRIMARY KEY,
    country_id integer   UNIQUE   NOT NULL,
    name       varchar (250) NOT NULL,
    ru_name    varchar (250) NOT NULL,
    code       integer  NOT NULL
);
ALTER SEQUENCE countries_id_seq OWNED BY countries.id INCREMENT BY 3;

CREATE TABLE IF NOT EXISTS user_info
(
    id          integer  GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id     integer  NOT NULL,
    full_name   varchar (250),
    country_id  integer  NOT NULL,
    town        varchar (250),
    address     varchar (250),
    post_index  varchar (250),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (country_id) REFERENCES countries (id)
);

CREATE TABLE IF NOT EXISTS tags
(
    id              integer  GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    tag_name        varchar (50) NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS sections_id_seq;
CREATE TABLE sections
(
    id              integer  NOT NULL DEFAULT nextval('sections_id_seq') PRIMARY KEY,
    name            varchar (100) NOT NULL,
    section_identifier integer  UNIQUE NOT NULL,
    updated  timestamp NOT NULL,
    active  boolean NOT NULL
);
ALTER SEQUENCE sections_id_seq OWNED BY sections.id INCREMENT BY 3;

CREATE SEQUENCE IF NOT EXISTS listings_id_seq;
CREATE TABLE listings
(
    id          integer  NOT NULL DEFAULT nextval('listings_id_seq') PRIMARY KEY,
    title       varchar (250)       NOT NULL,
    price       integer   NOT NULL,
    section_id  integer  NOT NULL,
    listing_identifier integer  UNIQUE NOT NULL,
    sku_number varchar(50) NOT NULL,
    updated  timestamp NOT NULL,
    active  boolean NOT NULL,
    FOREIGN KEY (section_id) REFERENCES sections (id)
);
ALTER SEQUENCE listings_id_seq OWNED BY listings.id INCREMENT BY 3;

CREATE SEQUENCE IF NOT EXISTS images_id_seq;
CREATE TABLE listings_images
(
    id             integer  NOT NULL DEFAULT nextval('images_id_seq') PRIMARY KEY,
    listing_id     integer  NOT NULL,
    image_url      varchar (250) NOT NULL,
    updated timestamp NOT NULL,
    active  boolean NOT NULL,
    FOREIGN KEY (listing_id) REFERENCES listings (id)
);
ALTER SEQUENCE images_id_seq OWNED BY listings_images.id INCREMENT BY 3;

CREATE SEQUENCE IF NOT EXISTS options_id_seq;
CREATE TABLE listings_options
(
    id             integer  NOT NULL DEFAULT nextval('options_id_seq') PRIMARY KEY,
    listing_id     integer  NOT NULL,
    option1_name      varchar (150),
    option1_value     varchar (150),
    option2_name      varchar (150),
    option2_value     varchar (150),
    price 	integer NOT NULL,
    updated timestamp NOT NULL,
    active  boolean NOT NULL,
    FOREIGN KEY (listing_id) REFERENCES listings (id)
);
ALTER SEQUENCE options_id_seq OWNED BY listings_options.id INCREMENT BY 3;

CREATE TABLE orders
(
    id          integer  GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id     integer  NOT NULL,
    status      varchar (20),
    created     timestamp NOT NULL,
    summary     integer,
    track_number varchar (20),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE ordered_listings
(
    id           integer  GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    order_id     integer,
    listing_id   integer,
    option_id    integer,
    quantity     integer,
    FOREIGN KEY (order_id) REFERENCES orders (id),
    FOREIGN KEY (listing_id) REFERENCES listings (id),
    FOREIGN KEY (option_id) REFERENCES listings_options (id)
);

CREATE TABLE listings_favorite
(
    id           integer  GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id      integer,
    listing_id   integer,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (listing_id) REFERENCES listings (id)
);

CREATE SEQUENCE IF NOT EXISTS listing_tags_id_seq;
CREATE TABLE listings_tags
(
    id             integer  NOT NULL DEFAULT nextval('listing_tags_id_seq') PRIMARY KEY,
    listing_id     integer  NOT NULL,
    tag_id         integer  NOT NULL,
    updated  timestamp NOT NULL,
    active  boolean NOT NULL,
    FOREIGN KEY (listing_id) REFERENCES listings (id),
    FOREIGN KEY (tag_id) REFERENCES tags (id)
);
ALTER SEQUENCE listing_tags_id_seq OWNED BY listings_tags.id INCREMENT BY 3;

CREATE TABLE user_cart
(
    id           integer  GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id      integer,
    listing_id   integer,
    option_id    integer,
    quantity     integer,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (listing_id) REFERENCES listings (id),
    FOREIGN KEY (option_id) REFERENCES listings_options (id)
);

CREATE TABLE user_messages
(
    id         		integer  GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id    		integer  NOT NULL,
    order_id   		integer  NOT NULL,
    message    		text NOT NULL,
    created    		timestamp NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (order_id) REFERENCES orders (id)
);

CREATE TABLE user_chat_activity
(
    id         		integer  GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id    		integer,
    order_id   		integer,
    last_activity   timestamp NOT NULL,
    announced  		timestamp,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (order_id) REFERENCES orders (id)
);