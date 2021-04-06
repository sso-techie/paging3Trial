package com.ssotory.paging3trial.gallery

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import com.ssotory.paging3trial.gallery.data.ImageData

class GalleryImageLoader(private val contentResolver: ContentResolver) {
    fun getImages(offset: Int, limit: Int): List<ImageData> {
        val queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        return contentResolver.query(
            queryUri,
            arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED
            ), null, null, "${MediaStore.Images.Media.DATE_ADDED} DESC LIMIT $limit OFFSET $offset"
        )?.use {
            val list = arrayListOf<ImageData>()
            do {
                var columnIndex = it.getColumnIndex(MediaStore.Images.Media._ID)
                val uri = if (columnIndex != -1) {
                    ContentUris.withAppendedId(queryUri, it.getLong(columnIndex))
                } else null

                if (uri != null) {
                    columnIndex = it.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                    val name = if (columnIndex != -1) {
                        it.getString(columnIndex)
                    } else ""

                    columnIndex = it.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
                    val dataAdded = if (columnIndex != -1) {
                        it.getLong(columnIndex)
                    } else 0L

                    list.add(ImageData(uri, name, dataAdded))
                }
            } while (it.moveToNext())
            list
        } ?: arrayListOf()
    }
}