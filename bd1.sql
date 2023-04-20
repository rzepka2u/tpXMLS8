CREATE TABLE Acteur (
idActeur int(3)  ,
nom VARCHAR (30) ,
prenom VARCHAR (30) ,
datenaiss date,
CONSTRAINT PKActeur PRIMARY KEY (idActeur));

CREATE TABLE Internaute (
email varchar(100)  ,
nomInt VARCHAR (30) ,
prenomInt VARCHAR (30) ,
paysInt VARCHAR (30),
CONSTRAINT PKInternaute PRIMARY KEY (email));

CREATE TABLE Film (
idFilm int(3)  ,
titre VARCHAR (50) ,
datesortie date ,
genre VARCHAR (20) ,
duree int(3) ,
paysfilm VARCHAR (20),
entree int(8),
CONSTRAINT PKFilm PRIMARY KEY (idFilm));

CREATE TABLE Notation (
email VARCHAR (40)  ,
idFilm int(3) ,
note int(2) check (note between 1 and 10),
datenote date,
CONSTRAINT PKNotation PRIMARY KEY (idFilm, email),
FOREIGN KEY (idFilm) REFERENCES Film (idfilm),
FOREIGN KEY (email) REFERENCES Internaute (email));

CREATE TABLE Jouer (
idFilm int(3)  ,
idActeur int(3)  ,
PRIMARY KEY (idActeur,idFilm),
FOREIGN KEY (idFilm) REFERENCES Film (idFilm),
FOREIGN KEY (idActeur) REFERENCES Acteur (idActeur));


insert into film values(1,'Edward aux mains d''argent',STR_TO_DATE('10/04/1991', '%d/%m/%Y'),'fantastique',105,'USA', 545338);
insert into film values(2,'Alice aux pays des merveilles',STR_TO_DATE('24/03/2010', '%d/%m/%Y'),'fantastique',109,'USA', 34333);
insert into film values(3,'Charlie et la chocolaterie',STR_TO_DATE('13/07/2005', '%d/%m/%Y'),'comédie',116,'USA', 54876);
insert into film values(4,'Le parrain',STR_TO_DATE('15/03/1972', '%d/%m/%Y'),'policier',175,'USA', 654332);
insert into film values(5,'Avatar',STR_TO_DATE('16/12/2009', '%d/%m/%Y'),'sciences fiction',162,'USA', 2763445);
insert into film values(6,'La vie des autres',STR_TO_DATE('31/01/2007', '%d/%m/%Y'),'drame',137,'Allemagne',65443);
insert into film values(7,'Inception',STR_TO_DATE('21/07/2010', '%d/%m/%Y'),'sciences fiction',148,'USA',1653877);
insert into film values(8,'Shutter Island',STR_TO_DATE('24/02/2010', '%d/%m/%Y'),'thriller',137,'USA',987655);
insert into film values(9,'La même',STR_TO_DATE('14/02/2007', '%d/%m/%Y'),'drame',140,'France',1345548);
insert into film values(10,'Les petits mouchoirs',STR_TO_DATE('20/10/2010', '%d/%m/%Y'),'comédie dramatique',154,'France',765596);
insert into film values(11,'Des hommes et des dieux',STR_TO_DATE('08/09/2010', '%d/%m/%Y'),'drame',120,'France',633213);
insert into film values(12,'OSS 117 : Rio ne répond plus',STR_TO_DATE('15/04/2009', '%d/%m/%Y'),'comédie',100,'France',531667);
insert into film values(13,'OSS 117: Le Caire nid d''espions',STR_TO_DATE('9/04/2006', '%d/%m/%Y'),'comédie',99,'France',654332);
insert into film values(14,'Jean de Florette',STR_TO_DATE('27/08/1986', '%d/%m/%Y'),'drame',120,'Suisse',345211);
insert into film values(15,'Manon des sources',STR_TO_DATE('19/11/1986', '%d/%m/%Y'),'drame',120,'Italie',444553);
insert into film values(16,'Animaux et Cie',STR_TO_DATE('09/02/2011', '%d/%m/%Y'),'animation',93,'Allemagne',653321);
insert into film values(17,'Le discours d''un Roi',STR_TO_DATE('09/02/2011', '%d/%m/%Y'),'historique',118,'Grande Bretagne',764332);
insert into film values(18,'Le choix de Luna',STR_TO_DATE('09/02/2011', '%d/%m/%Y'),'drame',160,'Autriche',76445);
insert into film values(19,'Une vie de chat',STR_TO_DATE('15/12/2010', '%d/%m/%Y'),'animation',70,'France',123432);
insert into film values(20,'Le dernier des templiers',STR_TO_DATE('12/01/2011', '%d/%m/%Y'),'aventure',95,'USA',53289);
insert into film values(21,'Valérian et la cité des mille planètes',STR_TO_DATE('26/07/2017', '%d/%m/%Y'),'sciences fiction',137,'France',1640680);

