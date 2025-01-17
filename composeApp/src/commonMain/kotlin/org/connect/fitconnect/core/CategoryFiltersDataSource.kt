package org.connect.fitconnect.core

import androidx.compose.ui.graphics.Color
import org.connect.fitconnect.domain.workout.MuscleGroup

data class CategoryUiState (
    val name : String,
    val color : Color,
    val id : Long,
    var isSelected : Boolean
) {
    companion object {
        val Init = emptyList<CategoryUiState>()

        val colors = mapOf(
            "Back" to Color(0xFF4CAF50),        // Green
            "Chest" to Color(0xFFFF5722),      // Deep Orange
            "Triceps" to Color(0xFFFFC107),    // Amber
            "Biceps" to Color(0xFF03A9F4),     // Light Blue
            "Legs" to Color(0xFF9C27B0),       // Purple
            "Shoulder" to Color(0xFF673AB7),   // Deep Purple
            "Abs" to Color(0xFFFFEB3B),        // Yellow
            "Glutes" to Color(0xFFFF9800),     // Orange
            "Forearms" to Color(0xFF2196F3),   // Blue
            "Calves" to Color(0xFFE91E63)      // Pink
        )

        fun getCategoriesForUi(categories : List<MuscleGroup>) : List<CategoryUiState> {
            val list = mutableListOf<CategoryUiState>()
                categories.forEach { category ->
                    list.add(
                        CategoryUiState(
                            name = category.name,
                            color = getColorForMuscleGroup(category.name),
                            isSelected = false,
                            id = category.muscle_number
                        )
                    )
                }
            return list
        }

        fun getColorForMuscleGroup(categoryName: String): Color {
            return colors[categoryName] ?: Color.Gray // Default to Gray if the category is not found
        }
    }


}