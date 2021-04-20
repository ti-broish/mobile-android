package bg.dabulgaria.tibroish.domain.calculators

interface IHashCalculator {

    fun calculate(vararg args: String): String
}