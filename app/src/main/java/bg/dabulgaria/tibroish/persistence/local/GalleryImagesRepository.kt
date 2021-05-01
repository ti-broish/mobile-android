package bg.dabulgaria.tibroish.persistence.local

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import bg.dabulgaria.tibroish.domain.image.IGalleryImagesRepository
import bg.dabulgaria.tibroish.domain.image.PickedImage
import bg.dabulgaria.tibroish.domain.image.PickedImageSource
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.infrastructure.di.annotations.AppContext
import java.util.*
import javax.inject.Inject

class GalleryImagesRepository @Inject constructor(@AppContext private val context: Context,
                                                  private val logger:ILogger): IGalleryImagesRepository {

    override fun getImages():List<PickedImage> {

        val list = mutableListOf<PickedImage>()

        val cr = context.contentResolver ?: return list

        val columns = arrayOf(MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.DATA)

        val where = "${MediaStore.Images.Media.MIME_TYPE} = ?"
        val mimeTypeJpg = "image/jpeg"
        val whereArgs = arrayOf(mimeTypeJpg)
        val orderBy = MediaStore.Images.Media.DATE_TAKEN + " DESC"

        val calendar = Calendar.getInstance()

        var cursor: Cursor? = null
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        try {

            cursor = cr.query(uri, columns, where, whereArgs, orderBy) ?:return list

            var dateModified: Long = 0

            while (cursor?.moveToNext() == true ) {

                val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID))

                val imageUriString = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id).toString()

                val imageFilePath = if(imageUriString.isEmpty())
                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                else
                   imageUriString

                if( imageFilePath.isEmpty())
                    continue

                val height = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT))
                val width = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH))

                if( height <= 0 || width <= 0) {

                    logger.i("IMG_NO_SIZE", imageFilePath)
                    continue
                }

                dateModified = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN))

                calendar.timeInMillis = dateModified

                list.add( PickedImage(id.toString(),
                        PickedImageSource.Gallery,
                        imageFilePath,
                        width,
                        height,
                        calendar.time))
            }

        } finally {

            cursor?.close()
        }

        return list
    }
}