package com.example.testbottomnavigationbar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.testbottomnavigationbar.remote_db.Account;
import com.example.testbottomnavigationbar.remote_db.BodyCondition;
import com.example.testbottomnavigationbar.remote_db.Exercise;
import com.example.testbottomnavigationbar.remote_db.ExerciseTag;
import com.example.testbottomnavigationbar.remote_db.ExerciseToTag;
import com.example.testbottomnavigationbar.remote_db.ExerciseTrainingType;
import com.example.testbottomnavigationbar.remote_db.Friendship;
import com.example.testbottomnavigationbar.remote_db.HttpWork;
import com.example.testbottomnavigationbar.remote_db.Muscle;
import com.example.testbottomnavigationbar.remote_db.TargetMuscle;
import com.example.testbottomnavigationbar.remote_db.Training;
import com.example.testbottomnavigationbar.remote_db.TrainingExercise;
import com.example.testbottomnavigationbar.remote_db.TrainingExerciseNote;
import com.example.testbottomnavigationbar.remote_db.TrainingExerciseSuccess;
import com.example.testbottomnavigationbar.remote_db.TrainingType;
import com.example.testbottomnavigationbar.remote_db.tasks.PullExerciseTask;
import com.example.testbottomnavigationbar.remote_db.tasks.PullRemoteDBTask;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class SQLiteHelper {
    public static final String dbName = "app.db";
    public static final String currentAccountDBName = "currentaccount.db";

    public static String getDbName() {
        return dbName;
    }

    public static String getCurrentAccountDBName() {
        return currentAccountDBName;
    }

    public static boolean isLocalDBExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    public static void handleDBAbsence(Context context, ProgressBar progressBar) throws IOException {
        if (isLocalDBExist(context, currentAccountDBName)) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "currentAccountDb exist");
            }
        }

        if (isLocalDBExist(context, dbName)) {
            context.deleteDatabase(dbName);
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, "db exist");
            }
//            return;
        }

        SQLiteDatabase db = context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
        SQLiteDatabase currentAccountDB = context.openOrCreateDatabase(currentAccountDBName, Context.MODE_PRIVATE, null);
        db.execSQL("PRAGMA foreign_keys=ON;");

        currentAccountDB.execSQL("CREATE TABLE IF NOT EXISTS CurrentAccount (AccountId INTEGER PRIMARY KEY NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS Exercise (ExerciseId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Title VARCHAR(256) NOT NULL, Description TEXT, VideoPath TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS ExerciseTag (TagId INTEGER PRIMARY KEY NOT NULL, Title VARCHAR(256))");
        db.execSQL("CREATE TABLE IF NOT EXISTS ExerciseToTag (ExerciseId INTEGER NOT NULL, TagId INTEGER NOT NULL, CONSTRAINT fk_Exercise FOREIGN KEY(ExerciseId) REFERENCES Exercise(ExerciseId) ON DELETE CASCADE, CONSTRAINT fk_ExerciseTag FOREIGN KEY(TagId) REFERENCES ExerciseTag(TagId) ON DELETE CASCADE, PRIMARY KEY(ExerciseId, TagId))");
        db.execSQL("CREATE TABLE IF NOT EXISTS Account (AccountId INTEGER PRIMARY KEY NOT NULL, Username VARCHAR(256), HashPassword VARCHAR(256), BirthDate DATE NOT NULL, RegistrationDate DATE NOT NULL, Sex VARCHAR(256), TrainingId INTEGER, FirstName VARCHAR(256) NOT NULL, SecondName VARCHAR(256) NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Training (TrainingId INTEGER PRIMARY KEY NOT NULL, Title TEXT, AccountId INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS TrainingExercise (TrainingId INTEGER NOT NULL, DayOfWeek INTEGER CHECK (DayOfWeek >= 0 AND DayOfWeek < 7), OrderNumber INTEGER CHECK (OrderNumber > 0), ExerciseId INTEGER NOT NULL, SetNumber INTEGER CHECK (SetNumber > 0), RepsNum INTEGER CHECK (RepsNum >= 0), Timer INTEGER CHECK (Timer >= 0), AccountId INTEGER NOT NULL, CONSTRAINT fk_Exercise FOREIGN KEY(ExerciseId) REFERENCES Exercise(ExerciseId) ON DELETE CASCADE, CONSTRAINT fk_Training FOREIGN KEY(TrainingId) REFERENCES Training(TrainingId) ON DELETE CASCADE, PRIMARY KEY(TrainingId, DayOfWeek, OrderNumber, SetNumber, AccountId))");
        db.execSQL("CREATE TABLE IF NOT EXISTS TrainingExerciseSuccess (AccountId INTEGER NOT NULL, TrainingId INTEGER NOT NULL, DayOfWeek INTEGER CHECK (DayOfWeek >= 0 AND DayOfWeek < 7), OrderNumber INTEGER CHECK (OrderNumber > 0), ExerciseId INTEGER NOT NULL, SetNumber INTEGER CHECK (SetNumber > 0), RepsNum INTEGER CHECK (RepsNum >= 0), Weight INTEGER CHECK (Weight >= 0), Timer INTEGER CHECK (Timer >= 0), CONSTRAINT fk_Account FOREIGN KEY(AccountId) REFERENCES Account(AccountId) ON DELETE CASCADE, CONSTRAINT fk_Training FOREIGN KEY(TrainingId) REFERENCES Training(TrainingId) ON DELETE CASCADE, PRIMARY KEY(AccountId, TrainingId, DayOfWeek, OrderNumber, SetNumber))");
        db.execSQL("CREATE TABLE IF NOT EXISTS TrainingExerciseNote (TrainingId INETEGER NOT NULL, AccountId INTEGER NOT NULL, DayOfWeek INTEGER CHECK (DayOfWeek >= 0 AND DayOfWeek < 7), OrderNumber INTEGER CHECK (OrderNumber > 0), Note TEXT, ExerciseId INTEGER NOT NULL, CONSTRAINT fk_Exercise FOREIGN KEY(ExerciseId) REFERENCES Exercise(ExerciseId) ON DELETE CASCADE, CONSTRAINT fk_Account FOREIGN KEY(AccountId) REFERENCES Account(AccountId) ON DELETE CASCADE, CONSTRAINT fk_Training FOREIGN KEY(TrainingId) REFERENCES Training(TrainingId) ON DELETE CASCADE, PRIMARY KEY(AccountId, TrainingId, DayOfWeek, OrderNumber))");
        db.execSQL("CREATE TABLE IF NOT EXISTS TrainingType (TypeId INTEGER PRIMARY KEY NOT NULL, Title VARCHAR(256) NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS ExerciseTrainingType (TypeId INTEGER NOT NULL, ExerciseId INTEGER NOT NULL, CONSTRAINT fk_TrainingType FOREIGN KEY(TypeId) REFERENCES TrainingType(TypeId) ON DELETE CASCADE, CONSTRAINT fk_Exercise FOREIGN KEY(ExerciseId) REFERENCES Exercise(ExerciseId) ON DELETE CASCADE, PRIMARY KEY(TypeId, ExerciseId))");
        db.execSQL("CREATE TABLE IF NOT EXISTS Muscle (MuscleId INTEGER PRIMARY KEY NOT NULL, Title VARCHAR(256) NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS TargetMuscle (ExerciseId INTEGER NOT NULL, MuscleId INTEGER NOT NULL, CONSTRAINT fk_Exercise FOREIGN KEY(ExerciseId) REFERENCES Exercise(ExerciseId) ON DELETE CASCADE, CONSTRAINT fk_Muscle FOREIGN KEY(MuscleId) REFERENCES Muscle(MuscleId) ON DELETE CASCADE, PRIMARY KEY(ExerciseId, MuscleId))");
        db.execSQL("CREATE TABLE IF NOT EXISTS BodyCondition (AccountId INTEGER PRIMARY KEY NOT NULL, Weight REAL NOT NULL, Height REAL NOT NULL, Age INTEGER NOT NULL, BodyFatShare REAL CHECK (BodyFatShare >= 0 AND BodyFatShare <= 100), CONSTRAINT fk_Account FOREIGN KEY(AccountId) REFERENCES Account(AccountId) ON DELETE CASCADE)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Friendship (AccountId INTEGER NOT NULL, FriendId INTEGER NOT NULL, CONSTRAINT fk_Account FOREIGN KEY(AccountId) REFERENCES Account(AccountId) ON DELETE CASCADE, CONSTRAINT fk_Account FOREIGN KEY(FriendId) REFERENCES Account(AccountId) ON DELETE CASCADE, PRIMARY KEY(AccountId, FriendId))");

