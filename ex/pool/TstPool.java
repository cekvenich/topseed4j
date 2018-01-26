package pool;

import org.apache.commons.pool2.impl.GenericObjectPool;

public class TstPool {

	public static GenericObjectPool<String> POOL;

	public static void main(String[] args) {

		SFact fact = new SFact();
		POOL = new GenericObjectPool(fact);
		POOL.setConfig(fact.returnPoolConfig());// sets it to 2
		POOL.setMaxTotal(3);

		System.out.println(POOL.getMaxTotal());
		for (int i = 0; i <= 5; i++) {
			System.out.print(i);
			try {
				System.out.println(POOL.borrowObject());
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		} // f

	}// ()

}// class
