package com.khater.rwaq.data.source.remote.branch

import com.khater.rwaq.data.dto.branch.BranchDto

interface BranchDataSource {
    suspend fun getAllBranches() : List<BranchDto>
}