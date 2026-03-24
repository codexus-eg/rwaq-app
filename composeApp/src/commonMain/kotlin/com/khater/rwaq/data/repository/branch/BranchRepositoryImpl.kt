package com.khater.rwaq.data.repository.branch

import com.khater.rwaq.data.source.local.database.car.CarDao
import com.khater.rwaq.data.source.local.database.car.toCarLocalDto
import com.khater.rwaq.data.source.local.database.car.toDomain
import com.khater.rwaq.data.source.remote.branch.BranchDataSource
import com.khater.rwaq.domain.entities.branch.Branch
import com.khater.rwaq.domain.entities.car.Car
import com.khater.rwaq.domain.repository.branch.BranchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BranchRepositoryImpl(
    private val dataSource: BranchDataSource,
    private val carDao: CarDao
): BranchRepository {
    override suspend fun getAllBranches(): List<Branch> {
        return dataSource.getAllBranches().map { it.toDomain() }
    }

    override suspend fun insertCar(car: Car) {
        carDao.insertCar(car.toCarLocalDto())
    }

    override fun getAllCars(): Flow<List<Car>> {
       return carDao.getAllCars().map {
           it.toDomain()
       }
    }
    override suspend fun deleteCarById(id: String) {
        carDao.deleteCarById(id)
    }
}