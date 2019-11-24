import java.util.ConcurrentModificationException;

/**
 * Maintains a pair of associated locks, one for read-only operations and one
 * for writing. The read lock may be held simultaneously by multiple reader
 * threads, so long as there are no writers. The write lock is exclusive, but
 * also tracks which thread holds the lock. If unlock is called by any other
 * thread, a {@link ConcurrentModificationException} is thrown.
 *
 * @see SimpleLock
 * @see SimpleReadWriteLock
 */
public class SimpleReadWriteLock {

	/** The lock used for reading. */
	private final SimpleLock readerLock;

	/** The lock used for writing. */
	private final SimpleLock writerLock;

	/** Tracks the number of reader threads. */
	private int readers;

	/** Tracks the number of writer threads. */
	private int writers;

	/** The thread that will hold the write lock. */
	private Thread writingThread;

	/** The lock to synchronize on. */
	private final Object lock;

	/**
	 * Initializes a new simple read/write lock.
	 */
	public SimpleReadWriteLock() {
		readerLock = new ReadLock();
		writerLock = new WriteLock();

		readers = 0;
		writers = 0;
		writingThread = null;
		lock = this;
	}

	/**
	 * Returns the reader lock.
	 *
	 * @return the reader lock
	 */
	public SimpleLock readLock() {
		return readerLock;
	}

	/**
	 * Returns the writer lock.
	 *
	 * @return the writer lock
	 */
	public SimpleLock writeLock() {
		return writerLock;
	}

	/**
	 * Determines whether the thread running this code and the other thread are
	 * in fact the same thread.
	 *
	 * @param other the other thread to compare
	 * @return true if the thread running this code and the other thread are not
	 * null and have the same ID
	 *
	 * @see Thread#getId()
	 * @see Thread#currentThread()
	 */
	public static boolean sameThread(Thread other) {
		return other != null && other.getId() == Thread.currentThread().getId();
	}

	/**
	 * Used to maintain simultaneous read operations.
	 */
	private class ReadLock implements SimpleLock {

		/**
		 * Will wait until there are no active writers in the system, and then will
		 * increase the number of active readers.
		 */
		@Override
		public void lock() {
			synchronized (lock) {
				while (writers > 0) {
					try {
						lock.wait();
					}
					catch (InterruptedException e) {
						System.err.println("Warning: ReadLock lock interrupted while waiting.");
						Thread.currentThread().interrupt();
					}
				}
				readers++;
				lock.notifyAll(); // TODO Remove, shouldn't need to wake up threads here
			}
		}

		/**
		 * Will decrease the number of active readers, and notify any waiting threads if
		 * necessary.
		 */
		@Override
		public void unlock() {
			synchronized (lock) {
				readers--;
				if (readers == 0) {
					lock.notifyAll();
				}
			}
		}

	}

	/**
	 * Used to maintain exclusive write operations.
	 */
	private class WriteLock implements SimpleLock {
		/**
		 * Will wait until there are no active readers or writers in the system, and
		 * then will increase the number of active writers and update which thread
		 * holds the write lock.
		 */
		@Override
		public void lock() {
			synchronized (lock) {
				while (writingThread != null || writers > 0 || readers > 0) {
					try {
						lock.wait();
					}
					catch (InterruptedException e) {
						System.err.println("Warning: WriteLock lock interrupted while waiting.");
						Thread.currentThread().interrupt();
					}
				}

				writers++;
				writingThread = Thread.currentThread();
			}
		}

		/**
		 * Will decrease the number of active writers, and notify any waiting threads if
		 * necessary. If unlock is called by a thread that does not hold the lock, then
		 * a {@link ConcurrentModificationException} is thrown.
		 *
		 * @see #sameThread(Thread)
		 *
		 * @throws ConcurrentModificationException if unlock is called without previously
		 * calling lock or if unlock is called by a thread that does not hold the write lock
		 */
		@Override
		public void unlock() throws ConcurrentModificationException {
			synchronized (lock) {
				if (!sameThread(writingThread)) {
					throw new ConcurrentModificationException();
				}

				writers--;
				writingThread = null;
				lock.notifyAll();
			}
		}
	}
}
