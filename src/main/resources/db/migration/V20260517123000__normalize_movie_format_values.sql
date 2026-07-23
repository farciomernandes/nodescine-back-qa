-- Normalize existing movie.format values to enum names (safe for empty DB)
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'movie') THEN
        UPDATE movie SET format = UPPER(format) WHERE format IS NOT NULL AND format <> '';
        UPDATE movie SET format = 'UNKNOWN' WHERE format IS NULL OR format = '';
    END IF;
END $$;

