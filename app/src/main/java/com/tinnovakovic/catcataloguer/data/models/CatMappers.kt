package com.tinnovakovic.catcataloguer.data.models

import com.tinnovakovic.catcataloguer.data.models.api.CatBreedDto
import com.tinnovakovic.catcataloguer.data.models.db.CatEntity
import com.tinnovakovic.catcataloguer.data.models.local.CatBreed
import com.tinnovakovic.catcataloguer.data.models.local.CatBreedDetail
import javax.inject.Inject

class CatMappers @Inject constructor() {

    fun CatBreedDto.toCatEntity(): CatEntity {
        return CatEntity(
            id = id,
            name = name,
            temperament = temperament,
            origin = origin,
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
            images = emptyList(),
        )
    }

    fun CatEntity.toCatBreed(): CatBreed {
        return CatBreed(
            id = id,
            name = name,
            origin = origin,
        )
    }

    fun CatEntity.toCatBreedDetail(): CatBreedDetail {
        return CatBreedDetail(
            id = id,
            name = name,
            temperament = temperament,
            origin = origin,
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
            images = images,
        )
    }

}
