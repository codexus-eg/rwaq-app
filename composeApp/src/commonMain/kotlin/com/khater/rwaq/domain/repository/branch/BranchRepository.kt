package com.khater.rwaq.domain.repository.branch

import com.khater.rwaq.domain.entities.branch.Branch
import com.khater.rwaq.domain.entities.car.Car
import kotlinx.coroutines.flow.Flow

interface BranchRepository {
    suspend fun getAllBranches() : List<Branch>
    suspend fun insertCar(car: Car)
    fun getAllCars() : Flow<List<Car>>
    suspend fun deleteCarById(id: String)
}