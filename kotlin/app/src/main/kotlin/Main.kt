internal object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        CoroutinesSingleProducerDemo().explore(
            load = 1,
            jobs = 7,
            capacity = 1,
            workers = 2,
            logMain = ::println,
            logDetail = ::println
        )
    }
}
