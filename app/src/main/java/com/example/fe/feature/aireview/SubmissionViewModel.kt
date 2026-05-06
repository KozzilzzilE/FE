package com.example.fe.feature.aireview

import androidx.lifecycle.ViewModel
import com.example.fe.feature.aireview.model.SubmissionEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SubmissionViewModel : ViewModel() {

    // TODO: API 연동 시 실제 데이터로 교체
    private val _entries = MutableStateFlow(mockEntries())
    val entries: StateFlow<List<SubmissionEntry>> = _entries.asStateFlow()

    private val _selectedEntry = MutableStateFlow<SubmissionEntry?>(null)
    val selectedEntry: StateFlow<SubmissionEntry?> = _selectedEntry.asStateFlow()

    fun selectEntry(historyId: Long) {
        _selectedEntry.value = _entries.value.find { it.historyId == historyId }
    }

    private fun mockEntries(): List<SubmissionEntry> = listOf(
        SubmissionEntry(
            historyId = 1L,
            problemTitle = "배열 두 배 만들기",
            language = "Python",
            date = "2026.03.31 14:29",
            isCorrect = true,
            sourceCode = "def solution(numbers):\n    return [n * 2 for n in numbers]"
        ),
        SubmissionEntry(
            historyId = 2L,
            problemTitle = "배열 두 배 만들기",
            language = "Python",
            date = "2026.03.31 14:15",
            isCorrect = false,
            sourceCode = "def solution(numbers):\n    return numbers"
        ),
        SubmissionEntry(
            historyId = 3L,
            problemTitle = "최빈값 구하기",
            language = "Java",
            date = "2026.03.30 10:05",
            isCorrect = true,
            sourceCode = "import java.util.*;\npublic class Solution {\n    public int solution(int[] array) {\n        // ...\n        return 0;\n    }\n}"
        ),
        SubmissionEntry(
            historyId = 4L,
            problemTitle = "문자열 뒤집기",
            language = "JavaScript",
            date = "2026.03.29 18:22",
            isCorrect = true,
            sourceCode = "function solution(my_string) {\n    return my_string.split('').reverse().join('');\n}"
        ),
        SubmissionEntry(
            historyId = 5L,
            problemTitle = "다음에 올 숫자",
            language = "C++",
            date = "2026.03.28 22:10",
            isCorrect = false,
            sourceCode = "#include <vector>\nusing namespace std;\nvector<int> solution(vector<int> common) {\n    return {};\n}"
        )
    )
}
