package de.unisaarland.cs.se.selab.enumerations


/**
 * enumeration for plant type
 */
enum class PlantType {
    POTATO,
    WHEAT,
    OAT,
    PUMPKIN,
    APPLE,
    ALMOND,
    CHERRY,
    GRAPE;

    /**
     * companion object
     */
    companion object {
        /**
         * helper function for parsing to check if the given plant type is a plantation plant
         */
        fun isPlantationPlant(plant: String): Boolean {
            val plantType = PlantType.valueOf(plant.uppercase())
            val plantationPlants = listOf(APPLE, ALMOND, CHERRY, GRAPE)
            return plantType in plantationPlants
        }

        /**
         * helper function for parsing to check if the given plant type is a field plant
         */
        fun isFieldPlant(plant: String): Boolean {
            val plantType = PlantType.valueOf(plant.uppercase())
            val fieldPlants = listOf(POTATO, WHEAT, OAT, PUMPKIN)
            return plantType in fieldPlants
        }
    }
}
