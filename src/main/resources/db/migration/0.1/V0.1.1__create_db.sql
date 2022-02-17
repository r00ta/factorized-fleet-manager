create table DINOSAUR
(
    id                 varchar(255) NOT NULL PRIMARY KEY,
    customer_id        varchar(255) NOT NULL,
    name               varchar(255) NOT NULL,
    submitted_at       timestamp    NOT NULL,
    published_at       timestamp,
    status             varchar(255),
    unique (customer_id, name)
);

create table SHARD
(
    id      varchar(255) NOT NULL PRIMARY KEY,
);

INSERT INTO SHARD(id) VALUES ('${shard-id}');
