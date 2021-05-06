package bg.dabulgaria.tibroish.utils

import android.content.res.AssetManager
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

class AssetReader {
    fun loadJsonFromAsset(jsonPath: String, assets: AssetManager): String? {
        return try {
            val inputStream: InputStream = assets.open(jsonPath)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
    }
}