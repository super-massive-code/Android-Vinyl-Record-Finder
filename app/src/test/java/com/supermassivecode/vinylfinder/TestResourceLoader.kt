package com.supermassivecode.vinylfinder

class TestResourceLoader {
    companion object {
        fun loadTextFile(fileName: String): String {
            return this::class.java.classLoader
                ?.getResource(fileName)
                ?.readText()
                ?: throw IllegalArgumentException("File not found: $fileName")
        }
    }
}