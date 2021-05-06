package bg.dabulgaria.tibroish.persistence.local

import bg.dabulgaria.tibroish.domain.providers.ILogger
import java.io.*
import javax.inject.Inject
import kotlin.jvm.Throws

interface IStreamCopier{

    @Throws(IOException::class)
    fun copy(inputStream: InputStream, output: OutputStream):Int
}

class StreamCopier @Inject constructor(private val logger:ILogger) :IStreamCopier {

    /**
     * @param input  InputStream object to read data from.
     * @param output OutputStream object to write data to.
     * @return number if bytes copied.
     */
    @Throws(IOException::class)
    override fun copy(inputStream: InputStream, output: OutputStream):Int  {

        val buffer = ByteArray( BUFFER_SIZE )
        val input = BufferedInputStream( inputStream, BUFFER_SIZE );
        val out = BufferedOutputStream( output, BUFFER_SIZE );

        var count = 0
        var n = 0

        try {
            while ( n != -1 ) {

                n = input.read( buffer, 0, BUFFER_SIZE )
                if( n == -1)
                    break

                out.write( buffer, 0, n )
                out.flush()
                count += n
            }
        } finally {

            try {
                input.close()
                out.close()
                inputStream.close()
                output.close()

            } catch ( ex:Exception) {

                logger.e( LOG_TAG, ex )
            }
        }

        return count
    }

    companion object{
        //region properties
        val  LOG_TAG = StreamCopier::class.java.simpleName

        val BUFFER_SIZE = 1024 * 100;
        //endregion

    }
}