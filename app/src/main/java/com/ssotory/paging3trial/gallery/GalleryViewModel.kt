package com.ssotory.paging3trial.gallery

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ssotory.paging3trial.gallery.paging.GalleryPagingSource

class GalleryViewModel(contentResolver: ContentResolver) : ViewModel() {
    val flow = Pager(PagingConfig(50)) {
        GalleryPagingSource(GalleryImageLoader(contentResolver))
    }.flow.cachedIn(viewModelScope)

    class Factory(private val contentResolver: ContentResolver) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return GalleryViewModel(contentResolver) as T
        }
    }
}