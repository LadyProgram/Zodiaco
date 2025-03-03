package com.ladyprogram.zodiaco

class Horoscope (
    val id: String,
    val name: Int,
    val dates: Int,
    val icon: Int
){

    companion object {


        val horoscopeList = listOf(
            Horoscope( "aries", R.string.horoscope_name_aries, R.string.horoscope_date_aries, R.drawable.aries_icon),
            Horoscope( "tauro", R.string.horoscope_name_tauro, R.string.horoscope_date_tauro, R.drawable.taurus_icon),
            Horoscope( "geminis", R.string.horoscope_name_geminis, R.string.horoscope_date_geminis, R.drawable.gemini_icon),
            Horoscope( "cancer", R.string.horoscope_name_cancer, R.string.horoscope_date_cancer, R.drawable.cancer_icon),
            Horoscope( "leo", R.string.horoscope_name_leo, R.string.horoscope_date_leo, R.drawable.leo_icon),
            Horoscope( "virgo", R.string.horoscope_name_virgo, R.string.horoscope_date_virgo, R.drawable.virgo_icon),
            Horoscope( "libra", R.string.horoscope_name_libra, R.string.horoscope_date_libra, R.drawable.libra_icon),
            Horoscope( "escorpio", R.string.horoscope_name_escorpio, R.string.horoscope_date_escorpio, R.drawable.scorpio_icon),
            Horoscope( "sagitario", R.string.horoscope_name_sagitario, R.string.horoscope_date_sagitario, R.drawable.sagittarius_icon),
            Horoscope( "capricornio", R.string.horoscope_name_capricornio, R.string.horoscope_date_capricornio, R.drawable.capricorn_icon),
            Horoscope( "acuario", R.string.horoscope_name_acuario, R.string.horoscope_date_acuario, R.drawable.aquarius_icon),
            Horoscope( "piscis", R.string.horoscope_name_piscis, R.string.horoscope_date_piscis, R.drawable.pisces_icon)

        )


        fun findById (id: String): Horoscope {
            return horoscopeList.first { it.id == id }
        }
    }


}