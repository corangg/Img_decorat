package com.example.img_decorat.dataModels

data class FontsResponse(
    val items: List<Font>
)

data class Font(
    val family: String,
    val variants: List<String>,
    val subsets: List<String>,
    val version: String,
    val lastModified: String,
    val files: Map<String, String>,
    val category: String,
    val kind: String
)
