package com.khater.rwaq.data.source.remote.branch

import com.khater.rwaq.data.dto.base.BaseResponse
import com.khater.rwaq.data.dto.branch.BranchDto
import com.khater.rwaq.data.dto.branch.BranchWorkTimeDto
import com.khater.rwaq.data.dto.branch.LocationDto
import com.khater.rwaq.data.util.getJson
import com.khater.rwaq.data.util.safeWrapper
import io.ktor.client.HttpClient

class BranchRemoteDataSource(
    private val httpClient: HttpClient
): BranchDataSource {


    override suspend fun getAllBranches(): List<BranchDto> {
        val response = safeWrapper {
            httpClient.getJson<BaseResponse<List<BranchDto>>>(
                path = BRANCHES_ENDPOINT,
            )
        }
        return response.data
    }

    companion object{
       const val BRANCHES_ENDPOINT = "api/branches"
    }
}