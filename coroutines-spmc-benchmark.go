package main

import (
	"fmt"
	"sync"
	"time"
)

type Job struct {
	id int
}

func ignore(msg string) {
}

func reveal(msg string) {
	println(msg)
}

func process(jobs int, capacity int, workers int, log func(string)) {
	begin := time.Now()
	channel := make(chan Job, capacity)
	go func() {
		for i := range jobs {
			channel <- Job{i + 1}
		}
		close(channel)
	}()
	var wg sync.WaitGroup
	wg.Add(workers)
	for i := range workers {
		go func(id int) {
			var handled = 0
			log(fmt.Sprintf("worker %d signing on", id))
			for job := range channel {
				handled++
				log(fmt.Sprintf("worker %d taking on job %d", id, job.id))
			}
			log(fmt.Sprintf("worker %d signing off", id))
			expected := float64(jobs) / float64(workers)
			if jobs > 100 && handled < int(0.5*expected) {
				panic(fmt.Sprintf("worker %d only took on %d jobs out of %.0f expected", id, handled, expected))
			}
			wg.Done()
		}(i + 1)
	}
	wg.Wait()
	ms := time.Since(begin).Milliseconds()
	fmt.Printf("%6d jobs, capacity %2d, %2d workers: %5d ms\n", jobs, capacity, workers, ms)
}

func main() {
	//process(20, 0, 2, reveal)
	jobs := 100_000
	for _, capacity := range [...]int{0, 64} {
		for _, workers := range [...]int{1, 5, 33, 99} {
            for range 3 {
                process(jobs, capacity, workers, ignore)
            }
		}
	}
}
