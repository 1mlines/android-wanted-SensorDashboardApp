package com.preonboarding.sensordashboard.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.preonboarding.sensordashboard.data.local.dao.SensorHistoryDao
import com.preonboarding.sensordashboard.data.local.entity.SensorHistoryEntity

class HistoryPagingSource(
    private val dao: SensorHistoryDao
) : PagingSource<Int, SensorHistoryEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SensorHistoryEntity> {
        val page = params.key ?: 1
        return try {
            val items = dao.getSensorDataList(
                page,
                params.loadSize
            )
            LoadResult.Page(
                data = items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (items.isEmpty()) null else page + (params.loadSize / 5)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SensorHistoryEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}