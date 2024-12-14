-- Insert Categories
INSERT INTO categories (name, description) VALUES
('Science', 'Questions about various scientific concepts and discoveries'),
('History', 'Questions about historical events and figures'),
('Geography', 'Questions about world geography and landmarks'),
('Literature', 'Questions about books, authors, and literary works'),
('Mathematics', 'Questions about mathematical concepts and problems'),
('Technology', 'Questions about computers, programming, and tech innovations');

-- Science Questions
INSERT INTO questions (category_id, difficulty, content) 
SELECT 
    (SELECT id FROM categories WHERE name = 'Science'),
    CASE WHEN RAND() < 0.33 THEN 'EASY' WHEN RAND() < 0.66 THEN 'MEDIUM' ELSE 'HARD' END,
    content
FROM (
    SELECT 'What is the chemical symbol for gold?' as content UNION ALL
    SELECT 'Which planet is known as the Red Planet?' UNION ALL
    SELECT 'What is the hardest natural substance on Earth?' UNION ALL
    SELECT 'What is the largest organ in the human body?' UNION ALL
    SELECT 'What is the process by which plants make their food?' UNION ALL
    SELECT 'What is the study of fossils called?' UNION ALL
    SELECT 'What is the smallest unit of matter?' UNION ALL
    SELECT 'Which gas do plants absorb from the atmosphere?' UNION ALL
    SELECT 'What is the speed of light?' UNION ALL
    SELECT 'What is the chemical formula for water?' UNION ALL
    SELECT 'What type of energy does the sun provide?' UNION ALL
    SELECT 'What is the process of water changing from liquid to gas called?' UNION ALL
    SELECT 'What is the unit of electrical resistance?' UNION ALL
    SELECT 'Which element has the atomic number 1?' UNION ALL
    SELECT 'What force keeps planets in orbit?' UNION ALL
    SELECT 'What is the study of weather called?' UNION ALL
    SELECT 'What is the largest organ in the human body?' UNION ALL
    SELECT 'What is the process of cell division called?' UNION ALL
    SELECT 'What type of rock is formed by cooling magma?' UNION ALL
    SELECT 'What is the study of living things called?'
) AS temp;

-- Insert options for Science questions (fixed version)
INSERT INTO options (question_id, content, is_correct)
SELECT 
    q.id,
    o.option_content,
    o.is_correct
FROM (
    SELECT id, ROW_NUMBER() OVER (ORDER BY id) as rn
    FROM questions 
    WHERE category_id = (SELECT id FROM categories WHERE name = 'Science')
) q
JOIN (
    -- Q1: Chemical symbol for gold
    SELECT 1 as qnum, 'Au' as option_content, true as is_correct UNION ALL
    SELECT 1, 'Ag', false UNION ALL
    SELECT 1, 'Fe', false UNION ALL
    SELECT 1, 'Cu', false UNION ALL
    
    -- Q2: Red Planet
    SELECT 2, 'Mars', true UNION ALL
    SELECT 2, 'Venus', false UNION ALL
    SELECT 2, 'Jupiter', false UNION ALL
    SELECT 2, 'Saturn', false UNION ALL
    
    -- Q3: Hardest natural substance
    SELECT 3, 'Diamond', true UNION ALL
    SELECT 3, 'Graphite', false UNION ALL
    SELECT 3, 'Quartz', false UNION ALL
    SELECT 3, 'Ruby', false UNION ALL
    
    -- Q4: Largest organ
    SELECT 4, 'Skin', true UNION ALL
    SELECT 4, 'Liver', false UNION ALL
    SELECT 4, 'Brain', false UNION ALL
    SELECT 4, 'Heart', false UNION ALL
    
    -- Q5: Plant food process
    SELECT 5, 'Photosynthesis', true UNION ALL
    SELECT 5, 'Respiration', false UNION ALL
    SELECT 5, 'Digestion', false UNION ALL
    SELECT 5, 'Fermentation', false UNION ALL
    
    -- Q6: Study of fossils
    SELECT 6, 'Paleontology', true UNION ALL
    SELECT 6, 'Archaeology', false UNION ALL
    SELECT 6, 'Geology', false UNION ALL
    SELECT 6, 'Biology', false UNION ALL
    
    -- Q7: Smallest unit of matter
    SELECT 7, 'Atom', true UNION ALL
    SELECT 7, 'Cell', false UNION ALL
    SELECT 7, 'Molecule', false UNION ALL
    SELECT 7, 'Electron', false UNION ALL
    
    -- Q8: Gas plants absorb
    SELECT 8, 'Carbon dioxide', true UNION ALL
    SELECT 8, 'Oxygen', false UNION ALL
    SELECT 8, 'Nitrogen', false UNION ALL
    SELECT 8, 'Hydrogen', false UNION ALL
    
    -- Q9: Speed of light
    SELECT 9, '299,792,458 m/s', true UNION ALL
    SELECT 9, '300,000,000 m/s', false UNION ALL
    SELECT 9, '299,000,000 m/s', false UNION ALL
    SELECT 9, '301,000,000 m/s', false UNION ALL
    
    -- Q10: Chemical formula for water
    SELECT 10, 'H2O', true UNION ALL
    SELECT 10, 'CO2', false UNION ALL
    SELECT 10, 'O2', false UNION ALL
    SELECT 10, 'H2O2', false UNION ALL
    
    -- Q11: Sun's energy
    SELECT 11, 'Solar energy', true UNION ALL
    SELECT 11, 'Kinetic energy', false UNION ALL
    SELECT 11, 'Potential energy', false UNION ALL
    SELECT 11, 'Nuclear energy', false UNION ALL
    
    -- Q12: Water to gas process
    SELECT 12, 'Evaporation', true UNION ALL
    SELECT 12, 'Condensation', false UNION ALL
    SELECT 12, 'Sublimation', false UNION ALL
    SELECT 12, 'Freezing', false UNION ALL
    
    -- Q13: Electrical resistance unit
    SELECT 13, 'Ohm', true UNION ALL
    SELECT 13, 'Ampere', false UNION ALL
    SELECT 13, 'Volt', false UNION ALL
    SELECT 13, 'Watt', false UNION ALL
    
    -- Q14: Atomic number 1
    SELECT 14, 'Hydrogen', true UNION ALL
    SELECT 14, 'Helium', false UNION ALL
    SELECT 14, 'Carbon', false UNION ALL
    SELECT 14, 'Oxygen', false UNION ALL
    
    -- Q15: Force keeping planets in orbit
    SELECT 15, 'Gravity', true UNION ALL
    SELECT 15, 'Magnetism', false UNION ALL
    SELECT 15, 'Friction', false UNION ALL
    SELECT 15, 'Nuclear force', false UNION ALL
    
    -- Q16: Study of weather
    SELECT 16, 'Meteorology', true UNION ALL
    SELECT 16, 'Geology', false UNION ALL
    SELECT 16, 'Astronomy', false UNION ALL
    SELECT 16, 'Geography', false UNION ALL
    
    -- Q17: Largest organ (duplicate question, different options)
    SELECT 17, 'Skin', true UNION ALL
    SELECT 17, 'Intestines', false UNION ALL
    SELECT 17, 'Lungs', false UNION ALL
    SELECT 17, 'Muscles', false UNION ALL
    
    -- Q18: Cell division process
    SELECT 18, 'Mitosis', true UNION ALL
    SELECT 18, 'Meiosis', false UNION ALL
    SELECT 18, 'Binary fission', false UNION ALL
    SELECT 18, 'Budding', false UNION ALL
    
    -- Q19: Rock from cooling magma
    SELECT 19, 'Igneous', true UNION ALL
    SELECT 19, 'Sedimentary', false UNION ALL
    SELECT 19, 'Metamorphic', false UNION ALL
    SELECT 19, 'Limestone', false UNION ALL
    
    -- Q20: Study of living things
    SELECT 20, 'Biology', true UNION ALL
    SELECT 20, 'Zoology', false UNION ALL
    SELECT 20, 'Botany', false UNION ALL
    SELECT 20, 'Ecology', false
) o ON q.rn = o.qnum;

