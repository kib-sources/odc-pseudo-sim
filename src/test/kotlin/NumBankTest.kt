import org.junit.jupiter.api.Test
import kotlin.random.Random.Default.nextBoolean
import kotlin.random.Random.Default.nextInt
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NumBankTest {
    @Test
    fun initialization() {
        val numBank = NumBank(255)
        numBank.print()
    }

    @Test
    fun consecutive() {
        val numBank = NumBank(255)
        var num = numBank.getNum()
        numBank.print()
        assertFalse(numBank.checkNum(num + 12u))
        assertTrue(numBank.checkNum(num))
        numBank.print()
        assertFalse(numBank.checkNum(num))

        repeat(100) {
            num = numBank.getNum()
            assertTrue(numBank.checkNum(num))
        }
    }

    @Test
    fun cumulative_forward() {
        val numBank = NumBank(255)
        val nums = Array(254) { numBank.getNum() }
        assertEquals(0, nums.count { num -> !numBank.checkNum(num) })
    }

    @Test
    fun cumulative_reverse() {
        val numBank = NumBank(255)
        val nums = Array(254) { numBank.getNum() }
        nums.reverse()
        assertEquals(0, nums.count { num -> !numBank.checkNum(num) })
    }

    @Test
    fun cumulative_random() {
        val numBank = NumBank(255)
        val nums = Array(254) { numBank.getNum() }
        nums.shuffle()
        assertEquals(0, nums.count { num -> !numBank.checkNum(num) })
    }

    @Test
    fun random() {
        val numBank = NumBank(1024)
        val nums = mutableListOf<ULong>()
        repeat(150000) {
            val tryCheck = nextBoolean()
            if (tryCheck && nums.isNotEmpty()) {
                val randomIndex = nextInt(nums.size)
                val randomNum = nums[randomIndex]
                nums.removeAt(randomIndex)

                assertTrue(numBank.checkNum(randomNum))
            } else if (nums.size < 1023) {
                val newNum = numBank.getNum()
                nums.add(newNum)
            }
        }
        numBank.print()
    }

    @Test
    fun check_false_positive() {
        val numBank = NumBank(1024)
        val nums = mutableListOf<ULong>()
        repeat(150000) {
            val tryCheck = nextBoolean()
            if (tryCheck && nums.isNotEmpty()) {
                val randomIndex = nextInt(nums.size)
                val randomNum = nums[randomIndex]
                nums.removeAt(randomIndex)

                assertTrue(numBank.checkNum(randomNum))
            } else if (nums.size < 1023) {
                val newNum = numBank.getNum()
                nums.add(newNum)
            }
        }
        var recordPostnum = 0UL
        val maxPostnum = nums.maxByOrNull { i -> i and 0x0000FFFFFFFFFFFFu } as ULong and 0x0000FFFFFFFFFFFFu
        val notOccupiedPostnum = maxPostnum + 255u
        repeat(1022) {
            assertFalse(numBank.checkNum(it.toULong() + 1u shl 48 or notOccupiedPostnum))
        }
        nums.forEach { i -> assertTrue(numBank.checkNum(i)) }
        repeat(1022) {
            assertFalse(numBank.checkNum(it.toULong() + 1u shl 48 or nextInt(0xFF, 0xFFFFFF).toULong()))
        }
    }
}