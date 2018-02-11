package org.apache.bro;

public interface ICurrentTestTime {

	public long getCurrentTestTime();

	/**
	 * Waits for duration
	 */
	public void _blockFor(int n);
}
