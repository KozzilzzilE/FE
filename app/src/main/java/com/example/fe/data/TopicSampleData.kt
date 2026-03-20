package com.example.fe.data

import com.example.fe.data.dto.TopicResult

object TopicSampleData {
    val sampleTopics = listOf(
        TopicResult(topicId = 1, name = "HASH", displayName = "해시"),
        TopicResult(topicId = 2, name = "STACK", displayName = "스택"),
        TopicResult(topicId = 3, name = "QUEUE", displayName = "큐"),
        TopicResult(topicId = 4, name = "HEAP", displayName = "힙"),
        TopicResult(topicId = 5, name = "SORT", displayName = "정렬"),
        TopicResult(topicId = 6, name = "BRUTEFORCE", displayName = "완전탐색"),
        TopicResult(topicId = 7, name = "GREEDY", displayName = "탐욕법"),
        TopicResult(topicId = 8, name = "DP", displayName = "동적 계획법"),
        TopicResult(topicId = 9, name = "DFS", displayName = "DFS"),
        TopicResult(topicId = 10, name = "BFS", displayName = "BFS"),
        TopicResult(topicId = 11, name = "BINARYSEARCH", displayName = "이분탐색"),
        TopicResult(topicId = 12, name = "GRAPH", displayName = "그래프")
    )
}
