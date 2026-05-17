-- Normalize existing movie.format values to enum names
-- 1) Convert existing values to uppercase (so they match enum names like MP4, MKV, etc.)
-- 2) Set empty or NULL values to 'UNKNOWN'

UPDATE movie
SET format = UPPER(format)
WHERE format IS NOT NULL AND format <> '';

UPDATE movie
SET format = 'UNKNOWN'
WHERE format IS NULL OR format = '';

