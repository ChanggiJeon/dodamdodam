package com.ssafy.family.util

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.DatabaseUtils
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import okhttp3.ResponseBody
import java.io.*
import java.text.DecimalFormat
import java.util.*
import kotlin.Comparator
import kotlin.math.roundToInt


class FileUtils {

    companion object {

        const val DOCUMENTS_DIR = "documents"



        // configured android:authorities in AndroidManifest (https://developer.android.com/reference/android/support/v4/content/FileProvider)

        const val AUTHORITY = "YOUR_AUTHORITY.provider"

        const val HIDDEN_PREFIX = "."



        /**

         * TAG for log messages.

         */

        const val TAG = "FileUtils"

        private const val DEBUG = false // Set to true to enable logging





        /**

         * File and folder comparator.

         */

        var sComparator =

            Comparator { f1: File, f2: File ->

                f1.name.lowercase(Locale.getDefault()).compareTo(

                    f2.name.lowercase(Locale.getDefault())

                )

            }



        /**

         * File (not directories) filter.

         */

        var sFileFilter = FileFilter { file: File ->

            val fileName = file.name

            file.isFile && !fileName.startsWith(HIDDEN_PREFIX)

        }



        /**

         * Folder (directories) filter.

         */

        var sDirFilter = FileFilter { file: File ->

            val fileName = file.name

            file.isDirectory && !fileName.startsWith(HIDDEN_PREFIX)

        }



        /**

         * Gets the extension of a file name, like ".png" or ".jpg".

         *

         * @param uri

         * @return Extension including the dot("."); "" if there is no extension;

         * null if uri was null.

         */

        fun getExtension(uri: String?): String? {

            if (uri == null) {

                return null

            }

            val dot = uri.lastIndexOf(".")

            return if (dot >= 0) {

                uri.substring(dot)

            } else {

                // No extension.

                ""

            }

        }



        /**

         * @return Whether the URI is a local one.

         */

        fun isLocal(url: String?): Boolean {

            return url != null && !url.startsWith("http://") && !url.startsWith("https://")

        }



        /**

         * @return True if Uri is a MediaStore Uri.

         * @author paulburke

         */

        fun isMediaUri(uri: Uri): Boolean {

            return "media".equals(uri.authority, ignoreCase = true)

        }



        /**

         * Convert File into Uri.

         *

         * @param file

         * @return uri

         */

        fun getUri(file: File?): Uri? {

            return if (file != null) Uri.fromFile(file) else null

        }



        /**

         * Returns the path only (without file name).

         *

         * @param file

         * @return

         */

        fun getPathWithoutFilename(file: File?): File? {

            return if (file != null) {

                if (file.isDirectory) {

                    // no file to be split off. Return everything

                    file

                } else {

                    val filename = file.name

                    val filepath = file.absolutePath



                    // Construct path without file name.

                    var pathwithoutname = filepath.substring(

                        0,

                        filepath.length - filename.length

                    )

                    if (pathwithoutname.endsWith("/")) {

                        pathwithoutname = pathwithoutname.substring(0, pathwithoutname.length - 1)

                    }

                    File(pathwithoutname)

                }

            } else null

        }



        /**

         * @return The MIME type for the given file.

         */

        fun getMimeType(file: File): String? {

            val extension = getExtension(file.name)

            return if (extension!!.length > 0) MimeTypeMap.getSingleton()

                .getMimeTypeFromExtension(extension.substring(1)) else "application/octet-stream"

        }



        /**

         * @return The MIME type for the give Uri.

         */

        fun getMimeType(context: Context, uri: Uri): String? {

            val file = File(getPath(context, uri))

            return getMimeType(file)

        }



        /**

         * @return The MIME type for the give String Uri.

         */

        fun getMimeType(context: Context, url: String?): String {

            var type = context.contentResolver.getType(Uri.parse(url))

            if (type == null) {

                type = "application/octet-stream"

            }

            return type

        }



        /**

         * @param uri The Uri to check.

         * @return Whether the Uri authority is local.

         */

        fun isLocalStorageDocument(uri: Uri): Boolean {

            return AUTHORITY == uri.authority

        }



        /**

         * @param uri The Uri to check.

         * @return Whether the Uri authority is ExternalStorageProvider.

         */

        fun isExternalStorageDocument(uri: Uri): Boolean {

            return "com.android.externalstorage.documents" == uri.authority

        }



        /**

         * @param uri The Uri to check.

         * @return Whether the Uri authority is DownloadsProvider.

         */

        fun isDownloadsDocument(uri: Uri): Boolean {

            return "com.android.providers.downloads.documents" == uri.authority

        }



        /**

         * @param uri The Uri to check.

         * @return Whether the Uri authority is MediaProvider.

         */

        fun isMediaDocument(uri: Uri): Boolean {

            return "com.android.providers.media.documents" == uri.authority

        }



        /**

         * @param uri The Uri to check.

         * @return Whether the Uri authority is Google Photos.

         */

        fun isGooglePhotosUri(uri: Uri): Boolean {

            return "com.google.android.apps.photos.content" == uri.authority

        }



        fun isGoogleDriveUri(uri: Uri): Boolean {

            return "com.google.android.apps.docs.storage.legacy" == uri.authority || "com.google.android.apps.docs.storage" == uri.authority

        }



        /**

         * Get the value of the data column for this Uri. This is useful for

         * MediaStore Uris, and other file-based ContentProviders.

         *

         * @param context       The context.

         * @param uri           The Uri to query.

         * @param selection     (Optional) Filter used in the query.

         * @param selectionArgs (Optional) Selection arguments used in the query.

         * @return The value of the _data column, which is typically a file path.

         */

        fun getDataColumn(

            context: Context, uri: Uri?, selection: String?,

            selectionArgs: Array<String>?

        ): String? {

            Log.d("FileUtils", uri.toString())

            var cursor: Cursor? = null

            val column = MediaStore.Files.FileColumns.DATA

            val projection = arrayOf(column)

            try {

                cursor = context.contentResolver.query(

                    uri!!, projection, selection, selectionArgs,

                    null

                )

                if (cursor != null && cursor.moveToFirst()) {

                    if (DEBUG) DatabaseUtils.dumpCursor(cursor)

                    val column_index = cursor.getColumnIndexOrThrow(column)

                    return cursor.getString(column_index)

                }

            } catch (e: Exception) {

                Log.e("FileUtils", "error", e)

            } finally {

                cursor?.close()

            }

            return null

        }



        /**

         * Get a file path from a Uri. This will get the the path for Storage Access

         * Framework Documents, as well as the _data field for the MediaStore and

         * other file-based ContentProviders.<br></br>

         * <br></br>

         * Callers should check whether the path is local before assuming it

         * represents a local file.

         *

         * @param context The context.

         * @param uri     The Uri to query.

         * @see .isLocal

         * @see .getFile

         */

        fun getPath(context: Context, uri: Uri): String {

            val absolutePath = getLocalPath(context, uri)

            return absolutePath ?: uri.toString()

        }



        private fun getLocalPath(

            context: Context,

            uri: Uri

        ): String? {

            if (DEBUG) Log.d(

                "$TAG File -",

                "Authority: " + uri.authority +

                        ", Fragment: " + uri.fragment +

                        ", Port: " + uri.port +

                        ", Query: " + uri.query +

                        ", Scheme: " + uri.scheme +

                        ", Host: " + uri.host +

                        ", Segments: " + uri.pathSegments.toString()

            )



            // DocumentProvider

            if (DocumentsContract.isDocumentUri(context, uri)) {

                // LocalStorageProvider

                if (isLocalStorageDocument(uri)) {

                    // The path is the id

                    return DocumentsContract.getDocumentId(uri)

                } else if (isExternalStorageDocument(uri)) {

                    val docId = DocumentsContract.getDocumentId(uri)

                    val split = docId.split(":".toRegex()).toTypedArray()

                    val type = split[0]

                    if ("primary".equals(type, ignoreCase = true)) {

                        return Environment.getExternalStorageDirectory()

                            .toString() + "/" + split[1]

                    } else if ("home".equals(type, ignoreCase = true)) {

                        return Environment.getExternalStorageDirectory()

                            .toString() + "/documents/" + split[1]

                    }

                } else if (isDownloadsDocument(uri)) {

                    val id = DocumentsContract.getDocumentId(uri)

                    if (id != null && id.startsWith("raw:")) {

                        return id.substring(4)

                    }

                    if (id != null && id.startsWith("msf:")) {

                        Log.d("tag", "is msf file.")

                        return id.substring(4)

                    }

                    val contentUriPrefixesToTry = arrayOf(

                        "content://downloads/public_downloads",

                        "content://downloads/my_downloads"

                    )

                    for (contentUriPrefix in contentUriPrefixesToTry) {

                        val contentUri = ContentUris.withAppendedId(

                            Uri.parse(contentUriPrefix),

                            java.lang.Long.valueOf(id!!)

                        )

                        try {

                            val path = getDataColumn(context, contentUri, null, null)

                            if (path != null) {

                                return path

                            }

                        } catch (e: Exception) {

                        }

                    }



                    // path could not be retrieved using ContentResolver, therefore copy file to accessible cache using streams

                    val fileName = getFileName(context, uri)

                    val cacheDir = getDocumentCacheDir(context)

                    val file = generateFileName(fileName, cacheDir)

                    var destinationPath: String? = null

                    if (file != null) {

                        destinationPath = file.absolutePath

                        saveFileFromUri(context, uri, destinationPath)

                    }

                    return destinationPath

                } else if (isMediaDocument(uri)) {

                    val docId = DocumentsContract.getDocumentId(uri)

                    val split = docId.split(":".toRegex()).toTypedArray()

                    val type = split[0]

                    var contentUri: Uri? = null

                    when (type) {

                        "image" -> {

                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

                        }

                        "video" -> {

                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

                        }

                        "audio" -> {

                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

                        }

                    }

                    val selection = "_id=?"

                    val selectionArgs = arrayOf(

                        split[1]

                    )

                    return getDataColumn(context, contentUri, selection, selectionArgs)

                } else if (isGoogleDriveUri(uri)) {

                    return getGoogleDriveFilePath(uri, context)

                }

            } else if ("content".equals(uri.scheme, ignoreCase = true)) {



                // Return the remote address

                if (isGooglePhotosUri(uri)) {

                    return uri.lastPathSegment

                } else if (isGoogleDriveUri(uri)) {

                    return getGoogleDriveFilePath(uri, context)

                }

                return getDataColumn(context, uri, null, null)

            } else if ("file".equals(uri.scheme, ignoreCase = true)) {

                return uri.path

            }

            return null

        }



        /**

         * Convert Uri into File, if possible.

         *

         * @return file A local file that the Uri was pointing to, or null if the

         * Uri is unsupported or pointed to a remote resource.

         * @author paulburke

         * @see .getPath

         */

        fun getFile(context: Context, uri: Uri): File {

            return if (checkFileType(context, uri) == "image") getResizedImageFile(context, uri)

            else File(getPath(context, uri))

        }



        fun checkFileType(context: Context, uri: Uri?): String {

            val extension = MimeTypeMap.getFileExtensionFromUrl(uri?.path)

            val contentType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: uri?.let {

                context.contentResolver.getType(it)

            }



            return contentType?.split("/")?.first() ?: ""

        }



        /**

         * Get the file size in a human-readable string.

         *

         * @param size

         * @return

         * @author paulburke

         */

        fun getReadableFileSize(size: Int): String {

            val BYTES_IN_KILOBYTES = 1024

            val dec = DecimalFormat("###.#")

            val KILOBYTES = " KB"

            val MEGABYTES = " MB"

            val GIGABYTES = " GB"

            var fileSize = 0f

            var suffix = KILOBYTES

            if (size > BYTES_IN_KILOBYTES) {

                fileSize = size / BYTES_IN_KILOBYTES.toFloat()

                if (fileSize > BYTES_IN_KILOBYTES) {

                    fileSize = fileSize / BYTES_IN_KILOBYTES

                    if (fileSize > BYTES_IN_KILOBYTES) {

                        fileSize = fileSize / BYTES_IN_KILOBYTES

                        suffix = GIGABYTES

                    } else {

                        suffix = MEGABYTES

                    }

                }

            }

            return dec.format(fileSize.toDouble()) + suffix

        }



        /**

         * Get the Intent for selecting content to be used in an Intent Chooser.

         *

         * @return The intent for opening a file with Intent.createChooser()

         */

        fun createGetContentIntent(): Intent {

            // Implicitly allow the user to select a particular kind of data

            val intent = Intent(Intent.ACTION_GET_CONTENT)

            // The MIME data type filter

            intent.type = "*/*"

            // Only return URIs that can be opened with ContentResolver

            intent.addCategory(Intent.CATEGORY_OPENABLE)

            return intent

        }



        /**

         * Creates View intent for given file

         *

         * @param file

         * @return The intent for viewing file

         */

        fun getViewIntent(context: Context?, file: File): Intent {

            //Uri uri = Uri.fromFile(file);

            val uri = FileProvider.getUriForFile(context!!, AUTHORITY, file)

            val intent = Intent(Intent.ACTION_VIEW)

            val url = file.toString()

            if (url.contains(".doc") || url.contains(".docx")) {

                // Word document

                intent.setDataAndType(uri, "application/msword")

            } else if (url.contains(".pdf")) {

                // PDF file

                intent.setDataAndType(uri, "application/pdf")

            } else if (url.contains(".ppt") || url.contains(".pptx")) {

                // Powerpoint file

                intent.setDataAndType(uri, "application/vnd.ms-powerpoint")

            } else if (url.contains(".xls") || url.contains(".xlsx")) {

                // Excel file

                intent.setDataAndType(uri, "application/vnd.ms-excel")

            } else if (url.contains(".zip") || url.contains(".rar")) {

                // WAV audio file

                intent.setDataAndType(uri, "application/x-wav")

            } else if (url.contains(".rtf")) {

                // RTF file

                intent.setDataAndType(uri, "application/rtf")

            } else if (url.contains(".wav") || url.contains(".mp3")) {

                // WAV audio file

                intent.setDataAndType(uri, "audio/x-wav")

            } else if (url.contains(".gif")) {

                // GIF file

                intent.setDataAndType(uri, "image/gif")

            } else if (url.contains(".jpg") || url.contains(".jpeg") || url.contains(".png")) {

                // JPG file

                intent.setDataAndType(uri, "image/jpeg")

            } else if (url.contains(".txt")) {

                // Text file

                intent.setDataAndType(uri, "text/plain")

            } else if (url.contains(".3gp") || url.contains(".mpg") || url.contains(".mpeg") ||

                url.contains(".mpe") || url.contains(".mp4") || url.contains(".avi")

            ) {

                // Video files

                intent.setDataAndType(uri, "video/*")

            } else {

                intent.setDataAndType(uri, "*/*")

            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

            return intent

        }



        val downloadsDir: File

            get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)



        fun getDocumentCacheDir(context: Context): File {

            val dir = File(context.cacheDir, DOCUMENTS_DIR)

            if (!dir.exists()) {

                dir.mkdirs()

            }

            logDir(context.cacheDir)

            logDir(dir)

            return dir

        }



        private fun logDir(dir: File) {

            if (!DEBUG) return

            Log.d(TAG, "Dir=$dir")

            val files = dir.listFiles()

            for (file in files) {

                Log.d(TAG, "File=" + file.path)

            }

        }



        fun generateFileName(name: String?, directory: File): File? {

            var name = name ?: return null

            var file = File(directory, name)

            if (file.exists()) {

                var fileName = name

                var extension = ""

                val dotIndex = name.lastIndexOf('.')

                if (dotIndex > 0) {

                    fileName = name.substring(0, dotIndex)

                    extension = name.substring(dotIndex)

                }

                var index = 0

                while (file.exists()) {

                    index++

                    name = "$fileName($index)$extension"

                    file = File(directory, name)

                }

            }

            try {

                if (!file.createNewFile()) {

                    return null

                }

            } catch (e: IOException) {

                Log.w(TAG, e)

                return null

            }

            logDir(directory)

            return file

        }



        /**

         * Writes response body to disk

         *

         * @param body ResponseBody

         * @param path file path

         * @return File

         */

        fun writeResponseBodyToDisk(body: ResponseBody, path: String): File? {

            return try {

                val target = File(path)

                var inputStream: InputStream? = null

                var outputStream: OutputStream? = null

                try {

                    val fileReader = ByteArray(4096)

                    inputStream = body.byteStream()

                    outputStream = FileOutputStream(target)

                    while (true) {

                        val read = inputStream.read(fileReader)

                        if (read == -1) {

                            break

                        }

                        outputStream.write(fileReader, 0, read)

                    }

                    outputStream.flush()

                    target

                } catch (e: IOException) {

                    null

                } finally {

                    inputStream?.close()

                    outputStream?.close()

                }

            } catch (e: IOException) {

                null

            }

        }



        private fun saveFileFromUri(

            context: Context,

            uri: Uri,

            destinationPath: String?

        ) {

            var `is`: InputStream? = null

            var bos: BufferedOutputStream? = null

            try {

                `is` = context.contentResolver.openInputStream(uri)

                bos = BufferedOutputStream(FileOutputStream(destinationPath, false))

                val buf = ByteArray(1024)

                `is`!!.read(buf)

                do {

                    bos.write(buf)

                } while (`is`.read(buf) != -1)

            } catch (e: IOException) {

                e.printStackTrace()

            } finally {

                try {

                    `is`?.close()

                    bos?.close()

                } catch (e: IOException) {

                    e.printStackTrace()

                }

            }

        }



        fun readBytesFromFile(filePath: String): ByteArray? {

            var fileInputStream: FileInputStream? = null

            var bytesArray: ByteArray? = null

            try {

                val file = File(filePath)

                bytesArray = ByteArray(file.length().toInt())



                //read file into bytes[]

                fileInputStream = FileInputStream(file)

                fileInputStream.read(bytesArray)

            } catch (e: IOException) {

                e.printStackTrace()

            } finally {

                if (fileInputStream != null) {

                    try {

                        fileInputStream.close()

                    } catch (e: IOException) {

                        e.printStackTrace()

                    }

                }

            }

            return bytesArray

        }



        @Throws(IOException::class)

        fun createTempImageFile(

            context: Context,

            fileName: String

        ): File {

            // Create an image file name

            val storageDir = File(context.cacheDir, DOCUMENTS_DIR)

            return File.createTempFile(fileName, ".jpg", storageDir)

        }



        fun getFileName(context: Context, uri: Uri): String? {

            val mimeType = context.contentResolver.getType(uri)

            var filename: String? = null

            if (mimeType == null && context != null) {

                val path = getPath(context, uri)

                filename = if (path == null) {

                    getName(uri.toString())

                } else {

                    val file = File(path)

                    file.name

                }

            } else {

                val returnCursor = context.contentResolver.query(

                    uri, null,

                    null, null, null

                )

                if (returnCursor != null) {

                    val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)

                    returnCursor.moveToFirst()

                    filename = returnCursor.getString(nameIndex)

                    returnCursor.close()

                }

            }

            return filename

        }



