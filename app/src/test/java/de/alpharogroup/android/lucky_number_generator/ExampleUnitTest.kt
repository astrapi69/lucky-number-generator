package de.alpharogroup.android.lucky_number_generator

import de.alpharogroup.android.lucky_number_generator.data.converter.ListLotteryNumberCountConverter
import de.alpharogroup.android.lucky_number_generator.data.LotteryNumberCount
import de.alpharogroup.collections.list.ListFactory
import de.alpharogroup.collections.map.MapFactory
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @Test
    fun listLotteryNumberCountConverterTest() {
        val listLotteryNumberCountConverter =
            ListLotteryNumberCountConverter()
        val numberCounterMap = LotteryNumberCount(
            id = UUID.fromString("edc6ea10-26d8-44d0-95ea-e519821be9d5"), lotteryGameType = "6of49", numberCounterMap = MapFactory.newCounterMap(
                ListFactory.newRangeList(
                    1,
                    49
                )
            ))
        val numberCounterMap2 = LotteryNumberCount(
            id = UUID.fromString("edc6ea10-28d6-44d0-95ea-e519821be9d5"), lotteryGameType = "6of49", numberCounterMap = MapFactory.newCounterMap(
                ListFactory.newRangeList(
                    1,
                    49
                )
            ))
        val list = mutableListOf<LotteryNumberCount>(numberCounterMap, numberCounterMap2)
        val actual = listLotteryNumberCountConverter.fromList(list)
        val expected = "[{\"id\":\"edc6ea10-26d8-44d0-95ea-e519821be9d5\",\"lotteryGameType\":\"6of49\",\"numberCounterMap\":{\"1\":0,\"2\":0,\"3\":0,\"4\":0,\"5\":0,\"6\":0,\"7\":0,\"8\":0,\"9\":0,\"10\":0,\"11\":0,\"12\":0,\"13\":0,\"14\":0,\"15\":0,\"16\":0,\"17\":0,\"18\":0,\"19\":0,\"20\":0,\"21\":0,\"22\":0,\"23\":0,\"24\":0,\"25\":0,\"26\":0,\"27\":0,\"28\":0,\"29\":0,\"30\":0,\"31\":0,\"32\":0,\"33\":0,\"34\":0,\"35\":0,\"36\":0,\"37\":0,\"38\":0,\"39\":0,\"40\":0,\"41\":0,\"42\":0,\"43\":0,\"44\":0,\"45\":0,\"46\":0,\"47\":0,\"48\":0,\"49\":0}},{\"id\":\"edc6ea10-28d6-44d0-95ea-e519821be9d5\",\"lotteryGameType\":\"6of49\",\"numberCounterMap\":{\"1\":0,\"2\":0,\"3\":0,\"4\":0,\"5\":0,\"6\":0,\"7\":0,\"8\":0,\"9\":0,\"10\":0,\"11\":0,\"12\":0,\"13\":0,\"14\":0,\"15\":0,\"16\":0,\"17\":0,\"18\":0,\"19\":0,\"20\":0,\"21\":0,\"22\":0,\"23\":0,\"24\":0,\"25\":0,\"26\":0,\"27\":0,\"28\":0,\"29\":0,\"30\":0,\"31\":0,\"32\":0,\"33\":0,\"34\":0,\"35\":0,\"36\":0,\"37\":0,\"38\":0,\"39\":0,\"40\":0,\"41\":0,\"42\":0,\"43\":0,\"44\":0,\"45\":0,\"46\":0,\"47\":0,\"48\":0,\"49\":0}}]"
        assertEquals(expected, actual)
        val fromString = listLotteryNumberCountConverter.fromString(actual)
        assertEquals(fromString, list)
    }
}
