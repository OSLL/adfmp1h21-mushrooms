package ru.ifmo.mushrooms.util

data class PlaceNameError(val code: Int, val msg: String)

class PlaceNameErrorCode {
    companion object {
        const val EMPTY_ERROR = 0
        const val LEN_ERROR = 1
        const val NEW_LINE_ERROR = 2
        const val SYMBOLS_ERROR = 3
    }
}

fun checkPlaceNameIsCorrect(name: String): PlaceNameError? {
    val re = Regex("[а-яА-Яa-zA-Z]")
    return when {
        name.isEmpty() -> PlaceNameError(
            PlaceNameErrorCode.EMPTY_ERROR,
            "Название места не введено."
        )
        name.length > 50 -> PlaceNameError(
            PlaceNameErrorCode.LEN_ERROR,
            "Название места должно быть не более 50 символов."
        )
        name.contains('\n') -> PlaceNameError(
            PlaceNameErrorCode.NEW_LINE_ERROR,
            "Название места не должно содержать символа перевода строки."
        )
        !name.contains(re) -> PlaceNameError(
            PlaceNameErrorCode.SYMBOLS_ERROR,
            "Название места должно содержать хотя бы одну русскую или английскую букву."
        )
        else -> null
    }
}