-- History Questions
INSERT INTO questions (category_id, difficulty, content)
SELECT 
    (SELECT id FROM categories WHERE name = 'History'),
    CASE WHEN RAND() < 0.33 THEN 'EASY' WHEN RAND() < 0.66 THEN 'MEDIUM' ELSE 'HARD' END,
    content
FROM (
    SELECT 'Who was the first President of the United States?' as content UNION ALL
    SELECT 'In which year did World War II end?' UNION ALL
    SELECT 'Who was the first Emperor of China?' UNION ALL
    SELECT 'What year did the Berlin Wall fall?' UNION ALL
    SELECT 'Who wrote the Communist Manifesto?' UNION ALL
    SELECT 'Which civilization built the pyramids?' UNION ALL
    SELECT 'Who was Joan of Arc?' UNION ALL
    SELECT 'What was the Renaissance?' UNION ALL
    SELECT 'When did the Industrial Revolution begin?' UNION ALL
    SELECT 'Who discovered America?' UNION ALL
    SELECT 'What was the Cold War?' UNION ALL
    SELECT 'Who was William Shakespeare?' UNION ALL
    SELECT 'What was the French Revolution?' UNION ALL
    SELECT 'Who was Genghis Khan?' UNION ALL
    SELECT 'What was the Age of Enlightenment?' UNION ALL
    SELECT 'Who was Leonardo da Vinci?' UNION ALL
    SELECT 'What was the Black Death?' UNION ALL
    SELECT 'Who was Alexander the Great?' UNION ALL
    SELECT 'What were the Crusades?' UNION ALL
    SELECT 'Who was Julius Caesar?'
) AS temp;

-- Insert options for History questions (fixed version)
INSERT INTO options (question_id, content, is_correct)
SELECT 
    q.id,
    o.option_content,
    o.is_correct
