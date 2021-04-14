CREATE TABLE public.Account
(
	AccountId SERIAL PRIMARY KEY,
	Username VARCHAR(256) NOT NULL,
	HashPassword VARCHAR(256) NOT NULL,
	BirthDate DATE NOT NULL,
	RegistrationDate DATE NOT NULL,
	Sex VARCHAR(256) NOT NULL,
	TrainingId INTEGER,
	FirstName VARCHAR(256) NOT NULL,
	SecondName VARCHAR(256) NOT NULL
)

WITH (
    OIDS = FALSE
);

ALTER TABLE public.Account
    OWNER to dchertanov;



CREATE TABLE public.Training
(
	TrainingId SERIAL PRIMARY KEY,
	Title TEXT,
	AccountId SERIAL,
	FOREIGN KEY(AccountId) REFERENCES Account(AccountId) ON DELETE CASCADE
)

WITH (
    OIDS = FALSE
);

ALTER TABLE public.Training
    OWNER to dchertanov;



CREATE TABLE public.Friendship
(
	AccountId SERIAL,
	FriendId SERIAL,
	FOREIGN KEY(AccountId) REFERENCES Account(AccountId) ON DELETE CASCADE,
	FOREIGN KEY(FriendId) REFERENCES Account(AccountId) ON DELETE CASCADE,
	PRIMARY KEY(AccountId, FriendId)
)

WITH (
    OIDS = FALSE
);

ALTER TABLE public.Friendship
    OWNER to dchertanov;




CREATE TABLE public.BodyCondition
(
	AccountId SERIAL PRIMARY KEY,
	Weight REAL NOT NULL,
	Height REAL NOT NULL,
	Age INTEGER NOT NULL,
	BodyFatShare REAL CHECK (BodyFatShare >= 0 AND BodyFatShare <= 100),
	FOREIGN KEY(AccountId) REFERENCES Account(AccountId) ON DELETE CASCADE
)

WITH (
    OIDS = FALSE
);

ALTER TABLE public.BodyCondition
    OWNER to dchertanov;




CREATE TABLE public.Exercise
(
	ExerciseId SERIAL PRIMARY KEY,
	Title VARCHAR(256) NOT NULL,
	Description TEXT,
	VideoPath TEXT
)

WITH (
    OIDS = FALSE
);

ALTER TABLE public.Exercise
    OWNER to dchertanov;




CREATE TABLE public.FavoriteExercise
(
	AccountId SERIAL,
	ExerciseId SERIAL,
	FOREIGN KEY(AccountId) REFERENCES Account(AccountId) ON DELETE CASCADE,
	FOREIGN KEY(ExerciseId) REFERENCES Exercise(ExerciseId) ON DELETE CASCADE,
	PRIMARY KEY(AccountId, ExerciseId)
)

WITH (
    OIDS = FALSE
);

ALTER TABLE public.FavoriteExercise
    OWNER to dchertanov;




CREATE TABLE public.Muscle
(
	MuscleId SERIAL PRIMARY KEY,
	Title VARCHAR(256) NOT NULL
)

WITH (
    OIDS = FALSE
);

ALTER TABLE public.Muscle
    OWNER to dchertanov;




CREATE TABLE public.TargetMuscle
(
	ExerciseId SERIAL,
	MuscleId SERIAL,
	FOREIGN KEY(ExerciseId) REFERENCES Exercise(ExerciseId) ON DELETE CASCADE,
	FOREIGN KEY(MuscleId) REFERENCES Muscle(MuscleId) ON DELETE CASCADE,
	PRIMARY KEY(ExerciseId, MuscleId)
)

WITH (
    OIDS = FALSE
);

ALTER TABLE public.TargetMuscle
    OWNER to dchertanov;



CREATE TABLE public.ExerciseTag
(
	TagId SERIAL PRIMARY KEY,
	Title VARCHAR(256)
)

WITH (
    OIDS = FALSE
);

ALTER TABLE public.ExerciseTag
    OWNER to dchertanov;



CREATE TABLE public.ExerciseToTag
(
	ExerciseId SERIAL,
	TagId SERIAL,
	FOREIGN KEY(ExerciseId) REFERENCES Exercise(ExerciseId) ON DELETE CASCADE,
	FOREIGN KEY(TagId) REFERENCES ExerciseTag(TagId) ON DELETE CASCADE,
	PRIMARY KEY(ExerciseId, TagId)
)

WITH (
    OIDS = FALSE
);

ALTER TABLE public.ExerciseToTag
    OWNER to dchertanov;



