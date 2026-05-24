package com.example.fe.feature.solver.data

object CodeTemplates {
    val JAVA = """
import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    }
}
""".trimIndent()

    val CPP = """
#include <bits/stdc++.h>
using namespace std;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    return 0;
}
""".trimIndent()

    val PYTHON = """
import sys
input = sys.stdin.readline

def main():
    pass

main()
""".trimIndent()

    val JAVASCRIPT = """
const readline = require('readline');
const rl = readline.createInterface({ input: process.stdin });
const lines = [];
rl.on('line', line => lines.push(line.trim()));
rl.on('close', () => {

});
""".trimIndent()

    fun getInitialCode(language: String): String = when (language.uppercase()) {
        "JAVA"                  -> JAVA
        "C++", "CPP"            -> CPP
        "PYTHON"                -> PYTHON
        "JAVASCRIPT", "JS"      -> JAVASCRIPT
        else                    -> JAVA
    }

    fun commentPrefix(language: String): String = when (language.uppercase()) {
        "PYTHON" -> "# "
        else     -> "// "
    }
}
