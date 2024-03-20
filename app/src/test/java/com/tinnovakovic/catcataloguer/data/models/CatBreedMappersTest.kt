package com.tinnovakovic.catcataloguer.data.models

import com.tinnovakovic.catcataloguer.data.models.api.CatBreedDto
import com.tinnovakovic.catcataloguer.data.models.api.CatBreedImageDto
import com.tinnovakovic.catcataloguer.data.models.api.WeightDto
import com.tinnovakovic.catcataloguer.data.models.db.CatBreedEntity
import com.tinnovakovic.catcataloguer.data.models.db.CatBreedImageEntity
import com.tinnovakovic.catcataloguer.data.models.local.CatBreed
import com.tinnovakovic.catcataloguer.data.models.local.CatBreedImage
import com.tinnovakovic.catcataloguer.data.models.local.CatDetail
import com.tinnovakovic.catcataloguer.data.models.local.Feature
import com.tinnovakovic.catcataloguer.data.models.local.PersonalityScore
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class CatBreedMappersTest {

    @Test
    fun `GIVEN CatBreedDto with nulls, WHEN toCatEntity(), THEN assert toCatEntity is mapped correctly`() {
        // Given a CatBreedDto with specific values (some nulls to test default handling)
        val dto = CatBreedDto(
            id = "1",
            name = "Test Breed",
            altNames = null, // Intentionally null to test default handling
            weight = WeightDto(metric = "5-6", imperial = "1-10"), // Partially null
            temperament = "Friendly",
            origin = "Test Origin",
            countryCode = null, // Intentionally null
            description = "Test Description",
            lifeSpan = "12-15",
            adaptability = 5,
            affectionLevel = 4,
            childFriendly = 3,
            dogFriendly = null, // Intentionally null to test default handling
            energyLevel = 5,
            grooming = 2,
            healthIssues = 1,
            intelligence = 5,
            sheddingLevel = 4,
            socialNeeds = 3,
            strangerFriendly = 2,
            vocalisation = 5,
            experimental = 0,
            hairless = 0,
            natural = 1,
            rare = 0,
            rex = 1,
            suppressedTail = 0,
            shortLegs = 1,
            hypoallergenic = 0,
            indoor = 0,
            lap = 1
        )

        // Expected CatEntity based on the given CatBreedDto
        val expectedEntity = CatBreedEntity(
            id = "1",
            name = "Test Breed",
            altNames = "",
            weightMetric = "5-6",
            weightImperial = "1-10",
            temperament = "Friendly",
            origin = "Test Origin",
            countryCode = "",
            description = "Test Description",
            lifeSpan = "12-15",
            adaptability = 5,
            affectionLevel = 4,
            childFriendly = 3,
            dogFriendly = 0,
            energyLevel = 5,
            grooming = 2,
            healthIssues = 1,
            intelligence = 5,
            sheddingLevel = 4,
            socialNeeds = 3,
            strangerFriendly = 2,
            vocalisation = 5,
            experimental = 0,
            hairless = 0,
            natural = 1,
            rare = 0,
            rex = 1,
            suppressedTail = 0,
            shortLegs = 1,
            hypoallergenic = 0,
            indoor = 0,
            lap = 1
        )

        // When toCatEntity is called
        val actualEntity = dto.toCatEntity()

        // Then verify that the resulting CatEntity matches the expected CatEntity
        assertEquals(expectedEntity, actualEntity)
    }

    @Test
    fun `GIVEN CatBreedEntity, WHEN toCat(), THEN assert CatBreed is mapped correctly`() {
        // Given
        val catBreedEntity = mockk<CatBreedEntity>(relaxed = true) {
            every { id } returns "1"
            every { name } returns "Test Breed"
            every { origin } returns "Test Origin"
            every { countryCode } returns "US"
        }

        // Expected CatBreed based on the given CatBreedEntity
        val expected = CatBreed(
            id = "1",
            name = "Test Breed",
            origin = "Test Origin",
            countryEmoji = "🇺🇸"
        )

        // When
        val actual = catBreedEntity.toCat()

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN CatBreedImageDto, WHEN toCatImageEntity, THEN assert CatImageEntity mapped correctly`() {
        // Given
        val catBreedImageDto = CatBreedImageDto(
            id = "img1",
            url = "https://example.com/cat.jpg",
            width = 1600,
            height = 1067
        )
        val catId = "1"

        // Expected CatBreedImageEntity based on the given CatBreedImageDto
        val expected = CatBreedImageEntity(
            imageId = "img1",
            url = "https://example.com/cat.jpg",
            catId = "1",
            width = 1600,
            height = 1067
        )

        // When
        val actual = catBreedImageDto.toCatImageEntity(catId)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN CatBreedImageEntity, WHEN toCatImage(), THEN assert CatImage mapped correctly`() {
        // Given
        val catBreedImageEntity = CatBreedImageEntity(
            imageId = "img1",
            url = "https://example.com/cat.jpg",
            catId = "1", // Not used in mapping, hence not checked
            width = 1600,
            height = 1067
        )

        // Expected CatBreedImage based on the given CatBreedImageEntity
        val expected = CatBreedImage(
            id = "img1",
            url = "https://example.com/cat.jpg",
            width = 1600f, // Note: Conversion to float
            height = 1067f  // Note: Conversion to float
        )

        // When
        val actual = catBreedImageEntity.toCatImage()

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN CatBreedEntity, WHEN toCatDetail(), THEN assert CatDetail mapped correctly`() {
        // Given
        val entity = CatBreedEntity(
            id = "1",
            name = "Test Breed",
            altNames = "Test Alt",
            weightMetric = "5-6 kg",
            weightImperial = "11-13 lbs",
            temperament = "Friendly, Active",
            origin = "Test Origin",
            countryCode = "US",
            description = "Test Description",
            lifeSpan = "12-15 years",
            adaptability = 5,
            affectionLevel = 4,
            childFriendly = 3,
            dogFriendly = 2,
            energyLevel = 5,
            grooming = 4,
            healthIssues = 1,
            intelligence = 5,
            sheddingLevel = 2,
            socialNeeds = 3,
            strangerFriendly = 4,
            indoor = 1,
            lap = 1,
            experimental = 0,
            hairless = 0,
            natural = 1,
            rare = 0,
            rex = 1,
            suppressedTail = 0,
            shortLegs = 1,
            hypoallergenic = 1,
            vocalisation = 3
        )

        // Expected CatDetail based on the given CatBreedEntity
        val expected = CatDetail(
            id = "1",
            name = "Test Breed",
            altNames = "Test Alt",
            weightMetric = "5-6 kg",
            weightImperial = "11-13 lbs",
            temperament = listOf("Friendly", "Active"),
            origin = "Test Origin",
            countryCode = "US",
            description = "Test Description",
            lifeSpan = "12-15 years",
            personalityScores = listOf(
                PersonalityScore("Adaptability", 5),
                PersonalityScore("Affection Level", 4),
                PersonalityScore("Child Friendly", 3),
                PersonalityScore("Dog Friendly", 2),
                PersonalityScore("Energy Level", 5),
                PersonalityScore("Grooming", 4),
                PersonalityScore("Health Issues", 1),
                PersonalityScore("Intelligence", 5),
                PersonalityScore("Shedding Level", 2),
                PersonalityScore("Social Needs", 3),
                PersonalityScore("Stranger Friendly", 4),
                PersonalityScore("Vocalisation", 3)

            ),
            features = listOf(
                Feature("Indoor", true),
                Feature("Lap", true),
                Feature("Experimental", false),
                Feature("Hairless", false),
                Feature("Natural", true),
                Feature("Rare", false),
                Feature("Rex", true),
                Feature("Suppressed Tail", false),
                Feature("Short Legs", true),
                Feature("Hypoallergenic", true),
            )
        )

        // When toCatDetail is called
        val actual = entity.toCatDetail()

        // Then verify that the resulting CatDetail matches the expected CatDetail
        assertEquals(expected, actual)
    }

    @Nested
    @DisplayName("getEmojiFlag() Tests")
    inner class TestForGetEmojiFlag {
        @Test
        fun `GIVEN valid US country code, WHEN getFlagEmoji(), THEN return US flag emoju`() {
            // Given a valid country code
            val countryCode = "US"

            // When getFlagEmoji is called
            val result = getFlagEmoji(countryCode)

            // Then the correct flag emoji is returned
            val expectedEmoji = "\uD83C\uDDFA\uD83C\uDDF8" // 🇺🇸
            assertEquals(expectedEmoji, result)
        }

        @Test
        fun `GIVEN invalid USA country code, WHEN getFlagEmoji(), THEN return USA`() {
            // Given an invalid country code (wrong length)
            val countryCode = "USA"

            // When getFlagEmoji is called
            val result = getFlagEmoji(countryCode)

            // Then the input string is returned as is
            assertEquals(countryCode, result)
        }
    }

    @Nested
    @DisplayName("String.capitalise() Tests")
    inner class TestForStringCapitalise {
        @Test
        fun `GIVEN lowercase string, WHEN capitalise(), THEN return uppercase`() {
            val input = "test"
            val expected = "Test"
            assertEquals(expected, input.capitalise())
        }

        @Test
        fun `GIVEN uppercase string, WHEN capitalise() return the inputted string`() {
            val input = "Test"
            val expected = "Test"
            assertEquals(expected, input.capitalise())
        }

        @Test
        fun `GIVEN string begins with numbers, WHEN capitalise(), THEN return the inputted string`() {
            val input = "123test"
            val expected = "123test"
            assertEquals(expected, input.capitalise())
        }
    }

    @Nested
    @DisplayName("String.separatePascalCase() Tests")
    inner class TestForStringSeparatePascalCase {
        @Test
        fun `GIVEN pascal case string, WHEN separatePascalCase(), THEN return separated string`() {
            val input = "PascalCaseString"
            val expected = "Pascal Case String"
            assertEquals(expected, input.separatePascalCase())
        }

        @Test
        fun `GIVEN non pascal case string, WHEN separatePascalCase(), THEN return inputted string`() {
            val input = "Already separated"
            val expected = "Already separated"
            assertEquals(expected, input.separatePascalCase())
        }

        @Test
        fun `GIVEN another version of a non pascal case string, WHEN separatePascalCase() returns inputted string`() {
            val input = "Singleword"
            val expected = "Singleword"
            assertEquals(expected, input.separatePascalCase())
        }
    }
}
