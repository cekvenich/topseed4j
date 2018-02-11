package org.info.util;

/**
 * 'Atomic' - pattern.
 *
 * @author vic A background thread updates needed variables - lock free. Mostly
 *         singleton.
 */
public interface IDaemon extends Runnable {

	void _start();// static { INSTANCE._start(); }

	// Force ENUM Singleton on Daemons
	int ordinal();
}
