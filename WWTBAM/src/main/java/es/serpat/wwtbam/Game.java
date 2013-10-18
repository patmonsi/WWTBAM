package es.serpat.wwtbam;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SergiuDaniel on 15/10/13.
 */
public class Game extends FragmentActivity {

    //Constants for saved data.
    private String SHARED_PREF_GAME = "isGameSaved";
    private String SHARED_PREF_QUESTION_NUMBER = "questionNumber";
    private String SHARED_PREF_NAME_KEY = "playerName";
    private String SHARED_PREF_AUDIENCE = "isUsedAudienceJoker";
    private String SHARED_PREF_FIFTY_USED = "questionFiftyJokerWereUsed";
    private String SHARED_PREF_FIFTY = "isUsedFiftyJoker";
    private String SHARED_PREF_PHONE = "isUsedPhoneJoker";

    private List<Question> questionList = null;
    private int actualQuestion;

    private boolean isUsedAudienceJoker;
    private boolean isUsedFiftyJoker;
    private int questionFiftyJokerWereUsed;
    private boolean isUsedPhoneJoker;

    private boolean isGameSaved;

    private String playerName = null;

    private Play activity;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private int listLevels[] = {0, 100, 200, 300, 500, 1000, 2000, 4000, 8000,
            16000, 32000, 64000, 125000, 250000, 500000, 1000000};

    public Game(Play activity) {
        this.activity = activity;

        preferences = activity.getSharedPreferences(activity.getResources().getString(R.string.pref_file), Context.MODE_PRIVATE);
        editor = preferences.edit();

        if (questionList == null)
            questionList = generateQuestionList();

        isGameSaved = preferences.getBoolean(SHARED_PREF_GAME, false);

        editor.putBoolean(SHARED_PREF_GAME, true);
        editor.commit();

    }

    private List<String> getArrayJokers() {
        List<String> list = new ArrayList<String>();

        if (!isUsedPhoneJoker)
            list.add(activity.getResources().getString(R.string.phone));
        if (!isUsedFiftyJoker)
            list.add(activity.getResources().getString(R.string.fifty));
        if (!isUsedAudienceJoker)
            list.add(activity.getResources().getString(R.string.audience));

        return list;
    }

    public String numToLetter(String s) {
        if (s.equals("1"))
            return "A";
        else if (s.equals("2"))
            return "B";
        else if (s.equals("3"))
            return "C";
        else
            return "D";
    }

    /**
     * Test if the given answer is the right answer or not and according to the answer and game level, display a different dialog in the PlayActivity
     *
     * @param answer, the answer chosen by the player
     */
    public void testAnswer(String answer) {
        if (answer.equals(questionList.get(actualQuestion).right)) {
            if (actualQuestion >= questionList.size() - 1) {
                setUnsaveGame();
                activity.questionAnswered("win");
            } else {
                actualQuestion++;
                activity.questionAnswered("right");
            }
        } else {
            setUnsaveGame();
            activity.questionAnswered("wrong");
        }
    }

    public void askForJoker(String joker) {
        if (joker.equals(activity.getResources().getString(R.string.phone))) {
            isUsedPhoneJoker = true;
            activity.showPhoneCallAnswer(numToLetter(questionList.get(actualQuestion).phone));
        } else if (joker.equals(activity.getResources().getString(R.string.fifty))) {
            isUsedFiftyJoker = true;
            questionFiftyJokerWereUsed = actualQuestion;
            activity.hideButton(numToLetter(questionList.get(actualQuestion).fifty1));
            activity.hideButton(numToLetter(questionList.get(actualQuestion).fifty2));
        } else if (joker.equals(activity.getResources().getString(R.string.audience))) {
            isUsedAudienceJoker = true;
            activity.showAudienceAnswer(numToLetter(questionList.get(actualQuestion).audience));
        }
    }

    public void saveGameData() {
        editor.putInt(SHARED_PREF_QUESTION_NUMBER, actualQuestion);
        editor.putString(SHARED_PREF_NAME_KEY, playerName);
        editor.putBoolean(SHARED_PREF_PHONE, isUsedPhoneJoker);
        editor.putBoolean(SHARED_PREF_FIFTY, isUsedFiftyJoker);
        editor.putInt(SHARED_PREF_FIFTY_USED, questionFiftyJokerWereUsed);
        editor.putBoolean(SHARED_PREF_AUDIENCE, isUsedAudienceJoker);
        editor.commit();
    }

    public void restoreGameData() {
        actualQuestion = preferences.getInt(SHARED_PREF_QUESTION_NUMBER, 0);
        playerName = preferences.getString(SHARED_PREF_NAME_KEY, activity.getResources().getString(R.string.default_user_name));
        isUsedPhoneJoker = preferences.getBoolean(SHARED_PREF_PHONE, false);
        isUsedFiftyJoker = preferences.getBoolean(SHARED_PREF_FIFTY, false);
        questionFiftyJokerWereUsed = preferences.getInt(SHARED_PREF_FIFTY_USED, -1);
        isUsedAudienceJoker = preferences.getBoolean(SHARED_PREF_AUDIENCE, false);
    }

