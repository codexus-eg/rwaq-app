package com.khater.rwaq.data.source.local.database.car

import com.khater.rwaq.domain.entities.car.Car

fun Car.toCarLocalDto(): CarLocalDto{
    return CarLocalDto(
        id = id,
        carName = name,
        carImage = imageUrl,
        carColor = color,
        carNumber = carNumber,
        carColorName = colorName
    )
}

fun CarLocalDto.toDomain(): Car{
    return Car(
        id = id,
        name = carName,
        imageUrl = carImage,
        color = carColor,
        carNumber = carNumber,
        colorName = carColorName
    )
}

fun List<CarLocalDto>.toDomain(): List<Car> =
    map(CarLocalDto::toDomain)