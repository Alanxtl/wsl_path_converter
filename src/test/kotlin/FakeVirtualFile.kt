import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileSystem
import java.io.InputStream
import java.io.OutputStream

class FakeVirtualFile(
    private val myPath: String,
    private val myFs: VirtualFileSystem
) : VirtualFile() {
    override fun getPath(): String = myPath
    override fun getFileSystem(): VirtualFileSystem = myFs
    override fun getName(): String = myPath.substringAfterLast('\\').substringAfterLast('/')
    override fun getParent(): VirtualFile? = null
    override fun getChildren(): Array<VirtualFile> = emptyArray()
    override fun isWritable(): Boolean = false
    override fun isDirectory(): Boolean = false
    override fun isValid(): Boolean = true
    override fun getLength(): Long = 0
    override fun getTimeStamp(): Long = 0
    override fun getModificationStamp(): Long = 0
    override fun contentsToByteArray(): ByteArray = ByteArray(0)
    override fun getInputStream(): InputStream = throw UnsupportedOperationException()
    override fun getOutputStream(requestor: Any?, newModificationStamp: Long, newTimeStamp: Long): OutputStream =
        throw UnsupportedOperationException()
    override fun refresh(asynchronous: Boolean, recursive: Boolean, postRunnable: Runnable?) {}
}
