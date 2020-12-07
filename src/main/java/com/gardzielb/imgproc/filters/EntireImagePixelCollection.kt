package com.gardzielb.imgproc.filters


// Represents all pixels of a given image
class EntireImagePixelCollection(private val imgWidth: Int, private val imgHeight: Int) : PixelCollection {

	override fun iterator(): Iterator<Pixel> = ImagePixelIterator(imgWidth, imgHeight)
}


// traverses all pixels of the image, x first, y second
class ImagePixelIterator(private val imgWidth: Int, private val imgHeight: Int) : Iterator<Pixel> {

	private var x = 0
	private var y = 0

	override fun hasNext(): Boolean = x < imgWidth && y < imgHeight

	override fun next(): Pixel {

		val current = Pixel(x, y)

		if (x < imgWidth - 1) x++
		else {
			y++
			x = 0
		}

		return current
	}
}