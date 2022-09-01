CREATE OR REPLACE PROCEDURE insert_user_with_random_data(user_email text, user_name text)
    LANGUAGE plpgsql
AS
$$
DECLARE
    last_id int;
BEGIN
    INSERT INTO public.users (email, name)
    VALUES (user_email, user_name)
    RETURNING id INTO last_id;
    INSERT INTO public.addresses
    VALUES (last_id,
            concat(user_name, ' city'),
            concat(user_name, ' street'), 123, 456789);
END;
$$;

CREATE OR REPLACE FUNCTION user_insert_after_trigger()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO users (email, name) VALUES (concat(new.email, ' copy'), concat(new.name, ' copy'));
    RETURN new;
END;
$$;

CREATE TRIGGER user_insert_trigger
    AFTER INSERT
    ON users
    FOR EACH row
    WHEN ( pg_trigger_depth() = 0 )
EXECUTE PROCEDURE user_insert_after_trigger();

CREATE OR REPLACE FUNCTION lmao(in int, in int, out result text)
    LANGUAGE plpgsql AS
$$
BEGIN
    IF $1 > $2 THEN
        result := concat($1, ' larger than ', $2);
    ELSE
        result := concat($2, ' cooler than ', $1);
    END IF;
END;
$$;

CREATE OR REPLACE FUNCTION rofl(int, int) RETURNS RECORD
    LANGUAGE plpgsql
    STABLE AS
$$
DECLARE
    my_rec RECORD;
BEGIN
    IF ($1 < $2 AND $2 - $1 < 10) THEN
        WHILE $1 < $2
            LOOP
                RAISE NOTICE '$1 is now %', $1;
                SELECT concat('Numbero ', $1) INTO my_rec;
                $1 := $1 + 1;
            end loop;
    ELSE
        my_rec := ($1::INTEGER, ' versus '::TEXT, $2::INTEGER, ' ...maybe next time'::TEXT);
    END IF;
    RETURN my_rec;
END;
$$;

CREATE OR REPLACE PROCEDURE print_users(sort_type int2)
    LANGUAGE plpgsql
    STABLE AS
$$
DECLARE
    record_count SMALLINT := 10;
    my_record    RECORD;
    query        TEXT;
BEGIN
    query := 'SELECT * FROM users';
    IF sort_type = 1 THEN
        query := query || ' ORDER BY id';
    ELSEIF sort_type = 2 THEN
        query := query || ' ORDER BY email';
    ELSE
        RAISE 'Invalid sort type %', sort_type;
    END IF;
    query := query || ' LIMIT $1';
    FOR my_record IN EXECUTE query USING record_count
        LOOP
            RAISE NOTICE '% ||| % ||| %', my_record.id, my_record.email, my_record.name;
        END LOOP;
END;
$$;

CREATE OR REPLACE PROCEDURE evaluate_user_name(user_id bigint)
    LANGUAGE plpgsql
    STABLE AS
$$
DECLARE
    target_name users.name%TYPE;
    eval_result TEXT;
BEGIN
    SELECT name INTO target_name FROM users WHERE id = user_id;
    IF FOUND THEN
        CASE LENGTH(target_name)
            WHEN 1 THEN eval_result := 'very short name';
            WHEN 2 THEN eval_result := 'still very short name';
            WHEN 3 THEN eval_result := 'just a short name';
            WHEN 4 THEN eval_result := 'still a short name';
            WHEN 5 THEN eval_result := 'short-ish name';
            ELSE eval_result := 'name is not short';
            END CASE;
        RAISE NOTICE 'Name eval verdict : %', eval_result;
    END IF;
END;
$$;

CREATE OR REPLACE PROCEDURE number_pairs(first_limit int, second_limit int)
    LANGUAGE plpgsql
    STABLE AS
$this_procedure$
DECLARE
    i int := 0;
    j int := 0;
BEGIN
    <<outerloop>>
    LOOP
        i := i + 1;
        CONTINUE WHEN MOD(i, 2) = 0;
        EXIT outerloop WHEN i > first_limit;
        j := 0;
        <<innerloop>>
        LOOP
            j := j + 1;
            CONTINUE WHEN MOD(j, 2) = 0;
            EXIT innerloop WHEN j > second_limit;
            RAISE NOTICE '(i,j) :: (%,%)', i, j;
        END LOOP innerloop;
    END LOOP outerloop;
END;
$this_procedure$;

-- FETCH PRACT
BEGIN WORK;

DECLARE my_cursor SCROLL CURSOR FOR SELECT * FROM users;
FETCH FORWARD 3 FROM my_cursor;
FETCH RELATIVE 0 FROM my_cursor;
FETCH RELATIVE 0 FROM my_cursor;
FETCH RELATIVE 3 FROM my_cursor;
FETCH RELATIVE 0 FROM my_cursor;
FETCH PRIOR FROM my_cursor;
FETCH FIRST FROM my_cursor;

CLOSE my_cursor;
COMMIT WORK;

-- MOVE PRAC
BEGIN WORK;

DECLARE my_cursor SCROLL CURSOR FOR SELECT * FROM users;
MOVE FORWARD 10 IN my_cursor;
FETCH RELATIVE 0 FROM my_cursor;
MOVE FORWARD 10 IN my_cursor;
FETCH RELATIVE 0 FROM my_cursor;
MOVE LAST IN my_cursor;
FETCH RELATIVE 0 FROM my_cursor;
MOVE FIRST IN my_cursor;
FETCH RELATIVE 0 FROM my_cursor;

CLOSE my_cursor;
COMMIT WORK;