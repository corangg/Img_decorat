package com.example.img_decorat

import android.graphics.Color
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Util {
    companion object{
        val UNSPLASH_BASE_URL = "https://api.unsplash.com/"

        val colorsList : MutableList<Int> = mutableListOf(
            Color.BLACK,
            Color.WHITE,
            Color.TRANSPARENT,
            Color.LTGRAY,
            Color.GRAY,
            Color.DKGRAY,
            0xFFFFE4C4.toInt(),//비스크
            0xFFF5DEB3.toInt(),//휘트
            0xFFFFDAB9.toInt(),//피치퍼프
            0xFFFFE4B5.toInt(),//모카신
            0xFFFFDEAD.toInt(),//나바호화이트
            0xFFFFA07A.toInt(),//라이트살몬
            0xFFFA8072.toInt(),//살몬
            0xFFE9967A.toInt(),//다크살몬
            0xFFF08080.toInt(),//라이트코랄
            0xFFFF7F50.toInt(),//코랄
            0xFFFF6347.toInt(),//토마토
            0xFFCD5C5C.toInt(),//인디안레드
            0xFFB22222.toInt(),//파이어브릭
            Color.RED,
            0xFFDC143C.toInt(),//크림슨
            0xFF800000.toInt(),//마룬
            0xFF8B0000.toInt(),//다크레드
            0xFFA52A2A.toInt(),//브라운
            0xFFA0522D.toInt(),//시에나
            0xFF8B4513.toInt(),//세틀브라운
            0xFFF4A460.toInt(),//샌디브라운
            0xFFDEB887.toInt(),//벌리우드
            0xFFCD853F.toInt(),//페루
            0xFFD2691E.toInt(),//초콜릿
            0xFFFF4500.toInt(),//오렌지레드
            0xFFFFA500.toInt(),//오렌지
            0xFFFF8C00.toInt(),//다크 오렌지
            0xFFFFD700.toInt(),//골드
            0xFFDAA520.toInt(),//콜덴로드
            0xFFB8860B.toInt(),//다크골덴로드
            Color.YELLOW,
            Color.GREEN,
            Color.CYAN,
            Color.BLUE,
            Color.MAGENTA,
            )
    }

    object RetrofitClient {
        val instance: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.unsplash.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }



}