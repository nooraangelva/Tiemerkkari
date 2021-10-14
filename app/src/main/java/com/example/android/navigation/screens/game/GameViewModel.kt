package com.example.android.navigation.screens.game

import android.util.Log
import android.widget.RadioButton
import androidx.lifecycle.*
import com.example.android.navigation.R


private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val NO_BUZZ_PATTERN = longArrayOf(0)

data class Question(
        val text: String,
        val answers: List<String>)

// The first answer is the correct one.  We randomize the answers before showing the text.
// All questions must have four answers.  We'd want these to contain references to string
// resources so we could internationalize. (or better yet, not define the questions in code...)
private lateinit var questions: MutableList<Question>
private val questionsMul = mutableListOf(
        Question(text = "3x2= ?",
                answers = listOf("6", "2", "3", "0")),
        Question(text = "4x2=?",
                answers = listOf("8", "0", "1", "4")),
        Question(text = "1x0=?",
                answers = listOf("0", "1", "2", "-1")),
        Question(text = "0x0=?",
                answers = listOf("0", "1", "-1", "2")),
        Question(text = "-1x1=?",
                answers = listOf("-1", "-2", "0", "1")),
        Question(text = "4x4=?",
                answers = listOf("16", "1", "TÄh?", "4")),
        Question(text = "2x3=?",
                answers = listOf("6", "0", "1", "2")),
        Question(text = "4x3=?",
                answers = listOf("12", "0", "-1", "2")),
        Question(text = "8x9?",
                answers = listOf("72", "0", "2", "3")),
        Question(text = "3x3?",
                answers = listOf("9", "1", "-3", "3"))
)
private val questionsAdd = mutableListOf(
        Question(text = "3+2= ?",
                answers = listOf("5", "2", "3", "0")),
        Question(text = "4+2=?",
                answers = listOf("6", "0", "1", "4")),
        Question(text = "1+0=?",
                answers = listOf("1", "0", "2", "-1")),
        Question(text = "0+0=?",
                answers = listOf("0", "1", "-1", "2")),
        Question(text = "-1+1=?",
                answers = listOf("0", "-1", "-2", "1")),
        Question(text = "4+4=?",
                answers = listOf("8", "1", "TÄh?", "4")),
        Question(text = "2+3=?",
                answers = listOf("5", "0", "1", "2")),
        Question(text = "4+3=?",
                answers = listOf("7", "0", "-1", "2")),
        Question(text = "8+9?",
                answers = listOf("17", "0", "2", "3")),
        Question(text = "3+3?",
                answers = listOf("6", "1", "-3", "3"))
)
private val questionsSub = mutableListOf(
        Question(text = "3-2= ?",
                answers = listOf("1", "2", "3", "0")),
        Question(text = "4-2=?",
                answers = listOf("2", "0", "1", "4")),
        Question(text = "1-0=?",
                answers = listOf("1", "0", "2", "-1")),
        Question(text = "0-0=?",
                answers = listOf("0", "1", "-1", "2")),
        Question(text = "-1-1=?",
                answers = listOf("-2", "-1", "0", "1")),
        Question(text = "4-4=?",
                answers = listOf("0", "1", "TÄh?", "4")),
        Question(text = "2-3=?",
                answers = listOf("-1", "0", "1", "2")),
        Question(text = "4-3=?",
                answers = listOf("1", "0", "-1", "2")),
        Question(text = "8-9?",
                answers = listOf("-1", "0", "2", "3")),
        Question(text = "3-3?",
                answers = listOf("0", "1", "-3", "3"))
)


/**
 * ViewModel containing all the logic needed to run the game
 */
