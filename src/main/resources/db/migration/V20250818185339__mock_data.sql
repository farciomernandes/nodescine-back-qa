-- Seed Categories
insert into category_tbl (uuid, name, slug, image_url)
values
    ('11111111-1111-1111-1111-111111111111', 'Action', 'action', 'https://picsum.photos/seed/action/640/360'),
    ('22222222-2222-2222-2222-222222222222', 'Drama', 'drama', 'https://picsum.photos/seed/drama/640/360'),
    ('33333333-3333-3333-3333-333333333333', 'Comedy', 'comedy', 'https://picsum.photos/seed/comedy/640/360')
on conflict do nothing;

-- Seed Genres
insert into genre_tbl (uuid, name)
values
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1', 'Thriller'),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa2', 'Sci-Fi'),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3', 'Romance'),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa4', 'Adventure'),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa5', 'Animation'),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa6', 'Crime')
on conflict do nothing;

-- Seed Films (omit columns that may vary across environments)
insert into film_tbl (
    uuid, title, slug, director, release_year, duration_in_minutes, description, poster_url, premium, featured, category_id
) values
      ('f0000000-0000-0000-0000-000000000001', 'Neon Shadows', 'neon-shadows', 'A. Rivera', 2023, 118,
       'In a city of lights, a detective uncovers a conspiracy that could erase the future.',
       'https://picsum.photos/seed/neonshadows/420/630',
       true, true, '11111111-1111-1111-1111-111111111111'),

      ('f0000000-0000-0000-0000-000000000002', 'Echoes of Tomorrow', 'echoes-of-tomorrow', 'M. Chen', 2024, 105,
       'Two scientists race against time when messages from the future begin to arrive.',
       'https://picsum.photos/seed/echoes/420/630',
       true, false, '22222222-2222-2222-2222-222222222222'),

      ('f0000000-0000-0000-0000-000000000003', 'Hearts at Dusk', 'hearts-at-dusk', 'S. Almeida', 2022, 112,
       'A bittersweet romance unfolds across three cities as day turns to night.',
       'https://picsum.photos/seed/hearts/420/630',
       false, true, '22222222-2222-2222-2222-222222222222'),

      ('f0000000-0000-0000-0000-000000000004', 'Laugh Lines', 'laugh-lines', 'K. Patel', 2021, 96,
       'A stand-up comic finds his voice on and off stage in this heartfelt comedy.',
       'https://picsum.photos/seed/laughlines/420/630',
       false, false, '33333333-3333-3333-3333-333333333333'),

      ('f0000000-0000-0000-0000-000000000005', 'Skyward Bound', 'skyward-bound', 'J. Okafor', 2025, 124,
       'A daring crew attempts the first civilian mission beyond low Earth orbit.',
       'https://picsum.photos/seed/skyward/420/630',
       true, true, '11111111-1111-1111-1111-111111111111')
on conflict do nothing;
