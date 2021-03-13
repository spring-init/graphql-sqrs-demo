create table USER
(
    ID int auto_increment,
    NAME varchar2(128) not null,
    constraint USER_PK
        primary key (ID)
);

comment on table USER is 'Tabla usuarios'