    public String getJokerName(int pos) {
        return getArrayJokers().get(pos);
    }

    public String[] getAllowedJokers() {
        String[] str = new String[getArrayJokers().size()];
        return getArrayJokers().toArray(str);
    }

    public boolean areJokersLeft() {
        return 3 - getAllowedJokers().length < preferences.getInt("numberJokers", 3);
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public boolean isGameSaved() {
        return isGameSaved;
    }

    public int getActualQuestion() {
        return actualQuestion;
    }

    public int getQuestionFiftyJokerWereUsed() {
        return questionFiftyJokerWereUsed;
    }

    public boolean isUsedFiftyJoker() {
        return isUsedFiftyJoker;
    }

    public List<Question> generateQuestionList() {
        List<Question> list = new ArrayList<Question>();
        Question q;

        q = new Question(
                "1",
                "Which is the Sunshine State of the US?",
                "North Carolina",
                "Florida",
                "Texas",
                "Arizona",
                "2",
                "2",
                "2",
                "1",
                "4"
        );
        list.add(q);

        q = new Question(
                "2",
                "Which of these is not a U.S. state?",
                "New Hampshire",
                "Washington",
                "Wyoming",
                "Manitoba",
                "4",
                "4",
                "4",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "3",
                "What is Book 3 in the Pokemon book series?",
                "Charizard",
                "Island of the Giant Pokemon",
                "Attack of the Prehistoric Pokemon",
                "I Choose You!",
                "3",
                "2",
                "3",
                "1",
                "4"
        );
        list.add(q);

        q = new Question(
                "4",
                "Who was forced to sign the Magna Carta?",
                "King John",
                "King Henry VIII",
                "King Richard the Lion-Hearted",
                "King George III",
                "1",
                "3",
                "1",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "5",
                "Which ship was sunk in 1912 on its first voyage, although people said it would never sink?",
                "Monitor",
                "Royal Caribean",
                "Queen Elizabeth",
                "Titanic",
                "4",
                "4",
                "4",
                "1",
                "2"
        );
        list.add(q);

        q = new Question(
                "6",
                "Who was the third James Bond actor in the MGM films? (Do not include Casino Royale.)",
                "Roger Moore",
                "Pierce Brosnan",
                "Timothy Dalton",
                "Sean Connery",
                "1",
                "3",
                "3",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "7",
                "Which is the largest toothed whale?",
                "Humpback Whale",
                "Blue Whale",
                "Killer Whale",
                "Sperm Whale",
                "4",
                "2",
                "2",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "8",
                "In what year was George Washington born?",
                "1728",
                "1732",
                "1713",
                "1776",
                "2",
                "2",
                "2",
                "1",
                "4"
        );
        list.add(q);

        q = new Question(
                "9",
                "Which of these rooms is in the second floor of the White House?",
                "Red Room",
                "China Room",
                "State Dining Room",
                "East Room",
                "2",
                "2",
                "2",
                "3",
                "4"
        );
        list.add(q);

        q = new Question(
                "10",
                "Which Pope began his reign in 963?",
                "Innocent III",
                "Leo VIII",
                "Gregory VII",
                "Gregory I",
                "2",
                "1",
                "2",
                "3",
                "4"
        );
        list.add(q);

        q = new Question(
                "11",
                "What is the second longest river in South America?",
                "Parana River",
                "Xingu River",
                "Amazon River",
                "Rio Orinoco",
                "1",
                "1",
                "1",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "12",
                "What Ford replaced the Model T?",
                "Model U",
                "Model A",
                "Edsel",
                "Mustang",
                "2",
                "4",
                "4",
                "1",
                "3"
        );
        list.add(q);

        q = new Question(
                "13",
                "When was the first picture taken?",
                "1860",
                "1793",
                "1912",
                "1826",
                "4",
                "4",
                "4",
                "1",
                "3"
        );
        list.add(q);

        q = new Question(
                "14",
                "Where were the first Winter Olympics held?",
                "St. Moritz, Switzerland",
                "Stockholm, Sweden",
                "Oslo, Norway",
                "Chamonix, France",
                "4",
                "1",
                "4",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "15",
                "Which of these is not the name of a New York tunnel?",
                "Brooklyn-Battery",
                "Lincoln",
                "Queens Midtown",
                "Manhattan",
                "4",
                "4",
                "4",
                "1",
                "3"
        );
        list.add(q);

        return list;
    }

    public void setUnsaveGame() {
        editor.putBoolean(SHARED_PREF_GAME, false);
        editor.commit();
    }
}