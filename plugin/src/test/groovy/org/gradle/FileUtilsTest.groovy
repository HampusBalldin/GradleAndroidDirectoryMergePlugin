import org.junit.Test
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import org.hampus.android.packaging.FileUtils 

class FileUtilsTest {
    @Test public void testCanConstruct() {
	FileUtils fu = new FileUtils()
	assertNotNull "fu must construct to not null", fu
    }

    @Test public void testCorrectInputPackagingConf() {
	FileUtils fu = new FileUtils()
	def list = ['layout','res','test1','test2','test3']
	int i = 0;
	fu.getLines().each{
		assertEquals(it, list[i])
		i++
	}
    }
}

