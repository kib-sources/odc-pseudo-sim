class NumBank(maxAddress: Int) {
    private val bank = Array(maxAddress) { i -> compileNum(i.toULong(), 0u) }

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
    }

    fun getNum(): ULong {
        return 0u
    }

    fun checkNum(num: ULong): Boolean {
        return false
    }
}