        fun getName(filename: String?): String? {

            if (filename == null) {

                return null

            }

            val index = filename.lastIndexOf('/')

            return filename.substring(index + 1)

        }



        private fun getGoogleDriveFilePath(

            uri: Uri,

            context: Context

        ): String {

            val returnCursor =

                context.contentResolver.query(uri, null, null, null, null)

            /*

         * Get the column indexes of the data in the Cursor,

         *     * move to the first row in the Cursor, get the data,

         *     * and display it.

         * */

            val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)

            val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)

            returnCursor.moveToFirst()

            val name = returnCursor.getString(nameIndex)

            val size = returnCursor.getLong(sizeIndex).toString()

            val file = File(context.cacheDir, name)

            try {

                val inputStream =

                    context.contentResolver.openInputStream(uri)

                val outputStream = FileOutputStream(file)

                var read = 0

                val maxBufferSize = 1 * 1024 * 1024

                val bytesAvailable = inputStream!!.available()

                val bufferSize = Math.min(bytesAvailable, maxBufferSize)

                val buffers = ByteArray(bufferSize)

                while (inputStream.read(buffers).also { read = it } != -1) {

                    outputStream.write(buffers, 0, read)

                }

                inputStream.close()

                outputStream.close()

            } catch (e: Exception) {

                e.printStackTrace()

            }

            return file.path

        }



        private fun getResizedImageFile(context: Context, data: Uri): File {

            val openInputStream: InputStream = context.contentResolver.openInputStream(data)!!

            val bitmap: Bitmap = BitmapFactory.decodeStream(openInputStream)

            val resizedBitmap: Bitmap? = scaleDown(bitmap, 1024f, true)



            openInputStream.close()



            Log.d("tag", "${context.cacheDir}/tmp-herbee.jpg")

            val resizedFile = File("${context.cacheDir}/tmp-herbee.jpg")



            resizedFile.createNewFile()

            val out: OutputStream = FileOutputStream(resizedFile)

            resizedBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, out)



            return resizedFile

        }



        private fun scaleDown(

            realImage: Bitmap,

            maxImageSize: Float,

            filter: Boolean

        ): Bitmap? {

            val ratio = (maxImageSize / realImage.width).coerceAtMost(maxImageSize / realImage.height)

            val width = (ratio * realImage.width).roundToInt()

            val height = (ratio * realImage.height).roundToInt()

            return Bitmap.createScaledBitmap(

                realImage, width,

                height, filter

            )

        }

    }

}
