package ru.ifmo.mushrooms.util

fun getResourceId(name: String, c: Class<*>): Int? {
    return try {
        val idField = c.getDeclaredField(name)
        idField.getInt(idField)
    } catch (e: Exception) {
        null
    }
}