//        addTestData(context);
        pullRemoteData(db, context, progressBar);
    }

    private static void pullRemoteData(SQLiteDatabase db, Context context, ProgressBar progressBar) throws IOException {
        new PullRemoteDBTask(context, progressBar).execute();

//        pullAccount(worker, db);
//        pullExercise(worker, db);
//        pullExerciseToTag(worker, db);
//        pullExerciseTag(worker, db);
//        pullTraining(worker, db);
//        pullTrainingExercise(worker, db);
//        pullTrainingExerciseNote(worker, db);
//        pullTrainingExerciseSuccess(worker, db);
    }

    public static Account getAccountFromId(SQLiteDatabase db, int accountId) {
        Cursor cursor = db.rawQuery("SELECT Username, Sex, FirstName, SecondName FROM Account WHERE AccountId = " + accountId + ";", null);
        if (cursor != null && cursor.moveToFirst()) {
            String username = cursor.getString(0);
            String sex = cursor.getString(1);
            String firstName = cursor.getString(2);
            String secondName = cursor.getString(3);

            return new Account(accountId, username, null, null, null, sex, -1, firstName, secondName);
        }

        return null;
    }

    public static BodyCondition getBodyConditionFromAccountId(SQLiteDatabase db, int accountId) {
        Cursor cursor = db.rawQuery("SELECT Weight, Height, Age, BodyFatShare FROM BodyCondition WHERE AccountId = " + accountId + ";", null);
        if (cursor != null && cursor.moveToFirst()) {
            float weight = cursor.getFloat(0);
            float height = cursor.getFloat(1);
            int age = cursor.getInt(2);
            float bodyFatShare = cursor.getFloat(3);

            return new BodyCondition(accountId, weight, height, age, bodyFatShare);
        }

        return null;
    }

    public static int getFriendsCnt(SQLiteDatabase db, int accountId) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Friendship WHERE AccountId = " + accountId + ";", null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(0);
        }

        return 0;
    }

    public static boolean isFriend(SQLiteDatabase db, SQLiteDatabase currentAccountDB, int friendId) {
        int accountId = getCurrentAccountId(currentAccountDB);

        Cursor cursor = db.rawQuery("SELECT * FROM Friendship WHERE AccountId = " + accountId + " AND FriendId = " + friendId + ";", null);
        return (cursor != null && cursor.moveToFirst());
    }

    public static int getTrainingCnt(SQLiteDatabase db, int accountId) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Training WHERE AccountId = " + accountId + ";", null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(0);
        }

        return 0;
    }

    public static int getCurrentAccountId(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT * FROM CurrentAccount;", null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(0);
        }

        return -1;
    }

    public static int getAccountIdFromUsername(SQLiteDatabase db, String username) {
        Cursor cursor = db.rawQuery("SELECT AccountId FROM Account WHERE Username = '" + username + "';", null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(0);
        }

        return -1;
    }

    public static int getCurrentTrainingId(SQLiteDatabase db, SQLiteDatabase currentAccountDB) {
        int accountId = getCurrentAccountId(currentAccountDB);

        Cursor cursor = db.rawQuery("SELECT TrainingId FROM Account WHERE AccountId = " + accountId + ";", null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(0);
        }

        return -1;
    }

    public static int getMuscleIdFromTitle(SQLiteDatabase db, String title) {
        Cursor cursor = db.rawQuery("SELECT MuscleId FROM Muscle WHERE Title = '" + title + "';", null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(0);
        }

        return -1;
    }

    public static int getTagIdFromTitle(SQLiteDatabase db, String title) {
        Cursor cursor = db.rawQuery("SELECT TagId FROM ExerciseTag WHERE Title = '" + title + "';", null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(0);
        }

        return -1;
    }

    public static int getTrainingIdFromTitle(SQLiteDatabase db, String title, int accountId) {
        Cursor cursor = db.rawQuery("SELECT TrainingId FROM Training WHERE Title = '" + title + "' AND AccountId = " + accountId + ";", null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(0);
        }

        return -1;
    }

    public static String getTrainingTitleFromId(SQLiteDatabase db, int trainingId) {
        Cursor cursor = db.rawQuery("SELECT Title FROM Training WHERE TrainingId = " + trainingId + ";", null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(0);
        }

        return "";
    }

    public static int getExerciseIdFromTitle(SQLiteDatabase db, String title) {
        Cursor cursor = db.rawQuery("SELECT ExerciseId FROM Exercise WHERE Title = '" + title + "';", null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(0);
        }

        return -1;
    }

    public static int getMaxSetNumberFromTrainingExerciseSuccess(SQLiteDatabase db, int accountId, int trainingId, int dayOfWeek, int orderNumber) {
        Cursor cursor = db.rawQuery("SELECT MAX(SetNumber) FROM TrainingExerciseSuccess \n" +
                "WHERE AccountId = " + accountId + " AND TrainingId = " + trainingId + " AND DayOfWeek = " + dayOfWeek + " AND OrderNumber = " + orderNumber + ";", null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(0);
        }

        return  -1;
    }

    public static int getMaxSetNumberFromTrainingExercise(SQLiteDatabase db, int accountId, int trainingId, int dayOfWeek, int orderNumber) {
        Cursor cursor = db.rawQuery("SELECT MAX(SetNumber) FROM TrainingExercise \n" +
                "WHERE AccountId = " + accountId + " AND TrainingId = " + trainingId + " AND DayOfWeek = " + dayOfWeek + " AND OrderNumber = " + orderNumber + ";", null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(0);
        }

        return  -1;
    }

    public static void pullAccount(HttpWork worker, SQLiteDatabase db) throws IOException {
        Account[] accounts = worker.pullAllAccounts();
        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "Account count  " + accounts.length);
        }
        for (Account account : accounts) {
            db.execSQL(account.getSQLiteInsertQuery());
        }
    }

    public static void pullExercise(HttpWork worker, SQLiteDatabase db) throws IOException {
        Exercise[] exercises = worker.pullAllExercises();
        for (Exercise exercise : exercises) {
            db.execSQL(exercise.getSQLiteInsertQuery());
        }

        if (MainActivity.LOG) {
            Log.d(MainActivity.TEG, "Exercise count  " + exercises.length);
        }
    }

    public static void pullExerciseToTag(HttpWork worker, SQLiteDatabase db) throws IOException {
        ExerciseToTag[] exerciseToTags = worker.pullAllExerciseToTag();
        for (ExerciseToTag exerciseToTag : exerciseToTags) {
            if (MainActivity.LOG) {
                Log.d(MainActivity.TEG, exerciseToTag.getSQLiteInsertQuery());
            }

            db.execSQL(exerciseToTag.getSQLiteInsertQuery());
        }
    }

    public static void pullExerciseTag(HttpWork worker, SQLiteDatabase db) throws IOException {
        ExerciseTag[] exerciseTags = worker.pullAllExerciseTags();
        for (ExerciseTag exerciseTag : exerciseTags) {
            db.execSQL(exerciseTag.getSQLiteInsertQuery());
        }
    }

    public static void pullTraining(HttpWork worker, SQLiteDatabase db) throws IOException {
        Training[] trainings = worker.pullAllTrainings();
        for (Training training : trainings) {
            db.execSQL(training.getSQLiteInsertQuery());
        }
    }

    public static void pullTrainingExercise(HttpWork worker, SQLiteDatabase db) throws IOException {
        TrainingExercise[] trainingExercises = worker.pullAllTrainingExercises();
        for (TrainingExercise trainingExercise : trainingExercises) {
            db.execSQL(trainingExercise.getSQLiteInsertQuery());
        }
    }

    public static void pullTrainingExerciseNote(HttpWork worker, SQLiteDatabase db) throws IOException {
        TrainingExerciseNote[] trainingExerciseNotes = worker.pullAllTrainingExerciseNote();
        for (TrainingExerciseNote trainingExerciseNote : trainingExerciseNotes) {
            db.execSQL(trainingExerciseNote.getSQLiteInsertQuery());
        }
    }

    public static void pullTrainingExerciseSuccess(HttpWork worker, SQLiteDatabase db) throws IOException {
        TrainingExerciseSuccess[] trainingExerciseSuccesses = worker.pullAllTrainingExerciseSuccess();
        for (TrainingExerciseSuccess trainingExerciseSuccess : trainingExerciseSuccesses) {
            db.execSQL(trainingExerciseSuccess.getSQLiteInsertQuery());
        }
    }

    public static void pullTrainingType(HttpWork worker, SQLiteDatabase db) throws IOException {
        TrainingType[] trainingTypes = worker.pullAllTrainingTypes();
        for (TrainingType trainingType : trainingTypes) {
            db.execSQL(trainingType.getSQLiteInsertQuery());
        }
    }

    public static void pullExerciseTrainingType(HttpWork worker, SQLiteDatabase db) throws IOException {
        ExerciseTrainingType[] exerciseTrainingTypes = worker.pullAllExerciseTrainingTypes();
        for (ExerciseTrainingType exerciseTrainingType : exerciseTrainingTypes) {
            db.execSQL(exerciseTrainingType.getSQLiteInsertQuery());
        }
    }

    public static void pullMuscle(HttpWork worker, SQLiteDatabase db) throws IOException {
        Muscle[] muscles = worker.pullAllMuscles();
        for (Muscle muscle : muscles) {
            db.execSQL(muscle.getSQLiteInsertQuery());
        }
    }

    public static void pullTargetMuscle(HttpWork worker, SQLiteDatabase db) throws IOException {
        TargetMuscle[] targetMuscles = worker.pullAllTargetMuscles();
        for (TargetMuscle targetMuscle : targetMuscles) {
            db.execSQL(targetMuscle.getSQLiteInsertQuery());
        }
    }

    public static void pullBodyCondition(HttpWork worker, SQLiteDatabase db) throws IOException {
        BodyCondition[] bodyConditions = worker.pullAllBodyConditions();
        for (BodyCondition bodyCondition : bodyConditions) {
            db.execSQL(bodyCondition.getSQLiteInsertQuery());
        }
    }

    public static void pullFriendship(HttpWork worker, SQLiteDatabase db) throws IOException {
        Friendship[] friendships = worker.pullAllFriendships();
        for (Friendship friendship : friendships) {
            db.execSQL(friendship.getSQLiteInsertQuery());
        }
    }

