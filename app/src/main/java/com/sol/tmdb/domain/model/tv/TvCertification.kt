package com.sol.tmdb.domain.model.tv

enum class TvCertification(
    val certification: String,
    val meaning: String,
    val order: Int,
    val region: String
) {
    // Certificaciones de EE. UU.
    TV_MA(
        "TV-MA",
        "This program is specifically designed to be viewed by adults and therefore may be unsuitable for children under 17.",
        6,
        "US"
    ),
    TV_Y("TV-Y", "This program is designed to be appropriate for all children.", 1, "US"),
    TV_14(
        "TV-14",
        "This program contains some material that many parents would find unsuitable for children under 14 years of age.",
        5,
        "US"
    ),
    NR("NR", "No rating information.", 0, "US"),
    TV_PG(
        "TV-PG",
        "This program contains material that parents may find unsuitable for younger children.",
        4,
        "US"
    ),
    TV_Y7("TV-Y7", "This program is designed for children age 7 and above.", 2, "US"),
    TV_G("TV-G", "Most parents would find this program suitable for all ages.", 3, "US"),

    // Certificaciones de México
    AA("AA", "Aimed at children (can be broadcast anytime).", 1, "MX"),
    A("A", "Appropriate for all ages.", 2, "MX"),
    B("B", "Designed for ages 12 and older (allowed only between 16:00 and 05:59).", 3, "MX"),
    B15("B-15", "Designed for ages 15 and up (allowed only between 19:00 and 05:59).", 4, "MX"),
    C(
        "C",
        "Designed to be viewed by adults aged 18 or older (allowed only between 21:00 and 05:59).",
        5,
        "MX"
    ),
    D(
        "D",
        "Exclusively for adults aged 18 or older (allowed only between midnight and 05:00).",
        6,
        "MX"
    );

    companion object {
        fun fromCertification(certification: String, region: String): TvCertification? {
            return values().find { it.certification == certification && it.region == region }
        }
    }
}
