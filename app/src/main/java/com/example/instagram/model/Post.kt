package com.example.instagram.model

import java.io.Serializable

//Firebase에서 사용하기위해 기본생성자가 있게 만듬
data class Post(
    var email: String = "",
    var imageUrl2: String = ""
) : Serializable