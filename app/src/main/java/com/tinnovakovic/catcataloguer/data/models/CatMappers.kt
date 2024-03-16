package com.tinnovakovic.catcataloguer.data.models

import com.tinnovakovic.catcataloguer.data.models.api.CatBreedDto
import com.tinnovakovic.catcataloguer.data.models.api.CatImageDto
import com.tinnovakovic.catcataloguer.data.models.db.CatEntity
import com.tinnovakovic.catcataloguer.data.models.db.CatImageEntity
import com.tinnovakovic.catcataloguer.data.models.local.Cat
import com.tinnovakovic.catcataloguer.data.models.local.CatImage

fun CatBreedDto.toCatEntity(): CatEntity {
    return CatEntity(
        id = id,
        name = name,
        temperament = temperament,
        origin = origin,
        countryCode = countryCode,
        description = description,
        lifeSpan = lifeSpan,
        indoor = indoor,
        adaptability = adaptability,
        affectionLevel = affectionLevel,
        childFriendly = childFriendly,
        dogFriendly = dogFriendly,
        energyLevel = energyLevel,
        grooming = grooming,
        healthIssues = healthIssues,
        intelligence = intelligence,
        sheddingLevel = sheddingLevel,
        socialNeeds = socialNeeds,
        strangerFriendly = strangerFriendly,
        vocalisation = vocalisation,
        experimental = experimental,
        hairless = hairless,
        natural = natural,
        rare = rare,
        rex = rex,
        suppressedTail = suppressedTail,
        shortLegs = shortLegs,
        hypoallergenic = hypoallergenic,
    )
}

fun CatEntity.toCat(): Cat {
    return Cat(
        id = id,
        name = name,
        origin = origin,
        countryEmoji = getFlagEmoji(countryCode)
    )
}

fun CatImageDto.toCatImageEntity(catId: String): CatImageEntity {
    return CatImageEntity(
        imageId = id,
        url = url,
        catId = catId
    )
}

fun CatImageEntity.toCatImage(): CatImage {
    return CatImage(
        id = imageId,
        url = url
    )
}

fun getFlagEmoji(countryCode: String): String {
    // Validate the country code (2 uppercase letters)
    if (countryCode.length != 2 || !countryCode.all { it.isUpperCase() && it in 'A'..'Z' }) {
        return countryCode
    }

    // Convert the country code directly to a flag emoji
    return countryCode
        .map { char ->
            // Calculate the Unicode point for each regional indicator symbol directly
            val offset = char - 'A' + 0x1F1E6
            String(Character.toChars(offset))
        }
        .joinToString(separator = "")
}