insert into acteur values(1,'Montand','Yves',STR_TO_DATE('13/10/1921', '%d/%m/%Y'));
insert into acteur values(2,'Auteuil','Daniel',STR_TO_DATE('24/01/1950', '%d/%m/%Y'));
insert into acteur values(3,'Béart','Emanuelle',STR_TO_DATE('14/08/1965', '%d/%m/%Y'));
insert into acteur values(4,'Depardieu','Gérard',STR_TO_DATE('27/12/1948', '%d/%m/%Y'));
insert into acteur values(5,'Depp','Johnny',STR_TO_DATE('09/06/1963', '%d/%m/%Y'));
insert into acteur values(6,'Ryder','Winona',STR_TO_DATE('29/10/1971', '%d/%m/%Y'));
insert into acteur values(7,'Brando','Marlon',STR_TO_DATE('03/04/1924', '%d/%m/%Y'));
insert into acteur values(8,'Pacino','Al',STR_TO_DATE('25/04/1940', '%d/%m/%Y'));
insert into acteur values(9,'Caan','James',STR_TO_DATE('26/03/1939', '%d/%m/%Y'));
insert into acteur values(10,'Worthington','Sam',STR_TO_DATE('02/08/1976', '%d/%m/%Y'));
insert into acteur values(11,'Saldana','Zoe',STR_TO_DATE('19/06/1978', '%d/%m/%Y'));
insert into acteur values(12,'Thieme','Thomas',STR_TO_DATE('29/10/1948', '%d/%m/%Y'));
insert into acteur values(13,'Gedeck','Martina',STR_TO_DATE('14/09/1961', '%d/%m/%Y'));
insert into acteur values(14,'DiCaprio','Leonardo',STR_TO_DATE('11/11/1974', '%d/%m/%Y'));
insert into acteur values(15,'Cotillard','Marion',STR_TO_DATE('30/09/1975', '%d/%m/%Y'));
insert into acteur values(16,'Cluzet','Francois',STR_TO_DATE('21/09/1955', '%d/%m/%Y'));
insert into acteur values(17,'Wilson','Lambert',STR_TO_DATE('03/08/1958', '%d/%m/%Y'));
insert into acteur values(18,'Lonsdale','Michael',STR_TO_DATE('24/05/1931', '%d/%m/%Y'));
insert into acteur values(19,'Dujardin','Jean',STR_TO_DATE('19/06/1972', '%d/%m/%Y'));
insert into acteur values(20,'Schmitz','Ralf',STR_TO_DATE('03/11/1974', '%d/%m/%Y'));
insert into acteur values(21,'Firth','Colin',STR_TO_DATE('10/09/1960', '%d/%m/%Y'));
insert into acteur values(22,'Cvitesic','Zrinka',STR_TO_DATE('18/07/1979', '%d/%m/%Y'));
insert into acteur values(23,'Blanc','Dominique',STR_TO_DATE('25/04/1956', '%d/%m/%Y'));
insert into acteur values(24,'Salomone','Bruno',STR_TO_DATE('13/07/1970', '%d/%m/%Y'));
insert into acteur values(25,'Cage','Nicolas',STR_TO_DATE('07/01/1964', '%d/%m/%Y'));
insert into acteur values(26,'Weaver','Sigourney',STR_TO_DATE('09/10/1949', '%d/%m/%Y'));
insert into acteur values(27,'DeHaan','Dane', STR_TO_DATE('6/02/1987', '%d/%m/%Y'));
insert into acteur values(28,'Delevingne','Cara',STR_TO_DATE('12/08/1992', '%d/%m/%Y'));

