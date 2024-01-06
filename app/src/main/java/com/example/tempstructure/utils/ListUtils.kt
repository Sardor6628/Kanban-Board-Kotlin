package com.example.tempstructure.utils

import com.example.tempstructure.R
import com.example.tempstructure.data.model.IntroModel

class ListUtils {

    companion object {

        fun getIntroList(): ArrayList<IntroModel> {
            val itemList = arrayListOf<IntroModel>()

            itemList.add(
                IntroModel(
                    "Intro 1",
                    "Description",
                    R.drawable.logo
                )
            )
            itemList.add(
                IntroModel(
                    "Intro 1",
                    "Description",
                    R.drawable.logo
                )
            )
            itemList.add(
                IntroModel(
                    "Intro 1",
                    "Description",
                    R.drawable.logo
                )
            )

            return itemList
        }


    }

}