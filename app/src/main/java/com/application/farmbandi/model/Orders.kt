package com.application.farmbandi.model

data class Orders(
    val upcoming: ArrayList<Order>,
    val ongoing: ArrayList<Order>,
    val completed: ArrayList<Order>
)