FROM (
    SELECT id, ROW_NUMBER() OVER (ORDER BY id) as rn
    FROM questions 
    WHERE category_id = (SELECT id FROM categories WHERE name = 'History')
) q
JOIN (
    -- Q1: First US President
    SELECT 1 as qnum, 'George Washington' as option_content, true as is_correct UNION ALL
    SELECT 1, 'Thomas Jefferson', false UNION ALL
    SELECT 1, 'John Adams', false UNION ALL
    SELECT 1, 'Benjamin Franklin', false UNION ALL
    
    -- Q2: End of WWII
    SELECT 2, '1945', true UNION ALL
    SELECT 2, '1944', false UNION ALL
    SELECT 2, '1946', false UNION ALL
    SELECT 2, '1943', false UNION ALL
    
    -- Q3: First Emperor of China
    SELECT 3, 'Qin Shi Huang', true UNION ALL
    SELECT 3, 'Han Wudi', false UNION ALL
    SELECT 3, 'Emperor Wu', false UNION ALL
    SELECT 3, 'Sun Yat-sen', false UNION ALL
    
    -- Q4: Berlin Wall fall
    SELECT 4, '1989', true UNION ALL
    SELECT 4, '1991', false UNION ALL
    SELECT 4, '1988', false UNION ALL
    SELECT 4, '1990', false UNION ALL
    
    -- Q5: Communist Manifesto author
    SELECT 5, 'Karl Marx', true UNION ALL
    SELECT 5, 'Friedrich Engels', false UNION ALL
    SELECT 5, 'Vladimir Lenin', false UNION ALL
    SELECT 5, 'Joseph Stalin', false UNION ALL
    
    -- Q6: Civilization that built pyramids
    SELECT 6, 'Egyptians', true UNION ALL
    SELECT 6, 'Mayans', false UNION ALL
    SELECT 6, 'Aztecs', false UNION ALL
    SELECT 6, 'Romans', false UNION ALL
    
    -- Q7: Joan of Arc
    SELECT 7, 'French heroine', true UNION ALL
    SELECT 7, 'English queen', false UNION ALL
    SELECT 7, 'Spanish explorer', false UNION ALL
    SELECT 7, 'Italian artist', false UNION ALL
    
    -- Q8: Renaissance
    SELECT 8, 'Cultural movement', true UNION ALL
    SELECT 8, 'Military campaign', false UNION ALL
    SELECT 8, 'Political revolution', false UNION ALL
    SELECT 8, 'Economic crisis', false UNION ALL
    
    -- Q9: Industrial Revolution start
    SELECT 9, '18th century', true UNION ALL
    SELECT 9, '17th century', false UNION ALL
    SELECT 9, '19th century', false UNION ALL
    SELECT 9, '20th century', false UNION ALL
    
    -- Q10: America discoverer
    SELECT 10, 'Christopher Columbus', true UNION ALL
    SELECT 10, 'Leif Erikson', false UNION ALL
    SELECT 10, 'Ferdinand Magellan', false UNION ALL
    SELECT 10, 'Amerigo Vespucci', false UNION ALL
    
    -- Q11: Cold War
    SELECT 11, 'Political tension', true UNION ALL
    SELECT 11, 'Military conflict', false UNION ALL
    SELECT 11, 'Economic depression', false UNION ALL
    SELECT 11, 'Cultural renaissance', false UNION ALL
    
    -- Q12: William Shakespeare
    SELECT 12, 'Playwright', true UNION ALL
    SELECT 12, 'Scientist', false UNION ALL
    SELECT 12, 'Explorer', false UNION ALL
    SELECT 12, 'Philosopher', false UNION ALL
    
    -- Q13: French Revolution
    SELECT 13, '1789', true UNION ALL
    SELECT 13, '1776', false UNION ALL
    SELECT 13, '1804', false UNION ALL
    SELECT 13, '1815', false UNION ALL
    
    -- Q14: Genghis Khan
    SELECT 14, 'Mongol leader', true UNION ALL
    SELECT 14, 'Chinese emperor', false UNION ALL
    SELECT 14, 'Japanese shogun', false UNION ALL
    SELECT 14, 'Indian maharaja', false UNION ALL
    
    -- Q15: Age of Enlightenment
    SELECT 15, 'Intellectual movement', true UNION ALL
    SELECT 15, 'Religious crusade', false UNION ALL
    SELECT 15, 'Military campaign', false UNION ALL
    SELECT 15, 'Economic policy', false UNION ALL
    
    -- Q16: Leonardo da Vinci
    SELECT 16, 'Renaissance artist', true UNION ALL
    SELECT 16, 'Medieval knight', false UNION ALL
    SELECT 16, 'Roman emperor', false UNION ALL
    SELECT 16, 'Greek philosopher', false UNION ALL
    
    -- Q17: Black Death
    SELECT 17, 'Bubonic plague', true UNION ALL
    SELECT 17, 'Smallpox', false UNION ALL
    SELECT 17, 'Influenza', false UNION ALL
    SELECT 17, 'Cholera', false UNION ALL
    
    -- Q18: Alexander the Great
    SELECT 18, 'Macedonian king', true UNION ALL
    SELECT 18, 'Roman general', false UNION ALL
    SELECT 18, 'Greek philosopher', false UNION ALL
    SELECT 18, 'Persian emperor', false UNION ALL
    
    -- Q19: Crusades
    SELECT 19, 'Religious wars', true UNION ALL
    SELECT 19, 'Trade expeditions', false UNION ALL
    SELECT 19, 'Scientific explorations', false UNION ALL
    SELECT 19, 'Colonial conquests', false UNION ALL
    
    -- Q20: Julius Caesar
    SELECT 20, 'Roman dictator', true UNION ALL
    SELECT 20, 'Greek philosopher', false UNION ALL
    SELECT 20, 'Egyptian pharaoh', false UNION ALL
    SELECT 20, 'Persian emperor', false
) o ON q.rn = o.qnum;

-- Geography Questions
INSERT INTO questions (category_id, difficulty, content)
SELECT 
    (SELECT id FROM categories WHERE name = 'Geography'),
    CASE WHEN RAND() < 0.33 THEN 'EASY' WHEN RAND() < 0.66 THEN 'MEDIUM' ELSE 'HARD' END,
    content
FROM (
    SELECT 'What is the largest continent by land area?' as content UNION ALL
    SELECT 'Which is the longest river in the world?' UNION ALL
    SELECT 'What is the capital of Australia?' UNION ALL
    SELECT 'Which is the largest ocean on Earth?' UNION ALL
    SELECT 'What is the highest mountain peak in the world?' UNION ALL
    SELECT 'Which country has the largest population?' UNION ALL
    SELECT 'What is the capital of Canada?' UNION ALL
    SELECT 'Which desert is the largest in the world?' UNION ALL
    SELECT 'What is the smallest country in the world?' UNION ALL
    SELECT 'Which continent contains the most countries?' UNION ALL
    SELECT 'What is the capital of Brazil?' UNION ALL
    SELECT 'Which is the deepest ocean trench?' UNION ALL
    SELECT 'What is the largest island in the world?' UNION ALL
    SELECT 'Which country has the most lakes?' UNION ALL
    SELECT 'What is the capital of Japan?' UNION ALL
    SELECT 'Which is the driest continent?' UNION ALL
    SELECT 'What is the longest mountain range?' UNION ALL
    SELECT 'Which country has the most time zones?' UNION ALL
    SELECT 'What is the largest waterfall by volume?' UNION ALL
    SELECT 'Which sea is the saltiest?'
) AS temp;

-- Insert options for Geography questions (fixed version)
INSERT INTO options (question_id, content, is_correct)
SELECT 
    q.id,
    o.option_content,
    o.is_correct
