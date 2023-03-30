-- ovo sam ostavio za vas, ako provalite kako da naterate Spring da napravi Database pri pokretanju aplikacije
-- meni nije radilo. Ovo je standardan oblik database scheme koji bismo napravili rucno u pgadminu.
-- ovo bi trebalo da se pokrene kad se spring startuje zato sto sam postavio :
--      spring.sql.init.schema-locations=classpath:schema-postgresql.
--      sqlspring.jpa.hibernate.ddl-auto=none
--      spring.sql.init.mode=always
-- ali nece :(
-- tako da se ova skriptica nigde ne koristi, ali me boli dusa da je brisem jer je 2 ujutru vec.
CREATE DATABASE busep
    WITH OWNER = postgres -- ovde stoji postgres posto se povezujemo s bazom kao postgres user
    ENCODING = 'UTF8' -- standard
    LC_COLLATE = 'en_US.UTF-8' -- standard
    LC_CTYPE = 'en_US.UTF-8' -- standard
    TABLESPACE = pg_default -- default, lol
    CONNECTION LIMIT = -1; -- ovo znaci da ne postoji limit koliko ljudi se moze povezati sa bazom