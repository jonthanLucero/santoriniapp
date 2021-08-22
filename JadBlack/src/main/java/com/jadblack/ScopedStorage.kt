@file:JvmName("ScopedStorage")
@file:JvmMultifileClass

package com.jadblack

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.*
import java.net.URLEncoder

public class ScopedStorage {
    private val MEDIA_LOCATION_PERMISSION_REQUEST_CODE = 3
    //Check if Permission granted for Accessing Media Location
    public  fun isPermissionGrantedForMediaLocationAccess(context: Context): Boolean {
        val result: Int =
                ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.ACCESS_MEDIA_LOCATION
                )
        return result == PackageManager.PERMISSION_GRANTED
    }

    //Request Permission if not given
    @RequiresApi(Build.VERSION_CODES.Q)
    fun requestPermissionForAccessMediaLocation(context: Context) {
        ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(android.Manifest.permission.ACCESS_MEDIA_LOCATION),
                MEDIA_LOCATION_PERMISSION_REQUEST_CODE
        )

    }


    public  fun handleImageRequest(data: Intent?, activity: Activity) : File? {
        var imageUri: Uri? = null
        var file: File? = null
        if (data?.data != null) {     //Photo from gallery
            imageUri = data.data!!
            file = getDriveFilePath(activity,imageUri)

        }
        return file;
    }
    fun getDriveFilePath(context: Context, uri: Uri): File

    {
        val returnUri = uri
        val returnCursor: Cursor? = context.contentResolver.query(returnUri, null, null, null, null)     /*

       * Get the column indexes of the data in the Cursor,

       *     * move to the first row in the Cursor, get the data,

       *     * and display it.

       * */
        val nameIndex: Int = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex: Int = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name: String = (returnCursor.getString(nameIndex))
        val size = java.lang.Long.toString(returnCursor.getLong(sizeIndex))
        val file = File(context.cacheDir, URLEncoder.encode(name,"utf-8"))
        try

            {
                val inputStream:InputStream? = context.contentResolver.openInputStream(uri)
                val outputStream = FileOutputStream(file)
                val read: Int = 0
                val maxBufferSize: Int = 1 * 1024 * 1024
                val bytesAvailable: Int = inputStream!!.available()       //int bufferSize = 1024;
                val bufferSize: Int = Math.min(bytesAvailable, maxBufferSize)
                val buffers = ByteArray(bufferSize)
                inputStream.use{
                    inputStream: InputStream ->
                    outputStream.use{ fileOut ->
                        while (true)
                        {
                            val length = inputStream.read(buffers)
                            if (length <= 0)
                            break
                            fileOut.write(buffers, 0, length)
                        }

                        fileOut.flush()
                        fileOut.close()

                    }

                }
                inputStream.close()
            } catch (e: Exception)

            {

                Log.e("Exception", e.message.toString())

            }

            return file

        }

    //Function for Image check on
    public fun fetchMediaLocation(activity: Activity, selectPictureRequestCode: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {

            openChooser(activity,selectPictureRequestCode)

        } else {

                openChooser(activity,selectPictureRequestCode)

        }
    }
    fun openChooser(activity: Activity,selectPictureRequestCode: Int) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
       intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        intent.action = Intent.ACTION_PICK
        activity.startActivityForResult(Intent.createChooser(intent, "Seleccionar Imagen"), selectPictureRequestCode)
    }

}