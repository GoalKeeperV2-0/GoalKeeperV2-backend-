insert into category values ('STUDY'),('ETC'),('EXERCISE'),('HABIT'),('HOBBY');
INSERT INTO user VALUES (1,80,'jeongye0001@gmail.com',true,'정예원','https://lh3.googleusercontent.com/a/AEdFTp5XFsygrXFX1pHKKmHFsNo6ka6BpFL66FHCIGgf=s96-c',10000000,'MAN'),(2,null,'0108dlatnqls@gmail.com',false,'robin','https://lh3.googleusercontent.com/a/AEdFTp7rC-sC7IRp7wWKk11kma2sT-r1140BlSHnK0Ww=s96-c',500,null);
INSERT INTO user_category_point VALUES ('ETC',1,0),('ETC',2,0),('EXERCISE',1,0),('EXERCISE',2,0),('HABIT',1,0),('HABIT',2,0),('HOBBY',1,0),('HOBBY',2,0),('STUDY',1,0),('STUDY',2,0);
insert into goal (dtype, id, content, goal_state, point, reward, title, category, user_id,end_date, start_date,hold_request_able) values  ('OneTimeGoal', 1, 'test', 'WAITING_CERT_COMPLETE', 100, 'HIGH_RETURN', 'test', 'STUDY', 1,'2024-03-13', '2023-03-09',false),('ManyTimeGoal', 2, 'dd', 'WAITING_CERT_COMPLETE', 100, 'HIGH_RETURN', 'dd', 'STUDY', 1,'2024-03-13', '2023-03-09',false);
insert into one_time_goal ( id) values  (1);
insert into many_time_goal (success_count,fail_count, id) values  (0, 0, 2);
insert into many_time_goal_cert_date (cert_date, goal_id) values  ('2024-03-09', 2),('2024-03-11', 2),('2024-03-12', 2),('2024-03-13', 2);
insert into certification (dtype, id, content, date, fail_count, picture, state, success_count, goal_id)values  ('OneTimeCertification', 1, 'ddd', '2024-03-11', 3, 'dddddd', 'ONGOING', 0, 1),('ManyTimeCertification', 2, 'ddd', '2024-03-09', 0, 'dddddd', 'ONGOING', 0, 2);
insert into one_time_certification (id) values  (1);
insert into many_time_certification (id) values  (2);