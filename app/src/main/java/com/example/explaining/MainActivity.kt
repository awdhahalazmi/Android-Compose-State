
package com.example.explaining
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


import com.example.explaining.ui.theme.ExplainingTheme

data class Question(val text: String, val isCorrect: Boolean)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExplainingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TrueFalseGame()
                }
            }
        }
    }
}

@Composable
fun TrueFalseGame() {
    var questions by remember { mutableStateOf<List<Question>>(emptyList()) }
    var currentQuestionIndex = remember { mutableStateOf(0) }
    var AnsrCorrect by remember { mutableStateOf(false) }
    var showFeedback by remember { mutableStateOf(false) }
    var showAnswerOptionsRow by remember { mutableStateOf(true) }
    var userScore = remember { mutableStateOf(0) }
    var showResetButton by remember { mutableStateOf(false) }


    if (questions.isEmpty()) {
        questions = listOf(
            Question(stringResource(R.string.can_count_till_10), true),
            Question(stringResource(R.string.the_water_has_a_blue_color), false),
            Question(stringResource(R.string.the_sky_is_blue), true),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Step 1
        Text(
            text = questions[currentQuestionIndex.value].text,
            modifier = Modifier.padding(bottom = 20.dp)
        )

//the condition of the correct answer
        if (AnsrCorrect && showFeedback ) {
            AnswerFeedback("Correct!", MaterialTheme.colorScheme.secondary, true)
            //CorrectAnswerImage()
            Text(text = "Score: ${userScore.value}")

            Button(
                onClick = {
                    showFeedback = false
                    showAnswerOptionsRow = true
                    // Move to the next question or show the final score
                    if (currentQuestionIndex.value == questions.size - 1) {
                        showResetButton = true

                        AnsrCorrect = false
                    }

                    if (currentQuestionIndex.value < questions.size - 1) {
                        currentQuestionIndex.value++

                    }


                },
                modifier = Modifier
                    .width(200.dp)
                    .padding(10.dp),
            ) {
                Text(text = "Next Question")
            }
        } else if (!AnsrCorrect && showFeedback) {
            // Keep true and false buttons visible
            showFeedback = true
            // Show feedback for wrong answer
            AnswerFeedback("Wrong!", MaterialTheme.colorScheme.error, false)
            //WrongAnswerImage()

        }

        if (showAnswerOptionsRow) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            )
            {

                TrueFalseButton("True") {
                    AnsrCorrect = true == questions[currentQuestionIndex.value].isCorrect
                    if (AnsrCorrect) {
                        userScore.value += 1
                        showAnswerOptionsRow = false
                    }
                    showFeedback = true

                }
                TrueFalseButton("False") {
                    AnsrCorrect = false == questions[currentQuestionIndex.value].isCorrect
                    if (AnsrCorrect) {
                        userScore.value += 1
                        showAnswerOptionsRow = false
                    }
                    showFeedback = true

                }
            }
        }

        if (showResetButton) {
            showAnswerOptionsRow = false
            Button(onClick = {
                showAnswerOptionsRow = true
                showResetButton = false

                // Show the final score and restart button
                currentQuestionIndex.value = 0
                userScore.value = 0
                showAnswerOptionsRow = true


            }) {
                Text(text = "Reset Game")
            }
        }
    }
}


@Composable
fun TrueFalseButton(text: String, showAnswerOptionsRow: () -> Unit) {
    Button(
        onClick = {
            showAnswerOptionsRow()

        },
        modifier = Modifier
            .width(120.dp)
            .height(40.dp)

    ) {
        Text(text = text)
    }
}

@Composable
fun AnswerFeedback(message: String, backgroundColor: Color, isCorrect: Boolean) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Show the correct or wrong answer image based on the 'isCorrect' parameter
        val imagePainter = if (isCorrect) {
            painterResource(id = R.drawable.correct_answer)
        } else {
            painterResource(id = R.drawable.wrong_answer)
        }

        Image(
            painter = imagePainter,
            contentDescription = if (isCorrect) "Correct Answer Image" else "Wrong Answer Image",
            modifier = Modifier.fillMaxSize()
        )

        Text(
            text = message,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}



@Preview(showBackground = true)
@Composable
fun TrueFalseGamePreview() {
    TrueFalseGame()
}