//    public static void addTestData(Context context) {
//        SQLiteDatabase db = context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
//
//        db.execSQL("INSERT INTO Exercise(Title, Description, VideoPath)\n" +
//                "VALUES ('Жим лежа', 'Сделайте глубокий вдох и задержите дыхание. Опустите штангу и коснитесь ею середины груди. В нижней точке ваши предплечья должны находиться в вертикальном положении. Если они находятся под наклоном, измените ширину хвата. Плечи должны располагаться под углом в 75° к телу. Если вы расставляете локти широко, можете травмировать плечи, если прижимаете к телу — снижаете эффективность движения. Отталкивая ногами пол и напрягая ягодицы, выжмите штангу в исходное положение — строго над плечами. Если проследить амплитуду снаряда, получится, что она идёт по небольшой дуге от середины груди до плеч. Если вы будете выжимать штангу строго вверх по вертикали, в верхней точке она окажется не над плечами, а впереди. Это увеличит рычаг до плеч, утомит их и не позволит вам выложиться по полной. Чтобы сохранить правильное положение тела, используйте один совет: представляйте, что вы не жмёте штангу, а отжимаетесь от неё. Толкайте тело в лавку, не забывая при этом сводить лопатки и сохранять прогиб в грудном отделе. Такой мысленный трюк поможет вам удержать плечи от выхода вперёд. Прежде чем увеличивать вес в этом упражнении, попросите кого-нибудь заснять вас на видео с разных ракурсов и оцените положение предплечий и плеч во время жима, размещение грифа на старте и амплитуду его движения. Если вы выполняете упражнение технично и вам легко делать 8–10 раз с грифом, можете увеличивать вес, но делайте это постепенно.',\n" +
//                " 'https://www.youtube.com/watch?v=Czl9NcGrIsE')");
//        db.execSQL("INSERT INTO Exercise(Title, Description, VideoPath)\n" +
//                "VALUES ('Становая тяга', 'Плечи и лопатки отведите немного назад. Ноги расставьте уже уровня плеч (примерно на уровне бедер), коснитесь голенями грифа. Положение стоп – параллельно друг другу. Гриф проходит над центром стопы (а не над центром ее передней части). Присядьте – отведите таз назад и выпрямите спину, но не выводите колени за линию носков. Возьмитесь руками за гриф хватом на ширине плеч (расстояние между кистями – 40 см, колени внутри рук). Руки – прямые, локти зафиксированы. При классическом хвате ладони повернуты вниз. Выставьте грудь и плечи немного вперед за линию штанги. Взгляд направлен строго вниз. Не раскачиваясь и не перенося вес тела на носки, сорвите штангу движением мышц ног – квадрицепсов и ягодиц. После того как штанга прошла 20-30% амплитуды, выпрямите поясницу, выталкивая таз вперед, и зафиксируйте позицию. В точке подъема веса можно выставить грудь вперед (но не сводить лопатки). Перед тем как опустить штангу, отведите бедра назад так, чтобы не задеть грифом колени. При движении вниз держите позвоночник в нейтральном положении – не выгибайте и не прогибайте. Только после того, как гриф достигнет уровня колен, их можно согнуть.',\n" +
//                " 'https://www.youtube.com/watch?v=KddlE57Yjt0')");
//        db.execSQL("INSERT INTO Exercise(Title, Description, VideoPath)\n" +
//                "VALUES ('Подтягивания обратным хватом', 'При подтягивании обратным хватом можно использовать один из вариантов: средний либо узкий. Среднее расположение ладоней при подтягивании обратным хватом считается классическим. Ладони располагаются на ширине плеч захватом на себя, большие пальцы образуют «замок». Движение вверх следует начинать не спеша и следить за тем, чтобы не поднимать по инерции плечи: их нужно отводить вниз-назад, сводя лопатки. Правильное выполнение упражнения прокачивает бицепс и включает в работу группу спинных мышц. Обратный хват с узким расположением рук позволяет отработать не только бицепсы, но и нижнюю часть широчайших мышц спины. Перед выполнением упражнения следует положить ладони на перекладину на наименьшем расстоянии друг от друга, развернув их на себя и закрыв «замком» больших пальцев. Поднимая туловище вверх, нужно сводить лопатки вместе, отводя назад плечи. В наивысшей точке нижняя часть груди должна прикоснуться к перекладине. Во время выполнения упражнения на плечевые суставы приходится довольно большая нагрузка, поэтому необходимо двигаться плавно, без резких движений и раскачиваний. Выдох следует делать при движении вниз, следя за тем, чтобы локти не расходились в стороны и были параллельны друг другу. Двигаясь вверх, прижмите подбородок и слегка скрутите корпус, чтобы выделить бицепсы и снизить нагрузку на спинные мышцы. Максимальное напряжение бицепса должно приходиться на верхнюю точку амплитуды. Подтянувшись, зафиксируйте положение тела, обеспечив бицепсу максимальную статическую нагрузку. Опускаться вниз нужно медленно, без рывков — по времени эта фаза должна занимать около 3 секунд.',\n" +
//                " 'https://www.youtube.com/watch?v=bF4T6-Mh0MU')");
//        db.execSQL("INSERT INTO Exercise(Title, Description, VideoPath)\n" +
//                "VALUES ('Гиперэкстензии', 'Делается упор бедрами в мягкую подушку чуть ниже линии сгиба поясничного отдела. Живот не должен мешать. Далее делается упор прямых стоп параллельно друг другу в платформу, закрепляется фиксирующими валиками. Для правильного выполнения гиперэкстензии важно проследить за положением коленных суставов. Ноги постоянно должны быть слегка согнуты, чтобы нагрузка равномерно распределялась. Периодическое разгибание спины происходит только до той линии, на которой находятся ноги. Чрезмерный прогиб может привести к травме позвонков. Угол сгиба не должен превышать 90 градусов. От положения рук зависит нагрузка на шейный отдел позвоночника. Если сложить их крестом на груди, то акцент перейдет на ягодицы. Если за шею - на разгибатели. Все движения должны выполняться медленно, без резких рывков. Плавно вниз, чуть быстрее вверх. Дыхание должно быть свободным, на каждом подъеме - выдох. Возможна задержка вверху - 1-2 секунды. Чтобы дать максимальную нагрузку на ягодицы, необходимо поддерживать спину скругленной и акцентировать внимание на ягодичном отделе. Напрягать попу и бицепсы бедер нужно в самом начале упражнения, и держать напряжение до самого конца. В нижней точке гиперэкстензии следует делать паузу, чтобы лишний раз убедиться в растяжении ягодиц. Аналогично в верхней точке. Руки и подбородок в данном варианте упражнения тесно прижимаются к груди, а взгляд направляется вниз. Нервно-мышечная связь имеет ключевое значение: сокращения ягодичных мышц нужно делать осознанно. Усилить нагрузку на заднюю часть ног можно более высоким положением тела относительно мягкой подушки (когда таз зависает в воздухе, а упор производится на середину бедра). При акценте же на мышечные разгибатели спину нужно выпрямлять, а взгляд направлять вперед. Руки можно закладывать за голову, чтобы акцентировать внимание на ровной осанке. ',\n" +
//                " 'https://www.youtube.com/watch?v=k76TkJCe8Xs')");
//        db.execSQL("INSERT INTO Exercise(Title, Description, VideoPath)\n" +
//                "VALUES ('Мертвая тяга', 'Ноги на ширине плеч, руки расположены чуть шире плеч. Чтобы не ошибиться с шириной рук, ориентируйтесь по насечкам на грифе. Гриф штанги должен почти касаться голеней. В таком случае стопы будут располагаться под грифом, где-то треть стопы будет за грифом. Вы немного сгибаете ноги в коленях, отводите таз назад и делаете наклон вперёд. Спина прямая, от копчика до шеи — одна линия. Взгляд направлен вперёд. На начальном этапе движения, когда вы с прямой спиной наклоняетесь к штанге, растягиваются ягодичные мышцы и бицепс бедра — основные рабочие мышцы в этом упражнении. Если у вас короткий бицепс бедра, произойдёт следующее: при наклоне бицепс потянет за собой поясницу, так что вы не сможете держать спину прямо. Во время подъёма штанга расположена очень близко к телу: гриф штанги практически скользит по голеням (касание не обязательно, хотя возможно, особенно на первых этапах, чтобы привыкнуть к правильной технике), а затем поднимается выше по бёдрам. Когда вы отрываете штангу от земли, ваш центр тяжести совмещается с центром тяжести штанги. Когда вы наклоняетесь, центр тяжести смещается с крестца вперёд. Если вы держите штангу близко к голеням, центр тяжести штанги совпадает с вашим смещённым центром тяжести и вы удерживаете равновесие. Если же вы встали далеко от штанги, центры тяжести не совпадут и штанга потянет вас вперёд, увеличивая нагрузку на поясницу. Гриф ведём по ногам. В момент отрыва штанги от земли (или платформы) необходимо напрячь ягодицы и мышцы бёдер. Сделать это нужно сознательно, не ожидая, когда напряжение возникнет само по себе. Напряжение ягодичных мышц необходимо, чтобы стабилизировать тазобедренный сустав. Напряжение мышц заставляет головку бедренной кости вращаться наружу, где она занимает максимально выгодную позицию для передачи усилия. Таким образом, вы стабилизируете сустав и обеспечиваете нейтральное положение позвоночника, за счёт чего нагрузка передаётся на ягодицы и заднюю часть бедра. Из этого положения вы полностью выпрямляетесь, а затем начинаете движение вниз, к исходному положению. Важно выполнять опускание штанги так же плавно, как и подъём, и вести гриф очень близко к бёдрам и голеням.',\n" +
//                " 'https://www.youtube.com/watch?v=oqh7hHMlNyU')");
//        db.execSQL("INSERT INTO Exercise(Title, Description, VideoPath)\n" +
//                "VALUES ('Сгибание ног лежа', 'Рассмотрим технику выполнения упражнения лежа. Делая сгибания сидя или стоя, соблюдайте те же самые принципы. Прежде всего отрегулируйте тренажер под свой рост и длину ног. Коленные суставы должны выходить за край скамьи, а валик, в который вы будете упираться нижней частью голени, должен располагаться на несколько сантиметров выше пятки. Тут все просто – чем ближе валик к пятке, тем больше рычаг и тем эффективнее упражнение. На место изгиба скамьи вы ложитесь животом так, чтобы вам было комфортно и в пояснице не было напряжения. Лягте на тренажер и заведите ноги под валик. Руками возьмитесь за специальные ручки или за края скамьи. На выдохе согните голени, стараясь максимально приблизить валик к ягодицам. Передняя поверхность бедер при этом прижата к скамье. На вдохе разгибайте голени, аккуратно опуская вес вниз. Полностью выпрямлять колени и расслаблять бицепс бедра в нижней точке не нужно. Повторите нужное количество раз. Вы можете варьировать нагрузку в этом упражнении, меняя положение носков стоп. Разведя носки наружу, вы сместите акцент в сторону внешней поверхности бедер. Сведя их внутрь – в сторону внутренней. Можно также чередовать сгибания на станке в сидячем или лежачем положении. При наличии соответствующего тренажера, стоит попробовать делать упражнение стоя сначала одной ногой, потом другой. Чем более разносторонней будет проработка мышц, тем качественнее вы получите результат. После тренировки сделайте небольшую растяжку. Это поможет расслабить бицепс бедра и улучшит кровообращение.',\n" +
//                " 'https://www.youtube.com/watch?v=Hy3WFtcY_o8')");
//        db.execSQL("INSERT INTO Exercise(Title, Description, VideoPath)\n" +
//                "VALUES ('Отжимания на брусьях', 'В исходном положении (верхней точке) ваш корпус расположен вертикально, вы держитесь на прямых руках, локти развернуты назад. Сделав вдох, опускайтесь вниз настолько, насколько позволяет гибкость ваших плечевых суставов. Ориентируйтесь на угол 90 градусов в локтях. Во время движения локти развернуты назад и прижаты к корпусу. На выдохе, за счет разгибания рук поднимитесь вверх. Если упражнение дается вам с большим трудом, в верхней точке разогните локти. Это даст трицепсам короткую передышку. Если же вы опытный спортсмен, оставляйте небольшой угол в локтях, чтобы не снимать нагрузку с целевых мышц. Работая на массу трицепса, стремитесь выполнить 10–15 повторов в 3–4 подходах. Опускайтесь вниз медленно, а поднимайтесь быстро. Как только все это будет удаваться вам достаточно просто, начинайте осваивать отжимания на брусьях с отягощением. Торопиться в этом вопросе не следует, так как, переусердствовав с нагрузкой, можно травмироваться и, вообще, лишить себя тренировок на долгое время. Перед тренировкой не забываем разминать всё тело(!), а в особенности мышцы, которые собираетесь сегодня проработать.',\n" +
//                " 'https://www.youtube.com/watch?v=kvwNKPrRBtA')");
//        db.execSQL("INSERT INTO Exercise(Title, Description, VideoPath)\n" +
//                "VALUES ('Разгибание ног в тренажере', 'Настроить спинку так, чтобы она была опорой. У большинства тренажеров регулируется наклон спинки и есть возможность придвинуть и отодвинуть ее. Правильное исходное положение – спина опирается на спинку, бедро лежит на сиденье, голень зафиксирована подушкой тренажера, а угол между голенью и бедром составляет не более 90 градусов. В реабилитации допускается неполная амплитуда, когда валик крепится выше, чем 90 градусов. Носки должны быть направлены чуть на себя, выполнять упражнение лучше начинать с сокращения квадрицепсов. Движение вниз не должно быть форсированным, лучше плавно опускаться и плавно разгибать голень. Спина должна быть прижатой к спинке полностью, поясничный отдел тоже. Разгибание нужно делать на выдохе, сгибание – на вдохе.',\n" +
//                " 'https://www.youtube.com/watch?v=eT4thXaPR8w')");
//        db.execSQL("INSERT INTO Exercise(Title, Description, VideoPath)\n" +
//                "VALUES ('Приседания со штангой', 'Перед подходом подготовьтесь психологически, сосредоточьтесь и сконцентрируйтесь на поднимаемом весе. Перед приседаниями разогрейте тело с помощью кардиоупражнений. Первый сет начинают с нескольких подходов с минимальными отягощениями. Подойдите к штанге, расположенной на уровне ключиц. Возьмитесь за гриф прямым закрытым хватом. Выберите комфортную ширину постановки ладоней, как правило, это немного шире плеч: таким образом лопатки останутся сведенными, а спина ровной в течение всего подхода. Если плечевые суставы зажаты, расставьте руки шире, хотя при таком хвате увеличивается вероятность потери равновесия. Плотно сожмите гриф ладонями, слегка согните ноги в коленях и шагните вперед. Пройдите под штангой, подставьте вторую стопу и приподнимитесь так, чтобы лопатки сошлись, а гриф уперся в верхнюю часть спины. При правильной технике выполнения движений вес ляжет на трапециевидную мышцу и будет поддерживаться задними дельтами. В случае сильного давления на позвонки вернитесь в исходное положение и повторите подход к штанге. Напрягите мышцы ног, разогните колени и снимите вес с упоров. Сделайте небольшой шаг назад и подставьте вторую стопу. Примите нейтральное положение головы – так легче удерживать поясницу прогнутой, меньше риск потери равновесия. Расставьте ноги шире плеч и слегка разведите носки в стороны. Зафиксируйте положение и максимально сконцентрируйтесь перед приседом. На глубоком вдохе отведите таз назад и разведите колени в стороны. Не «заваливайтесь» вперед, достаточно естественного наклона корпуса. Правильный присед напоминает опускание ягодиц на воображаемый стул. Опустившись в нижнюю точку, сразу же поднимайтесь и выдыхайте воздух из легких. Техника подъема – за счет распрямления ног, а не за счет разгибания спины.',\n" +
//                " 'https://www.youtube.com/watch?v=GotRb2Ob7MA')");
//        db.execSQL("INSERT INTO Exercise(Title, Description, VideoPath)\n" +
//                "VALUES ('Подъем штанги на бицепс', 'Встаньте прямо и установите ноги на ширине плеч. Ступни расположены почти параллельно, носки направлены немного в стороны. Возьмите штангу хватом снизу (ладони смотрят вверх) на ширине плеч. Держите спину прямой, не сутультесь. Взгляд направлен вперед. Сделайте глубокий вдох, задержите дыхание и, сгибая руки в локтях, поднимите штангу до уровня верха груди. Во время подъема штанги не двигайте локтями, держите их по бокам туловища и не сгибайте руки в запястьях. Как только кисти окажутся на уровне верха груди, сделайте паузу, выдохните и еще сильнее напрягите бицепсы. Плавно опустите штангу вниз, но не разгибайте руки полностью (не блокируйте локтевой сустав). Во время всего движения не наклоняйте торс ни вперед, ни назад. Держите правильную осанку.',\n" +
//                " 'https://www.youtube.com/watch?v=FqF11YdwxrQ')");
//
//
//        db.execSQL("INSERT INTO ExerciseTag(Title)\n" +
//                "VALUES ('Грудь')");
//        db.execSQL("INSERT INTO ExerciseTag(Title)\n" +
//                "VALUES ('Трицепс')");
//        db.execSQL("INSERT INTO ExerciseTag(Title)\n" +
//                "VALUES ('Ноги')");
//        db.execSQL("INSERT INTO ExerciseTag(Title)\n" +
//                "VALUES ('Спина')");
//        db.execSQL("INSERT INTO ExerciseTag(Title)\n" +
//                "VALUES ('Ягодицы')");
//        db.execSQL("INSERT INTO ExerciseTag(Title)\n" +
//                "VALUES ('Бицепс')");
//
//
//        db.execSQL("INSERT INTO ExerciseToTag(ExerciseId, TagId)\n" +
//                "VALUES ('1', '1')");
//        db.execSQL("INSERT INTO ExerciseToTag(ExerciseId, TagId)\n" +
//                "VALUES ('1', '2')");
//        db.execSQL("INSERT INTO ExerciseToTag(ExerciseId, TagId)\n" +
//                "VALUES ('2', '3')");
//        db.execSQL("INSERT INTO ExerciseToTag(ExerciseId, TagId)\n" +
//                "VALUES ('2', '4')");
//        db.execSQL("INSERT INTO ExerciseToTag(ExerciseId, TagId)\n" +
//                "VALUES ('2', '5')");
//        db.execSQL("INSERT INTO ExerciseToTag(ExerciseId, TagId)\n" +
//                "VALUES ('3', '4')");
//        db.execSQL("INSERT INTO ExerciseToTag(ExerciseId, TagId)\n" +
//                "VALUES ('3', '6')");
//        db.execSQL("INSERT INTO ExerciseToTag(ExerciseId, TagId)\n" +
//                "VALUES ('4', '4')");
//        db.execSQL("INSERT INTO ExerciseToTag(ExerciseId, TagId)\n" +
//                "VALUES ('4', '5')");
//        db.execSQL("INSERT INTO ExerciseToTag(ExerciseId, TagId)\n" +
//                "VALUES ('5', '3')");
//        db.execSQL("INSERT INTO ExerciseToTag(ExerciseId, TagId)\n" +
//                "VALUES ('5', '5')");
//        db.execSQL("INSERT INTO ExerciseToTag(ExerciseId, TagId)\n" +
//                "VALUES ('6', '3')");
//        db.execSQL("INSERT INTO ExerciseToTag(ExerciseId, TagId)\n" +
//                "VALUES ('7', '1')");
//        db.execSQL("INSERT INTO ExerciseToTag(ExerciseId, TagId)\n" +
//                "VALUES ('7', '2')");
//        db.execSQL("INSERT INTO ExerciseToTag(ExerciseId, TagId)\n" +
//                "VALUES ('8', '3')");
//        db.execSQL("INSERT INTO ExerciseToTag(ExerciseId, TagId)\n" +
//                "VALUES ('9', '3')");
//        db.execSQL("INSERT INTO ExerciseToTag(ExerciseId, TagId)\n" +
//                "VALUES ('9', '4')");
//        db.execSQL("INSERT INTO ExerciseToTag(ExerciseId, TagId)\n" +
//                "VALUES ('9', '5')");
//        db.execSQL("INSERT INTO ExerciseToTag(ExerciseId, TagId)\n" +
//                "VALUES ('10', '6')");
//
//
//
//        db.execSQL("INSERT INTO Training(Title)\n" +
//                "VALUES ('Тренировка #1');");
//        db.execSQL("INSERT INTO Training(Title)\n" +
//                "VALUES ('Тренировка #2');");
//        db.execSQL("INSERT INTO Training(Title)\n" +
//                "VALUES ('Тренировка #3');");
//        db.execSQL("INSERT INTO Training(Title)\n" +
//                "VALUES ('Упрощенная тренировка #1');");
//
//
//        db.execSQL("INSERT INTO Account(Username, HashPassword, BirthDate, RegistrationDate, Sex, TrainingId)\n" +
//                "VALUES ('dchertanov', 'HashPassword', '2000-08-21', '2021-03-31', 'Male', 1);");
//
//
//        db.execSQL("INSERT INTO TrainingExercise(TrainingId, DayOfWeek, OrderNumber, ExerciseId, SetNumber, RepsNum)\n" +
//                "VALUES ('1', '0', '1', '1', '3', '10');");
//        db.execSQL("INSERT INTO TrainingExercise(TrainingId, DayOfWeek, OrderNumber, ExerciseId, SetNumber, RepsNum)\n" +
//                "VALUES ('1', '0', '2', '2', '3', '10');");
//        db.execSQL("INSERT INTO TrainingExercise(TrainingId, DayOfWeek, OrderNumber, ExerciseId, SetNumber, RepsNum)\n" +
//                "VALUES ('1', '0', '3', '3', '3', '10');");
//        db.execSQL("INSERT INTO TrainingExercise(TrainingId, DayOfWeek, OrderNumber, ExerciseId, SetNumber, RepsNum)\n" +
//                "VALUES ('1', '1', '1', '4', '3', '10');");
//        db.execSQL("INSERT INTO TrainingExercise(TrainingId, DayOfWeek, OrderNumber, ExerciseId, SetNumber, RepsNum)\n" +
//                "VALUES ('1', '1', '2', '5', '3', '10');");
//        db.execSQL("INSERT INTO TrainingExercise(TrainingId, DayOfWeek, OrderNumber, ExerciseId, SetNumber, RepsNum)\n" +
//                "VALUES ('1', '1', '3', '6', '3', '10');");
//        db.execSQL("INSERT INTO TrainingExercise(TrainingId, DayOfWeek, OrderNumber, ExerciseId, SetNumber, RepsNum)\n" +
//                "VALUES ('1', '2', '1', '7', '3', '10');");
//        db.execSQL("INSERT INTO TrainingExercise(TrainingId, DayOfWeek, OrderNumber, ExerciseId, SetNumber, RepsNum)\n" +
//                "VALUES ('1', '2', '2', '8', '3', '10');");
//        db.execSQL("INSERT INTO TrainingExercise(TrainingId, DayOfWeek, OrderNumber, ExerciseId, SetNumber, RepsNum)\n" +
//                "VALUES ('1', '2', '3', '9', '3', '10');");
//    }
}