FROM (
    SELECT id, ROW_NUMBER() OVER (ORDER BY id) as rn
    FROM questions 
    WHERE category_id = (SELECT id FROM categories WHERE name = 'Geography')
) q
JOIN (
    -- Q1: Largest continent
    SELECT 1 as qnum, 'Asia' as option_content, true as is_correct UNION ALL
    SELECT 1, 'Africa', false UNION ALL
    SELECT 1, 'North America', false UNION ALL
    SELECT 1, 'Europe', false UNION ALL
    
    -- Q2: Longest river
    SELECT 2, 'Nile', true UNION ALL
    SELECT 2, 'Amazon', false UNION ALL
    SELECT 2, 'Mississippi', false UNION ALL
    SELECT 2, 'Yangtze', false UNION ALL
    
    -- Q3: Capital of Australia
    SELECT 3, 'Canberra', true UNION ALL
    SELECT 3, 'Sydney', false UNION ALL
    SELECT 3, 'Melbourne', false UNION ALL
    SELECT 3, 'Perth', false UNION ALL
    
    -- Q4: Largest ocean
    SELECT 4, 'Pacific Ocean', true UNION ALL
    SELECT 4, 'Atlantic Ocean', false UNION ALL
    SELECT 4, 'Indian Ocean', false UNION ALL
    SELECT 4, 'Arctic Ocean', false UNION ALL
    
    -- Q5: Highest peak
    SELECT 5, 'Mount Everest', true UNION ALL
    SELECT 5, 'K2', false UNION ALL
    SELECT 5, 'Kangchenjunga', false UNION ALL
    SELECT 5, 'Mount Kilimanjaro', false UNION ALL
    
    -- Q6: Largest population
    SELECT 6, 'China', true UNION ALL
    SELECT 6, 'India', false UNION ALL
    SELECT 6, 'United States', false UNION ALL
    SELECT 6, 'Indonesia', false UNION ALL
    
    -- Q7: Capital of Canada
    SELECT 7, 'Ottawa', true UNION ALL
    SELECT 7, 'Toronto', false UNION ALL
    SELECT 7, 'Vancouver', false UNION ALL
    SELECT 7, 'Montreal', false UNION ALL
    
    -- Q8: Largest desert
    SELECT 8, 'Sahara Desert', true UNION ALL
    SELECT 8, 'Arabian Desert', false UNION ALL
    SELECT 8, 'Gobi Desert', false UNION ALL
    SELECT 8, 'Antarctica', false UNION ALL
    
    -- Q9: Smallest country
    SELECT 9, 'Vatican City', true UNION ALL
    SELECT 9, 'Monaco', false UNION ALL
    SELECT 9, 'San Marino', false UNION ALL
    SELECT 9, 'Liechtenstein', false UNION ALL
    
    -- Q10: Most countries
    SELECT 10, 'Africa', true UNION ALL
    SELECT 10, 'Europe', false UNION ALL
    SELECT 10, 'Asia', false UNION ALL
    SELECT 10, 'South America', false UNION ALL
    
    -- Q11: Capital of Brazil
    SELECT 11, 'Brasilia', true UNION ALL
    SELECT 11, 'Rio de Janeiro', false UNION ALL
    SELECT 11, 'São Paulo', false UNION ALL
    SELECT 11, 'Salvador', false UNION ALL
    
    -- Q12: Deepest ocean trench
    SELECT 12, 'Mariana Trench', true UNION ALL
    SELECT 12, 'Puerto Rico Trench', false UNION ALL
    SELECT 12, 'Japan Trench', false UNION ALL
    SELECT 12, 'Philippine Trench', false UNION ALL
    
    -- Q13: Largest island
    SELECT 13, 'Greenland', true UNION ALL
    SELECT 13, 'New Guinea', false UNION ALL
    SELECT 13, 'Borneo', false UNION ALL
    SELECT 13, 'Madagascar', false UNION ALL
    
    -- Q14: Most lakes
    SELECT 14, 'Canada', true UNION ALL
    SELECT 14, 'Russia', false UNION ALL
    SELECT 14, 'United States', false UNION ALL
    SELECT 14, 'Finland', false UNION ALL
    
    -- Q15: Capital of Japan
    SELECT 15, 'Tokyo', true UNION ALL
    SELECT 15, 'Kyoto', false UNION ALL
    SELECT 15, 'Osaka', false UNION ALL
    SELECT 15, 'Yokohama', false UNION ALL
    
    -- Q16: Driest continent
    SELECT 16, 'Antarctica', true UNION ALL
    SELECT 16, 'Australia', false UNION ALL
    SELECT 16, 'Africa', false UNION ALL
    SELECT 16, 'Asia', false UNION ALL
    
    -- Q17: Longest mountain range
    SELECT 17, 'Andes Mountains', true UNION ALL
    SELECT 17, 'Rocky Mountains', false UNION ALL
    SELECT 17, 'Himalayas', false UNION ALL
    SELECT 17, 'Alps', false UNION ALL
    
    -- Q18: Most time zones
    SELECT 18, 'France', true UNION ALL
    SELECT 18, 'Russia', false UNION ALL
    SELECT 18, 'United States', false UNION ALL
    SELECT 18, 'China', false UNION ALL
    
    -- Q19: Largest waterfall
    SELECT 19, 'Angel Falls', true UNION ALL
    SELECT 19, 'Victoria Falls', false UNION ALL
    SELECT 19, 'Niagara Falls', false UNION ALL
    SELECT 19, 'Iguazu Falls', false UNION ALL
    
    -- Q20: Saltiest sea
    SELECT 20, 'Dead Sea', true UNION ALL
    SELECT 20, 'Red Sea', false UNION ALL
    SELECT 20, 'Mediterranean Sea', false UNION ALL
    SELECT 20, 'Black Sea', false
) o ON q.rn = o.qnum;

