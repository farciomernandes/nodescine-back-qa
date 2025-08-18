-- Create category table
create table if not exists category_tbl (
                                            uuid uuid primary key,
                                            created_at timestamp,
                                            updated_at timestamp,
                                            deleted_at timestamp,
                                            name varchar(255),
                                            slug varchar(255),
                                            image_url text
);

-- Create genre table
create table if not exists genre_tbl (
                                         uuid uuid primary key,
                                         created_at timestamp,
                                         updated_at timestamp,
                                         deleted_at timestamp,
                                         name varchar(255)
);

-- Create film table
create table if not exists film_tbl (
                                        uuid uuid primary key,
                                        created_at timestamp,
                                        updated_at timestamp,
                                        deleted_at timestamp,
                                        title varchar(255),
                                        slug varchar(255),
                                        director varchar(255),
                                        release_year int,
                                        price varchar(50),
                                        duration_in_minutes int,
                                        description text,
                                        poster_url text,
                                        trailer_url text,
                                        video_url text,
                                        premium boolean,
                                        featured boolean,
                                        rating double precision,
                                        view_count int,
                                        category_id uuid references category_tbl(uuid)
);

-- Film cast (element collection)
create table if not exists film_cast (
                                         film_id uuid not null references film_tbl(uuid) on delete cascade,
                                         actor_name varchar(255) not null,
                                         primary key (film_id, actor_name)
);

-- Film genres join table
create table if not exists film_genres (
                                           film_id uuid not null references film_tbl(uuid) on delete cascade,
                                           genre_id uuid not null references genre_tbl(uuid) on delete cascade,
                                           primary key (film_id, genre_id)
);

-- Helpful indexes
create index if not exists idx_film_category on film_tbl(category_id);
create index if not exists idx_film_slug on film_tbl(slug);
create index if not exists idx_genre_name on genre_tbl(name);

