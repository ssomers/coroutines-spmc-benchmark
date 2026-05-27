import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce

class CoroutinesSingleProducerDemo {
    private data class Job(
        val id: Int,
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun CoroutineScope.produceJobs(jobs: Int, capacity: Int): ReceiveChannel<Job> =
        produce(Dispatchers.Default, capacity = capacity) {
            (1..jobs).forEach {
                send(Job(it))
            }
        }

    private fun CoroutineScope.launchWorker(
        id: Int,
        channel: ReceiveChannel<Job>,
        load: Int,
        logMain: (msg: String) -> Unit,
        logDetail: (msg: String) -> Unit
    ) = launch(Dispatchers.Default) {
        logMain("worker $id signing on")
        var handled = 0
        for (job in channel) {
            repeat(load) { logDetail("worker $id taking on job ${job.id}") }
            handled += 1
        }
        logMain("worker $id handled $handled jobs")
    }

    fun explore(
        load: Int,
        jobs: Int,
        capacity: Int,
        workers: Int,
        logMain: (msg: String) -> Unit,
        logDetail: (msg: String) -> Unit
    ) {
        runBlocking {
            val channel = produceJobs(jobs = jobs, capacity = capacity)
            (1..workers).forEach { launchWorker(it, channel, load, logMain, logDetail) }
        }
    }
}