-- Literature Questions
INSERT INTO questions (category_id, difficulty, content)
SELECT 
    (SELECT id FROM categories WHERE name = 'Literature'),
    CASE WHEN RAND() < 0.33 THEN 'EASY' WHEN RAND() < 0.66 THEN 'MEDIUM' ELSE 'HARD' END,
    content
FROM (
    SELECT 'Who wrote "Romeo and Juliet"?' as content UNION ALL
    SELECT 'What is the first book of the Harry Potter series?' UNION ALL
    SELECT 'Who wrote "1984"?' UNION ALL
    SELECT 'What is the name of the hobbit in "The Lord of the Rings"?' UNION ALL
    SELECT 'Who wrote "Pride and Prejudice"?' UNION ALL
    SELECT 'Which novel begins with "Call me Ishmael"?' UNION ALL
    SELECT 'Who wrote "The Divine Comedy"?' UNION ALL
    SELECT 'What is the name of Don Quixote''s horse?' UNION ALL
    SELECT 'Who wrote "War and Peace"?' UNION ALL
    SELECT 'Which Shakespeare play features the line "To be, or not to be"?' UNION ALL
    SELECT 'Who wrote "The Catcher in the Rye"?' UNION ALL
    SELECT 'What is the title of the first book in "The Chronicles of Narnia"?' UNION ALL
    SELECT 'Who wrote "The Great Gatsby"?' UNION ALL
    SELECT 'What is the name of the main character in "Oliver Twist"?' UNION ALL
    SELECT 'Who wrote "One Hundred Years of Solitude"?' UNION ALL
    SELECT 'What is the setting of "The Count of Monte Cristo"?' UNION ALL
    SELECT 'Who wrote "The Odyssey"?' UNION ALL
    SELECT 'What is the name of the monster in "Frankenstein"?' UNION ALL
    SELECT 'Who wrote "The Art of War"?' UNION ALL
    SELECT 'What is the real name of George Orwell?'
) AS temp;

-- Insert options for Literature questions (fixed version)
INSERT INTO options (question_id, content, is_correct)
SELECT 
    q.id,
    o.option_content,
    o.is_correct
FROM (
    SELECT id, ROW_NUMBER() OVER (ORDER BY id) as rn
    FROM questions 
    WHERE category_id = (SELECT id FROM categories WHERE name = 'Literature')
) q
JOIN (
    -- Q1: Romeo and Juliet author
    SELECT 1 as qnum, 'William Shakespeare' as option_content, true as is_correct UNION ALL
    SELECT 1, 'Charles Dickens', false UNION ALL
    SELECT 1, 'Christopher Marlowe', false UNION ALL
    SELECT 1, 'Geoffrey Chaucer', false UNION ALL
    
    -- Q2: First Harry Potter book
    SELECT 2, 'Harry Potter and the Philosopher''s Stone', true UNION ALL
    SELECT 2, 'Harry Potter and the Chamber of Secrets', false UNION ALL
    SELECT 2, 'Harry Potter and the Prisoner of Azkaban', false UNION ALL
    SELECT 2, 'Harry Potter and the Goblet of Fire', false UNION ALL
    
    -- Q3: 1984 author
    SELECT 3, 'George Orwell', true UNION ALL
    SELECT 3, 'Aldous Huxley', false UNION ALL
    SELECT 3, 'Ray Bradbury', false UNION ALL
    SELECT 3, 'Philip K. Dick', false UNION ALL
    
    -- Q4: LOTR hobbit
    SELECT 4, 'Frodo Baggins', true UNION ALL
    SELECT 4, 'Bilbo Baggins', false UNION ALL
    SELECT 4, 'Samwise Gamgee', false UNION ALL
    SELECT 4, 'Pippin Took', false UNION ALL
    
    -- Q5: Pride and Prejudice author
    SELECT 5 as qnum, 'Jane Austen' as option_content, true as is_correct UNION ALL
    SELECT 5, 'Emily Bronte', false UNION ALL
    SELECT 5, 'Charlotte Bronte', false UNION ALL
    SELECT 5, 'Mary Shelley', false UNION ALL
    
    -- Q6: Call me Ishmael novel
    SELECT 6, 'Moby Dick', true UNION ALL
    SELECT 6, 'The Old Man and the Sea', false UNION ALL
    SELECT 6, 'Treasure Island', false UNION ALL
    SELECT 6, 'Robinson Crusoe', false UNION ALL
    
    -- Q7: Divine Comedy author
    SELECT 7, 'Dante Alighieri', true UNION ALL
    SELECT 7, 'Giovanni Boccaccio', false UNION ALL
    SELECT 7, 'Francesco Petrarca', false UNION ALL
    SELECT 7, 'Niccolò Machiavelli', false UNION ALL
    
    -- Q8: Don Quixote's horse
    SELECT 8, 'Rocinante', true UNION ALL
    SELECT 8, 'Bucephalus', false UNION ALL
    SELECT 8, 'Babieca', false UNION ALL
    SELECT 8, 'Pegasus', false UNION ALL
    
    -- Q9: War and Peace author
    SELECT 9, 'Leo Tolstoy', true UNION ALL
    SELECT 9, 'Fyodor Dostoevsky', false UNION ALL
    SELECT 9, 'Anton Chekhov', false UNION ALL
    SELECT 9, 'Ivan Turgenev', false UNION ALL
    
    -- Q10: To be or not to be play
    SELECT 10, 'Hamlet', true UNION ALL
    SELECT 10, 'Macbeth', false UNION ALL
    SELECT 10, 'King Lear', false UNION ALL
    SELECT 10, 'Othello', false UNION ALL
    
    -- Q11: Catcher in the Rye author
    SELECT 11, 'J.D. Salinger', true UNION ALL
    SELECT 11, 'Ernest Hemingway', false UNION ALL
    SELECT 11, 'F. Scott Fitzgerald', false UNION ALL
    SELECT 11, 'John Steinbeck', false UNION ALL
    
    -- Q12: First Narnia book
    SELECT 12, 'The Lion, the Witch and the Wardrobe', true UNION ALL
    SELECT 12, 'Prince Caspian', false UNION ALL
    SELECT 12, 'The Magician''s Nephew', false UNION ALL
    SELECT 12, 'The Horse and His Boy', false UNION ALL
    
    -- Q13: Great Gatsby author
    SELECT 13, 'F. Scott Fitzgerald', true UNION ALL
    SELECT 13, 'Ernest Hemingway', false UNION ALL
    SELECT 13, 'William Faulkner', false UNION ALL
    SELECT 13, 'John Steinbeck', false UNION ALL
    
    -- Q14: Oliver Twist character
    SELECT 14, 'Oliver Twist', true UNION ALL
    SELECT 14, 'David Copperfield', false UNION ALL
    SELECT 14, 'Nicholas Nickleby', false UNION ALL
    SELECT 14, 'Pip', false UNION ALL
    
    -- Q15: 100 Years of Solitude author
    SELECT 15, 'Gabriel García Márquez', true UNION ALL
    SELECT 15, 'Jorge Luis Borges', false UNION ALL
    SELECT 15, 'Pablo Neruda', false UNION ALL
    SELECT 15, 'Isabel Allende', false UNION ALL
    
    -- Q16: Count of Monte Cristo setting
    SELECT 16, 'France', true UNION ALL
    SELECT 16, 'Italy', false UNION ALL
    SELECT 16, 'Spain', false UNION ALL
    SELECT 16, 'England', false UNION ALL
    
    -- Q17: Odyssey author
    SELECT 17, 'Homer', true UNION ALL
    SELECT 17, 'Virgil', false UNION ALL
    SELECT 17, 'Sophocles', false UNION ALL
    SELECT 17, 'Euripides', false UNION ALL
    
    -- Q18: Frankenstein monster name
    SELECT 18, 'The Creature', true UNION ALL
    SELECT 18, 'Frankenstein', false UNION ALL
    SELECT 18, 'Adam', false UNION ALL
    SELECT 18, 'Monster', false UNION ALL
    
    -- Q19: Art of War author
    SELECT 19, 'Sun Tzu', true UNION ALL
    SELECT 19, 'Confucius', false UNION ALL
    SELECT 19, 'Lao Tzu', false UNION ALL
    SELECT 19, 'Mencius', false UNION ALL
    
    -- Q20: George Orwell real name
    SELECT 20, 'Eric Arthur Blair', true UNION ALL
    SELECT 20, 'Samuel Clemens', false UNION ALL
    SELECT 20, 'Charles Dodgson', false UNION ALL
    SELECT 20, 'Eric Stanley Gardner', false
) o ON q.rn = o.qnum;

