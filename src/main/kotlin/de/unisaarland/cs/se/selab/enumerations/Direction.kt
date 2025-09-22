package de.unisaarland.cs.se.selab.enumerations

/**
 * enumeration for direction
 */
enum class Direction {
    EAST,
    WEST,
    NORTH,
    SOUTH,
    NORTH_WEST,
    NORTH_EAST,
    SOUTH_WEST,
    SOUTH_EAST;

    /**
     * helper function that should work without an instance of Direction;
     */
    companion object {
        /**
         * converts angles to direction enum
         */
        fun getDirectionByAngle(angle: Int): Direction {
            return when (angle) {
                0 -> NORTH
                NE_ANGLE -> NORTH_EAST
                E_ANGLE -> EAST
                SE_ANGLE -> SOUTH_EAST
                S_ANGLE -> SOUTH
                SW_ANGLE -> SOUTH_WEST
                W_ANGLE -> WEST
                NW_ANGLE -> NORTH_WEST
                else -> error("Invalid angle $angle")
            }
        }
    }
}

const val NE_ANGLE = 45
const val E_ANGLE = 90
const val SE_ANGLE = 135
const val S_ANGLE = 180
const val SW_ANGLE = 225
const val W_ANGLE = 270
const val NW_ANGLE = 315