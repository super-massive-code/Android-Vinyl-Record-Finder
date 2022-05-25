package com.supermassivecode.vinylfinder

import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

class TestResourceLoader {

    companion object {

        fun loadTextFile(filePath: String): String {
            val br = BufferedReader(InputStreamReader(FileInputStream(filePath)))
            val sb = StringBuilder()
            var line: String = br.readLine()
            try {
                while (true) {
                    sb.append(line)
                    line = br.readLine()
                }
            } catch (e: NullPointerException) {
                return sb.toString()
            }
        }
    }
}
