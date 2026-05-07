package com.example.fe.feature.solver.data

object CodeTemplates {
    val JAVA = """
        import java.util.*;
        
        public class Solution {
            public int[] solution(int[] nums) {
                // 여기에 코드를 작성하세요...
                return new int[] {};
            }
        }
    """.trimIndent()

    val CPP = """
        #include <iostream>
        #include <vector>
        #include <string>
        
        using namespace std;
        
        vector<int> solution(vector<int> nums) {
            // 여기에 코드를 작성하세요...
            return {};
        }
    """.trimIndent()

    val PYTHON = """
        def solution(nums):
            # 여기에 코드를 작성하세요...
            return []
    """.trimIndent()

    val JAVASCRIPT = """
        function solution(nums) {
            // 여기에 코드를 작성하세요...
            return [];
        }
    """.trimIndent()

    fun getInitialCode(language: String): String {
        return when (language.uppercase()) {
            "JAVA" -> JAVA
            "C++", "CPP" -> CPP
            "PYTHON" -> PYTHON
            "JAVASCRIPT", "JS" -> JAVASCRIPT
            else -> JAVA
        }
    }
}
