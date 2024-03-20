package com.tinnovakovic.catcataloguer.data.models

import com.tinnovakovic.catcataloguer.data.models.api.CatBreedDto
import com.tinnovakovic.catcataloguer.data.models.api.CatBreedImageDto
import com.tinnovakovic.catcataloguer.data.models.db.CatBreedEntity
import com.tinnovakovic.catcataloguer.data.models.db.CatBreedImageEntity
import com.tinnovakovic.catcataloguer.data.models.local.CatBreed
import com.tinnovakovic.catcataloguer.data.models.local.CatDetail
import com.tinnovakovic.catcataloguer.data.models.local.CatBreedImage
import com.tinnovakovic.catcataloguer.data.models.local.Feature
import com.tinnovakovic.catcataloguer.data.models.local.PersonalityScore
import java.util.Locale

fun CatBreedDto.toCatEntity(): CatBreedEntity {
    return CatBreedEntity(
        id = id,
        name = name,
        altNames = altNames ?: "",
        weightMetric = weight?.metric ?: "" ,
        weightImperial = weight?.imperial ?: "",
        temperament = temperament ?: "",
        origin = origin ?: "",
        countryCode = countryCode ?: "",
        description = description ?: "",
        lifeSpan = lifeSpan ?: "",
        adaptability = adaptability ?: 0,
        affectionLevel = affectionLevel ?: 0,
        childFriendly = childFriendly ?: 0,
        dogFriendly = dogFriendly ?: 0,
        energyLevel = energyLevel ?: 0,
        grooming = grooming ?: 0,
        healthIssues = healthIssues ?: 0,
        intelligence = intelligence ?: 0,
        sheddingLevel = sheddingLevel ?: 0,
        socialNeeds = socialNeeds ?: 0,
        strangerFriendly = strangerFriendly ?: 0,
        vocalisation = vocalisation ?: 0,
        experimental = experimental ?: 0,
        hairless = hairless ?: 0,
        natural = natural ?: 0,
        rare = rare ?: 0,
        rex = rex ?: 0,
        suppressedTail = suppressedTail ?: 0,
        shortLegs = shortLegs ?: 0,
        hypoallergenic = hypoallergenic ?: 0,
        indoor = indoor ?: 0,
        lap = lap ?: 0,
    )
}

fun CatBreedEntity.toCatDetail(): CatDetail {
    return CatDetail(
        id = id,
        name = name,
        altNames = altNames,
        weightMetric = weightMetric,
        weightImperial = weightImperial,
        temperament = temperament.split(", ").toList(),
        origin = origin,
        countryCode = countryCode,
        description = description,
        lifeSpan = lifeSpan,
        personalityScores = listOf(
            PersonalityScore(::adaptability.name.capitalise().separatePascalCase(), adaptability),
            PersonalityScore(::affectionLevel.name.capitalise().separatePascalCase(), affectionLevel),
            PersonalityScore(::childFriendly.name.capitalise().separatePascalCase(), childFriendly),
            PersonalityScore(::dogFriendly.name.capitalise().separatePascalCase(), dogFriendly),
            PersonalityScore(::energyLevel.name.capitalise().separatePascalCase(), energyLevel),
            PersonalityScore(::grooming.name.capitalise().separatePascalCase(), grooming),
            PersonalityScore(::healthIssues.name.capitalise().separatePascalCase(), healthIssues),
            PersonalityScore(::intelligence.name.capitalise().separatePascalCase(), intelligence),
            PersonalityScore(::sheddingLevel.name.capitalise().separatePascalCase(), sheddingLevel),
            PersonalityScore(::socialNeeds.name.capitalise().separatePascalCase(), socialNeeds),
            PersonalityScore(::strangerFriendly.name.capitalise().separatePascalCase(), strangerFriendly),
            PersonalityScore(::vocalisation.name.capitalise().separatePascalCase(), vocalisation)
        ),
        features = listOf(
            Feature(::indoor.name.capitalise().separatePascalCase(), indoor == 1),
            Feature(::lap.name.capitalise().separatePascalCase(), lap == 1),
            Feature(::experimental.name.capitalise().separatePascalCase(), experimental == 1),
            Feature(::hairless.name.capitalise().separatePascalCase(), hairless == 1),
            Feature(::natural.name.capitalise().separatePascalCase(), natural == 1),
            Feature(::rare.name.capitalise().separatePascalCase(), rare == 1),
            Feature(::rex.name.capitalise().separatePascalCase(), rex == 1),
            Feature(::suppressedTail.name.capitalise().separatePascalCase(), suppressedTail == 1),
            Feature(::shortLegs.name.capitalise().separatePascalCase(), shortLegs == 1),
            Feature(::hypoallergenic.name.capitalise().separatePascalCase(), hypoallergenic == 1),
        )
    )
}

fun CatBreedEntity.toCat(): CatBreed {
    return CatBreed(
        id = id,
        name = name,
        origin = origin,
        countryEmoji = getFlagEmoji(countryCode)
    )
}

fun CatBreedImageDto.toCatImageEntity(catId: String): CatBreedImageEntity {
    return CatBreedImageEntity(
        imageId = id,
        url = url,
        catId = catId,
        width = width,
        height = height
    )
}

fun CatBreedImageEntity.toCatImage(): CatBreedImage {
    return CatBreedImage(
        id = imageId,
        url = url,
        width = width.toFloat(),
        height = height.toFloat()
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

fun String.capitalise(): String {
    return replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

fun String.separatePascalCase(): String {
    // Check if the string contains a second uppercase letter
    val containsSecondCapital = this.drop(1).any { it.isUpperCase() }

    // If not, return the string as is
    if (!containsSecondCapital) {
        return this
    }

    // Regular expression to find uppercase letters that are not at the beginning of the string
    val regex = "(?<!^)([A-Z])".toRegex()

    // Replace the found positions with a space followed by the uppercase letter
    return regex.replace(this) { " ${it.value}" }
}
