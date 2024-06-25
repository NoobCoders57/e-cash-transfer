CREATE TABLE CLIENT (
    numtel VARCHAR(20) PRIMARY KEY,
    nom VARCHAR(50),
    sexe VARCHAR(10),
    pays VARCHAR(50),
    solde INT,
    mail VARCHAR(100)
);

CREATE TABLE TAUX (
    idtaux SERIAL PRIMARY KEY,
    montant1 INT,
    montant2 INT
);

CREATE TABLE FRAIS (
    idfrais SERIAL PRIMARY KEY,
    montant1 INT,
    montant2 INT,
    frais INT
);

CREATE TABLE ENVOYER (
    idEnv SERIAL PRIMARY KEY,
    numEnvoyeur VARCHAR(20) REFERENCES CLIENT(numtel),
    numRecepteur VARCHAR(20) REFERENCES CLIENT(numtel),
    montant INT,
    date TIMESTAMP,
    raison VARCHAR(255)
);
