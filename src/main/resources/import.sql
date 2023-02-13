insert into category values ('STUDY'),('ETC'),('EXERCISE'),('HABIT'),('HOBBY');
INSERT INTO user VALUES (1,80,'jeongye0001@gmail.com',true,'정예원','','https://lh3.googleusercontent.com/a/AEdFTp5XFsygrXFX1pHKKmHFsNo6ka6BpFL66FHCIGgf=s96-c',10000000,'MAN'),(2,20,'0108dlatnqls@gmail.com',true,'robin','','https://lh3.googleusercontent.com/a/AEdFTp7rC-sC7IRp7wWKk11kma2sT-r1140BlSHnK0Ww=s96-c',500,'MAN');
INSERT INTO user_category_point VALUES ('ETC',1,0),('ETC',2,0),('EXERCISE',1,0),('EXERCISE',2,0),('HABIT',1,0),('HABIT',2,0),('HOBBY',1,0),('HOBBY',2,0),('STUDY',1,0),('STUDY',2,0);
insert into goal (dtype, id, content, goal_state, point, reward, title, category, user_id,end_date, start_date) values  ('OneTimeGoal', 1, 'test', 'ONGOING', 100, 'HIGH_RETURN', 'test', 'STUDY', 1,'2023-03-13', '2023-03-09'),('ManyTimeGoal', 2, 'dd', 'ONGOING', 100, 'HIGH_RETURN', 'dd', 'STUDY', 1,'2023-03-13', '2023-03-09');
insert into one_time_goal ( id) values  (1);
insert into many_time_goal (success_count, id) values  (0, 2);
insert into certification (dtype, id, content, date, fail_count, picture, state, success_count, goal_id)values  ('OneTimeCertification', 1, 'ddd', '2023-02-28', 0, 'dddddd', 'ONGOING', 0, 1),('ManyTimeCertification', 2, 'ddd', '2023-03-09', 0, 'dddddd', 'ONGOING', 0, 2);
insert into one_time_certification (id) values  (1);
insert into many_time_certification (id) values  (2);