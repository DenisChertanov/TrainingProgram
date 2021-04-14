CREATE FUNCTION get_all_exercises() RETURNS SETOF Exercise AS $$
	SELECT * FROM Exercise;
$$ LANGUAGE SQL;


CREATE FUNCTION get_all_sorted_exercises() RETURNS SETOF Exercise AS $$
	SELECT * FROM Exercise ORDER BY Title;
$$ LANGUAGE SQL;


CREATE FUNCTION get_all_sorted_exercises_titles() RETURNS SETOF TEXT AS $$
	SELECT Title FROM Exercise ORDER BY Title;
$$ LANGUAGE SQL;



CREATE FUNCTION get_all_exercises_tags() RETURNS SETOF Exercisetag AS $$
	SELECT * FROM ExerciseTag;
$$ LANGUAGE SQL;

CREATE FUNCTION get_all_exercises_to_tags() RETURNS SETOF ExerciseToTag AS $$
	SELECT * FROM ExerciseToTag;
$$ LANGUAGE SQL;

CREATE FUNCTION get_all_trainings() RETURNS SETOF Training AS $$
	SELECT * FROM Training;
$$ LANGUAGE SQL;

CREATE FUNCTION get_all_accounts() RETURNS SETOF Account AS $$
	SELECT * FROM Account;
$$ LANGUAGE SQL;

CREATE FUNCTION get_all_trainings_exercises() RETURNS SETOF TrainingExercise AS $$
	SELECT * FROM TrainingExercise;
$$ LANGUAGE SQL;

CREATE FUNCTION get_all_trainings_exercises_success() RETURNS SETOF TrainingExerciseSuccess AS $$
	SELECT * FROM TrainingExerciseSuccess;
$$ LANGUAGE SQL;

CREATE FUNCTION get_all_trainings_exercises_notes() RETURNS SETOF TrainingExerciseNote AS $$
	SELECT * FROM TrainingExerciseNote;
$$ LANGUAGE SQL;

CREATE FUNCTION get_sample_exercise() RETURNS Exercise AS $$
	SELECT * FROM Exercise WHERE Title = 'Жим лежа';
$$ LANGUAGE SQL;



CREATE FUNCTION get_all_training_types() RETURNS SETOF TrainingType AS $$
	SELECT * FROM TrainingType;
$$ LANGUAGE SQL;

CREATE FUNCTION get_all_exercise_training_types() RETURNS SETOF ExerciseTrainingType AS $$
	SELECT * FROM ExerciseTrainingType;
$$ LANGUAGE SQL;

CREATE FUNCTION get_all_muscles() RETURNS SETOF Muscle AS $$
	SELECT * FROM Muscle;
$$ LANGUAGE SQL;

CREATE FUNCTION get_all_target_muscles() RETURNS SETOF TargetMuscle AS $$
	SELECT * FROM TargetMuscle;
$$ LANGUAGE SQL;

CREATE FUNCTION get_all_body_conditions() RETURNS SETOF BodyCondition AS $$
	SELECT * FROM BodyCondition;
$$ LANGUAGE SQL;

CREATE FUNCTION get_all_friendships() RETURNS SETOF Friendship AS $$
	SELECT * FROM Friendship;
$$ LANGUAGE SQL;



CREATE FUNCTION add_training_exercise_success(INTEGER, INTEGER, INTEGER, INTEGER, INTEGER, INTEGER, INTEGER, INTEGER, INTEGER) RETURNS INTEGER AS $$
	INSERT INTO TrainingExerciseSuccess(AccountId, TrainingId, DayOfWeek, OrderNumber, ExerciseId, SetNumber, RepsNum, Weight, Timer)
	VALUES ('$1', '$2', '$3', '$4', '$5', '$6', '$7', '$8', '$9');
$$ LANGUAGE SQL;