class GameViewModel(givenGameType: Int) : ViewModel() {

    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }
    companion object {
        // These represent different important times in the game, such as game length.
        // This is the number of milliseconds in a second
        private const val ONE_SECOND = 1000L
    }

    private val _gameType = MutableLiveData<Int>()
    val gameType: LiveData<Int>
        get() = _gameType

    // Event that triggers the phone to buzz using different patterns, determined by BuzzType
    private val _currentQuestion = MutableLiveData<String>()
    val currentQuestion: LiveData<String>
        get() = _currentQuestion

    private val _answers = MutableLiveData<MutableList<String>>()
    val answers: LiveData<MutableList<String>>
        get() = _answers

    private val _eventBuzz = MutableLiveData<BuzzType>()
    val eventBuzz: LiveData<BuzzType>
        get() = _eventBuzz

    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    private val _eventSubmit = MutableLiveData<Boolean>()
    val eventSubmit: LiveData<Boolean>
        get() = _eventSubmit

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _numQuestions = MutableLiveData<Int>()
    val numQuestions: LiveData<Int>
        get() = _numQuestions

    private val _questionIndex = MutableLiveData<Int>()
    val questionIndex: LiveData<Int>
        get() = _questionIndex

    private val _eventChangeTitle = MutableLiveData<Boolean>()
    val eventChangeTitle: LiveData<Boolean>
        get() = _eventChangeTitle

    init{
        Log.i("GameViewModel", "GameViewModel created.")

        // Shuffles the questions and sets the question index to the first question.
        _questionIndex.value = 0
        _numQuestions.value = Math.min((questionsSub.size + 1) / 2, 3)
        randomizeQuestions()
        _score.value = 0
        _gameType.value = givenGameType

    }

    //FUNCTIONS

    fun onGameFinishComplete() {

        _eventBuzz.value = BuzzType.CORRECT
        _eventGameFinish.value = false
    }

    fun onBuzzComplete() {
        _eventBuzz.value = BuzzType.NO_BUZZ
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun eventSubmit(){
        //TODO link correctly to radio button
        _eventBuzz.value = BuzzType.CORRECT

        val count: Int = questionRadioGroup.getChildCount()
        val listOfRadioButtons = ArrayList<RadioButton>()
        for (i in 0 until count) {
            val o: View = radioGroup.getChildAt(i)
            if (o is RadioButton) {
                listOfRadioButtons.add(o as RadioButton)
            }
        }
        if(radioButton.isChecked())
        {
            // is checked
        }
        else
        {
            // not checked
        }
        // Do nothing if nothing is checked (id == -1)
        if (-1 != checkedId) {
            var answerIndex = 0
            when (checkedId) {
                R.id.secondAnswerRadioButton -> answerIndex = 1
                R.id.thirdAnswerRadioButton -> answerIndex = 2
                R.id.fourthAnswerRadioButton -> answerIndex = 3
            }
            // The first answer in the original question is always the correct one, so if our
            // answer matches, we have the correct answer.

            if (answers.value!![answerIndex] == questions[_questionIndex.value!!].answers[0]) {
                _score.value = (_score.value)?.plus(1)
            }
            _questionIndex.value = (_questionIndex.value)?.plus(1)
            // Advance to the next question
            if (_questionIndex.value!! < _numQuestions.value!!) {
                _currentQuestion.value = questions[_questionIndex.value!!].text
                setQuestion()
                //binding.invalidateAll()
            } else {
                // We've won!  Navigate to the gameWonFragment.
                _eventGameFinish.value = true
            }
        }
    }

    fun submitComplete(){
        _eventBuzz.value = BuzzType.NO_BUZZ
        _eventSubmit.value = false
    }

    // randomize the questions and set the first question
    fun randomizeQuestions() {

        when(_gameType.value){
            0 -> questions = questionsMul
            1 -> questions = questionsAdd
            2 -> questions = questionsSub
        }
        questions.shuffle()
        setQuestion()
    }

    // Sets the question and randomizes the answers.  This only changes the data, not the UI.
    // Calling invalidateAll on the FragmentGameBinding updates the data.
    fun setQuestion() {
        _currentQuestion.value = questions[_questionIndex.value!!].text

        // randomize the answers into a copy of the array
        _answers.value = questions[_questionIndex.value!!].answers.toMutableList()
        val temp = questions[_questionIndex.value!!].answers.toMutableList()
        // and shuffle them
        //TODO fix
        temp.shuffle()
        _answers.value = temp
        _eventChangeTitle.value = true


    }

    fun changeActionBarTitleComplete(){
        _eventChangeTitle.value = false
    }



}