insert into jouer values(15,1);
insert into jouer values(15,2);
insert into jouer values(15,3);
insert into jouer values(14,1);
insert into jouer values(14,2);
insert into jouer values(14,4);
insert into jouer values(1,6);
insert into jouer values(1,5);
insert into jouer values(2,5);
insert into jouer values(3,5);
insert into jouer values(4,7);
insert into jouer values(4,8);
insert into jouer values(4,9);
insert into jouer values(5,10);
insert into jouer values(5,11);
insert into jouer values(6,12);
insert into jouer values(6,13);
insert into jouer values(7,14);
insert into jouer values(7,15);
insert into jouer values(8,14);
insert into jouer values(9,15);
insert into jouer values(9,4);
insert into jouer values(10,15);
insert into jouer values(10,16);
insert into jouer values(11,17);
insert into jouer values(11,18);
insert into jouer values(12,19);
insert into jouer values(13,19);
insert into jouer values(16,20);
insert into jouer values(17,21);
insert into jouer values(18,22);
insert into jouer values(19,23);
insert into jouer values(19,24);
insert into jouer values(20,25);
insert into jouer values(5,26);
insert into jouer values(21,27);
insert into jouer values(21,28);

insert into internaute values ('Isabelle.Pruvat@yahoo.fr','Pruvat','Isabelle','France');
insert into internaute values ('Amandine.Dahan@gmail.fr','Dahan','Amandine','France');
insert into internaute values ('pierre.Berger@sfr.fr','Berger','pierre','France');
insert into internaute values ('Jean.Vanier@yahoo.fr','Vanier','Jean','France');
insert into internaute values ('Paul.Ribo@neuf.fr','Ribo','Paul','Italie');
insert into internaute values ('Julie.Virard@yahoo.fr','Virard','Julie','Belgique');
insert into internaute values ('Lina.Melin@hotmail.fr','Melin','Lina','Italie');
insert into internaute values ('Ines.Fragin@yahoo.fr','Fragin','Ines','Belgique');
insert into internaute values ('Alexandre.Tounez@gmail.fr','Tounez','Alexandre','Espagne');
insert into internaute values ('Emmanuel.Garvez@yahoo.fr','Garvez','Emmanuel','Espagne');
insert into internaute values ('Sandra.Pineto@neuf.fr','Pineto','Sandra','Suisse');
insert into internaute values ('Juliette.Luna@gmail.fr','Luna','Juliette','Italie');
insert into internaute values ('Christina.Malko@yahoo.fr','Malko','Christina','France');
insert into internaute values ('Antoine.Cagelin@hotmail.fr','Cagelin','Antoine','France');
insert into internaute values ('Arthur.Daunot@yahoo.fr','Daunot','Arthur','France');
insert into internaute values ('Vincent.Viard@yahoo.fr','Viard', 'Vincent','France');

