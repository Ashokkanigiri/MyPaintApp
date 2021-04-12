package com.example.paintapp.utils

import com.example.paintapp.R

enum class ToolsEnum (val toolName: String, val id: Int){
    BRUSH("Brush", R.drawable.ic_baseline_brush_24),
    ERASER("Eraser", R.drawable.ic_eraser),
    COLOUR("Colour", R.drawable.ic_baseline_color_lens_24),
    BACKGROUND("Background", R.drawable.ic_baseline_filter_hdr_24),
    RETURN("Return", R.drawable.ic_baseline_keyboard_return_24),
    BRUSHSIZE("Brush Size", R.drawable.ic_baseline_crop_24)
}