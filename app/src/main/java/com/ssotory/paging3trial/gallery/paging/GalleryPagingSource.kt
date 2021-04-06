package com.ssotory.paging3trial.gallery.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ssotory.paging3trial.gallery.GalleryImageLoader
import com.ssotory.paging3trial.gallery.data.ImageData

class GalleryPagingSource(private val galleryImageLoader: GalleryImageLoader): PagingSource<Int, ImageData>() {
    companion object {
        private const val FETCH_SIZE = 100
    }

    override fun getRefreshKey(state: PagingState<Int, ImageData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageData> {
        val nextPageNumber = params.key ?: 0
        val list = galleryImageLoader.getImages(nextPageNumber, FETCH_SIZE)
        val prevKey = nextPageNumber - FETCH_SIZE
        return LoadResult.Page(
            list, if (prevKey < 0) null else prevKey, nextPageNumber + FETCH_SIZE
        )
    }
}