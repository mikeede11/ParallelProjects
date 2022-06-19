# ParallelProjects
This repo contains a collection of projects that emphasize concepts in parallel programming
## AcceleratedGUI
This project renders an image (a mandlebrot set) using a user specified number of threads. 
Mandlebrot sets are somewhat intense calculations so the more threads used the greater the speedup the user will observe.
## BusyTutor
A program that simulates a tutor that can see only one student at a time and only a certain number can be in the waiting room.
The students are modeled as threads that try to "meet up" the tutor thread and get tutored. Similarly the tutor wants to tutor,
but can  only tutor one at a time. The program meets the constraints by using concurrency tools such as mutexes, semaphores, and CyclicBarriers.
## Chat Client
This program is a simple chat client that has a server processing client requests to communicate with other clients.
the key is to make the server multi-threaded so it can  process multiple client requests concurrently - otherwise the server would be agonizingly inefficient!
## File Search
A program that searches for a file in a very large (hundreds of thousands+) file system. A sequential (single-threaded) search algorithm is devised in
`SequentialFileSearch.java` and a parallel one is devised in `ParallelFileSearch.java`. The project requires devising using concurrency in a 
particular way to reap the benefits.
## MatrixOps
A program that sped up Matrix Addition and Multiplication with parallelism. a detailed report is available in the `assignments/MatrixOps/MatrixOps.pdf`
## PingPongVolatile
A program where two threads, a PING thread and a PONG thread "play" ping-pong. The program must coordinate between the two threads so that we get a strictly alternating sequence of PINGs and PONGs. 
The `Volatile` keyword is used to achieve this thread coordination.
## RWLock
When multiple threads are reading and writing data we want to ensure that the threads are coordinating with each other so they do not mess up the data.
Use of `synchronized` `notify()`, and `notifyAll()` are some of the Java concurrency features we use to obtain these Read and Write locks (mutexes).
## WordFrequenies
A program that has both a sequential and a parallel algorithm for finding the word frequencies of words given a body of text. 
See `WordFrequencies.pdf` for details and results
