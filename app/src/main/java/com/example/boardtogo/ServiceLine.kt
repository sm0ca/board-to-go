package com.example.boardtogo

import androidx.compose.ui.graphics.Color

enum class ServiceLine(val abbreviation: String, val fullName: String, val bgColor: Color) {
    BR("BR", "Barrie", Color(0xFF2556AF)),
    KI("KI", "Kitchener", Color(0xFF3A8346)),
    LE("LE", "Lakeshore East", Color(0xFFEA3624)),
    LW("LW", "Lakeshore West", Color(0xFF8B1A31)),
    MI("MI", "Milton", Color(0xFFE6853E)),
    RH("RH", "Richmond Hill", Color(0xFF4397C3)),
    ST("ST", "Stouffville", Color(0xFF724715)),
    UN("?", "Unknown Line", Color(0xFF777777))
    ;

    companion object {
        private val serviceLineMap = entries.associateBy(ServiceLine::fullName)
        fun fromFullName(fullName: String?) = serviceLineMap[fullName] ?: UN
    }
}
