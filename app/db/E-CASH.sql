create database cash
    with owner postgres;

create sequence public.taux_idtaux_seq
    as integer;

alter sequence public.taux_idtaux_seq owner to postgres;

create sequence public.frais_idfrais_seq
    as integer;

alter sequence public.frais_idfrais_seq owner to postgres;

create table public.client
(
    numtel varchar(20) not null
        primary key,
    nom    varchar(50),
    sexe   varchar(10),
    pays   varchar(50),
    solde  numeric     not null,
    mail   varchar(100)
);

alter table public.client
    owner to postgres;

create table public.taux
(
    idtaux   varchar(100) default nextval('taux_idtaux_seq'::regclass) not null
        primary key,
    montant1 numeric                                                   not null,
    montant2 numeric                                                   not null
);

alter table public.taux
    owner to postgres;

alter sequence public.taux_idtaux_seq owned by public.taux.idtaux;

create table public.frais
(
    idfrais  varchar(50) default nextval('frais_idfrais_seq'::regclass) not null
        primary key,
    montant1 numeric                                                    not null,
    montant2 numeric                                                    not null,
    frais    numeric                                                    not null
);

alter table public.frais
    owner to postgres;

alter sequence public.frais_idfrais_seq owned by public.frais.idfrais;

create table public.envoyer
(
    idenv        serial
        primary key,
    numenvoyeur  varchar(20)
        references public.client
            on update cascade on delete cascade,
    numrecepteur varchar(20)
        references public.client
            on update cascade on delete cascade,
    montant      numeric not null,
    date         timestamp,
    raison       varchar(255)
);

alter table public.envoyer
    owner to postgres;

