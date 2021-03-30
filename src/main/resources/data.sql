INSERT INTO users (id, username, password, creation_date) VALUES
('c776e082-6407-49a5-a246-9d7265fc2583', 'Yupiel', '$2y$12$VAcIZbFYSzidr5f9DUr3t.Cl0X/5dchQZiq9haWEDkePGK.4oQvdS', '2021-03-20'),
('b1805241-3365-455f-8ba9-6228458c55b0', 'tester', '$2y$12$VAcIZbFYSzidr5f9DUr3t.Cl0X/5dchQZiq9haWEDkePGK.4oQvdS', '2021-03-30');

INSERT INTO activities(id, type, creation_date, user_id) VALUES
('10af72c0-b4df-4a8b-b80f-49b5a46ba4a0', 'DRINK_WATER', '2021-03-24', 'c776e082-6407-49a5-a246-9d7265fc2583'),
('0e1d87fd-1ba5-41d5-9f8a-321a4f52d507', 'WALKING', '2021-03-24', 'c776e082-6407-49a5-a246-9d7265fc2583'),
('ae23daae-7cef-4752-b951-a73d899348d9', 'RUNNING', '2021-03-24', 'b1805241-3365-455f-8ba9-6228458c55b0');

INSERT INTO challenges(id, activity_type, amount_of_times_day, start_date, expiration_date, challenge_status, user_id) VALUES
('c776e082-4752-9f8a-49b5a46ba4a0', 'DRINK_WATER', '10', '2021-03-29', '2022-03-27', 'IN_PROGRESS', 'c776e082-6407-49a5-a246-9d7265fc2583');
