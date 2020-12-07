package com.gardzielb.ui

import com.gardzielb.imgproc.loadScaledImage
import java.io.File

class FileImageGenerator(private val imgFile: File) : ImageGenerator {

	override fun createImage(width: Double, height: Double) = loadScaledImage(imgFile, width, height)
}