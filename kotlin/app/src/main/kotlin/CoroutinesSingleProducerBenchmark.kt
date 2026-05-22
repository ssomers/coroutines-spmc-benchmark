package justForYouJMH

import CoroutinesSingleProducerDemo
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Warmup
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 1)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
class CoroutinesSingleProducerBenchmark {
    @Param("1", "100")
    var load = 0

    @Param("100000")
    var jobs = 0

    @Param("0", "64")
    var capacity = 0

    @Param("1", "2", "3", "4", "5", "6", "7", "8", "9", "33", "99")
    var workers = 0

    @Benchmark
    fun b() {
        CoroutinesSingleProducerDemo().explore(
            load = load,
            jobs = jobs,
            capacity = capacity,
            workers = workers,
            logMain = ::println,
            logDetail = fun(msg) {}
        )
    }
}
