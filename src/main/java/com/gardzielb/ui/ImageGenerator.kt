package com.gardzielb.ui

import javafx.scene.image.Image

interface ImageGenerator {

	fun createImage(width: Double, height: Double): Image
}