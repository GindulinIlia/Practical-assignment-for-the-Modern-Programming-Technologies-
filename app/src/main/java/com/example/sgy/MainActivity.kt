package com.example.sgy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sgy.ui.theme.SGYTheme
import androidx.compose.foundation.layout.Row

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SGYTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SGYApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// ================= ДАННЫЕ =================

data class Exam(val subject: String, val minScore: Int, val maxScore: Int)

data class Direction(val title: String, val code: String, val exams: List<Exam>)

// Данные направления
val allDirections = listOf(
    Direction(
        title = "Архитектура",
        code = "07.03.01",
        exams = listOf(
            Exam("Русский язык", 40, 100),
            Exam("Математика", 40, 100),
            Exam("Рисунок", 40, 100)
        )
    ),
    Direction(
        title = "Дизайн",
        code = "54.03.01",
        exams = listOf(
            Exam("Русский язык", 40, 100),
            Exam("Литература", 40, 100),
            Exam("Рисунок", 40, 100)
        )
    ),
    Direction(
        title = "Сервис",
        code = "43.03.01",
        exams = listOf(
            Exam("Русский язык", 40, 100),
            Exam("Обществознание", 44, 100),
            Exam("Математика", 40, 100),
            Exam("История", 36, 100),
            Exam("География", 40, 100),
            Exam("Иностранный язык", 30, 100)
        )
    ),
    Direction(
        title = "Строительство",
        code = "08.03.01",
        exams = listOf(
            Exam("Русский язык", 40, 100),
            Exam("Математика", 40, 100),
            Exam("Информатика", 44, 100),
            Exam("Физика", 39, 100)
        )
    ),
    Direction(
        title = "Прикладная информатика",
        code = "09.03.03",
        exams = listOf(
            Exam("Русский язык", 40, 100),
            Exam("Математика", 40, 100),
            Exam("Информатика", 44, 100),
            Exam("Физика", 39, 100)
        )
    )
)

// ================= НАВИГАЦИЯ =================

@Composable
fun SGYApp(modifier: Modifier = Modifier) {
    // null = главный экран, иначе = экран выбранного направления
    var selectedDirection by remember { mutableStateOf<Direction?>(null) }

    if (selectedDirection == null) {
        MainScreen(
            modifier = modifier,
            onDirectionClick = { selectedDirection = it } // открываем экран направления
        )
    } else {
        DirectionScreen(
            direction = selectedDirection!!,
            modifier = modifier,
            onBack = { selectedDirection = null } // возврат на главный
        )
    }
}

// ================= ГЛАВНЫЙ ЭКРАН =================

@Composable
fun MainScreen(modifier: Modifier = Modifier, onDirectionClick: (Direction) -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Логотип СОЧИ ГУ",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(180.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Выберите направление подготовки",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            allDirections.forEach { direction ->
                Button(onClick = { onDirectionClick(direction) }) {
                    Text(text = direction.title)
                }
            }
        }
    }
}

// ================= ЭКРАН НАПРАВЛЕНИЯ (ТАБЛИЦА) =================

@Composable
fun DirectionScreen(
    direction: Direction,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Заголовок направления
        Text(
            text = direction.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Шапка таблицы
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text("КОД", Modifier.weight(0.9f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text("ВСТУПИТЕЛЬНЫЕ ИСПЫТАНИЯ ПО ЕГЭ", Modifier.weight(2.2f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text("МИН. БАЛЛОВ", Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center)
            Text("МАКС. БАЛЛОВ", Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center)
        }

        // Строки таблицы
        if (direction.exams.isEmpty()) {
            Text("Информация уточняется...", modifier = Modifier.padding(top = 16.dp))
        } else {
            direction.exams.forEachIndexed { index, exam ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
                    // Код показываем только в первой строке, как в макете
                    Text(
                        if (index == 0) direction.code else "",
                        Modifier.weight(0.9f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(exam.subject, Modifier.weight(2.2f), fontSize = 14.sp)
                    Text(exam.minScore.toString(), Modifier.weight(1f), fontSize = 14.sp, textAlign = TextAlign.Center)
                    Text(exam.maxScore.toString(), Modifier.weight(1f), fontSize = 14.sp, textAlign = TextAlign.Center)
                }
            }
        }

        // Растягиваем пустое место, чтобы кнопка прижалась к низу
        Spacer(modifier = Modifier.weight(1f))

        // Кнопка возврата внизу
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3498DB)) // синий как в макете
        ) {
            Text("Назад к направлениям", color = Color.White)
        }
    }
}

// ================= ПРЕВЬЮ =================

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    SGYTheme { MainScreen(onDirectionClick = {}) }
}

@Preview(showBackground = true)
@Composable
fun DirectionScreenPreview() {
    SGYTheme { DirectionScreen(direction = allDirections[0], onBack = {}) }
}