CREATE TABLE public.TrainingExercise
(
	TrainingId SERIAL,
	DayOfWeek INTEGER CHECK (DayOfWeek >= 0 AND DayOfWeek < 7),
	OrderNumber INTEGER CHECK (OrderNumber > 0),
	ExerciseId SERIAL,
	SetNumber INTEGER CHECK (SetNumber > 0),
	RepsNum INTEGER CHECK (RepsNum >= 0),
	Timer INTEGER CHECK (Timer >= 0),
	AccountId INTEGER,
	FOREIGN KEY(AccountId) REFERENCES Account(AccountId) ON DELETE CASCADE,
	FOREIGN KEY(ExerciseId) REFERENCES Exercise(ExerciseId) ON DELETE CASCADE,
	FOREIGN KEY(TrainingId) REFERENCES Training(TrainingId) ON DELETE CASCADE,
	PRIMARY KEY(TrainingId, DayOfWeek, OrderNumber, SetNumber, AccountId)
)

WITH (
    OIDS = FALSE
);

ALTER TABLE public.TrainingExercise
    OWNER to dchertanov;



CREATE TABLE public.FavoriteTraining
(
	AccountId SERIAL,
	TrainingId SERIAL,
	FOREIGN KEY(AccountId) REFERENCES Account(AccountId) ON DELETE CASCADE,
	FOREIGN KEY(TrainingId) REFERENCES Training(TrainingId) ON DELETE CASCADE,
	PRIMARY KEY(AccountId, TrainingId)
)

WITH (
    OIDS = FALSE
);

ALTER TABLE public.FavoriteTraining
    OWNER to dchertanov;



CREATE TABLE public.TrainingExerciseSuccess
(
	AccountId SERIAL,
	TrainingId SERIAL,
	DayOfWeek INTEGER CHECK (DayOfWeek >= 0 AND DayOfWeek < 7),
	OrderNumber INTEGER CHECK (OrderNumber > 0),
	ExerciseId SERIAL,
	SetNumber INTEGER CHECK (SetNumber > 0),
	RepsNum INTEGER CHECK (RepsNum >= 0),
	Weight INTEGER CHECK (Weight >= 0),
	Timer INTEGER CHECK (Timer >= 0),
	FOREIGN KEY(ExerciseId) REFERENCES Exercise(ExerciseId) ON DELETE CASCADE,
	FOREIGN KEY(AccountId) REFERENCES Account(AccountId) ON DELETE CASCADE,
	FOREIGN KEY(TrainingId) REFERENCES Training(TrainingId) ON DELETE CASCADE,
	PRIMARY KEY(AccountId, TrainingId, DayOfWeek, OrderNumber, SetNumber)
)

WITH (
    OIDS = FALSE
);

ALTER TABLE public.TrainingExerciseSuccess
    OWNER to dchertanov;



CREATE TABLE public.TrainingExerciseNote
(
	TrainingId SERIAL,
	AccountId SERIAL,
	DayOfWeek INTEGER CHECK (DayOfWeek >= 0 AND DayOfWeek < 7),
	OrderNumber INTEGER CHECK (OrderNumber > 0),
	ExerciseId SERIAL,
	Note TEXT,
	FOREIGN KEY(ExerciseId) REFERENCES Exercise(ExerciseId) ON DELETE CASCADE,
	FOREIGN KEY(AccountId) REFERENCES Account(AccountId) ON DELETE CASCADE,
	FOREIGN KEY(TrainingId) REFERENCES Training(TrainingId) ON DELETE CASCADE,
	PRIMARY KEY(AccountId, TrainingId, DayOfWeek, OrderNumber)
)

WITH (
    OIDS = FALSE
);

ALTER TABLE public.TrainingExerciseNote
    OWNER to dchertanov;



CREATE TABLE public.TrainingType
(
	TypeId SERIAL PRIMARY KEY,
	Title VARCHAR(256) NOT NULL
)

WITH (
    OIDS = FALSE
);

ALTER TABLE public.TrainingType
    OWNER to dchertanov;




CREATE TABLE public.ExerciseTrainingType
(
	TypeId SERIAL,
	ExerciseId SERIAL,
	FOREIGN KEY(TypeId) REFERENCES TrainingType(TypeId) ON DELETE CASCADE,
	FOREIGN KEY(ExerciseId) REFERENCES Exercise(ExerciseId) ON DELETE CASCADE,
	PRIMARY KEY(TypeId, ExerciseId)
)

WITH (
    OIDS = FALSE
);

ALTER TABLE public.ExerciseTrainingType
    OWNER to dchertanov;