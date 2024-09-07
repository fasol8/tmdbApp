package com.sol.tmdb.domain.model.movie

enum class Certification(
    val country: String,
    val certification: String,
    val meaning: String,
    val order: Int
) {

    // Certificaciones para México (MX)
    MX_AA("MX", "AA", "Informative-only rating: Understandable for children under 7 years.", 1),
    MX_A("MX", "A", "Information-only rating: For all age groups.", 2),
    MX_B("MX", "B", "Information-only rating: For adolescents 12 years and older.", 3),
    MX_B_15("MX", "B-15", "Information-only rating: Not recommended for children under 15.", 4),
    MX_C("MX", "C", "Restrictive rating: For adults 18 and older.", 5),
    MX_D(
        "MX",
        "D",
        "Restrictive rating: Adult movies (legally prohibited to those under 18 years of age).",
        6
    ),

    // Certificaciones para Estados Unidos (US)
    US_R("US", "R", "Under 17 requires accompanying parent or adult guardian 21 or older...", 4),
    US_PG("US", "PG", "Some material may not be suitable for children under 10...", 2),
    US_NC_17(
        "US",
        "NC-17",
        "These films contain excessive graphic violence, intense or explicit sex...",
        5
    ),
    US_G(
        "US",
        "G",
        "All ages admitted. There is no content that would be objectionable to most parents...",
        1
    ),
    US_NR("US", "NR", "No rating information.", 0),
    US_PG_13("US", "PG-13", "Some material may be inappropriate for children under 13...", 3);

    companion object {
        fun fromCountryAndCertification(country: String, certification: String): Certification? {
            return values().find { it.country == country && it.certification == certification }
        }
    }
}