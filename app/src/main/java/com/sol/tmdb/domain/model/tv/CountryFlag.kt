package com.sol.tmdb.domain.model.tv

enum class CountryFlag(val code: String, val flag: String) {
    US("US", "🇺🇸"),
    MX("MX", "🇲🇽"),
    CA("CA", "🇨🇦"),
    GB("GB", "🇬🇧"),
    FR("FR", "🇫🇷"),
    DE("DE", "🇩🇪"),
    IT("IT", "🇮🇹"),
    JP("JP", "🇯🇵"),
    CN("CN", "🇨🇳"),
    IN("IN", "🇮🇳"),
    ES("ES", "🇪🇸"),
    BR("BR", "🇧🇷"),
    AR("AR", "🇦🇷"),
    AU("AU", "🇦🇺"),
    RU("RU", "🇷🇺"),
    KR("KR", "🇰🇷"),
    NL("NL", "🇳🇱"),
    SE("SE", "🇸🇪"),
    NO("NO", "🇳🇴"),
    FI("FI", "🇫🇮"),
    DK("DK", "🇩🇰"),
    ZA("ZA", "🇿🇦"),
    NG("NG", "🇳🇬"),
    EG("EG", "🇪🇬"),
    SA("SA", "🇸🇦"),
    TR("TR", "🇹🇷"),
    GR("GR", "🇬🇷");

    companion object {
        fun getFlagByCode(code: String): String {
            return values().find { it.code == code }?.flag ?: "🏳️" // Bandera por defecto si no se encuentra
        }
    }
}