-- Mathematics Questions
INSERT INTO questions (category_id, difficulty, content)
SELECT 
    (SELECT id FROM categories WHERE name = 'Mathematics'),
    CASE WHEN RAND() < 0.33 THEN 'EASY' WHEN RAND() < 0.66 THEN 'MEDIUM' ELSE 'HARD' END,
    content
FROM (
    SELECT 'What is the value of π (pi) to two decimal places?' as content UNION ALL
    SELECT 'What is the square root of 144?' UNION ALL
    SELECT 'What is the next prime number after 7?' UNION ALL
    SELECT 'What is 15% of 200?' UNION ALL
    SELECT 'What is the sum of angles in a triangle?' UNION ALL
    SELECT 'What is the formula for the area of a circle?' UNION ALL
    SELECT 'What is 5 factorial (5!)?' UNION ALL
    SELECT 'What is the value of x in 2x + 5 = 15?' UNION ALL
    SELECT 'What is the Pythagorean theorem?' UNION ALL
    SELECT 'What is the sum of the first 10 natural numbers?' UNION ALL
    SELECT 'What is the area of a square with side length 8?' UNION ALL
    SELECT 'What is the least common multiple of 6 and 8?' UNION ALL
    SELECT 'What is the value of x² when x = -3?' UNION ALL
    SELECT 'What is the perimeter of a rectangle with length 5 and width 3?' UNION ALL
    SELECT 'What is the sum of interior angles of a hexagon?' UNION ALL
    SELECT 'What is the slope of a horizontal line?' UNION ALL
    SELECT 'What is the value of log₁₀(100)?' UNION ALL
    SELECT 'What is the geometric mean of 4 and 16?' UNION ALL
    SELECT 'What is 2 to the power of 8?' UNION ALL
    SELECT 'What is the median of numbers 2, 5, 7, 8, 12?'
) AS temp;

-- Insert options for Mathematics questions (fixed version)
INSERT INTO options (question_id, content, is_correct)
SELECT 
    q.id,
    o.option_content,
    o.is_correct
