CREATE TABLE IF NOT EXISTS public."users"
(
    id    SERIAL NOT NULL,
    email TEXT   NOT NULL,
    name  TEXT   NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id)
) TABLESPACE pg_default;

ALTER TABLE "users"
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS public."addresses"
(
    user_id      SERIAL NOT NULL,
    city         TEXT   NOT NULL,
    street       TEXT   NOT NULL,
    house_number INT    NOT NULL,
    index        INT    NOT NULL,

    CONSTRAINT addresses_pkey PRIMARY KEY (user_id),
    CONSTRAINT addresses_fkey FOREIGN KEY (user_id) REFERENCES public."users" (id)
        ON UPDATE NO ACTION ON DELETE NO ACTION
) TABLESPACE pg_default;

ALTER TABLE "addresses"
    OWNER TO postgres;