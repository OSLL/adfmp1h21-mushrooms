package ru.ifmo.mushrooms

import org.junit.Assert
import org.junit.Test
import ru.ifmo.mushrooms.util.*
import java.io.File


class MushroomsUnitTest {

    @Test
    fun test_place_name_is_correct() {
        Assert.assertEquals(PlaceNameErrorCode.EMPTY_ERROR, checkPlaceNameIsCorrect("")?.code)
        Assert.assertEquals(PlaceNameErrorCode.SYMBOLS_ERROR, checkPlaceNameIsCorrect("\t")?.code)
        Assert.assertEquals(PlaceNameErrorCode.SYMBOLS_ERROR, checkPlaceNameIsCorrect("                ")?.code)
        Assert.assertEquals(PlaceNameErrorCode.LEN_ERROR, checkPlaceNameIsCorrect("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")?.code)
        Assert.assertEquals(PlaceNameErrorCode.NEW_LINE_ERROR, checkPlaceNameIsCorrect("a\nb")?.code)
        Assert.assertEquals(PlaceNameErrorCode.SYMBOLS_ERROR, checkPlaceNameIsCorrect("12345")?.code)
        Assert.assertEquals(PlaceNameErrorCode.SYMBOLS_ERROR, checkPlaceNameIsCorrect("!!!!")?.code)

        Assert.assertNull(checkPlaceNameIsCorrect("ABC"))
        Assert.assertNull(checkPlaceNameIsCorrect("az"))
        Assert.assertNull(checkPlaceNameIsCorrect("Привет"))
        Assert.assertNull(checkPlaceNameIsCorrect("подберезовики"))
        Assert.assertNull(checkPlaceNameIsCorrect("в123"))
        Assert.assertNull(checkPlaceNameIsCorrect("djHjn45Yt"))
        Assert.assertNull(checkPlaceNameIsCorrect("место *_______* "))
    }


    @Test
    fun test_serialize_deserialize_place_info() {
        var placeInfo = PlaceInfo("myPlace", ":))", "path to image", 1.0, 5.4)
        var file = placeInfo.serializePlaceInfo(File("."))
        var deserializeInfo = deserializePlaceInfo(file)
        Assert.assertEquals(placeInfo, deserializeInfo)

        placeInfo = PlaceInfo("h_12345", "", "img", 0.0, -0.5)
        file = placeInfo.serializePlaceInfo(File("."))
        deserializeInfo = deserializePlaceInfo(file)
        Assert.assertEquals(placeInfo, deserializeInfo)

        val path = File("./placesInfo")
        path.deleteRecursively()
    }
}