FROM (
    SELECT id, ROW_NUMBER() OVER (ORDER BY id) as rn
    FROM questions 
    WHERE category_id = (SELECT id FROM categories WHERE name = 'Mathematics')
) q
JOIN (
    -- Q1: Value of pi
    SELECT 1 as qnum, '3.14' as option_content, true as is_correct UNION ALL
    SELECT 1, '3.16', false UNION ALL
    SELECT 1, '3.12', false UNION ALL
    SELECT 1, '3.18', false UNION ALL
    
    -- Q2: Square root of 144
    SELECT 2, '12', true UNION ALL
    SELECT 2, '14', false UNION ALL
    SELECT 2, '10', false UNION ALL
    SELECT 2, '16', false UNION ALL
    
    -- Q3: Prime after 7
    SELECT 3, '11', true UNION ALL
    SELECT 3, '9', false UNION ALL
    SELECT 3, '10', false UNION ALL
    SELECT 3, '13', false UNION ALL
    
    -- Q4: 15% of 200
    SELECT 4, '30', true UNION ALL
    SELECT 4, '20', false UNION ALL
    SELECT 4, '40', false UNION ALL
    SELECT 4, '25', false UNION ALL
    
    -- Q5: Triangle angles
    SELECT 5, '180 degrees', true UNION ALL
    SELECT 5, '360 degrees', false UNION ALL
    SELECT 5, '90 degrees', false UNION ALL
    SELECT 5, '270 degrees', false UNION ALL
    
    -- Q6: Circle area formula
    SELECT 6, 'πr²', true UNION ALL
    SELECT 6, '2πr', false UNION ALL
    SELECT 6, 'πd', false UNION ALL
    SELECT 6, '2πr²', false UNION ALL
    
    -- Q7: 5 factorial
    SELECT 7, '120', true UNION ALL
    SELECT 7, '60', false UNION ALL
    SELECT 7, '100', false UNION ALL
    SELECT 7, '24', false UNION ALL
    
    -- Q8: Solve for x
    SELECT 8, '5', true UNION ALL
    SELECT 8, '4', false UNION ALL
    SELECT 8, '6', false UNION ALL
    SELECT 8, '7', false UNION ALL
    
    -- Q9: Pythagorean theorem
    SELECT 9, 'a² + b² = c²', true UNION ALL
    SELECT 9, 'a + b = c', false UNION ALL
    SELECT 9, 'a² - b² = c²', false UNION ALL
    SELECT 9, '2a + 2b = c', false UNION ALL
    
    -- Q10: Sum of first 10 numbers
    SELECT 10, '55', true UNION ALL
    SELECT 10, '45', false UNION ALL
    SELECT 10, '50', false UNION ALL
    SELECT 10, '60', false UNION ALL
    
    -- Q11: Square area
    SELECT 11, '64', true UNION ALL
    SELECT 11, '32', false UNION ALL
    SELECT 11, '16', false UNION ALL
    SELECT 11, '56', false UNION ALL
    
    -- Q12: LCM of 6 and 8
    SELECT 12, '24', true UNION ALL
    SELECT 12, '12', false UNION ALL
    SELECT 12, '48', false UNION ALL
    SELECT 12, '16', false UNION ALL
    
    -- Q13: x² when x = -3
    SELECT 13, '9', true UNION ALL
    SELECT 13, '-9', false UNION ALL
    SELECT 13, '6', false UNION ALL
    SELECT 13, '-6', false UNION ALL
    
    -- Q14: Rectangle perimeter
    SELECT 14, '16', true UNION ALL
    SELECT 14, '15', false UNION ALL
    SELECT 14, '18', false UNION ALL
    SELECT 14, '20', false UNION ALL
    
    -- Q15: Hexagon angles
    SELECT 15, '720 degrees', true UNION ALL
    SELECT 15, '540 degrees', false UNION ALL
    SELECT 15, '360 degrees', false UNION ALL
    SELECT 15, '900 degrees', false UNION ALL
    
    -- Q16: Horizontal line slope
    SELECT 16, '0', true UNION ALL
    SELECT 16, '1', false UNION ALL
    SELECT 16, 'undefined', false UNION ALL
    SELECT 16, 'infinity', false UNION ALL
    
    -- Q17: Log of 100
    SELECT 17, '2', true UNION ALL
    SELECT 17, '1', false UNION ALL
    SELECT 17, '10', false UNION ALL
    SELECT 17, '100', false UNION ALL
    
    -- Q18: Geometric mean
    SELECT 18, '8', true UNION ALL
    SELECT 18, '10', false UNION ALL
    SELECT 18, '6', false UNION ALL
    SELECT 18, '12', false UNION ALL
    
    -- Q19: 2^8
    SELECT 19, '256', true UNION ALL
    SELECT 19, '128', false UNION ALL
    SELECT 19, '512', false UNION ALL
    SELECT 19, '64', false UNION ALL
    
    -- Q20: Median
    SELECT 20, '7', true UNION ALL
    SELECT 20, '5', false UNION ALL
    SELECT 20, '8', false UNION ALL
    SELECT 20, '6', false
) o ON q.rn = o.qnum;

-- Technology Questions
INSERT INTO questions (category_id, difficulty, content)
SELECT 
    (SELECT id FROM categories WHERE name = 'Technology'),
    CASE WHEN RAND() < 0.33 THEN 'EASY' WHEN RAND() < 0.66 THEN 'MEDIUM' ELSE 'HARD' END,
    content
FROM (
    SELECT 'What does CPU stand for?' as content UNION ALL
    SELECT 'Who is the co-founder of Microsoft?' UNION ALL
    SELECT 'What does HTML stand for?' UNION ALL
    SELECT 'Which programming language is known as the "father" of all programming languages?' UNION ALL
    SELECT 'What does URL stand for?' UNION ALL
    SELECT 'What is the most popular operating system for smartphones?' UNION ALL
    SELECT 'Who invented the World Wide Web?' UNION ALL
    SELECT 'What does Wi-Fi stand for?' UNION ALL
    SELECT 'What is the name of Apple''s voice assistant?' UNION ALL
    SELECT 'What does RAM stand for?' UNION ALL
    SELECT 'Which company owns Android?' UNION ALL
    SELECT 'What is the main function of a firewall?' UNION ALL
    SELECT 'What does SQL stand for?' UNION ALL
    SELECT 'Who is the CEO of Tesla?' UNION ALL
    SELECT 'What does PDF stand for?' UNION ALL
    SELECT 'What type of computer memory is volatile?' UNION ALL
    SELECT 'What programming language was created by James Gosling?' UNION ALL
    SELECT 'What does GUI stand for?' UNION ALL
    SELECT 'Which company developed the Python programming language?' UNION ALL
    SELECT 'What is the purpose of an IP address?'
) AS temp;

-- Insert options for Technology questions (fixed version)
INSERT INTO options (question_id, content, is_correct)
SELECT 
    q.id,
    o.option_content,
    o.is_correct
