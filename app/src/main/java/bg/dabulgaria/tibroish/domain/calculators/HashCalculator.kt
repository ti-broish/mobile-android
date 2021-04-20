package bg.dabulgaria.tibroish.domain.calculators

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class HashCalculator : IHashCalculator {

    override fun calculate(vararg args: String): String {

        val input = concatenateArgs(args.toList())
        val digest = generateDigest(input)
        return toHexString(digest)
    }

    private fun concatenateArgs(args: List<String>): String {

        val stringBuilder = StringBuilder()
        for (arg in args) {
            stringBuilder.append(arg)
        }
        return stringBuilder.toString()
    }

    /**
     * Generates MD5 digest from an input string.
     *
     * @param input - input string
     * @return a byte array of the MD5 digest
     */
    private fun generateDigest(input: String): ByteArray? {
        try {
            val md = MessageDigest.getInstance("MD5")
            md.update(input.toByteArray())

            return md.digest()
        } catch (nsae: NoSuchAlgorithmException) {
            nsae.printStackTrace()
        }

        return null
    }

    private fun toHexString(digest: ByteArray?): String {

        val HEX_CHARS = "0123456789abcdef".toCharArray()

        if( digest == null )
            return ""

        val result = StringBuilder(digest.size * 2)

        digest.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }

        return result.toString()
    }
}