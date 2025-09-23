-- Insert genres
INSERT INTO public.genre (created_at, name) VALUES
                                                (CURRENT_TIMESTAMP, 'Ação'),
                                                (CURRENT_TIMESTAMP, 'Drama'),
                                                (CURRENT_TIMESTAMP, 'Comédia'),
                                                (CURRENT_TIMESTAMP, 'Ficção Científica');

-- Insert users with password "senha123" (BCrypt encoded)
INSERT INTO public.users (created_at, email, name, password, status) VALUES
                                                                         (CURRENT_TIMESTAMP, 'john.doe@cinesk.com', 'John Doe', '$2a$10$z4g7i1j3h0X3vY2f3k0Z4eW6y6p8s9r3q2w1e5t7y8u9i0o1p2a3', 'ACTIVE'),
                                                                         (CURRENT_TIMESTAMP, 'jane.smith@cinesk.com', 'Jane Smith', '$2a$10$z4g7i1j3h0X3vY2f3k0Z4eW6y6p8s9r3q2w1e5t7y8u9i0o1p2a3', 'ACTIVE'),
                                                                         (CURRENT_TIMESTAMP, 'admin@cinesk.com', 'Admin User', '$2a$10$z4g7i1j3h0X3vY2f3k0Z4eW6y6p8s9r3q2w1e5t7y8u9i0o1p2a3', 'ACTIVE');

-- Insert user roles
INSERT INTO public.user_roles (user_id, role) VALUES
                                                  ((SELECT id FROM public.users WHERE email = 'john.doe@cinesk.com'), 'CUSTOMER'),
                                                  ((SELECT id FROM public.users WHERE email = 'jane.smith@cinesk.com'), 'CUSTOMER'),
                                                  ((SELECT id FROM public.users WHERE email = 'admin@cinesk.com'), 'ADMIN');

-- Insert user tokens (JWT tokens with 24-hour expiration as per application.security.jwt.expiration)
INSERT INTO public.user_tokens (active, expires_at, issued_at, token, user_email) VALUES
                                                                                      (true, CURRENT_TIMESTAMP + INTERVAL '24 hours', CURRENT_TIMESTAMP, 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBjaW5lc2suY29tIiwiaWF0IjoxNzI3MDg3NjAwLCJleHAiOjE3MjcxNzQwMDB9.abc123', 'john.doe@cinesk.com'),
                                                                                      (true, CURRENT_TIMESTAMP + INTERVAL '24 hours', CURRENT_TIMESTAMP, 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW5lLnNtaXRoQGNpbmVzay5jb20iLCJpYXQiOjE3MjcwODc2MDAsImV4cCI6MTcyNzE3NDAwMH0.def456', 'jane.smith@cinesk.com');

-- Insert movies (category_id references genre.id)
INSERT INTO public.movie (duration_in_minutes, featured, premium, rating, release_year, view_count, category_id, created_at, description, director, poster_url, price, slug, title, trailer_url, video_url) VALUES
                                                                                                                                                                                                                (120, true, false, 7.5, 2023, 1000, (SELECT id FROM public.genre WHERE name = 'Ação'), CURRENT_TIMESTAMP, 'Uma aventura cheia de ação.', 'Michael Bay', 'https://cinesk.com/posters/explosoes.jpg', '9.99', 'explosoes-2023', 'Explosões', 'https://cinesk.com/trailers/explosoes.mp4', 'https://cinesk.com/videos/explosoes.mp4'),
                                                                                                                                                                                                                (135, false, true, 8.0, 2022, 500, (SELECT id FROM public.genre WHERE name = 'Drama'), CURRENT_TIMESTAMP, 'Uma história emocionante de amor e perda.', 'Greta Gerwig', 'https://cinesk.com/posters/cordas.jpg', '14.99', 'cordas-2022', 'Cordas do Coração', 'https://cinesk.com/trailers/cordas.mp4', 'https://cinesk.com/videos/cordas.mp4'),
                                                                                                                                                                                                                (90, true, false, 6.8, 2024, 2000, (SELECT id FROM public.genre WHERE name = 'Comédia'), CURRENT_TIMESTAMP, 'Uma comédia hilária para todas as idades.', 'Judd Apatow', 'https://cinesk.com/posters/rir.jpg', '7.99', 'rir-2024', 'Rir é o Melhor Remédio', 'https://cinesk.com/trailers/rir.mp4', 'https://cinesk.com/videos/rir.mp4');

-- Insert transactions (aligned with Asaas payment context)
INSERT INTO public.transactions (created_at, user_id, amount, date, film, status) VALUES
                                                                                      (CURRENT_TIMESTAMP, (SELECT id FROM public.users WHERE email = 'john.doe@cinesk.com'), '9.99', '2025-09-23', 'Explosões', 'ACTIVE'),
                                                                                      (CURRENT_TIMESTAMP, (SELECT id FROM public.users WHERE email = 'jane.smith@cinesk.com'), '14.99', '2025-09-23', 'Cordas do Coração', 'ACTIVE'),
                                                                                      (CURRENT_TIMESTAMP, (SELECT id FROM public.users WHERE email = 'john.doe@cinesk.com'), '7.99', '2025-09-22', 'Rir é o Melhor Remédio', 'CANCELLED');