FROM (
    SELECT id, ROW_NUMBER() OVER (ORDER BY id) as rn
    FROM questions 
    WHERE category_id = (SELECT id FROM categories WHERE name = 'Technology')
) q
JOIN (
    -- Q1: CPU meaning
    SELECT 1 as qnum, 'Central Processing Unit' as option_content, true as is_correct UNION ALL
    SELECT 1, 'Central Program Utility', false UNION ALL
    SELECT 1, 'Computer Personal Unit', false UNION ALL
    SELECT 1, 'Control Processing Unit', false UNION ALL
    
    -- Q2: Microsoft co-founder
    SELECT 2, 'Bill Gates', true UNION ALL
    SELECT 2, 'Steve Jobs', false UNION ALL
    SELECT 2, 'Mark Zuckerberg', false UNION ALL
    SELECT 2, 'Jeff Bezos', false UNION ALL
    
    -- Q3: HTML meaning
    SELECT 3, 'HyperText Markup Language', true UNION ALL
    SELECT 3, 'High Tech Modern Language', false UNION ALL
    SELECT 3, 'HyperText Management Language', false UNION ALL
    SELECT 3, 'High Text Markup Language', false UNION ALL
    
    -- Q4: Father of programming
    SELECT 4, 'FORTRAN', true UNION ALL
    SELECT 4, 'COBOL', false UNION ALL
    SELECT 4, 'BASIC', false UNION ALL
    SELECT 4, 'Pascal', false UNION ALL
    
    -- Q5: URL meaning
    SELECT 5, 'Uniform Resource Locator', true UNION ALL
    SELECT 5, 'Universal Resource Link', false UNION ALL
    SELECT 5, 'Unified Resource Locator', false UNION ALL
    SELECT 5, 'Universal Reference Link', false UNION ALL
    
    -- Q6: Popular smartphone OS
    SELECT 6, 'Android', true UNION ALL
    SELECT 6, 'iOS', false UNION ALL
    SELECT 6, 'Windows', false UNION ALL
    SELECT 6, 'Linux', false UNION ALL
    
    -- Q7: WWW inventor
    SELECT 7, 'Tim Berners-Lee', true UNION ALL
    SELECT 7, 'Vint Cerf', false UNION ALL
    SELECT 7, 'Steve Wozniak', false UNION ALL
    SELECT 7, 'Larry Page', false UNION ALL
    
    -- Q8: Wi-Fi meaning
    SELECT 8, 'Wireless Fidelity', true UNION ALL
    SELECT 8, 'Wireless Finance', false UNION ALL
    SELECT 8, 'Wireless Finder', false UNION ALL
    SELECT 8, 'Wireless Filter', false UNION ALL
    
    -- Q9: Apple assistant
    SELECT 9, 'Siri', true UNION ALL
    SELECT 9, 'Alexa', false UNION ALL
    SELECT 9, 'Cortana', false UNION ALL
    SELECT 9, 'Google Assistant', false UNION ALL
    
    -- Q10: RAM meaning
    SELECT 10, 'Random Access Memory', true UNION ALL
    SELECT 10, 'Read Access Memory', false UNION ALL
    SELECT 10, 'Random Available Memory', false UNION ALL
    SELECT 10, 'Rapid Access Memory', false UNION ALL
    
    -- Q11: Android owner
    SELECT 11, 'Google', true UNION ALL
    SELECT 11, 'Apple', false UNION ALL
    SELECT 11, 'Microsoft', false UNION ALL
    SELECT 11, 'Samsung', false UNION ALL
    
    -- Q12: Firewall function
    SELECT 12, 'Network Security', true UNION ALL
    SELECT 12, 'Data Storage', false UNION ALL
    SELECT 12, 'File Sharing', false UNION ALL
    SELECT 12, 'Data Processing', false UNION ALL
    
    -- Q13: SQL meaning
    SELECT 13, 'Structured Query Language', true UNION ALL
    SELECT 13, 'System Query Language', false UNION ALL
    SELECT 13, 'Standard Query Logic', false UNION ALL
    SELECT 13, 'Structured Question Language', false UNION ALL
    
    -- Q14: Tesla CEO
    SELECT 14, 'Elon Musk', true UNION ALL
    SELECT 14, 'Jeff Bezos', false UNION ALL
    SELECT 14, 'Tim Cook', false UNION ALL
    SELECT 14, 'Satya Nadella', false UNION ALL
    
    -- Q15: PDF meaning
    SELECT 15, 'Portable Document Format', true UNION ALL
    SELECT 15, 'Personal Document File', false UNION ALL
    SELECT 15, 'Printed Document Format', false UNION ALL
    SELECT 15, 'Public Document File', false UNION ALL
    
    -- Q16: Volatile memory
    SELECT 16, 'RAM', true UNION ALL
    SELECT 16, 'ROM', false UNION ALL
    SELECT 16, 'Hard Drive', false UNION ALL
    SELECT 16, 'Flash Drive', false UNION ALL
    
    -- Q17: Java creator
    SELECT 17, 'James Gosling', true UNION ALL
    SELECT 17, 'Bjarne Stroustrup', false UNION ALL
    SELECT 17, 'Guido van Rossum', false UNION ALL
    SELECT 17, 'Dennis Ritchie', false UNION ALL
    
    -- Q18: GUI meaning
    SELECT 18, 'Graphical User Interface', true UNION ALL
    SELECT 18, 'General User Input', false UNION ALL
    SELECT 18, 'Graphical Unity Input', false UNION ALL
    SELECT 18, 'General User Interface', false UNION ALL
    
    -- Q19: Python developer
    SELECT 19, 'Python Software Foundation', true UNION ALL
    SELECT 19, 'Microsoft', false UNION ALL
    SELECT 19, 'Oracle', false UNION ALL
    SELECT 19, 'IBM', false UNION ALL
    
    -- Q20: IP address purpose
    SELECT 20, 'Device Identification', true UNION ALL
    SELECT 20, 'Data Storage', false UNION ALL
    SELECT 20, 'File Transfer', false UNION ALL
    SELECT 20, 'Security Protocol', false
) o ON q.rn = o.qnum;
