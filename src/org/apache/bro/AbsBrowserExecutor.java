package org.apache.bro;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.info.util.Pair;

import javolution.util.FastTable;

public abstract class AbsBrowserExecutor {

	protected ThreadPoolExecutor _execs;
	protected LinkedBlockingQueue _q = new LinkedBlockingQueue<Callable>();// unbounded task list
	protected List<Future<Pair>> _results = new FastTable();

	/**
	 * It starts with 1
	 */
	public void setNumbOfBrowsers(int n) {
		_execs.setMaximumPoolSize(n);
		_execs.setCorePoolSize(n);
	}

	public int getQsize() {
		return _execs.getQueue().size();
	}// ()

	/**
	 * Pending browser calls (not the Q)
	 */
	public int getActiveCount() throws Throwable {
		return _execs.getActiveCount();
	}

	public long getComplemetedCount() {
		return _execs.getCompletedTaskCount();
	}

	private long _lastCompleted = 0;

	/**
	 * How much did we get done since last call
	 */
	public long getComplemetedDelta() throws Throwable {
		long total = getComplemetedCount();
		long delta = total - _lastCompleted;
		_lastCompleted = total;
		return delta;
	}

	public void stop() {
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				_execs.shutdownNow();
			}// ()
		});
		t1.start();
	}// ()
}
