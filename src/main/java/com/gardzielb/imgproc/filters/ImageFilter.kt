package com.gardzielb.imgproc.filters

import javafx.scene.image.Image

abstract class ImageFilter(var img: Image) {

	// applies filter to all pixels from given collection and returns updated image
	abstract fun apply(pixels: PixelCollection): Image
}