package com.retroent.moviebuff.features.popularmovies

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.retroent.moviebuff.data.PopularMoviesDataRepository
import kotlinx.coroutines.flow.single

class PopularMoviePagingSource(
    private val popularRepo : PopularMoviesDataRepository
) : PagingSource<Int, MovieResult>() {

    override fun getRefreshKey(state: PagingState<Int, MovieResult>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResult> {
       return try{
            val page = params.key ?: 1
            val result = popularRepo.fetchPopularMovies(page).single()

           LoadResult.Page(
               data = result,
               prevKey = null,
               nextKey = if(result.isEmpty()) null else page.plus(1)
           )
        }catch (e:Exception){
            LoadResult.Error(Exception("Something Went wrong"))
        }
    }
}