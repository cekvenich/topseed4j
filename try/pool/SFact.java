package pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.da.AbsFactPool;

public class SFact extends AbsFactPool {

	@Override
	public GenericObjectPoolConfig returnPoolConfig() {
		GenericObjectPoolConfig c = new GenericObjectPoolConfig();
		c.setMaxTotal(2);
		c.setBlockWhenExhausted(true);
		c.setMaxWaitMillis(1000);
		return c;
	}

	@Override
	public PooledObject<String> makeObject() throws Exception {
		return new DefaultPooledObject<>("a String");
	}

	@Override
	public boolean validateObject(PooledObject po) {
		// TODO Auto-generated method stub
		return true;
	}

}
