package com.gardzielb.imgproc.filters


data class Pixel(val x: Int, val y: Int)

interface PixelCollection : Iterable<Pixel>

class NullPixelCollection : PixelCollection {
	override fun iterator(): Iterator<Pixel> = listOf<Pixel>().iterator()
}
