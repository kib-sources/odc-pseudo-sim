import kotlin.random.Random.Default.nextInt

class NumBank(maxAddress: Int) {
    private val bank = Array(maxAddress) { i -> compileNum(i.toULong(), if (i == 0) 1u else 0u) }

    private fun extractAddress(num: ULong): ULong {
        return num shr 48
    }

    private fun extractPostnum(num: ULong): ULong {
        return num and 0x0000FFFFFFFFFFFFu
    }

    private fun compileNum(address: ULong, postnum: ULong): ULong {
        return address shl 48 or postnum
    }

    private fun numToPrettyString(num: ULong): String {
        return "addr: 0x${extractAddress(num).toString(16).padStart(4, '0')}, postnum: 0x${
            extractPostnum(num).toString(
                16
            ).padStart(12, '0')
        }"
    }

    fun print() {
        println(" ===== BEGIN NUM BANK ===== ")
        var wasZero = false
        bank.forEach { num ->
            if (extractPostnum(num) > 0u) {
                println(numToPrettyString(num))
                wasZero = false
            } else if (!wasZero) {
                println("...")
                wasZero = true
            }
        }
        println(" =====  END NUM BANK  ===== ")
    }

    fun getNum(): ULong {
        val postnumStep = nextInt(1, 5).toULong()
        val maxPostnum = extractPostnum(bank[0])
        val nextPostnum = maxPostnum + postnumStep

        val blankNum = bank.first { i -> extractPostnum(i) == 0UL }
        val blankAddress = extractAddress(blankNum)
        val newNum = compileNum(blankAddress, nextPostnum)

        bank[blankAddress.toInt()] = newNum
        bank[0] = nextPostnum
        return newNum
    }

    fun checkNum(num: ULong): Boolean {
        val address = extractAddress(num)
        val postnum = extractPostnum(num)
        if (address + postnum == 0UL) return false

        val selectedNum = bank[address.toInt()]
        val selectedPostnum = extractPostnum(selectedNum)
        if (selectedPostnum == 0UL || selectedPostnum != postnum) return false

        bank[address.toInt()] = compileNum(address, 0u)
        return true
    }
}