import java.io.File;

import org.info.util.Confd;
import org.info.util.FileU;

public class Tst {

	static Confd P = Confd.INSTANCE;

	public static void main(String[] args) throws Throwable {
		System.out.println(P.getConf());

		if (true)
			return;
		File f = FileU.getNewestFile("./kkkk");
		System.out.println(f);

	}

}
