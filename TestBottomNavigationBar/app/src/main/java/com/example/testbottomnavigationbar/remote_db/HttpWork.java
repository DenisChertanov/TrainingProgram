    package com.example.testbottomnavigationbar.remote_db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.testbottomnavigationbar.MainActivity;
import com.example.testbottomnavigationbar.entities.ExerciseInTrainingEditing;
import com.example.testbottomnavigationbar.entities.InsertSetJSON;
import com.example.testbottomnavigationbar.entities.TrainingExerciseInstance;
import com.example.testbottomnavigationbar.remote_db.tasks.InsertNewExerciseTask;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpWork {
    private final String local_IP = "192.168.0.104";
    private final URL url_get_all_exercises = new URL("http://" + local_IP + ":3000/rpc/get_all_exercises");
    private final URL url_get_all_exercises_tags = new URL("http://" + local_IP + ":3000/rpc/get_all_exercises_tags");
    private final URL url_get_all_exercises_to_tags = new URL("http://" + local_IP + ":3000/rpc/get_all_exercises_to_tags");
    private final URL url_get_all_trainings = new URL("http://" + local_IP + ":3000/rpc/get_all_trainings");
    private final URL url_get_all_accounts = new URL("http://" + local_IP + ":3000/rpc/get_all_accounts");
    private final URL url_get_all_trainings_exercises = new URL("http://" + local_IP + ":3000/rpc/get_all_trainings_exercises");
    private final URL url_get_all_trainings_exercises_success = new URL("http://" + local_IP + ":3000/rpc/get_all_trainings_exercises_success");
    private final URL url_get_all_trainings_exercises_notes = new URL("http://" + local_IP + ":3000/rpc/get_all_trainings_exercises_notes");
    private final URL url_get_all_training_types = new URL("http://" + local_IP + ":3000/rpc/get_all_training_types");
    private final URL url_get_all_exercise_training_types = new URL("http://" + local_IP + ":3000/rpc/get_all_exercise_training_types");
    private final URL url_get_all_muscles = new URL("http://" + local_IP + ":3000/rpc/get_all_muscles");
    private final URL url_get_all_target_muscles = new URL("http://" + local_IP + ":3000/rpc/get_all_target_muscles");
    private final URL url_get_all_friendships = new URL("http://" + local_IP + ":3000/rpc/get_all_friendships");
    private final URL url_insert_training_exercise_success = new URL("http://" + local_IP + ":3000/trainingexercisesuccess");
    private final URL url_insert_exercise = new URL("http://" + local_IP + ":3000/exercise");
    private final URL url_insert_exercise_to_tag = new URL("http://" + local_IP + ":3000/exercisetotag");
    private final URL url_insert_target_muscle = new URL("http://" + local_IP + ":3000/targetmuscle");
    private final URL url_insert_training_exercise = new URL("http://" + local_IP + ":3000/trainingexercise");
    private final URL url_insert_training = new URL("http://" + local_IP + ":3000/training");
    private final URL url_insert_note = new URL("http://" + local_IP + ":3000/trainingexercisenote");
    private final URL url_insert_account = new URL("http://" + local_IP + ":3000/account");
    private final URL url_insert_body_condition = new URL("http://" + local_IP + ":3000/bodycondition");
    private final URL url_insert_friendship = new URL("http://" + local_IP + ":3000/friendship");
    private final URL url_get_all_body_conditions = new URL("http://" + local_IP + ":3000/rpc/get_all_body_conditions");

    public Exercise[] pullAllExercises() throws IOException {
        HttpURLConnection con = (HttpURLConnection) url_get_all_exercises.openConnection();
        tunePostConnection(con);

        String response = getResponseString(con);
        Gson gson = new Gson();

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "Exercises   " + response);

            Exercise[] q = gson.fromJson(response, Exercise[].class);
            Log.d(MainActivity.TEG, "videoPath   " + String.valueOf(q[0].getVideoPath()));
            Log.d(MainActivity.TEG, "videoPath   " + String.valueOf(q[1].getVideoPath()));
        }

        return gson.fromJson(response, Exercise[].class);
    }

    public ExerciseTag[] pullAllExerciseTags() throws IOException {
        HttpURLConnection con = (HttpURLConnection) url_get_all_exercises_tags.openConnection();
        tunePostConnection(con);

        String response = getResponseString(con);
        Gson gson = new Gson();

        return gson.fromJson(response, ExerciseTag[].class);
    }

    public ExerciseToTag[] pullAllExerciseToTag() throws IOException {
        HttpURLConnection con = (HttpURLConnection) url_get_all_exercises_to_tags.openConnection();
        tunePostConnection(con);

        String response = getResponseString(con);
        Gson gson = new Gson();

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, response);

            ExerciseToTag[] q = gson.fromJson(response, ExerciseToTag[].class);
            Log.d(MainActivity.TEG, q[0].getExerciseId() + " " + q[0].getTagId());
            Log.d(MainActivity.TEG, q[1].getExerciseId() + " " + q[1].getTagId());
        }

        return gson.fromJson(response, ExerciseToTag[].class);
    }

    public Training[] pullAllTrainings() throws IOException {
        HttpURLConnection con = (HttpURLConnection) url_get_all_trainings.openConnection();
        tunePostConnection(con);

        String response = getResponseString(con);
        Gson gson = new Gson();

        return gson.fromJson(response, Training[].class);
    }

    public Account[] pullAllAccounts() throws IOException {
        HttpURLConnection con = (HttpURLConnection) url_get_all_accounts.openConnection();
        tunePostConnection(con);

        String response = getResponseString(con);
        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "Response  " + response);
        }
        Gson gson = new Gson();

        return gson.fromJson(response, Account[].class);
    }

    public TrainingExercise[] pullAllTrainingExercises() throws IOException {
        HttpURLConnection con = (HttpURLConnection) url_get_all_trainings_exercises.openConnection();
        tunePostConnection(con);

        String response = getResponseString(con);
        Gson gson = new Gson();

        return gson.fromJson(response, TrainingExercise[].class);
    }

    public TrainingExerciseSuccess[] pullAllTrainingExerciseSuccess() throws IOException {
        HttpURLConnection con = (HttpURLConnection) url_get_all_trainings_exercises_success.openConnection();
        tunePostConnection(con);

        String response = getResponseString(con);
        Gson gson = new Gson();

        return gson.fromJson(response, TrainingExerciseSuccess[].class);
    }

    public TrainingExerciseNote[] pullAllTrainingExerciseNote() throws IOException {
        HttpURLConnection con = (HttpURLConnection) url_get_all_trainings_exercises_notes.openConnection();
        tunePostConnection(con);

        String response = getResponseString(con);
        Gson gson = new Gson();

        return gson.fromJson(response, TrainingExerciseNote[].class);
    }

    public TrainingType[] pullAllTrainingTypes() throws IOException {
        HttpURLConnection con = (HttpURLConnection) url_get_all_training_types.openConnection();
        tunePostConnection(con);

        String response = getResponseString(con);
        Gson gson = new Gson();

        return gson.fromJson(response, TrainingType[].class);
    }

    public ExerciseTrainingType[] pullAllExerciseTrainingTypes() throws IOException {
        HttpURLConnection con = (HttpURLConnection) url_get_all_exercise_training_types.openConnection();
        tunePostConnection(con);

        String response = getResponseString(con);
        Gson gson = new Gson();

        return gson.fromJson(response, ExerciseTrainingType[].class);
    }

    public Muscle[] pullAllMuscles() throws IOException {
        HttpURLConnection con = (HttpURLConnection) url_get_all_muscles.openConnection();
        tunePostConnection(con);

        String response = getResponseString(con);
        Gson gson = new Gson();

        return gson.fromJson(response, Muscle[].class);
    }

    public TargetMuscle[] pullAllTargetMuscles() throws IOException {
        HttpURLConnection con = (HttpURLConnection) url_get_all_target_muscles.openConnection();
        tunePostConnection(con);

        String response = getResponseString(con);
        Gson gson = new Gson();

        return gson.fromJson(response, TargetMuscle[].class);
    }

    public BodyCondition[] pullAllBodyConditions() throws IOException {
        HttpURLConnection con = (HttpURLConnection) url_get_all_body_conditions.openConnection();
        tunePostConnection(con);

        String response = getResponseString(con);
        Gson gson = new Gson();

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "BodyConditions:  " + response);
        }

        return gson.fromJson(response, BodyCondition[].class);
    }

    public Friendship[] pullAllFriendships() throws IOException {
        HttpURLConnection con = (HttpURLConnection) url_get_all_friendships.openConnection();
        tunePostConnection(con);

        String response = getResponseString(con);
        Gson gson = new Gson();

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "Friendships:  " + response);
        }

        return gson.fromJson(response, Friendship[].class);
    }

    public void insertTrainingExerciseSuccess(TrainingExerciseSuccess trainingExerciseSuccess) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url_insert_training_exercise_success.openConnection();
        tunePostConnection(con);

        String jsonInputString = trainingExerciseSuccess.getJSON();
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        }
    }

    public void insertTrainingExercise(TrainingExercise trainingExercise) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url_insert_training_exercise.openConnection();
        tunePostConnection(con);

        String jsonInputString = trainingExercise.getJSON();
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        }
    }

    public void insertTraining(Training training) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url_insert_training.openConnection();
        tunePostConnection(con);

        String jsonInputString = training.getJSON();
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        }
    }

    public void insertExercise(Exercise exercise) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url_insert_exercise.openConnection();
        tunePostConnection(con);

        String jsonInputString = exercise.getJSON();
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        }
    }

    public void insertExerciseToTag(ExerciseToTag exerciseToTag) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url_insert_exercise_to_tag.openConnection();
        tunePostConnection(con);

        String jsonInputString = exerciseToTag.getJSON();
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        }
    }

    public void insertTargetMuscle(TargetMuscle targetMuscle) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url_insert_target_muscle.openConnection();
        tunePostConnection(con);

        String jsonInputString = targetMuscle.getJSON();
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        }
    }

    public void insertTrainingExerciseNote(TrainingExerciseNote trainingExerciseNote) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url_insert_note.openConnection();
        tunePostConnection(con);

        String jsonInputString = trainingExerciseNote.getJSON();
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        }
    }

    public void insertAccount(Account account) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url_insert_account.openConnection();
        tunePostConnection(con);

        String jsonInputString = account.getJSON();
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        }
    }

    public void insertBodyCondition(BodyCondition bodyCondition) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url_insert_body_condition.openConnection();
        tunePostConnection(con);

        String jsonInputString = bodyCondition.getJSON();
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        }
    }

    public void insertFriendship(Friendship friendship) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url_insert_friendship.openConnection();
        tunePostConnection(con);

        String jsonInputString = friendship.getJSON();
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        }
    }

    public void updateTrainingExerciseSuccess(InsertSetJSON insertSetJSON, ExerciseInTrainingEditing exerciseInTrainingEditing) throws IOException {
        URL url_update_training_exercise_success = new URL("http://" + local_IP + ":3000/trainingexercisesuccess?and=(" +
                "accountid.eq." + exerciseInTrainingEditing.getAccountId() + "," +
                "trainingid.eq." + exerciseInTrainingEditing.getTrainingId() + "," +
                "dayofweek.eq." + exerciseInTrainingEditing.getDayOfWeek() + "," +
                "ordernumber.eq." + exerciseInTrainingEditing.getOrderNumber() + "," +
                "setnumber.eq." + exerciseInTrainingEditing.getSetNumber() + ")");

        HttpURLConnection con = (HttpURLConnection) url_update_training_exercise_success.openConnection();
        tunePatchConnection(con);

        String jsonInputString = insertSetJSON.getJSON();
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        }
    }

    public void updateTrainingExercise(InsertSetJSON insertSetJSON, ExerciseInTrainingEditing exerciseInTrainingEditing) throws IOException {
        URL url_update_training_exercise = new URL("http://" + local_IP + ":3000/trainingexercise?and=(" +
                "accountid.eq." + exerciseInTrainingEditing.getAccountId() + "," +
                "trainingid.eq." + exerciseInTrainingEditing.getTrainingId() + "," +
                "dayofweek.eq." + exerciseInTrainingEditing.getDayOfWeek() + "," +
                "ordernumber.eq." + exerciseInTrainingEditing.getOrderNumber() + "," +
                "setnumber.eq." + exerciseInTrainingEditing.getSetNumber() + ")");

        HttpURLConnection con = (HttpURLConnection) url_update_training_exercise.openConnection();
        tunePatchConnection(con);

        String jsonInputString = insertSetJSON.getJSON();
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        }
    }

    public void updateTrainingExerciseSuccessWithNewOrderNumber(int accountId, int orderNumber, TrainingExerciseInstance trainingExerciseInstance) throws IOException {
        URL url_update_training_exercise_success_with_order_number = new URL("http://" + local_IP + ":3000/trainingexercisesuccess?and=(" +
                "accountid.eq." + accountId + "," +
                "trainingid.eq." + trainingExerciseInstance.getTrainingId() + "," +
                "dayofweek.eq." + trainingExerciseInstance.getDayOfWeek() + "," +
                "ordernumber.eq." + trainingExerciseInstance.getOrderNumber() + ")");

        HttpURLConnection con = (HttpURLConnection) url_update_training_exercise_success_with_order_number.openConnection();
        tunePatchConnection(con);

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, url_update_training_exercise_success_with_order_number.toString());
        }

        String jsonInputString = "{ \"ordernumber\" : " + orderNumber + " }";
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        }
    }

    public void updateTrainingExerciseWithNewOrderNumber(int accountId, int orderNumber, TrainingExerciseInstance trainingExerciseInstance) throws IOException {
        URL url_update_training_exercise_with_order_number = new URL("http://" + local_IP + ":3000/trainingexercise?and=(" +
                "accountid.eq." + accountId + "," +
                "trainingid.eq." + trainingExerciseInstance.getTrainingId() + "," +
                "dayofweek.eq." + trainingExerciseInstance.getDayOfWeek() + "," +
                "ordernumber.eq." + trainingExerciseInstance.getOrderNumber() + ")");

        HttpURLConnection con = (HttpURLConnection) url_update_training_exercise_with_order_number.openConnection();
        tunePatchConnection(con);

        String jsonInputString = "{ \"ordernumber\" : " + orderNumber + " }";
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        }
    }

    public void updateTrainingExerciseNoteWithNewOrderNumber(int accountId, int orderNumber, TrainingExerciseInstance trainingExerciseInstance) throws IOException {
        URL url_update_training_exercise_note_with_order_number = new URL("http://" + local_IP + ":3000/trainingexercisenote?and=(" +
                "accountid.eq." + accountId + "," +
                "trainingid.eq." + trainingExerciseInstance.getTrainingId() + "," +
                "dayofweek.eq." + trainingExerciseInstance.getDayOfWeek() + "," +
                "ordernumber.eq." + trainingExerciseInstance.getOrderNumber() + ")");

        HttpURLConnection con = (HttpURLConnection) url_update_training_exercise_note_with_order_number.openConnection();
        tunePatchConnection(con);

        String jsonInputString = "{ \"ordernumber\" : " + orderNumber + " }";
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        }
    }

    public void updateTraining(String updateTrainingJSON, int trainingId) throws IOException {
        URL url_update_training = new URL("http://" + local_IP + ":3000/training?" +
                "trainingid=eq." + trainingId);

        HttpURLConnection con = (HttpURLConnection) url_update_training.openConnection();
        tunePatchConnection(con);

        String jsonInputString = updateTrainingJSON;
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        }
    }

    public void updateCurrentTraining(int accountId, int trainingId) throws IOException {
        URL url_update_current_training = new URL("http://" + local_IP + ":3000/account?" +
                "accountid=eq." + accountId);

        HttpURLConnection con = (HttpURLConnection) url_update_current_training.openConnection();
        tunePatchConnection(con);

        String jsonInputString = "{ \"trainingid\" : " + trainingId + " }";
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        }
    }

    public void updateNote(int accountId, int trainingId, int dayOfWeek, int orderNumber, String note) throws IOException {
        URL url_update_note = new URL("http://" + local_IP + ":3000/trainingexercisenote?and=(" +
                "accountid.eq." + accountId + "," +
                "trainingid.eq." + trainingId + "," +
                "dayofweek.eq." + dayOfWeek + "," +
                "ordernumber.eq." + orderNumber + ")");

        HttpURLConnection con = (HttpURLConnection) url_update_note.openConnection();
        tunePatchConnection(con);

        String jsonInputString = "{ \"note\" : \"" + note + "\" }";
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        }
    }

    public void updateAccountInfo(int accountId, Account newAccount) throws IOException {
        URL url_update_account_info = new URL("http://" + local_IP + ":3000/account?" +
                "accountid=eq." + accountId);

        HttpURLConnection con = (HttpURLConnection) url_update_account_info.openConnection();
        tunePatchConnection(con);

        String jsonInputString = "{ " +
                "\"firstname\" : \"" + newAccount.getFirstName() + "\", " +
                "\"secondname\" : \"" + newAccount.getSecondName() + "\", " +
                "\"username\" : \"" + newAccount.getUserName() + "\" " +
                " }";
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        }
    }

    public void updateBodyConditionInfo(int accountId, BodyCondition newBodyCondition) throws IOException {
        URL url_update_body_condition_info = new URL("http://" + local_IP + ":3000/bodycondition?" +
                "accountid=eq." + accountId);

        HttpURLConnection con = (HttpURLConnection) url_update_body_condition_info.openConnection();
        tunePatchConnection(con);

        String jsonInputString = "{ " +
                "\"age\" : " + newBodyCondition.getAge() + ", " +
                "\"weight\" : " + newBodyCondition.getWeight() + ", " +
                "\"height\" : " + newBodyCondition.getHeight() + ", " +
                "\"bodyfatshare\" : " + newBodyCondition.getBodyFatShare() + " " +
                "}";
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        }
    }

    public void deleteTrainingExerciseSuccess(ExerciseInTrainingEditing exerciseInTrainingEditing) throws IOException {
        URL url_delete_training_exercise_success = new URL("http://" + local_IP + ":3000/trainingexercisesuccess?and=(" +
                "accountid.eq." + exerciseInTrainingEditing.getAccountId() + "," +
                "trainingid.eq." + exerciseInTrainingEditing.getTrainingId() + "," +
                "dayofweek.eq." + exerciseInTrainingEditing.getDayOfWeek() + "," +
                "ordernumber.eq." + exerciseInTrainingEditing.getOrderNumber() + "," +
                "setnumber.eq." + exerciseInTrainingEditing.getSetNumber() + ")");

        HttpURLConnection con = (HttpURLConnection) url_delete_training_exercise_success.openConnection();
        tuneDeleteConnection(con);

        getResponseString(con);
    }

    public void deleteTrainingExercise(ExerciseInTrainingEditing exerciseInTrainingEditing) throws IOException {
        URL url_delete_training_exercise = new URL("http://" + local_IP + ":3000/trainingexercise?and=(" +
                "accountid.eq." + exerciseInTrainingEditing.getAccountId() + "," +
                "trainingid.eq." + exerciseInTrainingEditing.getTrainingId() + "," +
                "dayofweek.eq." + exerciseInTrainingEditing.getDayOfWeek() + "," +
                "ordernumber.eq." + exerciseInTrainingEditing.getOrderNumber() + "," +
                "setnumber.eq." + exerciseInTrainingEditing.getSetNumber() + ")");

        HttpURLConnection con = (HttpURLConnection) url_delete_training_exercise.openConnection();
        tuneDeleteConnection(con);

        getResponseString(con);
    }

    public void deleteTrainingExerciseSuccessWithOrderNumber(int accountId, TrainingExerciseInstance trainingExerciseInstance) throws IOException {
        URL url_delete_training_exercise_success_with_order_number = new URL("http://" + local_IP + ":3000/trainingexercisesuccess?and=(" +
                "accountid.eq." + accountId + "," +
                "trainingid.eq." + trainingExerciseInstance.getTrainingId() + "," +
                "dayofweek.eq." + trainingExerciseInstance.getDayOfWeek() + "," +
                "ordernumber.eq." + trainingExerciseInstance.getOrderNumber() + ")");

        HttpURLConnection con = (HttpURLConnection) url_delete_training_exercise_success_with_order_number.openConnection();
        tuneDeleteConnection(con);

        getResponseString(con);
    }

    public void deleteTrainingExerciseNoteWithOrderNumber(int accountId, TrainingExerciseInstance trainingExerciseInstance) throws IOException {
        URL url_delete_training_exercise_note_with_order_number = new URL("http://" + local_IP + ":3000/trainingexercisenote?and=(" +
                "accountid.eq." + accountId + "," +
                "trainingid.eq." + trainingExerciseInstance.getTrainingId() + "," +
                "dayofweek.eq." + trainingExerciseInstance.getDayOfWeek() + "," +
                "ordernumber.eq." + trainingExerciseInstance.getOrderNumber() + ")");

        HttpURLConnection con = (HttpURLConnection) url_delete_training_exercise_note_with_order_number.openConnection();
        tuneDeleteConnection(con);

        getResponseString(con);
    }

    public void deleteTrainingExerciseWithOrderNumber(int accountId, TrainingExerciseInstance trainingExerciseInstance) throws IOException {
        URL url_delete_training_exercise_with_order_number = new URL("http://" + local_IP + ":3000/trainingexercise?and=(" +
                "accountid.eq." + accountId + "," +
                "trainingid.eq." + trainingExerciseInstance.getTrainingId() + "," +
                "dayofweek.eq." + trainingExerciseInstance.getDayOfWeek() + "," +
                "ordernumber.eq." + trainingExerciseInstance.getOrderNumber() + ")");

        HttpURLConnection con = (HttpURLConnection) url_delete_training_exercise_with_order_number.openConnection();
        tuneDeleteConnection(con);

        getResponseString(con);
    }

    public void deleteTrainingFromId(int trainingId) throws IOException {
        URL url_delete_training = new URL("http://" + local_IP + ":3000/training?" +
                "trainingid=eq." + trainingId);

        HttpURLConnection con = (HttpURLConnection) url_delete_training.openConnection();
        tuneDeleteConnection(con);

        getResponseString(con);
    }

    public void deleteTrainingExerciseSuccessFromTraining(int accountId, int trainingId) throws IOException {
        URL url_delete_training_exercise_success_from_training = new URL("http://" + local_IP + ":3000/trainingexercisesuccess?and=(" +
                "trainingid.eq." + trainingId + "," +
                "accountid.eq." + accountId + ")");

        HttpURLConnection con = (HttpURLConnection) url_delete_training_exercise_success_from_training.openConnection();
        tuneDeleteConnection(con);

        getResponseString(con);
    }

    public void deleteTrainingExerciseNote(int accountId, int trainingId) throws IOException {
        URL url_delete_training_exercise_note = new URL("http://" + local_IP + ":3000/trainingexercisenote?and=(" +
                "trainingid.eq." + trainingId + "," +
                "accountid.eq." + accountId + ")");

        HttpURLConnection con = (HttpURLConnection) url_delete_training_exercise_note.openConnection();
        tuneDeleteConnection(con);

        getResponseString(con);
    }

    public void deleteFriendship(Friendship friendship) throws IOException {
        URL url_delete_friendship = new URL("http://" + local_IP + ":3000/friendship?and=(" +
                "accountid.eq." + friendship.getAccountId() + "," +
                "friendid.eq." + friendship.getFriendId() + ")");

        HttpURLConnection con = (HttpURLConnection) url_delete_friendship.openConnection();
        tuneDeleteConnection(con);

        getResponseString(con);
    }

    public void deleteAccount(int accountId) throws IOException {
        URL url_delete_account = new URL("http://" + local_IP + ":3000/account?" +
                "accountid=eq." + accountId);

        HttpURLConnection con = (HttpURLConnection) url_delete_account.openConnection();
        tuneDeleteConnection(con);

        getResponseString(con);
    }

    public void setNullCurrentTraining(int accountId) throws IOException {
        URL url_update_current_training = new URL("http://" + local_IP + ":3000/account?" +
                "accountid=eq." + accountId);

        HttpURLConnection con = (HttpURLConnection) url_update_current_training.openConnection();
        tunePatchConnection(con);

        String jsonInputString = "{ \"trainingid\" : null }";
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.flush();
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        }
    }

    private String getResponseString(HttpURLConnection con) throws IOException {
        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
        wr.flush();

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        return response.toString();
    }

    private void tunePostConnection(HttpURLConnection con) throws ProtocolException {
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
    }

    private void tunePatchConnection(HttpURLConnection con) throws ProtocolException {
        con.setRequestMethod("PATCH");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
    }

    private void tuneDeleteConnection(HttpURLConnection con) throws ProtocolException {
        con.setRequestMethod("DELETE");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
    }

    public HttpWork() throws MalformedURLException {
    }
}
