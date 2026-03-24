package com.khater.rwaq.domain.useCases

import com.khater.rwaq.domain.entities.car.Car
import com.khater.rwaq.domain.repository.branch.BranchRepository
import kotlinx.coroutines.flow.Flow

class GetAllBranchesUseCase(
    private val branchRepository: BranchRepository
) {
    suspend operator fun invoke() = branchRepository.getAllBranches()

    suspend fun insertCar(car: Car) = branchRepository.insertCar(car)
    suspend fun deleteCarById(id: String) = branchRepository.deleteCarById(id)
     fun getAllCars(): Flow<List<Car>> = branchRepository.getAllCars()
}