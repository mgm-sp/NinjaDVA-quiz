CREATE TABLE question_rounds (question_round INT PRIMARY KEY, total_questions INT, current_round BOOL);
INSERT INTO question_rounds VALUES (0,3,true);
INSERT INTO question_rounds VALUES (1,1,true);

CREATE TABLE questions (question_round INT NOT NULL,question_number INT NOT NULL, title VARCHAR(255), type VARCHAR(10), CONSTRAINT id PRIMARY KEY (question_round,question_number));
INSERT INTO questions VALUES (0,0,'Wofür Steht RSA?','radio');
INSERT INTO questions VALUES (0,1,'Nennen Sie zwei Merkmale von symmetrischen Chiffren?','text');
INSERT INTO questions VALUES (0,2,'Wer sind die Erfinder des DH-Schlüsselaustauschs?','checkbox');
INSERT INTO questions VALUES (1,0,'No question here!','radio');

CREATE TABLE possible_answers (question_round INT NOT NULL,question_number INT NOT NULL, answer_number INT NOT NULL, text VARCHAR(255), CONSTRAINT id PRIMARY KEY (question_round,question_number,answer_number));
INSERT INTO possible_answers VALUES (0,0,0,'Rivest Shamir Adleman');
INSERT INTO possible_answers VALUES (0,0,1,'Rich Source Ale');
INSERT INTO possible_answers VALUES (0,0,2,'Ranziger SuppenAlkohol');
INSERT INTO possible_answers VALUES (0,1,0,'Merkmal 1');
INSERT INTO possible_answers VALUES (0,1,1,'Merkmal 2');
INSERT INTO possible_answers VALUES (0,2,0,'Diffie');
INSERT INTO possible_answers VALUES (0,2,1,'Diffus');
INSERT INTO possible_answers VALUES (0,2,2,'Hellmann');
INSERT INTO possible_answers VALUES (0,2,3,'Hellman');
INSERT INTO possible_answers VALUES (0,2,4,'The Hell Man');
INSERT INTO possible_answers VALUES (1,0,0,'Nothing here');

CREATE TABLE answers (student VARCHAR(64) NOT NULL, question_round INT NOT NULL,question_number INT NOT NULL, answer_number INT NOT NULL, text VARCHAR(255), CONSTRAINT id PRIMARY KEY (student,question_round,question_number,answer_number));