insert into notation values ('Isabelle.Pruvat@yahoo.fr',1,6,STR_TO_DATE('24/12/1992', '%d/%m/%Y'));
insert into notation values ('Amandine.Dahan@gmail.fr',1,7,STR_TO_DATE('25/12/1992', '%d/%m/%Y'));
insert into notation values ('pierre.Berger@sfr.fr',1,9,STR_TO_DATE('04/04/1993', '%d/%m/%Y'));
insert into notation values ('Jean.Vanier@yahoo.fr',1,5,STR_TO_DATE('27/12/1992', '%d/%m/%Y'));
insert into notation values ('Paul.Ribo@neuf.fr',2,8,STR_TO_DATE('05/08/2010', '%d/%m/%Y'));
insert into notation values ('Julie.Virard@yahoo.fr',2,8,STR_TO_DATE('06/08/2010', '%d/%m/%Y'));
insert into notation values ('Lina.Melin@hotmail.fr',2,7,STR_TO_DATE('07/08/2010', '%d/%m/%Y'));
insert into notation values ('Ines.Fragin@yahoo.fr',2,8,STR_TO_DATE('08/08/2010', '%d/%m/%Y'));
insert into notation values ('Alexandre.Tounez@gmail.fr',3,10,STR_TO_DATE('15/08/2005', '%d/%m/%Y'));
insert into notation values ('Emmanuel.Garvez@yahoo.fr',3,4,STR_TO_DATE('16/08/2005', '%d/%m/%Y'));
insert into notation values ('Sandra.Pineto@neuf.fr',3,5,STR_TO_DATE('17/08/2005', '%d/%m/%Y'));
insert into notation values ('Juliette.Luna@gmail.fr',3,3,STR_TO_DATE('18/08/2005', '%d/%m/%Y'));
insert into notation values ('Christina.Malko@yahoo.fr',4,2,STR_TO_DATE('17/04/2010', '%d/%m/%Y'));
insert into notation values ('Antoine.Cagelin@hotmail.fr',4,4,STR_TO_DATE('18/04/2010', '%d/%m/%Y'));
insert into notation values ('Arthur.Daunot@yahoo.fr',4,8,STR_TO_DATE('19/04/2010', '%d/%m/%Y'));
insert into notation values ('Isabelle.Pruvat@yahoo.fr',5,8,STR_TO_DATE('24/01/2010', '%d/%m/%Y'));
insert into notation values ('Amandine.Dahan@gmail.fr',5,9,STR_TO_DATE('25/01/2010', '%d/%m/%Y'));
insert into notation values ('pierre.Berger@sfr.fr',5,8,STR_TO_DATE('26/01/2010', '%d/%m/%Y'));
insert into notation values ('Jean.Vanier@yahoo.fr',5,5,STR_TO_DATE('27/01/2010', '%d/%m/%Y'));
insert into notation values ('Paul.Ribo@neuf.fr',5,1,STR_TO_DATE('28/01/2010', '%d/%m/%Y'));
insert into notation values ('Julie.Virard@yahoo.fr',6,7,STR_TO_DATE('26/04/2007', '%d/%m/%Y'));
insert into notation values ('Lina.Melin@hotmail.fr',6,4,STR_TO_DATE('27/04/2007', '%d/%m/%Y'));
insert into notation values ('Ines.Fragin@yahoo.fr',6,2,STR_TO_DATE('28/04/2007', '%d/%m/%Y'));
insert into notation values ('Alexandre.Tounez@gmail.fr',7,8,STR_TO_DATE('28/08/2010', '%d/%m/%Y'));
insert into notation values ('Emmanuel.Garvez@yahoo.fr',7,9,STR_TO_DATE('29/08/2010', '%d/%m/%Y'));
insert into notation values ('Sandra.Pineto@neuf.fr',7,4,STR_TO_DATE('30/08/2010', '%d/%m/%Y'));
insert into notation values ('Juliette.Luna@gmail.fr',8,8,STR_TO_DATE('23/03/2010', '%d/%m/%Y'));
insert into notation values ('Christina.Malko@yahoo.fr',8,6,STR_TO_DATE('24/03/2010', '%d/%m/%Y'));
insert into notation values ('Antoine.Cagelin@hotmail.fr',8,3,STR_TO_DATE('25/03/2010', '%d/%m/%Y'));
insert into notation values ('Arthur.Daunot@yahoo.fr',9,1,STR_TO_DATE('19/02/2007', '%d/%m/%Y'));
insert into notation values ('Isabelle.Pruvat@yahoo.fr',9,2,STR_TO_DATE('20/02/2007', '%d/%m/%Y'));
insert into notation values ('Amandine.Dahan@gmail.fr',9,8,STR_TO_DATE('21/02/2007', '%d/%m/%Y'));
insert into notation values ('pierre.Berger@sfr.fr',10,6,STR_TO_DATE('25/10/2010', '%d/%m/%Y'));
insert into notation values ('Jean.Vanier@yahoo.fr',10,7,STR_TO_DATE('26/10/2010', '%d/%m/%Y'));
insert into notation values ('Paul.Ribo@neuf.fr',10,3,STR_TO_DATE('27/10/2010', '%d/%m/%Y'));
insert into notation values ('Julie.Virard@yahoo.fr',11,2,STR_TO_DATE('15/09/2010', '%d/%m/%Y'));
insert into notation values ('Lina.Melin@hotmail.fr',11,5,STR_TO_DATE('16/09/2010', '%d/%m/%Y'));
insert into notation values ('Ines.Fragin@yahoo.fr',11,7,STR_TO_DATE('17/09/2010', '%d/%m/%Y'));
insert into notation values ('Alexandre.Tounez@gmail.fr',11,4,STR_TO_DATE('18/09/2010', '%d/%m/%Y'));
insert into notation values ('Emmanuel.Garvez@yahoo.fr',12,3,STR_TO_DATE('16/04/2009', '%d/%m/%Y'));
insert into notation values ('Sandra.Pineto@neuf.fr',12,8,STR_TO_DATE('17/04/2009', '%d/%m/%Y'));
insert into notation values ('Juliette.Luna@gmail.fr',13,10,STR_TO_DATE('09/04/2006', '%d/%m/%Y'));
insert into notation values ('Christina.Malko@yahoo.fr',13,10,STR_TO_DATE('10/04/2006', '%d/%m/%Y'));
insert into notation values ('Antoine.Cagelin@hotmail.fr',14,6,STR_TO_DATE('27/08/1986', '%d/%m/%Y'));
insert into notation values ('Arthur.Daunot@yahoo.fr',14,7,STR_TO_DATE('28/08/1986', '%d/%m/%Y'));
insert into notation values ('Isabelle.Pruvat@yahoo.fr',15,6,STR_TO_DATE('19/11/1986', '%d/%m/%Y'));
insert into notation values ('Amandine.Dahan@gmail.fr',15,7,STR_TO_DATE('20/11/1986', '%d/%m/%Y'));
insert into notation values ('pierre.Berger@sfr.fr',15,3,STR_TO_DATE('21/11/1986', '%d/%m/%Y'));
insert into notation values ('Jean.Vanier@yahoo.fr',15,2,STR_TO_DATE('22/11/1986', '%d/%m/%Y'));
insert into notation values ('Paul.Ribo@neuf.fr',16,6,STR_TO_DATE('09/02/2011', '%d/%m/%Y'));
insert into notation values ('Julie.Virard@yahoo.fr',16,3,STR_TO_DATE('10/02/2011', '%d/%m/%Y'));
insert into notation values ('Lina.Melin@hotmail.fr',16,5,STR_TO_DATE('11/02/2011', '%d/%m/%Y'));
insert into notation values ('Ines.Fragin@yahoo.fr',17,4,STR_TO_DATE('09/02/2011', '%d/%m/%Y'));
insert into notation values ('Alexandre.Tounez@gmail.fr',17,7,STR_TO_DATE('10/02/2011', '%d/%m/%Y'));
insert into notation values ('Emmanuel.Garvez@yahoo.fr',18,9,STR_TO_DATE('09/02/2011', '%d/%m/%Y'));
insert into notation values ('Sandra.Pineto@neuf.fr',18,8,STR_TO_DATE('10/02/2011', '%d/%m/%Y'));
insert into notation values ('Juliette.Luna@gmail.fr',19,5,STR_TO_DATE('15/12/2010', '%d/%m/%Y'));
insert into notation values ('Christina.Malko@yahoo.fr',19,10,STR_TO_DATE('16/12/2010', '%d/%m/%Y'));
insert into notation values ('Antoine.Cagelin@hotmail.fr',20,6,STR_TO_DATE('12/01/2011', '%d/%m/%Y'));
insert into notation values ('Arthur.Daunot@yahoo.fr',20,4,STR_TO_DATE('13/01/2011', '%d/%m/%Y'));
insert into notation values ('Isabelle.Pruvat@yahoo.fr',11,7,STR_TO_DATE('21/10/2010', '%d/%m/%Y'));
insert into notation values ('Juliette.Luna@gmail.fr',16,5,STR_TO_DATE('12/02/2011', '%d/%m/%Y'));
insert into notation values ('Jean.Vanier@yahoo.fr',21,8,STR_TO_DATE('22/08/2017', '%d/%m/%Y'));
insert into notation values ('Ines.Fragin@yahoo.fr',21,7,STR_TO_DATE('17/09/2017', '%d/%m/%Y'));
